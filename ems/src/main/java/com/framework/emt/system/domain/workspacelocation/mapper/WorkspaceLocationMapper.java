package com.framework.emt.system.domain.workspacelocation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationQueryRequest;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationTreeResponse;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceExportResponse;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业单元 Mapper层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface WorkspaceLocationMapper extends BaseMapper<WorkspaceLocation> {

    /**
     * 作业单元分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<WorkspaceLocationResponse> page(IPage<WorkspaceLocationResponse> page,
                                          @Param("request") WorkspaceLocationQueryRequest request);

    /**
     * 作业单元列表
     *
     * @return
     */
    List<WorkspaceLocationTreeResponse> list();

    /**
     * 根据作业单元id列表或作业单元名称列表查询作业单元列表
     *
     * @param ids    作业单元id列表
     * @param titles 作业单元名称列表
     * @return
     */
    List<WorkspaceLocation> findWorkspaceList(@Param("ids") List<String> ids, @Param("titles") List<String> titles);

    /**
     * 根据作业单元名称+父级id拼接的字符串
     * 查询出此条作业单元是否存在
     *
     * @param id               作业单元id
     * @param titleParentIdStr 作业单元名称+父级id拼接的字符串
     * @return
     */
    WorkspaceLocation findByTitleParentIdStr(@Param("id") Long id, @Param("titleParentIdStr") String titleParentIdStr);

    /**
     * 根据作业单元名称+父级作业单元名称拼接的字符串列表
     * 查询出作业单元是否存在
     *
     * @param titles 作业单元名称+父级作业单元名称拼接的字符串列表
     * @return
     */
    WorkspaceExportResponse findByTitles(@Param("titles") List<String> titles);

    /**
     * 根据作业单元id列表查询作业单元列表
     *
     * @param ids 作业单元id列表
     * @return
     */
    List<WorkspaceLocationResponse> listByIds(@Param("ids") List<Long> ids);

}
