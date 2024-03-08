package com.framework.emt.system.domain.tag.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.tag.Tag;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.domain.tag.request.TagCreateRequest;
import com.framework.emt.system.domain.tag.request.TagUpdateRequest;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常标签 转换类
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Mapper
public interface TagConvert {

    TagConvert INSTANCE = Mappers.getMapper(TagConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return
     */
    @Mapping(target = "type", expression = "java(tagTypeEnumCodeToEnum(request.getType()))")
    Tag createRequestToEntity(TagCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity  标签实体表
     * @param request 参数
     * @return
     */
    @Mapping(target = "type", expression = "java(tagTypeEnumCodeToEnum(request.getType()))")
    Tag updateRequestToEntity(@MappingTarget Tag entity, TagUpdateRequest request);

    /**
     * 参数转换成创建参数
     *
     * @param content 标签内容
     * @param type    标签类型
     * @return
     */
    TagCreateRequest paramsToCreateRequest(String content, Integer type);

    /**
     * 实体转换成响应体
     *
     * @param entity
     * @return
     */
    TagResponse entityToResponse(Tag entity);

    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<TagResponse> entityList2Vo(List<Tag> itemList);

    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<TagResponse> pageVo(IPage<Tag> pages) {
        IPage<TagResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(TagConvert.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }


    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default TagTypeEnum tagTypeEnumCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(TagTypeEnum.class, code);
    }
}
