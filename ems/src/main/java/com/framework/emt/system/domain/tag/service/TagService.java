package com.framework.emt.system.domain.tag.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.entity.IResultCode;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.tag.Tag;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.domain.tag.request.TagCreateRequest;
import com.framework.emt.system.domain.tag.request.TagQueryRequest;
import com.framework.emt.system.domain.tag.request.TagUpdateRequest;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;


/**
 * 异常标签 服务层
 *
 * @author gaojia
 * @since 2023-08-02
 */
public interface TagService extends BaseService<Tag> {

    /**
     * 创建标签
     *
     * @param request 参数
     * @return 标签id
     */
    Long create(TagCreateRequest request);

    /**
     * 删除标签
     *
     * @param id 标签id
     */
    void delete(Long id);

    /**
     * 更新标签
     *
     * @param id      标签id
     * @param request 参数
     * @return 标签id
     */
    Long update(Long id, TagUpdateRequest request);

    /**
     * 标签详情
     *
     * @param id 标签id
     * @return 标签信息
     */
    TagResponse detail(Long id);

    /**
     * 标签分页列表
     *
     * @param request 参数
     * @return 标签信息列表
     */
    IPage<TagResponse> page(TagQueryRequest request);

    /**
     * 根据内容查询标签信息
     *
     * @param content 内容
     * @param type    类型
     * @return 标签信息
     */
    Tag findByContent(String content, Integer type);

    /**
     * 根据id和类型获取标签的数量
     *
     * @param id
     * @param type 类型
     * @return 数量
     */
    long countByList(List<Long> id, Integer type);

    /**
     * 根据关联表id查询此关联id下的标签列表
     *
     * @param sourceId       关联表id
     * @param sourceTypeEnum
     * @return
     */
    List<TagInfo> findTagListBySourceId(Long sourceId, SourceTypeEnum sourceTypeEnum);

    /**
     * 根据id查询此条异常标签
     * 数据异常则抛出错误信息
     *
     * @param id    主键id
     * @param error 错误信息
     * @return
     */
    Tag findById(Long id, IResultCode error);

    /**
     * 校验标签内容在数据库必须唯一
     *
     * @param id      主键id
     * @param content 内容
     * @param type
     */
    void checkNameUnique(Long id, String content, Integer type);

    /**
     * 异常原因TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> exceptionReasonTop(StatisticsTimeQueryRequest queryRequest);

    /**
     * 批量新增标签
     *
     * @param contents    标签内容列表
     * @param tagTypeEnum 标签类型
     * @return key为标签内容, value为标签id的map列表
     */
    Map<String, Long> batchCreate(List<String> contents, TagTypeEnum tagTypeEnum);

}
