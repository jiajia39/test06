package com.framework.emt.system.domain.reportnoticeprocess.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcessUser;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上报流程推送 Mapper层
 *
 * @author ds_C
 * @since 2023-07-24
 */
public interface ReportNoticeProcessUserMapper extends BaseMapper<ReportNoticeProcessUser> {

    /**
     * 上报人及上报消息详情
     *
     * @param processUserId
     * @return
     */
    ReportNoticeProcessResponse detail(@Param("processUserId") Long processUserId);

    /**
     * 上报人及上报消息分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<ReportNoticeProcessResponse> page(IPage<ReportNoticeProcessResponse> page,
                                            @Param("request") ReportNoticeProcessUserQueryRequest request);

    /**
     * 根据上报流程主键id查询上报人及上报消息列表
     *
     * @param processId 上报流程id
     * @return
     */
    List<ReportNoticeProcessResponse> findByReportNoticeProcessId(@Param("processId") Long processId);

}
