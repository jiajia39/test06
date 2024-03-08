package com.framework.emt.system.domain.tag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.tag.Tag;
import com.framework.emt.system.domain.tag.response.TagInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常标签 Mapper层
 *
 * @author gaojia
 * @since 2023-08-02
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据关联表id查询此关联id下的标签列表
     *
     * @param sourceId       关联表id
     * @param sourceTypeCode 标签关联类型编码
     * @return
     */
    List<TagInfo> findTagListBySourceId(@Param("sourceId") Long sourceId, @Param("sourceTypeCode") Integer sourceTypeCode);


    /**
     * 异常原因项TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> exceptionReasonTop(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);
}
