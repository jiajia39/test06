package com.framework.emt.system.infrastructure.exception.task.handing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.handing.request.TaskHandingExportQueryRequest;
import com.framework.emt.system.domain.task.handing.request.TaskHandingQueryRequest;
import com.framework.emt.system.domain.task.handing.response.HandingStatusNumResponse;
import com.framework.emt.system.domain.task.handing.response.TaskHandingExportResponse;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 异常任务处理 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskHandingService extends BaseService<ExceptionTaskHanding> {

    /**
     * 异常处理分页列表
     *
     * @param page
     * @param userId 当前用户id
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, Long userId, TaskHandingQueryRequest request);

    /**
     * 异常处理超时上报流程分页列表
     *
     * @param taskId 异常任务id
     * @param query  分页查询条件
     * @return
     */
    IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query);

    /**
     * 异常处理创建
     *
     * @param exceptionTaskId     异常任务id
     * @param handingVersion      异常处理版本号
     * @param planUserIds         计划处理人
     * @param handingExtendFields 处理附加字段
     * @param pendingExtendFields 挂起附加字段
     * @return 异常处理id
     */
    void create(@NonNull Long exceptionTaskId, @NonNull Integer handingVersion, @NonNull List<Long> planUserIds,
                List<FormFieldResponse> handingExtendFields, List<FormFieldResponse> pendingExtendFields);

    /**
     * 删除多余的处理信息
     *
     * @param taskId         任务id
     * @param id             响应id
     * @param handingVersion 处理版本号
     */
    void deleteHanding(Long taskId, Long id, Integer handingVersion);

    /**
     * 获取处理接受人外其他的用户信息
     *
     * @param taskId         任务id
     * @param id             响应id
     * @param handingVersion 处理版本号
     */
    List<Long> getUserIds(Long taskId, Long id, Integer handingVersion);

    /**
     * 根据异常处理id查询异常处理信息
     * 若数异常则抛出相应的错误信息
     *
     * @param id 异常处理id
     * @return
     */
    ExceptionTaskHanding info(@NonNull Long id, Long userId);

    /**
     * 异常处理 校验是否可提交
     *
     * @param id
     * @param userId
     * @return
     */
    ExceptionTaskHanding validateSubmit(@NonNull Long id, Long userId);

    /**
     * 根据异常处理id查询异常处理详情
     * 若数异常则抛出相应的错误信息
     *
     * @param id 异常处理id
     * @return
     */
    ExceptionTaskHanding findByIdThrowErr(Long id);

    /**
     * 异常处理详情
     *
     * @param id     异常处理id
     * @param userId 当前用户id
     * @return
     */
    TaskResponse detail(Long id, Long userId);

    /**
     * 获取处理当前用户各个状态的数量
     *
     * @param userId 当前用户id
     * @return 当前用户各个状态的数量
     */
    HandingStatusNumResponse statistics(Long userId);

    /**
     * 根据任务id获取异常任务配置验收数据
     *
     * @param taskId 任务id
     * @return
     */
    SettingCheckResponse accepted(Long taskId);

    /**
     * 根据任务号和版本查询处理信息
     *
     * @param taskId  任务id
     * @param version 版本
     * @return 处理信息
     */
    ExceptionTaskHanding findUniqueInfo(Long taskId, Integer version);

    /**
     * 异常处理复制
     *
     * @param taskHanding       处理信息
     * @param taskRejectRequest 驳回参数
     * @param handingVersion    处理版本号
     * @param handingRejectNum  处理驳回次数
     * @return
     */
    Long copy(ExceptionTaskHanding taskHanding, TaskRejectRequest taskRejectRequest, Integer handingVersion, Integer handingRejectNum);

    /**
     * 验证转派次数是否已超过最大值
     *
     * @param exceptionTaskHandingId 处理id
     * @return 处理信息
     */
    ExceptionTaskHanding validateTransferNumber(Long exceptionTaskHandingId);

    /**
     * 异常处理导出
     *
     * @param request 导出查询条件
     * @param userId  当前用户id
     * @return
     */
    List<TaskHandingExportResponse> findExportData(TaskHandingExportQueryRequest request, Long userId);

}