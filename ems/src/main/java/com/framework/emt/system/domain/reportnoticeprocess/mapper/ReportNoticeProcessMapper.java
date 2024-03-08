package com.framework.emt.system.domain.reportnoticeprocess.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcess;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportAndChildResponse;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上报流程 Mapper层
 *
 * @author ds_C
 * @since 2023-07-17
 */
public interface ReportNoticeProcessMapper extends BaseMapper<ReportNoticeProcess> {

    /**
     * 上报流程详情
     *
     * @param processId 上报流程主键id
     * @return
     */
    ReportNoticeProcessResponse detail(@Param("processId") Long processId);

    /**
     * 上报流程分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<ReportNoticeProcessResponse> page(IPage<ReportNoticeProcessResponse> page, @Param("request") ReportNoticeProcessQueryRequest request);

    /**
     * 查询上报流程信息以及上报流程下是否存在异常流程
     *
     * @param processId 上报流程id
     * @return
     */
    ReportAndChildResponse findReportAndExistChild(@Param("processId") Long processId);

    /**
     * 查询上报流程信息数量以及上报流程下异常流程的数量
     *
     * @param idList 上报流程id列表
     * @return
     */
    ReportAndChildResponse findReportCountAndExistChild(@Param("idList") List<Long> idList);

}
