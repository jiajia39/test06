package com.framework.emt.system.infrastructure.exception.task.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.entity.IResultCode;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.check.request.TaskCheckCreateRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckExportQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckUpdateRequest;
import com.framework.emt.system.domain.task.check.response.CheckStatusNumResponse;
import com.framework.emt.system.domain.task.check.response.TaskCheckExportResponse;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 异常任务验收 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskCheckService extends BaseService<ExceptionTaskCheck> {

    /**
     * 异常任务验收信息分页列表
     *
     * @param page    分页
     * @param userId  当前用户id
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, Long userId, TaskCheckQueryRequest request);

    /**
     * 创建多个异常任务验收信息
     *
     * @param request 参数
     * @return 验收id
     */
    void createCheck(TaskCheckCreateRequest request);

    /**
     * 删除异常任务验收信息
     *
     * @param id 验收id
     */
    void deleteCheck(Long id);

    /**
     * 更新异常任务验收信息
     *
     * @param id      验收id
     * @param request 参数
     * @return 标签id
     */
    Long updateCheck(Long id, TaskCheckUpdateRequest request);

    /**
     * 异常任务验收信息详情
     *
     * @param id      验收id
     * @param userId  当前用户id
     * @param version 验收版本号
     * @return
     */
    TaskResponse detail(Long id, Long userId, Integer version);

    /**
     * 根据id查询此条异常响应
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    ExceptionTaskCheck findById(Long id, IResultCode errorMessage);

    /**
     * 验收是否都已通过
     *
     * @param exceptionTaskId 任务id
     * @param checkVersion    验收版本
     * @return 结果
     */
    Boolean AllCheckPass(Long exceptionTaskId, Integer checkVersion);

    /**
     * 验收通过或不通过的个数
     *
     * @param exceptionTaskId 任务id
     * @param checkVersion    验收版本
     * @param pass            是否是验收通过
     * @return 验收通过的个数
     */
    int checkPassOrNoPassCount(Long exceptionTaskId, Integer checkVersion, Boolean pass);

    /**
     * 当验收驳回时，该任务其他验收数据都设置成已过期
     *
     * @param taskId 异常任务id
     */

    void setExpire(Long taskId, Long checkId);


    /**
     * 当验收驳回时，该任务其他验收数据都设置成已过期
     *
     * @param taskId 异常任务id
     * @param checkId 验收id
     */

    void deleteByTaskId(Long taskId, Long checkId, Integer version);

    /**
     * 验证验收人是否是当前操作人
     *
     * @param id 异常任务验收id
     * @return 验收信息
     */
    ExceptionTaskCheck checkUser(Long id, Long userId);

    /**
     * 获取当前用户验收时各个状态的数量
     *
     * @param userId 用户id
     * @return 各个状态的数量
     */
    CheckStatusNumResponse statistics(Long userId);

    /**
     * 验收驳回TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> checkReject(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常验收导出
     *
     * @param request 导出查询条件
     * @param userId  当前用户id
     * @return
     */
    List<TaskCheckExportResponse> findExportData(TaskCheckExportQueryRequest request, Long userId);

    /**
     * 根据任务id和验收版本号获取协同信息,并更新状态是已撤消
     *
     * @param taskId       任务id
     * @param checkVersion 处理版本号
     */
    void updateStatus(Long taskId, Integer checkVersion);

    /**
     * 根据任务id获取验收人id
     *
     * @param taskIds 任务id
     * @return 任务id和验收人信息
     */
    Map<Long, List<Long>> loadCheckUserId(List<Long> taskIds);
}
