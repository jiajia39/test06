package com.framework.emt.system.infrastructure.exception.task.response.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.response.request.TaskResponseExportQueryRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseQueryRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseUpdateRequest;
import com.framework.emt.system.domain.task.response.response.ResponseStatusNumResponse;
import com.framework.emt.system.domain.task.response.response.TaskResponseExportResponse;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 异常任务响应表 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskResponseService extends BaseService<ExceptionTaskResponse> {

    /**
     * 异常响应分页列表
     *
     * @param page
     * @param userId  当前用户id
     * @param request 筛选条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, Long userId, TaskResponseQueryRequest request);

    /**
     * 异常响应超时上报流程分页列表
     *
     * @param taskId 异常任务id
     * @param query  分页查询条件
     * @return
     */
    IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query);

    /**
     * 异常任务响应信息详情
     *
     * @param id 响应id
     * @return 标签信息
     */
    TaskResponse detail(Long id, Long userId);

    /**
     * 创建异常任务响应信息
     *
     * @param exceptionTaskId      任务id
     * @param responseVersion      响应版本号
     * @param planUserId           计划响应人
     * @param responseExtendFields 响应附加字段
     * @return 响应id
     */
    void create(@NonNull Long exceptionTaskId, @NonNull Integer responseVersion, @NonNull List<Long> planUserId, List<FormFieldResponse> responseExtendFields);

    /**
     * 更新异常任务响应信息
     *
     * @param taskResponse 响应信息
     * @param request      参数
     * @return 标签id
     */
    Long updateResponse(ExceptionTaskResponse taskResponse, TaskResponseUpdateRequest request);

    /**
     * 删除多余的响应信息
     *
     * @param taskId          任务id
     * @param id              响应id
     * @param responseVersion 响应版本号
     */
    void deleteResponse(Long taskId, Long id, Integer responseVersion);

    /**
     * 获取处理接受人外其他的响应信息
     *
     * @param taskId          任务id
     * @param id              响应id
     * @param responseVersion 响应版本号
     * @return 用户id
     */
    List<Long> getRespondent(Long taskId, Long id, Integer responseVersion);

    /**
     * 通过异常任务id+响应版本号查询异常响应
     *
     * @param exceptionTaskId 异常任务id
     * @param responseVersion 异常响应版本号
     * @return 异常响应信息
     */
    ExceptionTaskResponse infoByTask(Long exceptionTaskId, Integer responseVersion);

    /**
     * 根据id和响应人来获取响应数据
     *
     * @param userId 响应人
     * @param id     响应id
     * @return 响应数据
     */
    ExceptionTaskResponse getByIdAndUserId(Long id, Long userId);

    /**
     * 获取响应当前用户各个状态的数量
     *
     * @param userId 当前用户id
     * @return 当前用户各个状态的数量
     */
    ResponseStatusNumResponse statistics(Long userId);

    /**
     * 根据任务id查询异常任务配置信息中处理信息
     *
     * @param taskId 任务id
     * @return 处理信息
     */
    SettingHandingResponse handingInfo(Long taskId);

    /**
     * 异常响应复制
     *
     * @param sourceResponse    响应信息
     * @param taskRejectRequest 驳回参数
     * @param responseVersion   响应版本号
     * @param responseRejectNum 响应驳回次数
     * @return
     */
    Long copy(ExceptionTaskResponse sourceResponse, TaskRejectRequest taskRejectRequest, Integer responseVersion, Integer responseRejectNum);

    /**
     * 异常响应导出
     *
     * @param request 导出查询条件
     * @param userId  当前用户id
     * @return
     */
    List<TaskResponseExportResponse> findExportData(TaskResponseExportQueryRequest request, Long userId);

}
