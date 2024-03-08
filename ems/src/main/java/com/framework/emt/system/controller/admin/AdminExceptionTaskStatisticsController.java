package com.framework.emt.system.controller.admin;

import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.statistics.request.*;
import com.framework.emt.system.domain.statistics.response.*;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.infrastructure.exception.task.check.service.ExceptionTaskCheckService;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 异常统计接口
 *
 * @author gaojia
 * date 2023/8/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-统计"})
@ApiSupport(order = 31)
public class AdminExceptionTaskStatisticsController extends BaseController {

    private final ExceptionTaskService taskService;

    private final ExceptionTaskSubmitService submitService;

    private final ExceptionTaskCheckService checkService;

    private final TagService tagService;

    @GetMapping("/admin/exception/task/statistics/summary")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "汇总信息")
    public R<StatisticsSummaryResponse> summary(@Validated StatisticsSummaryQueryRequest queryRequest) {
        return R.data(taskService.statisticsSummary(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/real/time")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "实时信息")
    public R<StatisticsRealTimeResponse> realTime(@Validated StatisticsSummaryQueryRequest queryRequest) {
        return R.data(taskService.realTime(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/trend")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "趋势信息-处理状态")
    public R<StatisticsTrendResponse> trend(@Validated StatisticsTrendQueryRequest queryRequest) {
        return R.data(taskService.trend(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/trend/timeout")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "趋势信息-超时状态")
    public R<StatisticsTrendTimeoutResponse> trendTimeout(@Validated StatisticsTrendQueryRequest queryRequest) {
        return R.data(taskService.trendTimeout(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/category/proportion")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常分类第一级占比 ")
    public R<StatisticsProportionPieResponse> categoryProportion(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(taskService.categoryProportion(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/process/proportion")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常流程占比")
    public R<StatisticsProportionPieResponse> processProportion(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(taskService.processProportion(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/item/proportion")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常项发生频率TOP 10")
    public R<List<StatisticsProportionResponse>> itemProportion(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(taskService.itemProportion(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/exception/trend")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常统计-异常趋势")
    public R<StatisticsExceptionTrendResponse> exceptionTrend(@Validated StatisticsExceptionTrendQueryRequest queryRequest) {
        return R.data(taskService.exceptionTrend(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/exception/category/proportion")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常统计-异常分类占比")
    public R<StatisticsProportionPieResponse> exceptionCategoryProportion(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(taskService.exceptionCategoryProportion(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/exception/complete/rate")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常统计-异常关闭率")
    public R<StatisticsCompleteRateResponse> completeRate(@Validated StatisticsCompleteQueryRequest queryRequest) {
        return R.data(taskService.completeRate(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/avg/energy/consumption")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "异常平均耗能对比")
    public R<List<StatisticsAvgDetailResponse>> avgEnergyConsumption(@Validated StatisticsAvgRequest queryRequest) {
        return R.data(taskService.avgEnergyConsumption(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/exception/pending/top")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "异常挂起TOP10")
    public R<List<StatisticsProportionResponse>> exceptionPendingTop(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(taskService.exceptionPendingTop(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/submit/reject")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "提报驳回TOP10")
    public R<List<StatisticsProportionResponse>> submitReject(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(submitService.submitReject(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/check/reject")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "验收驳回TOP10")
    public R<List<StatisticsProportionResponse>> checkReject(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(checkService.checkReject(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/exception/item/top")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "异常原因TOP10")
    public R<List<StatisticsProportionResponse>> exceptionReasonTop(@Validated StatisticsTimeQueryRequest queryRequest) {
        return R.data(tagService.exceptionReasonTop(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/dept/board/summary")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "部门看板-汇总")
    public R<StatisticsDeptBoardResponse> deptBoardSummary(@Validated StatisticsDeptBoardRequest queryRequest) {
        return R.data(taskService.deptBoardSummary(queryRequest));
    }

    @GetMapping("/admin/exception/task/statistics/dept/board/page")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "部门看板-列表")
    public R<IPage<StatisticsDeptBoardListResponse>> deptBoardPage(@Validated StatisticsDeptBoardRequest request) {
        return R.data(taskService.deptBoardPage(request));
    }
}
