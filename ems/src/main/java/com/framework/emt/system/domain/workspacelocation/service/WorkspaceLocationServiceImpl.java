package com.framework.emt.system.domain.workspacelocation.service;

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
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.constant.WorkspaceConstant;
import com.framework.emt.system.domain.workspacelocation.constant.code.WorkspaceLocationErrorCode;
import com.framework.emt.system.domain.workspacelocation.convert.WorkspaceLocationConvert;
import com.framework.emt.system.domain.workspacelocation.mapper.WorkspaceLocationMapper;
import com.framework.emt.system.domain.workspacelocation.request.*;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * 作业单元 实现类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Service
@RequiredArgsConstructor
public class WorkspaceLocationServiceImpl extends BaseServiceImpl<WorkspaceLocationMapper, WorkspaceLocation> implements WorkspaceLocationService {

    @Override
    public Long create(WorkspaceLocationCreateRequest request) {
        // 校验同一父级作业单元下的子作业单元名称不能重复
        validateSameParentUnique(null, request.getTitle(), request.getParentId());
        // 获取父级ID路径
        String parentIdPath = NumberUtils.LONG_ZERO.equals(request.getParentId()) ? StrUtil.EMPTY : joinParentPath(request.getParentId());
        // 添加作业单元
        Long id = create(WorkspaceLocationConvert.INSTANCE.createRequestToEntity(request, parentIdPath));

        // 清除作业单元树状图缓存
        clearCache();
        return id;
    }

    @Override
    public void delete(Long id) {
        findByIdThrowErr(id);
        // 校验是否存在子集
        long childCount = this.count(new LambdaQueryWrapper<WorkspaceLocation>().eq(WorkspaceLocation::getParentId, id));
        if (childCount > NumberUtils.LONG_ZERO) {
            throw new ServiceException(WorkspaceLocationErrorCode.THIS_WORKSPACE_LOCATION_BOTTOM_HAVE_OTHER_WORKSPACE);
        }
        // 校验是否存在未完成的异常提报
        ExceptionTaskSubmitService taskSubmitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
        List<Long> taskIds = taskSubmitService.findById(null, null, id, null);
        if (CollectionUtil.isNotEmpty(taskIds)) {
            ExceptionTaskService taskService = SpringUtil.getBean(ExceptionTaskService.class);
            if (taskService.findFinishCountByIds(taskIds) > 0) {
                throw new ServiceException(WorkspaceLocationErrorCode.THIS_WORKSPACE_LOCATION_BOTTOM_HAVE_TASK_SUBMIT);
            }
        }
        this.deleteById(id);

        // 清除作业单元树状图缓存
        clearCache();
    }

    @Override
    public Long update(Long id, WorkspaceLocationUpdateRequest request) {
        WorkspaceLocation workspaceLocation = findByIdThrowErr(id);
        // 校验同一父级作业单元下的子作业单元名称不能重复
        validateSameParentUnique(id, request.getTitle(), workspaceLocation.getParentId());
        this.update(WorkspaceLocationConvert.INSTANCE.updateRequestToEntity(workspaceLocation, request));

        // 清除作业单元树状图缓存
        clearCache();
        return id;
    }

    @Override
    public WorkspaceLocationResponse detail(Long id) {
        WorkspaceLocation workspaceLocation = findByIdThrowErr(id);
        // 获取父级作业单元列表
        List<String> parentTitleList = findTitleList(DataHandleUtils.splitStr(workspaceLocation.getParentIdPath()));
        return WorkspaceLocationConvert.INSTANCE.entityToResponse(workspaceLocation, parentTitleList);
    }

