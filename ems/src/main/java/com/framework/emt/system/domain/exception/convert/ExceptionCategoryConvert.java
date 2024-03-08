package com.framework.emt.system.domain.exception.convert;

import cn.hutool.core.util.StrUtil;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryImportExcel;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryUpdateRequest;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 异常分类 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface ExceptionCategoryConvert {

    ExceptionCategoryConvert INSTANCE = Mappers.getMapper(ExceptionCategoryConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @param parentIdPath 父级ID路径
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum.ENABLE)")
    ExceptionCategory createRequestToEntity(ExceptionCategoryCreateRequest request,
                                            String parentIdPath);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    ExceptionCategory updateRequestToEntity(@MappingTarget ExceptionCategory entity,
                                            ExceptionCategoryUpdateRequest request);

    /**
     * 实体、父级标题列表转换成响应体
     *
     * @param entity
     * @param parentTitleList 异常分类父级标题列表
     * @return
     */
    @Mapping(target = "parentTitleList", expression = "java(parentTitleList)")
    ExceptionCategoryResponse entityToResponse(ExceptionCategory entity,
                                               @Context List<String> parentTitleList);

    /**
     * 异常分类标题列表转成实体列表
     *
     * @param titleList 异常分类标题列表
     * @return
     */
    List<ExceptionCategory> stringListToEntityList(List<String> titleList);

    /**
     * 异常分类标题转成实体
     *
     * @param title 异常分类标题
     * @return
     */
    @Mapping(target = "title", source = "title")
    @Mapping(target = "enableFlag", expression = "java(com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum.ENABLE)")
    @Mapping(target = "parentIdPath", expression = "java(cn.hutool.core.util.StrUtil.EMPTY)")
    ExceptionCategory stringToEntity(String title);

    /**
     * excel导入数据集转实体列表
     *
     * @param excelImportDataList excel导入的数据集
     * @return
     */
    List<ExceptionCategory> excelImportDataListToEntityList(List<ExceptionCategoryImportExcel> excelImportDataList);

    /**
     * excel导入数据集转实体列表（携带父级异常分类map列表）
     *
     * @param excelImportDataList excel导入的数据集
     * @param parentCategoryMap   key为父级异常分类名称，value为父级异常分类对象的map列表
     * @return
     */
    List<ExceptionCategory> excelImportDataListToEntityListWithParentCategoryMap(List<ExceptionCategoryImportExcel> excelImportDataList,
                                                                                 @Context Map<String, ExceptionCategory> parentCategoryMap);

    /**
     * 给异常分类对应的父ID和父级ID路径赋值
     *
     * @param exceptionCategory 异常分类对象
     * @param excelImportData   excel导入的数据
     * @param parentCategoryMap key为父级异常分类名称，value为父级异常分类对象的map列表
     */
    @AfterMapping
    default void setParentIdAndParentPath(@MappingTarget ExceptionCategory exceptionCategory,
                                          ExceptionCategoryImportExcel excelImportData,
                                          @Context Map<String, ExceptionCategory> parentCategoryMap) {
        Optional.ofNullable(parentCategoryMap.get(excelImportData.getParentTitle())).ifPresent(parentCategory ->
                {
                    exceptionCategory.setParentId(parentCategory.getId());
                    exceptionCategory.setParentIdPath(parentCategory.getParentIdPath() + StrUtil.COMMA + parentCategory.getId());
                }
        );
    }

}
