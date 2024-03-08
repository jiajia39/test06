package com.framework.emt.system.infrastructure.exception.task.cooperation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationExportQueryRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationQueryRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;
import com.framework.emt.system.domain.task.cooperation.response.TaskCooperationExportResponse;
import com.framework.emt.system.domain.task.handing.request.HandingCooperationQueryRequest;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务协同 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskCooperationService extends BaseService<ExceptionTaskCooperation> {

    /**
     * 异常协同创建
     *
     * @param request
     * @param exceptionTaskHanding
     * @param currentTime             当前时间
     * @param reportNoticeProcessName 超时上报流程名称
     * @param cooperateExtendFields   协同附加字段
     * @return
     */
    ExceptionTaskCooperation create(TaskCooperationCreateRequest request, ExceptionTaskHanding exceptionTaskHanding,
                                    LocalDateTime currentTime, String reportNoticeProcessName, List<FormFieldResponse> cooperateExtendFields);

    /**
     * 统计异常协同各个状态的数量
     *
     * @param currentUserId 当前用户id
     * @return
     */
    CooperationStatusNumResponse statistics(Long currentUserId);

    /**
     * 异常协同详情
     *
     * @param id      异常协同id
     * @param version 异常协同版本号
     * @return
     */
    TaskResponse detail(Long id, Integer version);

    /**
     * 异常协同分页列表
     *
     * @param page    分页对象
     * @param request 查询条件
     * @param userId  当前用户id
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, TaskCooperationQueryRequest request, Long userId);

    /**
     * 异常超时上报流程分页列表
     *
     * @param taskId 异常任务id
     * @param query  分页查询条件
     * @return
     */
    IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query);

    /**
     * 异常处理协同分页列表
     *
     * @param page    分页对象
     * @param request 查询条件
     * @param userId  当前用户id
     * @return
     */
    IPage<TaskResponse> handingCooperationPage(IPage<TaskResponse> page, HandingCooperationQueryRequest request, Long userId);

    /**
     * 获取excel导出结果集
     *
     * @param request 查询条件
     * @param userId  当前用户id
     * @return
     */
    List<TaskCooperationExportResponse> findExportData(TaskCooperationExportQueryRequest request, Long userId);

    /**
     * 通过异常协同id查询异常协同信息
     * 若数据异常则抛出相应的错误信息
     *
     * @param id 异常协同id
     * @return
     */
    ExceptionTaskCooperation findByIdThrowErr(Long id);

    /**
     * 校验协同状态是否可以接收
     *
     * @param id            异常协同id
     * @param currentUserId 当前用户id
     * @return
     */
    ExceptionTaskCooperation validateStatusAccept(@NonNull Long id, Long currentUserId);

    /**
     * 校验协同状态是否可以转派
     *
     * @param id             异常协同id
     * @param transferUserId 转派人id
     * @param currentUserId  当前用户id
     * @return
     */
    ExceptionTaskCooperation validateStatusTransfer(@NonNull Long id, Long transferUserId, Long currentUserId);

    /**
     * 校验协同状态是否可以提交
     *
     * @param id            异常协同id
     * @param currentUserId 当前用户id
     * @return
     */
    ExceptionTaskCooperation validateSubmit(@NonNull Long id, Long currentUserId);

    /**
     * 根据异常处理任务的相关信息获取协同数目
     *
     * @param exceptionTaskHandingId 异常处理任务id
     * @param handingVersion         异常处理版本号
     * @param status                 协同状态
     * @return 协同数目
     */
    Long count(@NonNull Long exceptionTaskHandingId, @NonNull Integer handingVersion, CooperationStatus status);

    /**
     * 验证协同数据是否超过协同任务最大数目
     *
     * @param handingId 处理id
     */

    void validExceedMaxVal(Long handingId);

    /**
     * 验证状态是否是待协同
     *
     * @param id 协同id
     * @return 协同信息
     */
    ExceptionTaskCooperation validateStatusWaitCooperation(Long id);

    /**
     * 验证状态是否是已撤销
     *
     * @param id 协同id
     * @return 协同信息
     */
    ExceptionTaskCooperation validateStatusCancel(Long id);

    /**
     * 根据任务id和处理版本号获取协同信息,并更新状态是已撤消
     *
     * @param taskId         任务id
     * @param HandingVersion 处理版本号
     */
    List<ExceptionTaskCooperation> updateStatus(Long taskId, Integer HandingVersion);

    /**
     * 验证协同人的数据是否已存在
     *
     * @param exceptionTaskHandingId 协同id
     * @param planUserId             计划协同人
     */
    void validIsExist(Long exceptionTaskHandingId, Long planUserId);
}
