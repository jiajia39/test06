package com.framework.emt.system.domain.reportnoticeprocess.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcess;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessCreateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUpdateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 上报流程 服务层
 *
 * @author ds_C
 * @since 2023-07-17
 */
public interface ReportNoticeProcessService extends BaseService<ReportNoticeProcess> {

    /**
     * 上报流程基础信息创建
     *
     * @param request
     * @return 上报流程id
     */
    Long create(ReportNoticeProcessCreateRequest request);

    /**
     * 上报流程删除
     *
     * @param id 上报流程id
     */
    void delete(Long id);

    /**
     * 上报流程批量删除
     *
     * @param ids 上报流程id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 上报流程更新
     *
     * @param id      上报流程id
     * @param request
     * @return 上报流程id
     */
    Long update(Long id, ReportNoticeProcessUpdateRequest request);

    /**
     * 上报流程详情
     *
     * @param id 上报流程id
     * @return
     */
    ReportNoticeProcessResponse detail(Long id);

    /**
     * 上报流程分页列表
     *
     * @param request
     * @return
     */
    IPage<ReportNoticeProcessResponse> page(ReportNoticeProcessQueryRequest request);

    /**
     * 上报流程禁用启用
     *
     * @param enableFlag 禁用启用状态
     * @param idList     上报流程id列表
     */
    void changeEnableFlag(Integer enableFlag, List<Long> idList);

    /**
     * 根据id查询此条上报流程
     * 数据异常则抛出错误信息
     *
     * @param id 上报流程id
     * @return
     */
    ReportNoticeProcess findByIdThrowErr(Long id);

    /**
     * 校验响应超时上报流程和处理超时上报流程在数据库中必须存在
     *
     * @param responseReportNoticeProcessId 响应超时上报流程id
     * @param handingReportNoticeProcessId  处理超时上报流程id
     */
    void checkExist(Long responseReportNoticeProcessId,
                    Long handingReportNoticeProcessId);

    /**
     * 通过上报人id装载上报人信息列表
     *
     * @param pageResult
     * @return
     */
    IPage<ReportNoticeProcessResponse> loadUserInfoList(IPage<ReportNoticeProcessResponse> pageResult);

}
