package com.framework.emt.system.infrastructure.exception.task.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.statistics.request.*;
import com.framework.emt.system.domain.statistics.response.*;
import com.framework.emt.system.domain.task.handing.response.HandingSuspendResponse;
import com.framework.emt.system.domain.task.response.response.StatisticsQuantityResponse;
import com.framework.emt.system.domain.task.task.request.TaskExportQueryRequest;
import com.framework.emt.system.domain.task.task.request.TaskQueryRequest;
import com.framework.emt.system.domain.task.task.response.TaskExportResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.RejectResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.TransferResponse;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskRecordResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import javax.validation.Valid;
import java.util.List;

/**
 * 异常任务 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Valid
public interface ExceptionTaskService extends BaseService<ExceptionTask> {

    /**
     * 异常任务创建
     *
     * @param taskSettingId 异常任务配置id
     * @param status        主状态待提交
     * @param subStatus     子状态待提交
     * @return
     */
    ExceptionTask create(Long taskSettingId, ExceptionStatus status, ExceptionSubStatus subStatus);

    /**
     * 通过异常任务id查询异常任务信息
     * 若数据异常则抛出相应的错误信息
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask findByIdThrowErr(Long id);

    /**
     * 校验异常任务-更新
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusUpdate(@NonNull Long id);

    /**
     * 校验异常任务-删除
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusDelete(@NonNull Long id);

    /**
     * 校验异常任务-关闭
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusClose(@NonNull Long id);

    /**
     * 校验异常任务-提报
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusSubmit(@NonNull Long id);

    /**
     * 校验异常任务-处理驳回
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusHandingReject(@NonNull Long id);

    /**
     * 校验异常任务-处理转派
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusHandingToOther(@NonNull Long id);

    /**
     * 校验异常任务-处理接受
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusHandingToAccept(Long id);

    /**
     * 校验异常任务-处理挂起
     *
     * @param id             异常任务id
     * @param suspendSeconds 挂起秒数
     * @return
     */
    ExceptionTask validateHandingSuspend(Long id, long suspendSeconds);

    /**
     * 校验异常任务-处理挂起延期
     *
     * @param id                  异常任务id
     * @param suspendSeconds      已挂起的秒数
     * @param suspendDelaySeconds 申请延期秒数
     * @return
     */
    ExceptionTask validateHandingSuspendDelay(Long id, long suspendSeconds, long suspendDelaySeconds);

    /**
     * 校验异常任务-手动恢复
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateHandingResume(Long id);

    /**
     * 校验异常任务-协同任务设置
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateHandingCooperationTask(Long id);

    /**
     * 校验异常任务-响应驳回、响应转派、响应接受
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusResponse(@NonNull Long id);

    /**
     * 校验异常任务-响应转派状态和转派次数
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validResponseToOther(@NonNull Long id);

    /**
     * 校验异常任务-响应提交处理人
     *
     * @param id 异常任务id
     * @return
     */

    ExceptionTask validateStatusResponseHanding(@NonNull Long id);

    /**
     * 校验异常验收-判断状态是否是待验收
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validateStatusCheck(@NonNull Long id);

    /**
     * 校验异常验收-判断状态是否是处理中
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTask validationProcessing(@NonNull Long id);

    /**
     * 异常任务分页列表
     *
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(TaskQueryRequest request);

    /**
     * 异常任务详情
     *
     * @param id 异常任务id
     * @return
     */
    TaskResponse detail(Long id);

    /**
     * 异常提报驳回历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<RejectResponse> submitRejectPage(Long id, Query query);

    /**
     * 异常响应转派历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<TransferResponse> responseTransferPage(Long id, Query query);

    /**
     * 异常响应驳回历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<RejectResponse> responseRejectPage(Long id, Query query);

    /**
     * 异常处理历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<TaskResponse> handingPage(Long id, Query query);

    /**
     * 异常处理转派历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<TransferResponse> handingTransferPage(Long id, Query query);

    /**
     * 异常处理挂起历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<HandingSuspendResponse> handingSuspendPage(Long id, Query query);

    /**
     * 异常处理驳回历史分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<RejectResponse> handingRejectPage(Long id, Query query);

    /**
     * 异常任务协同分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<TaskResponse> cooperationPage(Long id, Query query);

    /**
     * 异常任务协同详情
     *
     * @param cooperationId 异常任务协同id
     * @param version       协同版本号
     * @return
     */
    TaskResponse cooperationDetail(Long cooperationId, Integer version);

    /**
     * 异常任务验收分页列表
     *
     * @param id    异常任务id
     * @param query 分页查询条件
     * @return
     */
    IPage<TaskResponse> checkPage(Long id, Query query);

    /**
     * 异常任务验收详情
     *
     * @param checkId 异常验收id
     * @param version 异常验收版本号
     * @return
     */
    TaskResponse checkDetail(Long checkId, Integer version);

    /**
     * 异常任务履历分页列表
     *
     * @param id      异常任务id
     * @param keyWord 关键字
     * @param query   分页查询条件
     * @return
     */
    IPage<TaskRecordResponse> recordPage(Long id, String keyWord, Query query);

    /**
     * 异常列表导出
     *
     * @param request 导出查询条件
     * @param userId  当前用户id
     * @return
     */
    List<TaskExportResponse> findExportData(TaskExportQueryRequest request, Long userId);

    /**
     * 装载任务列表
     *
     * @param taskList 任务列表
     */
    void loadList(List<TaskResponse> taskList);

    /**
     * 装载任务
     *
     * @param task 任务详情
     */
    void loadDetail(TaskResponse task);

    /**
     * 汇总信息
     *
     * @param queryRequest 参数
     * @return 汇总信息
     */
    StatisticsSummaryResponse statisticsSummary(StatisticsSummaryQueryRequest queryRequest);

    /**
     * 实时信息
     *
     * @param queryRequest 参数
     * @return 实时信息
     */
    StatisticsRealTimeResponse realTime(StatisticsSummaryQueryRequest queryRequest);

    /**
     * 异常趋势信息--处理状态
     *
     * @param queryRequest 筛选条件
     * @return 趋势信息
     */
    StatisticsTrendResponse trend(StatisticsTrendQueryRequest queryRequest);

    /**
     * 异常趋势信息--超时状态
     *
     * @param queryRequest
     * @return
     */
    StatisticsTrendTimeoutResponse trendTimeout(StatisticsTrendQueryRequest queryRequest);

    /**
     * 异常分类第一级占比
     *
     * @param queryRequest 筛选条件
     * @return
     */
    StatisticsProportionPieResponse categoryProportion(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常流程占比
     *
     * @param queryRequest 筛选条件
     * @return
     */
    StatisticsProportionPieResponse processProportion(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常项发生频率TOP 10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> itemProportion(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常趋势折线图
     *
     * @param queryRequest 查询条件
     * @return
     */
    StatisticsExceptionTrendResponse exceptionTrend(StatisticsExceptionTrendQueryRequest queryRequest);

    /**
     * 异常统计-异常分类占比
     *
     * @param queryRequest 查询条件
     * @return
     */
    StatisticsProportionPieResponse exceptionCategoryProportion(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常挂起TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> exceptionPendingTop(StatisticsTimeQueryRequest queryRequest);

    /**
     * 异常关闭率
     *
     * @param queryRequest 筛选条件
     * @return
     */
    StatisticsCompleteRateResponse completeRate(StatisticsCompleteQueryRequest queryRequest);

    /**
     * 异常平均耗能对比
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsAvgDetailResponse> avgEnergyConsumption(StatisticsAvgRequest queryRequest);

    /**
     * 部门看板-汇总
     *
     * @param queryRequest 查询条件
     * @return
     */
    StatisticsDeptBoardResponse deptBoardSummary(StatisticsDeptBoardRequest queryRequest);

    /**
     * 部门看板-列表
     *
     * @param queryRequest 筛选条件
     * @return
     */
    IPage<StatisticsDeptBoardListResponse> deptBoardPage(StatisticsDeptBoardRequest queryRequest);

    /**
     * 任务终止
     *
     * @param id 任务id
     */
    void discontinue(Long id);

    /**
     * 查询当前用户提报、响应、处理、协同、验收 每个阶段的数量
     *
     * @param userId
     * @return
     */
    StatisticsQuantityResponse quantity(Long userId);

    /**
     * 获取到已完成的异常任务数量
     *
     * @param ids 任务id列表
     * @return
     */
    long findFinishCountByIds(List<Long> ids);

}
