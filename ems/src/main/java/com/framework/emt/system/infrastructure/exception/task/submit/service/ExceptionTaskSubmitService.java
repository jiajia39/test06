package com.framework.emt.system.infrastructure.exception.task.submit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitCreateRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitExportQueryRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitQueryRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitUpdateRequest;
import com.framework.emt.system.domain.task.submit.response.TaskSubmitExportResponse;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitDetailResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 异常任务提报 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskSubmitService extends BaseService<ExceptionTaskSubmit> {

    /**
     * 异常提报创建
     *
     * @param request               创建参数
     * @param exceptionProcessId    异常流程id
     * @param exceptionProcessTitle 异常流程名称
     * @param exceptionCategoryId   异常分类id
     * @param exceptionTaskId       异常任务id
     * @param submitVersion         提报版本号
     * @param extendDataList        附加数据
     * @return
     */
    ExceptionTaskSubmit create(Long userId, TaskSubmitCreateRequest request, Long exceptionProcessId, String exceptionProcessTitle,
                               Long exceptionCategoryId, Long exceptionTaskId, Integer submitVersion, List<FormFieldResponse> extendDataList);

    /**
     * 异常提报复制
     *
     * @param sourceSubmit      提报信息
     * @param taskRejectRequest 驳回参数
     * @param submitVersion     提报版本号
     * @return
     */
    Long copy(ExceptionTaskSubmit sourceSubmit, TaskRejectRequest taskRejectRequest, Integer submitVersion);

    /**
     * 异常提报更新
     *
     * @param exceptionTaskSubmit 异常提报
     * @param request             创建参数
     * @param exceptionProcess    异常流程
     * @param extendDataList      附加字段
     */
    void update(ExceptionTaskSubmit exceptionTaskSubmit, TaskSubmitUpdateRequest request,
                ExceptionProcessResponse exceptionProcess, List<FormFieldResponse> extendDataList);

    /**
     * 异常提报详情
     *
     * @param id     异常提报id
     * @param userId 当前用户id
     * @return
     */
    TaskResponse detail(Long id, Long userId);

    /**
     * 异常提报分页列表
     *
     * @param page    分页对象
     * @param request 查询条件
     * @param userId  当前用户id
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, TaskSubmitQueryRequest request, Long userId);

    /**
     * 根据异常提报id查询异常提报信息
     * 若数据异常则抛出相应的错误信息
     *
     * @param id 异常提报id
     * @return
     */
    ExceptionTaskSubmit findByIdThrowErr(Long id);

    /**
     * 提报任务校验-当前用户
     *
     * @param id            id
     * @param currentUserId 当前用户id
     * @return 异常提报
     */
    ExceptionTaskSubmit validateUser(@NonNull Long id, @NonNull Long currentUserId);

    /**
     * 查询excel导出的结果集
     *
     * @param request
     * @param userId  当前用户id
     * @return
     */
    List<TaskSubmitExportResponse> findExportData(TaskSubmitExportQueryRequest request, Long userId);

    /**
     * 根据异常任务id和提报版本号获取唯一的提报的信息
     *
     * @param exceptionTaskId 异常任务id
     * @param submitVersion   提报版本号
     * @return
     */
    ExceptionTaskSubmit findUniqueInfo(Long exceptionTaskId, Integer submitVersion);

    /**
     * 提报驳回次数
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> submitReject(StatisticsTimeQueryRequest queryRequest);

    /**
     * 根据id查询异常提报详情
     *
     * @param id 异常提报id
     * @return
     */
    TaskSubmitDetailResponse findDetailById(Long id);

    /**
     * 根据id获取异常提报
     *
     * @param itemId      异常项id
     * @param categoryId  异常分类id
     * @param workSpaceId 作业单元id
     * @param processId   异常流程id
     * @return
     */
    List<Long> findById(Long itemId, Long categoryId, Long workSpaceId, Long processId);

    /**
     * 异常提报新增编辑异常项下拉框
     *
     * @param processId 异常流程id
     * @return
     */
    List<ExceptionItemResponse> processOfItems(Long processId);

}
