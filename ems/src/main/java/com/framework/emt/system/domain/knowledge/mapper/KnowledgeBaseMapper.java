package com.framework.emt.system.domain.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.knowledge.KnowledgeBase;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseAppQueryRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseExportQueryRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseQueryRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseExportResponse;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库 Mapper层
 *
 * @author ds_C
 * @since 2023-07-14
 */
public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBase> {

    /**
     * 知识库详情
     *
     * @param knowledgeBaseId
     * @return
     */
    KnowledgeBaseResponse detail(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 知识库分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<KnowledgeBaseResponse> page(IPage<KnowledgeBaseResponse> page, @Param("request") KnowledgeBaseQueryRequest request);

    /**
     * 移动端知识库分页列表
     * 已启用状态
     *
     * @param page
     * @param request
     * @return
     */
    IPage<KnowledgeBaseResponse> pageApp(IPage<KnowledgeBaseResponse> page, @Param("request") KnowledgeBaseAppQueryRequest request);

    /**
     * 移动端知识库详情
     * 已启用状态
     *
     * @param knowledgeBaseId
     * @return
     */
    KnowledgeBaseResponse detailApp(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 查询导出的数量
     *
     * @param request
     * @param idList  主键id列表
     * @return
     */
    long findExportDataCount(@Param("request") KnowledgeBaseExportQueryRequest request, @Param("idList") List<Long> idList);

    /**
     * 查询导出的数据列表
     *
     * @param request
     * @param idList  主键id列表
     * @return
     */
    List<KnowledgeBaseExportResponse> findExportData(@Param("request") KnowledgeBaseExportQueryRequest request, @Param("idList") List<Long> idList);

}
