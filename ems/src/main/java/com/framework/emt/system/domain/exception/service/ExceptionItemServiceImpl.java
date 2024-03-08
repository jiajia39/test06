package com.framework.emt.system.domain.exception.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionItemErrorCode;
import com.framework.emt.system.domain.exception.convert.ExceptionItemConvert;
import com.framework.emt.system.domain.exception.mapper.ExceptionItemMapper;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionItemExportResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemShortResponse;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 异常项 实现类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Service
@RequiredArgsConstructor
public class ExceptionItemServiceImpl extends BaseServiceImpl<ExceptionItemMapper, ExceptionItem> implements ExceptionItemService {

    private final ExceptionCategoryService exceptionCategoryService;


    @Override
    public Long create(ExceptionItemCreateRequest request) {
        // 校验名称必须唯一
        checkTitleUnique(null, request.getExceptionCategoryId(), request.getTitle());
        // 新增异常项
        return this.create(ExceptionItemConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    public void delete(Long id) {
        // 校验异常项是否存在、是否已启用
        ExceptionItem exceptionItem = this.getOne(new LambdaQueryWrapper<ExceptionItem>()
                .eq(ExceptionItem::getEnableFlag, EnableFlagEnum.FORBIDDEN)
                .eq(ExceptionItem::getId, id));
        if (ObjectUtil.isNull(exceptionItem)) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        // 异常项下包含了未完成的异常提报，则不能删除
        ExceptionTaskSubmitService taskSubmitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
        List<Long> taskIds = taskSubmitService.findById(id, null, null, null);
        if (CollectionUtil.isNotEmpty(taskIds)) {
            ExceptionTaskService taskService = SpringUtil.getBean(ExceptionTaskService.class);
            if (taskService.findFinishCountByIds(taskIds) > 0) {
                throw new ServiceException(ExceptionItemErrorCode.THIS_ITEM_CONTAIN_IS_FINISH_SUBMIT);
            }
        }
        this.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 校验异常项是否存在、是否已启用
        long count = this.count(new LambdaQueryWrapper<ExceptionItem>()
                .eq(ExceptionItem::getEnableFlag, EnableFlagEnum.FORBIDDEN)
                .in(ExceptionItem::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        this.removeByIds(ids);
    }

    @Override
    public Long update(Long id, ExceptionItemUpdateRequest request) {
        ExceptionItem exceptionItem = findByIdThrowErr(id);
        // 校验名称必须唯一
        checkTitleUnique(id, exceptionItem.getId(), request.getTitle());
        // 修改异常项
        return this.update(ExceptionItemConvert.INSTANCE.updateRequestToEntity(exceptionItem, request));
    }

    @Override
    public ExceptionItemResponse detail(Long id) {
        ExceptionItemResponse detailResult = this.baseMapper.detail(findByIdThrowErr(id).getId());
        // 装载父级异常分类列表
        Optional.ofNullable(detailResult.getCategoryParentIdPath()).ifPresent(categoryParentIdPath -> {
            // 拆分异常分类父级ID路径
            List<String> categoryParentIds = DataHandleUtils.splitStr(categoryParentIdPath);
            // 查询父级异常分类列表
            List<String> categoryParentTitles = exceptionCategoryService.findTitleList(categoryParentIds);
            detailResult.setCategoryParentList(categoryParentTitles);
        });
        return detailResult;
    }

    @Override
    public IPage<ExceptionItemResponse> page(ExceptionItemQueryRequest request) {
        IPage<ExceptionItemResponse> pageResult = this.baseMapper.page(Condition.getPage(request), request);
        return CollectionUtil.isEmpty(pageResult.getRecords()) ? pageResult : this.loadCategoryParentList(pageResult);
    }

    @Override
    public List<ExceptionItemExportResponse> export(ExceptionItemExportQueryRequest request) {
        long exportCount = this.baseMapper.findExportDataCount(request, request.getIds());
        long maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<ExceptionItemExportResponse> exportResult = this.baseMapper.findExportData(request, request.getIds());
        if (CollectionUtil.isEmpty(exportResult)) {
            return exportResult;
        }
        exportResult.forEach(ExceptionItemExportResponse::init);
        return DataHandleUtils.loadUserName(exportResult);
    }

    @Override
    public ExceptionItem findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionItemErrorCode.EXCEPTION_ITEM_INFO_NOT_FIND);
    }

    @Override
    public void importExceptionItemExcel(List<ExceptionItemImportExcel> importDataList, Map<String, Long> categoryTitleOfIdMap) {
        this.saveBatch(ExceptionItemConvert.INSTANCE.importDataListToEntityList(importDataList, categoryTitleOfIdMap));
    }

    @Override
    public ExceptionItem findByShortList(List<ExceptionItemShortResponse> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return baseMapper.getByShortList(list);
    }

    @Override
    public List<ExceptionItemResponse> findItemsByCategoryId(Long categoryId) {
        List<Long> categoryChildIds = exceptionCategoryService.findChildIdsById(categoryId);
        List<Long> categoryIds = DataHandleUtils.mergeElements(Lists.newArrayList(categoryId), categoryChildIds);
        List<ExceptionItem> exceptionItems = this.list(new LambdaQueryWrapper<ExceptionItem>()
                .in(ExceptionItem::getExceptionCategoryId, categoryIds));
        return ExceptionItemConvert.INSTANCE.entitysToResposnes(exceptionItems);
    }

    @Override
    public void changeEnableFlag(Integer enableFlag, List<Long> ids) {
        long count = this.count(new LambdaQueryWrapper<ExceptionItem>().in(ExceptionItem::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.STATE_CHANGE_FAIL_DATA_NOT_EXIST_CAN_NOT_CHANGE);
        }
        this.update(new LambdaUpdateWrapper<ExceptionItem>().set(ExceptionItem::getEnableFlag, enableFlag).in(ExceptionItem::getId, ids));
    }

    @Override
    public Long validateExceptionCategory(Long exceptionProcessOfCategoryId, Long exceptionItemOfCategoryId) {
        ExceptionItem exceptionItem = this.findByIdThrowErr(exceptionItemOfCategoryId);
        //获取exceptionProcessOfCategoryId 下所有的子类id
        List<Long> childList = exceptionCategoryService.getChildById(exceptionProcessOfCategoryId);
        childList.add(exceptionProcessOfCategoryId);
        if (!childList.contains(exceptionItem.getExceptionCategoryId())) {
            throw new ServiceException(ExceptionItemErrorCode.CATEGORY_ID_NOT_EQUALS);
        }
        return exceptionItem.getExceptionCategoryId();
    }

    @Override
    public Map<Long, ExceptionItem> getMapByIds(List<Long> itemIds) {
        if (CollectionUtil.isEmpty(itemIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        List<ExceptionItem> itemList = this.baseMapper.listByIds(itemIds);
        if (CollectionUtil.isEmpty(itemList)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        return itemList.stream().collect(toMap(ExceptionItem::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    public List<ExceptionItem> findListByTitles(List<String> titles) {
        return this.list(new LambdaQueryWrapper<ExceptionItem>().in(ExceptionItem::getTitle, titles));
    }

    @Override
    public String findItemName(Long taskId, Integer submitVersion) {
        return Optional.ofNullable(this.baseMapper.findItemName(taskId, submitVersion)).orElse(StrUtil.EMPTY);
    }

    @Override
    public String getTitleById(Long id) {
        ExceptionItem exceptionItem = this.getOne(new LambdaQueryWrapper<ExceptionItem>()
                .select(ExceptionItem::getTitle).eq(ExceptionItem::getId, id));
        return Optional.ofNullable(exceptionItem).map(ExceptionItem::getTitle).orElse(StrUtil.EMPTY);
    }

    @Override
    public void validateCategoryExist(Long categoryId) {
        ExceptionItem exceptionItem = this.getOne(new LambdaQueryWrapper<ExceptionItem>()
                .eq(ExceptionItem::getExceptionCategoryId, categoryId)
                .last("limit 1"));
        if (ObjectUtil.isNull(exceptionItem)) {
            throw new ServiceException(ExceptionItemErrorCode.PLEASE_SELECT_ITEM_BOTTOM_CATEGORY);
        }
    }

    /**
     * 校验异常项名称在数据库中不能存在
     *
     * @param id    主键id
     * @param title 异常项名称
     */
    private void checkTitleUnique(Long id, Long exceptionCategoryId, String title) {
        LambdaQueryWrapper<ExceptionItem> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(ExceptionItem::getId, id);
        }
        queryWrapper.eq(ExceptionItem::getExceptionCategoryId, exceptionCategoryId);
        queryWrapper.eq(ExceptionItem::getTitle, title);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_TITLE_IS_EXIST);
        }
    }

    /**
     * 通过异常分类父级ID路径装载异常分类父级列表
     * 通过用户id装载用户名
     *
     * @param pageResult 分页结果集
     * @return
     */
    private IPage<ExceptionItemResponse> loadCategoryParentList(IPage<ExceptionItemResponse> pageResult) {
        // 过滤掉异常分类父级ID路径为空的元素
        List<ExceptionItemResponse> filteredRecords = pageResult.getRecords().stream()
                .filter(item -> StringUtils.isNotBlank(item.getCategoryParentIdPath())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(filteredRecords)) {
            return DataHandleUtils.loadUserName(pageResult);
        }
        // 查询出所有异常分类id对的异常分类名称
        List<String> allCategoryParentIds = filteredRecords.stream()
                .flatMap(item -> Arrays.stream(item.getCategoryParentIdPath().split(StrUtil.COMMA)))
                .distinct().collect(Collectors.toList());
        Map<String, String> categoryIdTitleMap = exceptionCategoryService.findIdTitleByIds(allCategoryParentIds);
        // 为每条异常项装载对应的父级异常分类列表
        filteredRecords.forEach(item -> {
            List<String> categoryParentIds = Arrays.stream(item.getCategoryParentIdPath().split(StrUtil.COMMA))
                    .map(String::trim).collect(Collectors.toList());
            item.setCategoryParentList(categoryParentIds.stream().map(categoryIdTitleMap::get).collect(Collectors.toList()));
        });
        return DataHandleUtils.loadUserName(pageResult);
    }

}
