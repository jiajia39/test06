package com.framework.emt.system.domain.knowledge.convert;

import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryCreateRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryUpdateRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseCategoryResponse;
import com.framework.emt.system.domain.knowledge.KnowledgeBaseCategory;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 知识库分类 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface KnowledgeBaseCategoryConvert {

    KnowledgeBaseCategoryConvert INSTANCE = Mappers.getMapper(KnowledgeBaseCategoryConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    KnowledgeBaseCategory createRequestToEntity(KnowledgeBaseCategoryCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    KnowledgeBaseCategory updateRequestToEntity(@MappingTarget KnowledgeBaseCategory entity,
                                                KnowledgeBaseCategoryUpdateRequest request);

    /**
     * 实体转换成响应体
     *
     * @param entity
     * @return
     */
    KnowledgeBaseCategoryResponse entityToResponse(KnowledgeBaseCategory entity);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default EnableFlagEnum enableFlagCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(EnableFlagEnum.class, code);
    }

}
