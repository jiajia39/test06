package com.framework.emt.system.domain.exception.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.common.redis.utils.CacheUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.convert.constant.ExceptionCategoryConstant;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionCategoryErrorCode;
import com.framework.emt.system.domain.exception.convert.ExceptionCategoryConvert;
import com.framework.emt.system.domain.exception.mapper.ExceptionCategoryMapper;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryImportExcel;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryQueryRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryUpdateRequest;
import com.framework.emt.system.domain.exception.response.ChildCountResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryTreeResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * 异常分类 实现类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Service
@RequiredArgsConstructor
public class ExceptionCategoryServiceImpl extends BaseServiceImpl<ExceptionCategoryMapper, ExceptionCategory> implements ExceptionCategoryService {

    @Override
    public Long create(ExceptionCategoryCreateRequest request) {
        // 校验同一父级异常分类下的子异常分类名称不能重复
        validateSameParentUnique(null, request.getTitle(), request.getParentId());
        // 获取父级ID路径
        String parentIdPath = NumberUtils.LONG_ZERO.equals(request.getParentId()) ? StrUtil.EMPTY : joinParentPath(request.getParentId());
        // 添加异常分类
        Long id = create(ExceptionCategoryConvert.INSTANCE.createRequestToEntity(request, parentIdPath));

        // 清除异常分类树状图缓存
        clearCache();
        return id;
    }

