package com.framework.emt.system.domain.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.knowledge.KnowledgeBaseCategory;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryCreateRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryQueryRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryUpdateRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseCategoryResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 知识库分类 服务层
 *
 * @author ds_C
 * @since 2023-07-14
 */
public interface KnowledgeBaseCategoryService extends BaseService<KnowledgeBaseCategory> {

    /**
     * 知识库分类创建
     *
     * @param request
     * @return 主键id
     */
    Long create(KnowledgeBaseCategoryCreateRequest request);

    /**
     * 知识库分类删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 知识库分类批量删除
     *
     * @param ids 主键id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 知识库分类更新
     *
     * @param id      主键id
     * @param request
     * @return 主键id
     */
    Long update(Long id, KnowledgeBaseCategoryUpdateRequest request);

    /**
     * 知识库分类详情
     *
     * @param id 主键id
     * @return
     */
    KnowledgeBaseCategoryResponse detail(Long id);

    /**
     * 知识库分类分页列表
     *
     * @param request
     * @return
     */
    IPage<KnowledgeBaseCategoryResponse> page(KnowledgeBaseCategoryQueryRequest request);

    /**
     * 知识库分类禁用启用
     *
     * @param enableFlag 禁用启用状态
     * @param ids        主键id列表
     */
    void changeEnableFlag(Integer enableFlag, List<Long> ids);

    /**
     * 根据id查询此条知识库分类
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    KnowledgeBaseCategory findByIdThrowErr(Long id);

    /**
     * 根据知识库分类标题列表查询知识库分类列表
     *
     * @param titles 知识库分类标题列表
     * @return
     */
    List<KnowledgeBaseCategory> findListByTitles(List<String> titles);

}
