package com.framework.emt.system.infrastructure.exception.task.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.statistics.request.*;
import com.framework.emt.system.domain.statistics.response.*;
import com.framework.emt.system.domain.task.handing.response.HandingSuspendResponse;
import com.framework.emt.system.domain.task.response.response.StatisticsQuantityResponse;
import com.framework.emt.system.domain.task.task.request.TaskExportQueryRequest;
import com.framework.emt.system.domain.task.task.request.TaskQueryRequest;
import com.framework.emt.system.domain.task.task.response.TaskExportResponse;
import com.framework.emt.system.infrastructure.common.request.IdVersionRequest;
import com.framework.emt.system.infrastructure.exception.task.record.response.RejectResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.TransferResponse;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskRecordResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskMapper extends BaseMapper<ExceptionTask> {

    List<TaskResponse> submitList(@Param("list") List<IdVersionRequest> list);

    /**
     * 异常任务分页列表
     *
     * @param page    分页查询条件
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("request") TaskQueryRequest request);

    /**
     * 查询excel导出的数量
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    List<TaskExportResponse> findExportData(@Param("request") TaskExportQueryRequest request,
                                            @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 异常任务详情
     *
     * @param id 异常任务id
     * @return
     */
    TaskResponse detail(@Param("id") Long id);

    /**
     * 异常任务驳回历史分页列表
     *
     * @param page            分页查询条件
     * @param recordNodeCodes 履历节点code列表
     * @param recordTypeCode  操作类型code
     * @param id              异常任务id
     * @return
     */
    IPage<RejectResponse> taskRejectPage(IPage<RejectResponse> page, @Param("recordNodeCodes") List<Integer> recordNodeCodes,
                                         @Param("recordTypeCode") Integer recordTypeCode, @Param("id") Long id);

    /**
     * 异常任务转派历史分页列表
     *
     * @param page           分页查询条件
     * @param recordNodeCode 履历节点code
     * @param recordTypeCode 操作类型code
     * @param id             异常任务id
     * @return
     */
    IPage<TransferResponse> taskTransferPage(IPage<TransferResponse> page, @Param("recordNodeCode") Integer recordNodeCode,
                                             @Param("recordTypeCode") Integer recordTypeCode, @Param("id") Long id);

    /**
     * 异常处理挂起历史分页列表
     *
     * @param page            分页查询条件
     * @param recordNodeCode  履历节点code
     * @param recordTypeCodes 操作类型code列表
     * @param id              异常任务id
     * @return
     */
    IPage<HandingSuspendResponse> handingSuspendPage(IPage<HandingSuspendResponse> page, @Param("recordNodeCode") Integer recordNodeCode,
                                                     @Param("recordTypeCodes") List<Integer> recordTypeCodes, @Param("id") Long id);

    /**
     * 异常处理历史分页列表
     *
     * @param page 分页查询条件
     * @param id   异常任务id
     * @return
     */
    IPage<TaskResponse> handingPage(IPage<TaskResponse> page, @Param("id") Long id);

    /**
     * 异常任务履历分页列表
     *
     * @param page     分页查询条件
     * @param id       异常任务id
     * @param keyWorld 关键字
     * @return
     */
    IPage<TaskRecordResponse> recordPage(IPage<TaskResponse> page, @Param("id") Long id, @Param("keyWorld") String keyWorld);

    /**
     * 异常任务协同分页列表
     *
     * @param page 分页查询条件
     * @param id   异常任务id
     * @return
     */
    IPage<TaskResponse> cooperationPage(IPage<TaskResponse> page, @Param("id") Long id);

    /**
     * 异常任务验收分页列表
     *
     * @param page 分页查询条件
     * @param id   异常任务id
     * @return
     */
    IPage<TaskResponse> checkPage(IPage<TaskResponse> page, @Param("id") Long id);

    /**
     * 汇总信息
     *
     * @param deptIds 部门及子部门id
     * @return 汇总信息
     */
    StatisticsSummaryResponse statisticsSummary(@Param("deptIds") List<Long> deptIds);

    /**
     * 实时信息
     *
     * @param deptIds 参数
     * @return 实时信息
     */
    StatisticsRealTimeResponse statisticsRealTime(@Param("deptIds") List<Long> deptIds);

    /**
     * 前12天每天提报、关闭、挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */

    List<StatisticsTrendValueResponse> statisticsDayTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

    /**
     * 前12周每天提报、关闭、挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsWeekTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);


    /**
     * 前12月每天提报、关闭、挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsMonthTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);


    /**
     * 每年提报、关闭、挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @param methodName          方法名，查询不同的方法结果value不同
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsYearTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId, @Param("methodName") String methodName);

    /**
     * 异常分类第一级占比
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> categoryProportion(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 异常共计
     *
     * @param queryRequest 筛选条件
     * @param isParent     是否查询第一级别父类信息
     * @return
     */
    Integer exceptionTotal(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("isParent") Boolean isParent);


    /**
     * 异常流程占比
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> processProportion(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 异常项发生频率TOP 10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> itemProportion(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 异常统计-异常分类占比
     *
     * @param queryRequest 查询条件
     * @return
     */
    List<StatisticsProportionResponse> exceptionCategoryProportion(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 异常挂起TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> exceptionPendingTop(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 异常关闭率
     *
     * @param queryRequest 筛选条件
     * @return
     */
    StatisticsCompleteRateResponse completeRate(@Param("request") StatisticsCompleteQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 平均响应时间
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsAvgDetailResponse> avgResponseTime(@Param("request") StatisticsAvgRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 平均处理时间
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsAvgDetailResponse> avgHandingTime(@Param("request") StatisticsAvgRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 平均验收时间
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsAvgDetailResponse> avgCheckTime(@Param("request") StatisticsAvgRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 部门看板-汇总
     *
     * @param deptIds 部门
     * @return
     */
    StatisticsDeptBoardResponse deptBoardSummary(@Param("deptIds") List<Long> deptIds, @Param("processIdList") List<Long> processIdList);

    /**
     * 超时数量
     *
     * @param deptIds 部门
     * @return
     */
    Integer timeoutQuantity(@Param("deptIds") List<Long> deptIds, @Param("processIdList") List<Long> processIdList);

    /**
     * 带协同数量
     *
     * @param deptIds 部门
     * @return
     */
    Integer suspendQuantity(@Param("deptIds") List<Long> deptIds, @Param("processIdList") List<Long> processIdList);

    /**
     * 部门看板-列表
     *
     * @param page    分页
     * @param deptIds 部门
     * @return
     */
    IPage<StatisticsDeptBoardListResponse> deptBoardPage(IPage<StatisticsDeptBoardResponse> page, @Param("deptIds") List<Long> deptIds, @Param("processIdList") List<Long> processIdList);

    /**
     * 查询当前用户提报、响应、处理、协同、验收 每个阶段的数量
     *
     * @param userId
     * @return
     */
    StatisticsQuantityResponse quantity(@Param("userId") Long userId);
}