    @Override
    public void delete(Long id) {
        findByIdThrowErr(id);
        // 校验是否被其它模块引用
        ChildCountResponse childCount = this.baseMapper.findChildCount(id);
        if (childCount.getChildrenCount() > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ExceptionCategoryErrorCode.THIS_EXCEPTION_CATEGORY_CONTAIN_CHILD_CATEGORY);
        }
        if (childCount.getExceptionItemCount() > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ExceptionCategoryErrorCode.THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_ITEM);
        }
        if (childCount.getExceptionProcessCount() > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ExceptionCategoryErrorCode.THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_PROCESS);
        }
        // 校验是否存在未完成的异常提报
        ExceptionTaskSubmitService taskSubmitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
        List<Long> taskIds = taskSubmitService.findById(null, id, null, null);
        if (CollectionUtil.isNotEmpty(taskIds)) {
            ExceptionTaskService taskService = SpringUtil.getBean(ExceptionTaskService.class);
            if (taskService.findFinishCountByIds(taskIds) > 0) {
                throw new ServiceException(ExceptionCategoryErrorCode.THIS_EXCEPTION_CATEGORY_CONTAIN_EXCEPTION_TASK_SUBMIT);
            }
        }
        this.deleteById(id);

        // 清除异常分类树状图缓存
        clearCache();
    }

    @Override
    public Long update(Long id, ExceptionCategoryUpdateRequest request) {
        ExceptionCategory exceptionCategory = findByIdThrowErr(id);
        // 校验同一父级异常分类下的子异常分类名称不能重复
        validateSameParentUnique(id, request.getTitle(), exceptionCategory.getParentId());
        this.update(ExceptionCategoryConvert.INSTANCE.updateRequestToEntity(exceptionCategory, request));

        // 清除异常分类树状图缓存
        clearCache();
        return id;
    }

    @Override
    public ExceptionCategoryResponse detail(Long id) {
        ExceptionCategory exceptionCategory = findByIdThrowErr(id);
        // 获取父级异常分类列表
        List<String> parentTitleList = findTitleList(DataHandleUtils.splitStr(exceptionCategory.getParentIdPath()));
        return ExceptionCategoryConvert.INSTANCE.entityToResponse(exceptionCategory, parentTitleList);
    }

    @Override
    public IPage<ExceptionCategoryResponse> page(ExceptionCategoryQueryRequest request) {
        return DataHandleUtils.loadUserName(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public List<ExceptionCategoryTreeResponse> tree() {
        String cacheName = ExceptionCategoryConstant.CATEGORY_TREE_CACHE_NAME;
        String keyPrefix = ExceptionCategoryConstant.CATEGORY_TREE_KEY_PREFIX;
        String cacheKey = ExceptionCategoryConstant.CATEGORY_TREE_CACHE_KEY;
        String tenantId = AuthUtil.getTenantId();
        // 从缓存获取数据
        List<ExceptionCategoryTreeResponse> categoryTree = CacheUtil.get(cacheName, keyPrefix, cacheKey, tenantId, List.class);
        if (categoryTree == null) {
            // 缓存中没有数据，从数据库获取并缓存到Redis中
            categoryTree = this.buildTree(this.baseMapper.list());
            // 将数据存入缓存
            CacheUtil.put(cacheName, keyPrefix, cacheKey, categoryTree, tenantId);
        }
        return categoryTree;
    }

    @Override
    @DSTransactional
    public void importExceptionCategory(List<ExceptionCategoryImportExcel> excelImportDataList, List<ExceptionCategory> existParentCategoryList, List<String> notExistParentTitleList) {
        // 获取excel中在数据库里不存在的父级异常分类map列表
        Map<String, ExceptionCategory> parentCategoryMap = getParentCategoryMap(existParentCategoryList, notExistParentTitleList);
        // 导入异常分类excel数据集
        this.saveBatch(CollectionUtil.isEmpty(parentCategoryMap) ? ExceptionCategoryConvert.INSTANCE.excelImportDataListToEntityList(excelImportDataList)
                : ExceptionCategoryConvert.INSTANCE.excelImportDataListToEntityListWithParentCategoryMap(excelImportDataList, parentCategoryMap));
        // 清除异常分类树状图缓存
        clearCache();
    }

    @Override
    public void validateItemCategory(Long id) {
        this.findByIdThrowErr(id);
        ExceptionItemService exceptionItemService = SpringUtil.getBean(ExceptionItemService.class);
        exceptionItemService.validateCategoryExist(id);
    }

    @Override
    public ExceptionCategory findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionCategoryErrorCode.EXCEPTION_CATEGORY_INFO_NOT_FIND);
    }

    @Override
    public ExceptionCategory findByTitles(List<String> titles) {
        return this.getOne(new LambdaQueryWrapper<ExceptionCategory>().in(ExceptionCategory::getTitle, titles).last("limit 1"));
    }

    @Override
    public List<String> findTitleList(List<String> ids) {
        return this.baseMapper.findCategoryList(ids, null).stream().map(ExceptionCategory::getTitle).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findIdTitleByIds(List<String> ids) {
        return this.baseMapper.findCategoryList(ids, null).stream().collect(Collectors.toMap(category -> String.valueOf(category.getId()), ExceptionCategory::getTitle));
    }

    @Override
    public Map<Long, ExceptionCategory> getMapByIds(List<Long> categoryIds) {
        if (CollectionUtil.isEmpty(categoryIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        List<ExceptionCategory> categoryList = this.baseMapper.listByIds(categoryIds);
        if (CollectionUtil.isEmpty(categoryIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        return categoryList.stream().collect(toMap(ExceptionCategory::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    public List<Long> findChildIdsById(Long id) {
        List<ExceptionCategory> exceptionCategories = this.list(new LambdaQueryWrapper<ExceptionCategory>()
                .select(ExceptionCategory::getId, ExceptionCategory::getParentId)
                .eq(ExceptionCategory::getParentId, id));
        return exceptionCategories.stream().map(ExceptionCategory::getId).collect(Collectors.toList());
    }

    /**
     * 根据异常分类父级id和父级id路径
     * 拼接出此条异常分类的父级id路径
     *
     * @param parentId
     * @return
     */
    public String joinParentPath(Long parentId) {
        return findByIdThrowErr(parentId).getParentIdPath() + StrUtil.COMMA + parentId;
    }

    /**
     * 得到excel中在数据库不存在的父级异常分类列表
     *
     * @param notExistParentTitleList excel中在数据库不存在的父级异常分类名称列表
     * @return
     */
    private List<ExceptionCategory> getNotExistCategoryList(List<String> notExistParentTitleList) {
        List<ExceptionCategory> exceptionCategoryList = ExceptionCategoryConvert.INSTANCE.stringListToEntityList(notExistParentTitleList);
        this.saveBatch(exceptionCategoryList);
        return exceptionCategoryList;
    }

    /**
     * 构建异常分类树状图
     *
     * @param categoryList 异常分类列表
     * @return 异常分类树状图
     */
    private List<ExceptionCategoryTreeResponse> buildTree(List<ExceptionCategoryTreeResponse> categoryList) {
        Map<Long, List<ExceptionCategoryTreeResponse>> exceptionCategoryMap = categoryList.stream().collect(Collectors.groupingBy(ExceptionCategoryTreeResponse::getParentId));
        categoryList.forEach(exceptionCategory -> exceptionCategory.setChildList(exceptionCategoryMap.getOrDefault(exceptionCategory.getId(), Collections.emptyList())));
        return categoryList.stream().filter(exceptionCategory -> NumberUtils.LONG_ZERO.equals(exceptionCategory.getParentId())).collect(Collectors.toList());
    }

    /**
     * 清除redis中的异常分类树状图
     */
    public void clearCache() {
        String cacheName = ExceptionCategoryConstant.CATEGORY_TREE_CACHE_NAME;
        String keyPrefix = ExceptionCategoryConstant.CATEGORY_TREE_KEY_PREFIX;
        String tenantId = AuthUtil.getTenantId();
        CacheUtil.clear(cacheName, keyPrefix, tenantId);
    }

    /**
     * 校验同一父级异常分类下的子异常分类名称不能重复
     *
     * @param id       主键id
     * @param title    异常分类名称
     * @param parentId 异常分类父id
     */
    private void validateSameParentUnique(Long id, String title, Long parentId) {
        String titleParentIdStr = title + parentId;
        ExceptionCategory exceptionCategory = this.baseMapper.findByTitleParentIdStr(id, titleParentIdStr);
        if (ObjectUtil.isNotNull(exceptionCategory)) {
            if (NumberUtils.LONG_ZERO.equals(parentId)) {
                throw new ServiceException(ExceptionCategoryErrorCode.TOP_TITLE_CAN_NOT_EQUALS);
            }
            throw new ServiceException(ExceptionCategoryErrorCode.SAME_PARENT_OF_CHILD_TITLE_CAN_NOT_EQUALS);
        }
    }

    /**
     * 获取excel中的父级异常分类map列表
     *
     * @param existParentCategoryList excel中在数据库存在的父级异常分类列表
     * @param notExistParentTitleList excel中在数据库不存在的父级异常分类名称列表
     * @return key为父级异常分类名称，value为父级异常分类对象的map列表
     */
    private Map<String, ExceptionCategory> getParentCategoryMap(List<ExceptionCategory> existParentCategoryList, List<String> notExistParentTitleList) {
        // existParentCategoryList为空、notExistParentTitleList为空：父级异常分类名称列表为空
        if (CollectionUtil.isEmpty(existParentCategoryList) && CollectionUtil.isEmpty(notExistParentTitleList)) {
            return Collections.emptyMap();
        }
        // existParentCategoryList不为空、notExistParentTitleList为空：父级异常分类名称列表的名称都在数据库存在
        if (CollectionUtil.isNotEmpty(existParentCategoryList) && CollectionUtil.isEmpty(notExistParentTitleList)) {
            return existParentCategoryList.stream().collect(Collectors.toMap(ExceptionCategory::getTitle, Function.identity()));
        }
        // existParentCategoryList为空、notExistParentTitleList不为空：父级异常分类名称列表的名称都在数据库不存在
        if (CollectionUtil.isEmpty(existParentCategoryList) && CollectionUtil.isNotEmpty(notExistParentTitleList)) {
            return getNotExistCategoryList(notExistParentTitleList).stream().collect(Collectors.toMap(ExceptionCategory::getTitle, Function.identity()));
        }
        // existParentCategoryList不为空，notExistParentTitleList不为空：父级异常分类名称列表的名称部分在数据库存在、部分在数据库不存在
        List<ExceptionCategory> notExistParentCategoryList = getNotExistCategoryList(notExistParentTitleList);
        return Stream.of(existParentCategoryList, notExistParentCategoryList).flatMap(List::stream).distinct().collect(Collectors.toMap(ExceptionCategory::getTitle, Function.identity()));
    }

    @Override
    public List<Long> getChildById(Long id) {
        return baseMapper.getChildById(id);
    }


}
