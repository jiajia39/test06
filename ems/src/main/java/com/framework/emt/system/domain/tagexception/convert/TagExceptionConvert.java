package com.framework.emt.system.domain.tagexception.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.tagexception.TagException;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateRequest;
import com.framework.emt.system.domain.tagexception.response.TagExceptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常标签关联表 转换类
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Mapper
public interface TagExceptionConvert {

    TagExceptionConvert INSTANCE = Mappers.getMapper(TagExceptionConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return
     */
    TagException createRequestToEntity(TagExceptionCreateRequest request);

    /**
     * 参数转换成实体
     *
     * @param sourceId
     * @param tagId
     * @param sourceType
     * @return
     */
    TagException paramsToEntity(Long sourceId, Long tagId, Integer sourceType);

    /**
     * 参数转换成创建参数
     *
     * @param sourceId   关联表id
     * @param content    异常标签名称
     * @param sourceType 异常关联类型
     * @return
     */
    TagExceptionCreateRequest paramsToCreatRequest(Long sourceId, String content, Integer sourceType);

    /**
     * 实体转换成响应体
     *
     * @param entity
     * @return
     */
    TagExceptionResponse entityToResponse(TagException entity);

    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<TagExceptionResponse> entityList2Vo(List<TagException> itemList);

    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<TagExceptionResponse> pageVo(IPage<TagException> pages) {
        IPage<TagExceptionResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(TagExceptionConvert.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }
}
