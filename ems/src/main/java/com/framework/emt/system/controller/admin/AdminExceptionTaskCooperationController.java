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
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationExportQueryRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationQueryRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationSubmitRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationTransferRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;
import com.framework.emt.system.domain.task.cooperation.response.TaskCooperationExportResponse;
import com.framework.emt.system.domain.task.cooperation.service.TaskCooperationServiceExtPt;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
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
 * 异常协同接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常协同"})
@ApiSupport(order = 30)
public class AdminExceptionTaskCooperationController extends BaseController {

    private final ExtensionExecutor extensionExecutor;

    private final ExceptionTaskCooperationService exceptionTaskCooperationService;

    private final BizScenario COOPERATION_BIZ_SCENARIO = BizScenario.valueOf(ExceptionTaskNode.COOPERATION.getBizId());

    @PostMapping("/admin/exception/task/cooperation/{id}/transfer")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常协同转派")
    public R<Boolean> transfer(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskCooperationTransferRequest request) {
        extensionExecutor.executeVoid(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.transfer(id, request, AuthUtil.getUser())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/cooperation/{id}/cancel")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常协同撤销")
    public R<Boolean> cancel(@PathVariable(value = "id") Long id) {
        extensionExecutor.executeVoid(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.cancel(id, AuthUtil.getUser())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/cooperation/{id}/accept")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常协同接收")
    public R<Boolean> accept(@PathVariable(value = "id") Long id) {
        extensionExecutor.executeVoid(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.accept(id, AuthUtil.getUser())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/cooperation/{id}/submit")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常协同提交")
    public R<Boolean> submit(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskCooperationSubmitRequest request) {
        extensionExecutor.executeVoid(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.submit(id, request.init(), AuthUtil.getUser())
        );
        return R.status(true);
    }

    @GetMapping("/admin/exception/task/cooperation/status/statistics")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常协同状态数量统计")
    public R<CooperationStatusNumResponse> statistics() {
        CooperationStatusNumResponse statisticsResult = extensionExecutor.execute(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.statistics(AuthUtil.getUserId())
        );
        return R.data(statisticsResult);
    }

    @GetMapping("/admin/exception/task/cooperation/{id}/detail")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常协同详情")
    public R<TaskResponse> detail(@PathVariable("id") Long id) {
        return R.data(exceptionTaskCooperationService.detail(id, null));
    }

    @GetMapping("/admin/exception/task/cooperation/list")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常协同分页列表")
    public R<IPage<TaskResponse>> page(@Validated TaskCooperationQueryRequest request) {
        return R.data(exceptionTaskCooperationService.page(Condition.getPage(request), request, AuthUtil.getUserId()));
    }

    @GetMapping("/admin/exception/task/cooperation/report/{taskId}/list")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常协同超时上报流程分页列表")
    public R<IPage<TaskScheduleResponse>> timeOutReportPage(@PathVariable(value = "taskId") Long taskId, Query query) {
        return R.data(exceptionTaskCooperationService.timeOutReportPage(taskId, query));
    }

    @PostMapping("/admin/exception/task/cooperation/{id}/delete")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常协同删除")
    public R<Boolean> delete(@PathVariable(value = "id") Long id) {
        extensionExecutor.executeVoid(TaskCooperationServiceExtPt.class, COOPERATION_BIZ_SCENARIO, taskCooperationServiceExtPt ->
                taskCooperationServiceExtPt.delete(id)
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/cooperation/export")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常协同导出")
    public void export(@Validated @RequestBody TaskCooperationExportQueryRequest request, HttpServletResponse response) {
        List<TaskCooperationExportResponse> exportData = exceptionTaskCooperationService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常协同" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常协同数据表", exportData, TaskCooperationExportResponse.class);
    }

}
