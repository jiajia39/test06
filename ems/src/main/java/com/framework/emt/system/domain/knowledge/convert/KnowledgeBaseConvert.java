package com.framework.emt.system.domain.knowledge.convert;

import com.framework.emt.system.domain.knowledge.KnowledgeBase;
import com.framework.emt.system.infrastructure.common.object.TitleMap;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCreateRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseImportExcel;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseUpdateRequest;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 知识库 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface KnowledgeBaseConvert {

    KnowledgeBaseConvert INSTANCE = Mappers.getMapper(KnowledgeBaseConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    KnowledgeBase createRequestToEntity(KnowledgeBaseCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    KnowledgeBase updateRequestToEntity(@MappingTarget KnowledgeBase entity, KnowledgeBaseUpdateRequest request);

    /**
     * excel导入数据集转实体列表
     *
     * @param importDataList excel导入的数据集
     * @param titleMap       知识库分类和异常项Map列表包装类
     * @return
     */
    List<KnowledgeBase> importDataListToEntityList(List<KnowledgeBaseImportExcel> importDataList, @Context TitleMap titleMap);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default EnableFlagEnum enableFlagCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(EnableFlagEnum.class, code);
    }

    /**
     * 给知识库对应的知识库分类id和异常项id赋值
     *
     * @param knowledgeBase 知识库
     * @param importData    excel导入结果集
     * @param titleMap      知识库分类和异常项Map列表包装类
     */
    @AfterMapping
    default void setCategoryIdAndItemId(@MappingTarget KnowledgeBase knowledgeBase,
                                        KnowledgeBaseImportExcel importData, @Context TitleMap titleMap) {
        knowledgeBase.setKnowledgeBaseCategoryId(titleMap.getCategoryTitleOfIdMap().get(importData.getKnowledgeBaseCategoryTitle()));
        knowledgeBase.setExceptionItemId(titleMap.getItemTitleOfIdMap().get(importData.getExceptionItemTitle()));
    }

}
