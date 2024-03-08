package com.framework.emt.system.domain.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.knowledge.KnowledgeBase;
import com.framework.emt.system.domain.knowledge.request.*;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseExportResponse;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseResponse;
import com.framework.emt.system.infrastructure.common.object.TitleMap;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 知识库 服务层
 *
 * @author ds_C
 * @since 2023-07-14
 */
public interface KnowledgeBaseService extends BaseService<KnowledgeBase> {

    /**
     * 知识库创建
     *
     * @param request
     * @return 主键id
     */
    Long create(KnowledgeBaseCreateRequest request);

    /**
     * 知识库删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 知识库批量删除
     *
     * @param ids 主键id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 知识库更新
     *
     * @param id      主键id
     * @param request
     * @return 主键id
     */
    Long update(Long id, KnowledgeBaseUpdateRequest request);

    /**
     * 知识库详情
     *
     * @param id 主键id
     * @return
     */
    KnowledgeBaseResponse detail(Long id);

    /**
     * 知识库分页列表
     *
     * @param request
     * @return
     */
    IPage<KnowledgeBaseResponse> page(KnowledgeBaseQueryRequest request);

    /**
     * 移动端知识库分页列表
     * 已启用状态
     *
     * @param request
     * @return
     */
    IPage<KnowledgeBaseResponse> pageApp(KnowledgeBaseAppQueryRequest request);

    /**
     * 移动端知识库详情
     * 已启用状态
     *
     * @param id 主键id
     * @return
     */
    KnowledgeBaseResponse detailApp(Long id);

    /**
     * 知识库禁用启用
     *
     * @param enableFlag 禁用启用状态
     * @param idList     主键id列表
     */
    void changeEnableFlag(Integer enableFlag, List<Long> idList);

    /**
     * 知识库标签新增
     *
     * @param request
     * @return
     */
    Long createTag(KnowledgeBaseTagCreateRequest request);

    /**
     * 知识库标签关系新增
     *
     * @param request
     * @return 异常标签id
     */
    Long createTagRelation(KnowledgeBaseTagRelationCreateRequest request);

    /**
     * 知识库标签删除
     *
     * @param request
     */
    void deleteTag(KnowledgeBaseTagDeleteRequest request);

    /**
     * 知识库导出
     *
     * @param request
     * @return
     */
    List<KnowledgeBaseExportResponse> export(KnowledgeBaseExportQueryRequest request);

    /**
     * 知识库导入
     *
     * @param importDataList excel到入结果集
     * @param keyWordsList   关键词列表
     * @param titleMap       知识库分类和异常项Map列表包装类
     */
    void importDataList(List<KnowledgeBaseImportExcel> importDataList, List<String> keyWordsList, TitleMap titleMap);

    /**
     * 通过知识库标题列表查询知识库
     *
     * @param titleList 知识库标题列表
     * @return
     */
    KnowledgeBase findByTitles(List<String> titleList);

}
