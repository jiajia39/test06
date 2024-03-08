package com.framework.emt.system.domain.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.knowledge.response.CategoryAndChildResponse;
import com.framework.emt.system.domain.knowledge.KnowledgeBaseCategory;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryQueryRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseCategoryResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库分类 Mapper层
 *
 * @author ds_C
 * @since 2023-07-14
 */
public interface KnowledgeBaseCategoryMapper extends BaseMapper<KnowledgeBaseCategory> {

    /**
     * 知识库分类分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<KnowledgeBaseCategoryResponse> pageKnowledgeBaseCategory(IPage<KnowledgeBaseCategoryResponse> page,
                                                                   @Param("request") KnowledgeBaseCategoryQueryRequest request);

    /**
     * 查询知识库分类信息以及知识分类下是否存在知识库
     *
     * @param id 知识库分类id
     * @return
     */
    CategoryAndChildResponse findCategoryAndExistChild(@Param("id") Long id);

    /**
     * 查询知识库分类信息数量以及知识分类下是否存在知识库
     *
     * @param idList 知识库分类id列表
     * @return
     */
    CategoryAndChildResponse findCategoryCountAndExistChild(@Param("idList") List<Long> idList);

}
