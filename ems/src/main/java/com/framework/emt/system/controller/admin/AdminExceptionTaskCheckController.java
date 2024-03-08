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
import com.framework.emt.system.domain.task.ExceptionTaskNode;
import com.framework.emt.system.domain.task.check.request.TaskCheckExportQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckRejectRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckRequest;
import com.framework.emt.system.domain.task.check.response.CheckStatusNumResponse;
import com.framework.emt.system.domain.task.check.response.TaskCheckExportResponse;
import com.framework.emt.system.domain.task.check.service.TaskCheckServiceExtPt;
import com.framework.emt.system.infrastructure.exception.task.check.service.ExceptionTaskCheckService;
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
 * 异常验收接口
 *
 * @author gaojia
 * date 2023/8/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常验收"})
@ApiSupport(order = 29)
public class AdminExceptionTaskCheckController extends BaseController {

    private final ExtensionExecutor extensionExecutor;

    private final TaskCheckServiceExtPt taskCheckService;

    private final ExceptionTaskCheckService checkService;

    private final BizScenario CHECK_BIZ_SCENARIO = BizScenario.valueOf(ExceptionTaskNode.CHECK.getBizId());

    @GetMapping("/admin/exception/check/page")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常验收列表")
    public R<IPage<TaskResponse>> page(@Validated TaskCheckQueryRequest request) {
        return R.data(checkService.page(Condition.getPage(request), AuthUtil.getUserId(), request));
    }

    @GetMapping("/admin/exception/check/{id}/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常验收详情")
    public R<TaskResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(checkService.detail(id, AuthUtil.getUserId(), null));
    }

    @PostMapping("/admin/exception/check/{id}/pass")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常验收通过")
    public R<Long> exceptionCheckPass(@Validated @RequestBody TaskCheckRequest request, @PathVariable(value = "id") Long id) {
        Long result = extensionExecutor.execute(TaskCheckServiceExtPt.class, CHECK_BIZ_SCENARIO, TaskCheckServiceExtPt ->
                taskCheckService.exceptionCheckPass(AuthUtil.getUser(), id, request)
        );
        return R.data(result);
    }

    @PostMapping("/admin/exception/check/{id}/reject")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常验收驳回")
    public R<Long> exceptionCheckReject(@Validated @RequestBody TaskCheckRejectRequest request, @PathVariable(value = "id") Long id) {
        Long result = extensionExecutor.execute(TaskCheckServiceExtPt.class, CHECK_BIZ_SCENARIO, TaskCheckServiceExtPt ->
                taskCheckService.exceptionCheckReject(AuthUtil.getUser(), id, request)
        );
        return R.data(result);
    }

    @GetMapping("/admin/exception/check/status/statistics")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "处理状态数量统计")
    public R<CheckStatusNumResponse> statistics() {
        CheckStatusNumResponse statisticsResult = extensionExecutor.execute(TaskCheckServiceExtPt.class, CHECK_BIZ_SCENARIO, TaskCheckServiceExtPt ->
                checkService.statistics(AuthUtil.getUserId())
        );
        return R.data(statisticsResult);
    }

    @PostMapping("/admin/exception/check/export")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常验收导出")
    public void export(@Validated @RequestBody TaskCheckExportQueryRequest request, HttpServletResponse response) {
        List<TaskCheckExportResponse> exportData = checkService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常验收" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常验收数据表", exportData, TaskCheckExportResponse.class);
    }

    @PostMapping("/admin/exception/check/{id}/administrator/pass")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "管理员--异常验收通过")
    public R<Long> administratorCheckPass(@Validated @RequestBody TaskCheckRequest request, @PathVariable(value = "id") Long id) {
        Long result = extensionExecutor.execute(TaskCheckServiceExtPt.class, CHECK_BIZ_SCENARIO, TaskCheckServiceExtPt ->
                taskCheckService.administratorCheckPass(AuthUtil.getUser(), id, request)
        );
        return R.data(result);
    }

    @PostMapping("/admin/exception/check/{id}/administrator/reject")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "管理员--异常验收驳回")
    public R<Long> administratorCheckReject(@Validated @RequestBody TaskCheckRejectRequest request, @PathVariable(value = "id") Long id) {
        Long result = extensionExecutor.execute(TaskCheckServiceExtPt.class, CHECK_BIZ_SCENARIO, TaskCheckServiceExtPt ->
                taskCheckService.administratorCheckReject(AuthUtil.getUser(), id, request)
        );
        return R.data(result);
    }
}