    @Override
    public IPage<WorkspaceLocationResponse> page(WorkspaceLocationQueryRequest request) {
        return DataHandleUtils.loadUserName(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public List<WorkspaceLocationTreeResponse> tree() {
        //从缓存取得数据不是最新的
        return this.buildTree(this.baseMapper.list());
//        String cacheName = WorkspaceConstant.WORKSPACE_TREE_CACHE_NAME;
//        String keyPrefix = WorkspaceConstant.WORKSPACE_TREE_KEY_PREFIX;
//        String cacheKey = WorkspaceConstant.WORKSPACE_TREE_CACHE_KEY;
//        String tenantId = AuthUtil.getTenantId();
//        // 从缓存获取数据
//        List<WorkspaceLocationTreeResponse> workspaceTree = CacheUtil.get(cacheName, keyPrefix, cacheKey, tenantId, List.class);
//        if (workspaceTree == null) {
//            // 缓存中没有数据，从数据库获取并缓存到Redis中
//            workspaceTree = this.buildTree(this.baseMapper.list());
//            // 将数据存入缓存
//            CacheUtil.put(cacheName, keyPrefix, cacheKey, workspaceTree, tenantId);
//        }
//        return workspaceTree;
    }

    @Override
    public WorkspaceLocationResponse detailApp(WorkspaceLocationDetailRequest request) {
        return WorkspaceLocationConvert.INSTANCE.entityToResponse(findByIdThrowErr(getWorkspaceLocationId(request)), null);
    }

    @Override
    @DSTransactional
    public void importWorkspaceLocation(List<WorkspaceLocationImportExcel> excelImportDataList,
                                        List<WorkspaceLocation> existParentWorkspaceList, List<String> notExistParentTitleList) {
        // 获取excel中的父级作业单元map列表
        Map<String, WorkspaceLocation> parentWorkspaceMap = getParentWorkspaceLocationMap(existParentWorkspaceList, notExistParentTitleList);
        // 导入作业单元excel数据集
        List<WorkspaceLocation> WorkspaceLocationList = CollectionUtil.isEmpty(parentWorkspaceMap)
                ? WorkspaceLocationConvert.INSTANCE.excelImportDataListToEntityList(excelImportDataList)
                : WorkspaceLocationConvert.INSTANCE.excelImportDataListToEntityListWithParentWorkspaceMap(excelImportDataList, parentWorkspaceMap);
        this.saveBatch(WorkspaceLocationList);
        // 清除作业单元树状图缓存
        clearCache();
    }

    @Override
    public WorkspaceLocation findByTitles(List<String> titles) {
        return this.getOne(new LambdaQueryWrapper<WorkspaceLocation>()
                .eq(WorkspaceLocation::getParentId, NumberUtils.LONG_ZERO)
                .in(WorkspaceLocation::getTitle, titles)
                .last("limit 1"));
    }

    @Override
    public WorkspaceLocation findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, WorkspaceLocationErrorCode.WORKSPACE_LOCATION_INFO_NOT_FIND);
    }

