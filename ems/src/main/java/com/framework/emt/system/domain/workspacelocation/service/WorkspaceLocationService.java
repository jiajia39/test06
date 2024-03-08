package com.framework.emt.system.domain.workspacelocation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.request.*;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 作业单元 服务层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface WorkspaceLocationService extends BaseService<WorkspaceLocation> {

    /**
     * 作业单元创建
     *
     * @param request
     * @return 主键id
     */
    Long create(WorkspaceLocationCreateRequest request);

    /**
     * 作业单元删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 作业单元更新
     *
     * @param id      主键id
     * @param request
     * @return 主键id
     */
    Long update(Long id, WorkspaceLocationUpdateRequest request);

    /**
     * 作业单元详情
     *
     * @param id 主键id
     * @return
     */
    WorkspaceLocationResponse detail(Long id);

    /**
     * 作业单元分页列表
     *
     * @param request
     * @return
     */
    IPage<WorkspaceLocationResponse> page(WorkspaceLocationQueryRequest request);

    /**
     * 作业单元树状图
     *
     * @return
     */
    List<WorkspaceLocationTreeResponse> tree();

    /**
     * 作业单元导入
     *
     * @param excelImportDataList      excel导入的数据集
     * @param existParentWorkspaceList excel中在数据库存在的父级作业单元列表
     * @param notExistParentTitleList  excel中在数据库不存在的父级作业单元名称列表
     */
    void importWorkspaceLocation(List<WorkspaceLocationImportExcel> excelImportDataList,
                                 List<WorkspaceLocation> existParentWorkspaceList, List<String> notExistParentTitleList);

    /**
     * 通过二维码查询作业单元详情
     *
     * @param request
     * @return
     */
    WorkspaceLocationResponse detailApp(WorkspaceLocationDetailRequest request);

    /**
     * 通过作业单元名称列表查询作业单元对象
     *
     * @param titles 作业单元名称列表
     * @return
     */
    WorkspaceLocation findByTitles(List<String> titles);

    /**
     * 根据id获取此条作业单元
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    WorkspaceLocation findByIdThrowErr(Long id);

    /**
     * 通过作业单元id列表获取 作业单元map
     *
     * @param workspaceIds 作业单元id列表
     * @return 作业单元map
     */
    Map<Long, WorkspaceLocationResponse> getMapByIds(List<Long> workspaceIds);

}
