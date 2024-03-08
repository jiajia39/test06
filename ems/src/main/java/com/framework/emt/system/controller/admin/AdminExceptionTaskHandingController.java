package com.framework.emt.system.controller.admin;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.task.ExceptionTaskNode;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.handing.request.*;
import com.framework.emt.system.domain.task.handing.response.HandingStatusNumResponse;
import com.framework.emt.system.domain.task.handing.response.TaskHandingExportResponse;
import com.framework.emt.system.domain.task.handing.service.TaskHandingServiceExtPt;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import com.framework.emt.system.infrastructure.exception.task.handing.service.ExceptionTaskHandingService;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常处理接口
 *
 * @author gaojia
 * date 2023/8/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常处理"})
@ApiSupport(order = 30)
public class AdminExceptionTaskHandingController extends BaseController {

    private final ExtensionExecutor extensionExecutor;

    private final TaskHandingServiceExtPt handingService;

    private final ExceptionTaskHandingService taskHandingService;

    private final ExceptionTaskCooperationService exceptionTaskCooperationService;

    private final BizScenario HANDING_BIZ_SCENARIO = BizScenario.valueOf(ExceptionTaskNode.HANDING.getBizId());

    @GetMapping("/admin/exception/handing/page")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常处理分页列表")
    public R<IPage<TaskResponse>> pageHanding(@Validated TaskHandingQueryRequest request) {
        return R.data(taskHandingService.page(Condition.getPage(request), AuthUtil.getUserId(), request));
    }

    @GetMapping("/admin/exception/handing/report/{taskId}/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常处理超时上报流程分页列表")
    public R<IPage<TaskScheduleResponse>> timeOutReportPage(@PathVariable(value = "taskId") Long taskId, Query query) {
        return R.data(taskHandingService.timeOutReportPage(taskId, query));
    }

    @GetMapping("/admin/exception/handing/cooperation/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常处理协同分页列表")
    public R<IPage<TaskResponse>> handingCooperationPage(@Validated HandingCooperationQueryRequest request) {
        return R.data(exceptionTaskCooperationService.handingCooperationPage(Condition.getPage(request), request, AuthUtil.getUserId()));
    }

    @GetMapping("/admin/exception/handing/{id}/detail")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常响应详情")
    public R<TaskResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(taskHandingService.detail(id, AuthUtil.getUserId()));
    }

    @PostMapping("/admin/exception/handing/{id}/reject")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常处理驳回")
    public R<Boolean> reject(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingRejectRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.reject(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/to/other")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "处理转派")
    public R<Boolean> toOther(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingToOtherRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.toOther(id, request.getUserId(), request.getOtherRemark())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/accept")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "处理接受")
    public R<Boolean> accept(@PathVariable(value = "id") Long id) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.accept(id)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/suspend")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "处理挂起")
    public R<Boolean> suspend(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingSuspendRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.suspend(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/suspend/delay")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "处理挂起延期")
    public R<Boolean> suspendDelay(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingSuspendDelayRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.suspendDelay(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/resume")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "处理挂起恢复")
    public R<Boolean> resume(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingResumeRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt -> {
            TaskResumeType resumeType = TaskResumeType.getEnum(request.getTaskResume());
            handingService.resume(id, resumeType);
        });
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/update")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "处理更新")
    public R<Boolean> update(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingUpdateRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.update(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/submit")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "提交验收")
    public R<Boolean> submit(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingSubmitRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.submit(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/create/cooperation")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "创建协同任务")
    public R<Boolean> createCooperation(@Validated @RequestBody TaskCooperationCreateRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.createCooperation(request, AuthUtil.getUser())
        );
        return R.status(true);
    }

    @GetMapping("/admin/exception/handing/status/statistics")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "处理状态数量统计")
    public R<HandingStatusNumResponse> statistics() {
        return R.data(taskHandingService.statistics(AuthUtil.getUserId()));
    }

    @GetMapping("/admin/exception/handing/check/info/{taskId}")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "获取验收信息")
    public R<SettingCheckResponse> checkInfo(@PathVariable(value = "taskId") Long taskId) {
        return R.data(taskHandingService.accepted(taskId));
    }

    @PostMapping("/admin/exception/handing/{taskId}/administrator/to/other")
    @ApiOperationSupport(order = 16)
    @ApiOperation(value = "管理员处理转派")
    public R<Boolean> adminToOther(@PathVariable(value = "taskId") Long taskId, @Validated @RequestBody HandingToOtherRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.adminToOther(taskId, request.getUserId(), request.getOtherRemark())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/export")
    @ApiOperationSupport(order = 17)
    @ApiOperation(value = "异常处理导出")
    public void export(@Validated @RequestBody TaskHandingExportQueryRequest request, HttpServletResponse response) {
        List<TaskHandingExportResponse> exportData = taskHandingService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常处理" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常处理数据表", exportData, TaskHandingExportResponse.class);
    }

    @PostMapping("/admin/exception/handing/{id}/administrator/update")
    @ApiOperationSupport(order = 18)
    @ApiOperation(value = "管理员-处理更新")
    public R<Boolean> administratorUpdate(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingUpdateRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.administratorUpdate(id, request)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/handing/{id}/administrator/submit")
    @ApiOperationSupport(order = 19)
    @ApiOperation(value = "管理员-提交验收")
    public R<Boolean> administratorSubmit(@PathVariable(value = "id") Long id, @Validated @RequestBody HandingSubmitRequest request) {
        extensionExecutor.executeVoid(TaskHandingServiceExtPt.class, HANDING_BIZ_SCENARIO, taskHandingServiceExtPt ->
                handingService.administratorSubmit(id, request)
        );
        return R.status(true);
    }


}
