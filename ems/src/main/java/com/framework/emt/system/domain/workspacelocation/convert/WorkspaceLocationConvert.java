package com.framework.emt.system.domain.workspacelocation.convert;

import cn.hutool.core.util.StrUtil;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationCreateRequest;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationImportExcel;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationUpdateRequest;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 作业单元 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface WorkspaceLocationConvert {

    WorkspaceLocationConvert INSTANCE = Mappers.getMapper(WorkspaceLocationConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @param parentIdPath 父级ID路径
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum.ENABLE)")
    WorkspaceLocation createRequestToEntity(WorkspaceLocationCreateRequest request,
                                            String parentIdPath);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    WorkspaceLocation updateRequestToEntity(@MappingTarget WorkspaceLocation entity,
                                            WorkspaceLocationUpdateRequest request);

    /**
     * 实体、父级标题列表转换成响应体
     *
     * @param entity
     * @param parentTitleList 作业单元父级标题列表
     * @return
     */
    @Mapping(target = "parentTitleList", expression = "java(parentTitleList)")
    WorkspaceLocationResponse entityToResponse(WorkspaceLocation entity,
                                               @Context List<String> parentTitleList);

    /**
     * 作业单元标题列表转成实体列表
     *
     * @param titleList 作业单元标题列表
     * @return
     */
    List<WorkspaceLocation> stringListToEntityList(List<String> titleList);

    /**
     * 作业单元标题转成实体
     *
     * @param title 作业单元标题
     * @return
     */
    @Mapping(target = "title", source = "title")
    @Mapping(target = "enableFlag", expression = "java(com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum.ENABLE)")
    @Mapping(target = "parentIdPath", expression = "java(cn.hutool.core.util.StrUtil.EMPTY)")
    WorkspaceLocation stringToEntity(String title);

    /**
     * excel导入数据集转实体列表
     *
     * @param excelImportDataList excel导入的数据集
     * @return
     */
    List<WorkspaceLocation> excelImportDataListToEntityList(List<WorkspaceLocationImportExcel> excelImportDataList);

    /**
     * excel导入数据集转实体列表（携带父级作业单元map列表）
     *
     * @param excelImportDataList excel导入的数据集
     * @param parentWorkspaceMap  key为父级作业单元名称，value为父级作业单元对象的map列表
     * @return
     */
    List<WorkspaceLocation> excelImportDataListToEntityListWithParentWorkspaceMap(List<WorkspaceLocationImportExcel> excelImportDataList,
                                                                                  @Context Map<String, WorkspaceLocation> parentWorkspaceMap);

    /**
     * 给作业单元对应的父ID和父级ID路径赋值
     *
     * @param workspaceLocation  作业单元对象
     * @param excelImportData    excel导入的数据
     * @param parentWorkspaceMap key为父级作业单元名称，value为父级作业单元对象的map列表
     */
    @AfterMapping
    default void setParentIdAndParentPath(@MappingTarget WorkspaceLocation workspaceLocation,
                                          WorkspaceLocationImportExcel excelImportData,
                                          @Context Map<String, WorkspaceLocation> parentWorkspaceMap) {
        Optional.ofNullable(parentWorkspaceMap.get(excelImportData.getParentTitle())).ifPresent(parentWorkspace ->
                workspaceLocation.setParentId(parentWorkspace.getId()).setParentIdPath(parentWorkspace.getParentIdPath() + StrUtil.COMMA + parentWorkspace.getId())
        );
    }

}
