package com.framework.emt.system.domain.tagexception.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.tagexception.TagException;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionQueryRequest;
import com.framework.emt.system.domain.tagexception.response.TagExceptionResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;


/**
 * 异常标签关联 服务层
 *
 * @author gaojia
 * @since 2023-08-02
 */
public interface TagExceptionService extends BaseService<TagException> {

    /**
     * 通过标签id列表新增关联模块信息
     *
     * @param request 参数
     * @return id
     */
    Long create(TagExceptionCreateRequest request);

    /**
     * 标签新增并关联模块
     *
     * @param request 参数
     */
    void createList(TagExceptionCreateListRequest request);

    /**
     * 通过标签id或者标签名称构建关联关系
     *
     * @param sourceId       关联表id
     * @param tagId          标签id
     * @param content        标签内容
     * @param sourceTypeCode 标签关联类型code
     * @return
     */
    Long create(Long sourceId, Long tagId, String content, Integer sourceTypeCode);

    /**
     * 删除标签关联信息
     *
     * @param id 标签关联id
     */
    void delete(Long id);

    /**
     * 根据id查询标签关联信息详情
     *
     * @param id 标签关联id
     * @return 标签关联信息
     */
    TagExceptionResponse detail(Long id);

    /**
     * 标签关联信息列表
     *
     * @param request 参数
     * @return 标签关联信息
     */
    IPage<TagExceptionResponse> page(TagExceptionQueryRequest request);

    /**
     * 根据关联表id删除对应的 标签关联信息
     *
     * @param sourceId 关联表id
     */
    void deleteBySourceId(Long sourceId);

    /**
     * 根据关联表id列表删除对应的 标签关联信息
     *
     * @param sourceIdList 关联表id列表
     */
    void deleteBySourceIdList(List<Long> sourceIdList);

    /**
     * 根据 关联id,标签id,关联类型查询 关联信息删除异常标签
     *
     * @param tagId      标签id
     * @param sourceId   关联id
     * @param sourceType 关联类型
     * @return
     */
    void deleteTagException(Long tagId, Long sourceId, Integer sourceType);

    /**
     * 根据 关联id,标签id,关联类型查询 关联信息
     *
     * @param tagId      标签id
     * @param sourceId   关联id
     * @param sourceType 关联类型
     * @return 关联信息
     */
    TagException getTagException(Long tagId, Long sourceId, Integer sourceType);

    /**
     * 验证关联表id的类型和关联类型是否一致
     *
     * @param sourceType 关联类型
     * @param sourceId   关联表id
     */
    void checkTypeIsConsistent(Integer sourceType, Long sourceId);

}