    @Override
    public Map<Long, WorkspaceLocationResponse> getMapByIds(List<Long> workspaceIds) {
        if (CollectionUtil.isEmpty(workspaceIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        List<WorkspaceLocationResponse> workspaceList = this.baseMapper.listByIds(workspaceIds);
        if (CollectionUtil.isEmpty(workspaceIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        return workspaceList.stream().collect(toMap(WorkspaceLocationResponse::getId, Function.identity(), (a, b) -> a));
    }

    /**
     * 根据作业单元父级id和父级id路径
     * 拼接出此条作业单元的父级id路径
     *
     * @param parentId
     * @return
     */
    public String joinParentPath(Long parentId) {
        return findByIdThrowErr(parentId).getParentIdPath() + StrUtil.COMMA + parentId;
    }

    /**
     * 根据作业单元id列表查询出对应的作业单元名称列表
     *
     * @param ids 主键id列表
     * @return
     */
    private List<String> findTitleList(List<String> ids) {
        return this.baseMapper.findWorkspaceList(ids, null).stream().map(WorkspaceLocation::getTitle).collect(Collectors.toList());
    }


    /**
     * 得到excel中在数据库不存在的父级作业单元列表
     *
     * @param notExistParentTitleList excel中在数据库不存在的父级作业单元名称列表
     * @return
     */
    private List<WorkspaceLocation> getNotExistWorkspaceList(List<String> notExistParentTitleList) {
        List<WorkspaceLocation> workspaceLocationList = WorkspaceLocationConvert.INSTANCE.stringListToEntityList(notExistParentTitleList);
        this.saveBatch(workspaceLocationList);
        return workspaceLocationList;
    }

    /**
     * 通过解读密文获取到作业单元主键id
     *
     * @param request
     * @return
     */
    private Long getWorkspaceLocationId(WorkspaceLocationDetailRequest request) {
        String plainText = new String(Base64.decodeBase64(request.getCipherText().getBytes()));
        Matcher matcher = WorkspaceConstant.CIPHER_TEXT_PATTERN.matcher(plainText);
        return matcher.find() ? Long.parseLong(matcher.group(NumberUtils.INTEGER_ONE)) : null;
    }

    /**
     * 构建作业单元树状图
     *
     * @param list 作业单元列表
     * @return 作业单元树状图
     */
    private List<WorkspaceLocationTreeResponse> buildTree(List<WorkspaceLocationTreeResponse> list) {
        Map<Long, List<WorkspaceLocationTreeResponse>> workspaceLocationMap = list.stream().collect(Collectors.groupingBy(WorkspaceLocationTreeResponse::getParentId));
        list.forEach(workspaceLocation -> workspaceLocation.setChildren(workspaceLocationMap.getOrDefault(workspaceLocation.getId(), Collections.emptyList())));
        return list.stream().filter(workspaceLocation -> NumberUtils.LONG_ZERO.equals(workspaceLocation.getParentId())).collect(Collectors.toList());
    }

    /**
     * 清除redis中的作业单元树状图
     */
    private void clearCache() {
        String cacheName = WorkspaceConstant.WORKSPACE_TREE_CACHE_NAME;
        String keyPrefix = WorkspaceConstant.WORKSPACE_TREE_KEY_PREFIX;
        String tenantId = AuthUtil.getTenantId();
        CacheUtil.clear(cacheName, keyPrefix, tenantId);
    }

    /**
     * 校验同一父级作业单元下的子作业单元名称不能重复
     *
     * @param id       主键id
     * @param title    作业单元名称
     * @param parentId 作业单元父id
     */
    private void validateSameParentUnique(Long id, String title, Long parentId) {
        String titleParentIdStr = title + parentId;
        WorkspaceLocation workspaceLocation = this.baseMapper.findByTitleParentIdStr(id, titleParentIdStr);
        if (ObjectUtil.isNotNull(workspaceLocation)) {
            if (NumberUtils.LONG_ZERO.equals(parentId)) {
                throw new ServiceException(WorkspaceLocationErrorCode.TOP_TITLE_CAN_NOT_EQUALS);
            }
            throw new ServiceException(WorkspaceLocationErrorCode.SAME_PARENT_OF_CHILD_TITLE_CAN_NOT_EQUALS);
        }
    }

    /**
     * 获取excel中的父级作业单元map列表
     *
     * @param existParentWorkspaceList excel中在数据库存在的父级作业单元列表
     * @param notExistParentTitleList  excel中在数据库不存在的父级作业单元名称列表
     * @return key为父级作业单元名称，value为父级作业单元对象的map列表
     */
    private Map<String, WorkspaceLocation> getParentWorkspaceLocationMap(List<WorkspaceLocation> existParentWorkspaceList, List<String> notExistParentTitleList) {
        // existParentWorkspaceList为空、notExistParentTitleList为空：父级作业单元名称列表为空
        if (CollectionUtil.isEmpty(existParentWorkspaceList) && CollectionUtil.isEmpty(notExistParentTitleList)) {
            return Collections.emptyMap();
        }
        // existParentWorkspaceList不为空、notExistParentTitleList为空：父级作业单元名称列表的名称都在数据库存在
        if (CollectionUtil.isNotEmpty(existParentWorkspaceList) && CollectionUtil.isEmpty(notExistParentTitleList)) {
            return existParentWorkspaceList.stream().collect(Collectors.toMap(WorkspaceLocation::getTitle, Function.identity()));
        }
        // existParentWorkspaceList为空、notExistParentTitleList不为空：父级作业单元名称列表的名称都在数据库不存在
        if (CollectionUtil.isEmpty(existParentWorkspaceList) && CollectionUtil.isNotEmpty(notExistParentTitleList)) {
            return getNotExistWorkspaceList(notExistParentTitleList).stream().collect(Collectors.toMap(WorkspaceLocation::getTitle, Function.identity()));
        }
        // existParentWorkspaceList不为空、notExistParentTitleList不为空：父级作业单元名称列表的名称部分在数据库存在、部分在数据库不存在
        List<WorkspaceLocation> notExistParentWorkspaceList = getNotExistWorkspaceList(notExistParentTitleList);
        return Stream.of(existParentWorkspaceList, notExistParentWorkspaceList).flatMap(List::stream).distinct().collect(Collectors.toMap(WorkspaceLocation::getTitle, Function.identity()));
    }

}
