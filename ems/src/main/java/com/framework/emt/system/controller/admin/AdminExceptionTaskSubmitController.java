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
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.task.ExceptionTaskNode;
import com.framework.emt.system.domain.task.submit.request.*;
import com.framework.emt.system.domain.task.submit.response.TaskSubmitExportResponse;
import com.framework.emt.system.domain.task.submit.service.TaskSubmitServiceExtPt;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
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
 * 异常提报接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常提报"})
@ApiSupport(order = 29)
public class AdminExceptionTaskSubmitController extends BaseController {

    private final ExtensionExecutor extensionExecutor;

    private final ExceptionTaskSubmitService exceptionTaskSubmitService;

    private final BizScenario SUBMIT_BIZ_SCENARIO = BizScenario.valueOf(ExceptionTaskNode.SUBMIT.getBizId());

    @PostMapping("/admin/exception/task/submit/create")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常提报创建")
    public R<Long> create(@Validated @RequestBody TaskSubmitCreateRequest request) {
        Long id = extensionExecutor.execute(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.create(request.init())
        );
        return R.data(id);
    }

    @PostMapping("/admin/exception/task/submit/{id}")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常提报更新")
    public R<Boolean> update(@PathVariable(value = "id") Long id, @Validated @RequestBody TaskSubmitUpdateRequest request) {
        extensionExecutor.executeVoid(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.update(id, request.init())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/submit/delete")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常提报删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        extensionExecutor.executeVoid(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.delete(request.getId())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/submit")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常提报")
    public R<Boolean> submit(@Validated @RequestBody TaskSubmitRequest request) {
        extensionExecutor.executeVoid(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.submit(request.getId(), AuthUtil.getUser())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/submit/close")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常提报关闭")
    public R<Boolean> close(@Validated @RequestBody TaskSubmitRequest request) {
        extensionExecutor.executeVoid(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.close(request.getId())
        );
        return R.status(true);
    }

    @GetMapping("/admin/exception/task/submit/{id}/detail")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常提报详情")
    public R<TaskResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(exceptionTaskSubmitService.detail(id, AuthUtil.getUserId()));
    }

    @GetMapping("/admin/exception/task/submit/list")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常提报分页列表")
    public R<IPage<TaskResponse>> page(@Validated TaskSubmitQueryRequest request) {
        return R.data(exceptionTaskSubmitService.page(Condition.getPage(request), request, AuthUtil.getUserId()));
    }

    @PostMapping("/admin/exception/task/submit/export")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常提报导出")
    public void export(@Validated @RequestBody TaskSubmitExportQueryRequest request, HttpServletResponse response) {
        List<TaskSubmitExportResponse> exportData = exceptionTaskSubmitService.findExportData(request, AuthUtil.getUserId());
        ExcelUtil.export(response, "异常提报" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常提报数据表", exportData, TaskSubmitExportResponse.class);
    }

    @PostMapping("/admin/exception/task/submit/cancel")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常提报撤销")
    public R<Boolean> cancel(@Validated @RequestBody TaskSubmitRequest request) {
        extensionExecutor.executeVoid(TaskSubmitServiceExtPt.class, SUBMIT_BIZ_SCENARIO, taskSubmitServiceExtPt ->
                taskSubmitServiceExtPt.cancel(request.getId())
        );
        return R.status(true);
    }

    @PostMapping("/admin/exception/task/submit/item/{processId}")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常提报新增编辑异常项下拉框")
    public R<List<ExceptionItemResponse>> processOfItems(@PathVariable(value = "processId") Long processId) {
        return R.data(exceptionTaskSubmitService.processOfItems(processId));
    }

}
