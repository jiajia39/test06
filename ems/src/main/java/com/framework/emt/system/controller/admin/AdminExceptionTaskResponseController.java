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
import com.framework.emt.system.domain.task.response.request.*;
import com.framework.emt.system.domain.task.response.response.TaskResponseExportResponse;
import com.framework.emt.system.domain.task.response.response.ResponseStatusNumResponse;
import com.framework.emt.system.domain.task.response.service.TaskResponseServiceExtPt;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.infrastructure.exception.task.response.service.ExceptionTaskResponseService;
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
 * 异常响应接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常响应"})
@ApiSupport(order = 29)
public class AdminExceptionTaskResponseController extends BaseController {

    private final ExtensionExecutor extensionExecutor;

    private final ExceptionTaskResponseService responseService;

    private final BizScenario RESPONSE_BIZ_SCENARIO = BizScenario.valueOf(ExceptionTaskNode.RESPONSE.getBizId());

    @GetMapping("/admin/exception/response/page")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常响应分页列表")
    public R<IPage<TaskResponse>> pageResponse(@Validated TaskResponseQueryRequest request) {
        return R.data(responseService.page(Condition.getPage(request), AuthUtil.getUserId(), request));
    }

    @GetMapping("/admin/exception/response/report/{taskId}/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常响应超时上报流程分页列表")
    public R<IPage<TaskScheduleResponse>> timeOutReportPage(@PathVariable(value = "taskId") Long taskId, Query query) {
        return R.data(responseService.timeOutReportPage(taskId, query));
    }

    @GetMapping("/admin/exception/response/{id}/detail")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常响应详情")
    public R<TaskResponse> detailResponse(@PathVariable(value = "id") Long id) {
        return R.data(responseService.detail(id, AuthUtil.getUserId()));
    }

    @PostMapping("/admin/exception/response/{id}/reject")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常响应驳回")
    public R<Long> rejectResponse(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskResponseRejectRequest request) {
        Long detailResponse = extensionExecutor.execute(TaskResponseServiceExtPt.class, RESPONSE_BIZ_SCENARIO, taskResponseServiceExtPt ->
                taskResponseServiceExtPt.reject(id, request.getRejectReason(), AuthUtil.getUser())
        );
        return R.data(detailResponse);
    }

    @PostMapping("/admin/exception/response/{id}/transfer")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常响应转派")
    public R<Long> transferResponse(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskResponseTransferRequest request) {
        Long detailResponse = extensionExecutor.execute(TaskResponseServiceExtPt.class, RESPONSE_BIZ_SCENARIO, taskResponseServiceExtPt ->
                taskResponseServiceExtPt.toOther(id, AuthUtil.getUser(), request.getOtherRemark(), request.getUserId())
        );
        return R.data(detailResponse);
    }

    @PostMapping("/admin/exception/response/{id}/accept")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常响应接受")
    public R<Long> acceptResponse(@PathVariable(value = "id") Long id) {
        Long detailResponse = extensionExecutor.execute(TaskResponseServiceExtPt.class, RESPONSE_BIZ_SCENARIO, taskResponseServiceExtPt ->
                taskResponseServiceExtPt.accept(id, AuthUtil.getUserId())
        );
        return R.data(detailResponse);
    }

    @PostMapping("/admin/exception/response/{id}/submit")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常响应提交")
    public R<Long> submit(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskResponseHandingRequest request) {
        Long detailResponse = extensionExecutor.execute(TaskResponseServiceExtPt.class, RESPONSE_BIZ_SCENARIO, taskResponseServiceExtPt ->
                taskResponseServiceExtPt.submit(id, request.getSubmitHandingUserId(), request.getSubmitExtendDatas())
        );
        return R.data(detailResponse);
    }

    @GetMapping("/admin/exception/response/info/{taskId}")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "获取响应配置信息")
    public R<SettingHandingResponse> handingInfo(@PathVariable(value = "taskId") Long taskId) {
        return R.data(responseService.handingInfo(taskId));
    }

    @GetMapping("/admin/exception/response/status/statistics")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "响应状态数量统计")
    public R<ResponseStatusNumResponse> statistics() {
        return R.data(responseService.statistics(AuthUtil.getUserId()));
    }

    @PostMapping("/admin/exception/response/{taskId}/administrator/transfer")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "管理员异常响应转派")
    public R<Long> adminResponseTransfer(@PathVariable(value = "taskId") Long taskId, @Validated @RequestBody TaskResponseTransferRequest request) {
        Long detailResponse = extensionExecutor.execute(TaskResponseServiceExtPt.class, RESPONSE_BIZ_SCENARIO, taskResponseServiceExtPt ->
                taskResponseServiceExtPt.adminResponseTransfer(taskId, request)
        );
        return R.data(detailResponse);
    }

    @PostMapping("/admin/exception/response/export")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "异常响应导出")
    public void export(@Validated @RequestBody TaskResponseExportQueryRequest request, HttpServletResponse response) {
        List<TaskResponseExportResponse> exportData = responseService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常响应" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常响应数据表", exportData, TaskResponseExportResponse.class);
    }

}
