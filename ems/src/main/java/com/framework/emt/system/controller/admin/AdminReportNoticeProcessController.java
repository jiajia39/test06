package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.reportnoticeprocess.request.*;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessService;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessUserService;
import com.framework.emt.system.infrastructure.common.request.ChangeEnableFlagRequest;
import com.framework.emt.system.infrastructure.common.request.IdListRequest;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 上报流程 控制层
 *
 * @author ds_C
 * @since 2023-07-17
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-上报流程"})
@ApiSupport(order = 27)
public class AdminReportNoticeProcessController extends BaseController {

    private final ReportNoticeProcessService reportNoticeProcessService;

    private final ReportNoticeProcessUserService reportNoticeProcessUserService;

    @PostMapping("/admin/report/notice/process")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "上报流程基础信息创建")
    public R<Long> create(@Validated @RequestBody ReportNoticeProcessCreateRequest request) {
        return R.data(reportNoticeProcessService.create(request));
    }

    @PostMapping("/admin/report/notice/process/user")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "上报人及上报消息创建")
    public R<Long> createUserAndMessage(@Validated @RequestBody ReportNoticeProcessUserCreateRequest request) {
        return R.data(reportNoticeProcessUserService.create(request.init()));
    }

    @PostMapping("/admin/report/notice/process/delete")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "上报流程删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        reportNoticeProcessService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/report/notice/process/delete/batch")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "上报流程批量删除")
    public R<Boolean> deleteBatch(@Validated @RequestBody IdListRequest request) {
        reportNoticeProcessService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/report/notice/process/user/delete")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "上报人及上报消息删除")
    public R<Boolean> deleteUserAndMessage(@Validated @RequestBody IdRequest request) {
        reportNoticeProcessUserService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/report/notice/process/user/delete/batch")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "上报人及上报消息批量删除")
    public R<Boolean> deleteBatchUserAndMessage(@Validated @RequestBody IdListRequest request) {
        reportNoticeProcessUserService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/report/notice/process/{id}")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "上报流程基础信息更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody ReportNoticeProcessUpdateRequest request) {
        return R.data(reportNoticeProcessService.update(id, request));
    }

    @PostMapping("/admin/report/notice/process/user/{id}")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "上报人及上报消息更新")
    public R<Long> updateUserAndMessage(@PathVariable(value = "id") Long id, @Validated @RequestBody ReportNoticeProcessUserUpdateRequest request) {
        return R.data(reportNoticeProcessUserService.update(id, request.init()));
    }

    @GetMapping("/admin/report/notice/process/{id}/detail")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "上报流程详情")
    public R<ReportNoticeProcessResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(reportNoticeProcessService.detail(id));
    }

    @GetMapping("/admin/report/notice/process/user/{id}/detail")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "上报人及上报消息详情")
    public R<ReportNoticeProcessResponse> detailUserAndMessage(@PathVariable(value = "id") Long id) {
        return R.data(reportNoticeProcessUserService.detail(id));
    }

    @GetMapping("/admin/report/notice/process/list")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "上报流程分页列表")
    public R<IPage<ReportNoticeProcessResponse>> page(@Validated ReportNoticeProcessQueryRequest request) {
        return R.data(reportNoticeProcessService.page(request));
    }

    @GetMapping("/admin/report/notice/process/user/list")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "上报人及上报消息分页列表")
    public R<IPage<ReportNoticeProcessResponse>> pageUserAndMessage(@Validated ReportNoticeProcessUserQueryRequest request) {
        return R.data(reportNoticeProcessUserService.page(request));
    }

    @PostMapping("/admin/report/notice/process/enableFlag")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "上报流程禁用启用")
    public R<Boolean> changeEnableFlag(@Validated @RequestBody ChangeEnableFlagRequest request) {
        reportNoticeProcessService.changeEnableFlag(request.getEnableFlag(), request.getIdList());
        return R.status(true);
    }

}
