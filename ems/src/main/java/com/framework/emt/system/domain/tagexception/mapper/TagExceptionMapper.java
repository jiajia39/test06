package com.framework.emt.system.domain.tagexception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.tagexception.TagException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常标签关联 Mapper层
 *
 * @author gaojia
 * @since 2023-08-02
 */
public interface TagExceptionMapper extends BaseMapper<TagException> {
    List<Long> getIdByTagIds(@Param("tagIds")  List<Long> tagIds);
}
