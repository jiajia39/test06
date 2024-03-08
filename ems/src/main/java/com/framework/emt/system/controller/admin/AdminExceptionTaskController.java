package com.framework.emt.system.controller.admin;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.task.handing.response.HandingSuspendResponse;
import com.framework.emt.system.domain.task.response.response.StatisticsQuantityResponse;
import com.framework.emt.system.domain.task.task.request.TaskExportQueryRequest;
import com.framework.emt.system.domain.task.task.request.TaskQueryRequest;
import com.framework.emt.system.domain.task.task.response.TaskExportResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.RejectResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.TransferResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskRecordResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务接口
 *
 * @author ds_C
 * @since 2023-08-20
 */
@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = {"管理平台-异常任务"})
@ApiSupport(order = 32)
public class AdminExceptionTaskController extends BaseController {

    private final ExceptionTaskService exceptionTaskService;

    @GetMapping("/admin/exception/task/page")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常任务分页列表")
    public R<IPage<TaskResponse>> page(@Validated TaskQueryRequest request) {
        return R.data(exceptionTaskService.page(request));
    }

    @GetMapping("/admin/exception/task/{id}/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常任务详情")
    public R<TaskResponse> detail(@PathVariable("id") Long id) {
        return R.data(exceptionTaskService.detail(id));
    }

    @GetMapping("/admin/exception/task/{id}/submit/reject/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常提报驳回历史分页列表")
    public R<IPage<RejectResponse>> submitRejectPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.submitRejectPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/response/transfer/page")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常响应转派历史分页列表")
    public R<IPage<TransferResponse>> responseTransferPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.responseTransferPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/response/reject/page")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常响应驳回历史分页列表")
    public R<IPage<RejectResponse>> responseRejectPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.responseRejectPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/handing/page")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常处理历史分页列表")
    public R<IPage<TaskResponse>> handingPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.handingPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/handing/transfer/page")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常处理转派历史分页列表")
    public R<IPage<TransferResponse>> handingTransferPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.handingTransferPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/handing/suspend/page")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常处理挂起历史分页列表")
    public R<IPage<HandingSuspendResponse>> handingSuspendPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.handingSuspendPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/handing/reject/page")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常处理驳回历史分页列表")
    public R<IPage<RejectResponse>> handingRejectPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.handingRejectPage(id, query));
    }

    @GetMapping("/admin/exception/task/{id}/cooperation/page")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常任务协同分页列表")
    public R<IPage<TaskResponse>> cooperationPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.cooperationPage(id, query));
    }

    @GetMapping("/admin/exception/task/{cooperationId}/cooperation/detail")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "异常任务协同详情")
    public R<TaskResponse> cooperationDetail(@PathVariable("cooperationId") Long cooperationId, Integer version) {
        return R.data(exceptionTaskService.cooperationDetail(cooperationId, version));
    }

    @GetMapping("/admin/exception/task/{id}/check/page")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "异常任务验收分页列表")
    public R<IPage<TaskResponse>> checkPage(@PathVariable("id") Long id, Query query) {
        return R.data(exceptionTaskService.checkPage(id, query));
    }

    @GetMapping("/admin/exception/task/{checkId}/check/detail")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "异常任务验收详情")
    public R<TaskResponse> checkDetail(@PathVariable("checkId") Long checkId, Integer version) {
        return R.data(exceptionTaskService.checkDetail(checkId, version));
    }

    @GetMapping("/admin/exception/task/{id}/record/page")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "异常任务履历分页列表")
    public R<IPage<TaskRecordResponse>> recordPage(@PathVariable("id") Long id, String keyWord, Query query) {
        return R.data(exceptionTaskService.recordPage(id, keyWord, query));
    }

    @PostMapping("/admin/exception/task/export")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "异常列表导出")
    public void export(@Validated @RequestBody TaskExportQueryRequest request, HttpServletResponse response) {
        List<TaskExportResponse> exportData = exceptionTaskService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常列表" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),"异常列表数据表", exportData, TaskExportResponse.class);
    }

    @PostMapping("/admin/exception/task/{id}/discontinue")
    @ApiOperationSupport(order = 16)
    @ApiOperation(value = "异常任务中止")
    public R<Boolean> discontinue(@PathVariable("id") Long id) {
        exceptionTaskService.discontinue(id);
        return R.status(true);
    }

    @GetMapping("/admin/exception/task/quantity")
    @ApiOperationSupport(order = 17)
    @ApiOperation(value = "查询当前用户每个阶段的数量")
    public R<StatisticsQuantityResponse> quantity() {
        return R.data(exceptionTaskService.quantity(AuthUtil.getUserId()));
    }
}
