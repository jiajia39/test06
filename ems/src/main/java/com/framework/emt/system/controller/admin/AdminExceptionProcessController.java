package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessSubmitResponse;
import com.framework.emt.system.domain.exception.service.ExceptionProcessService;
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
 * 异常流程 控制层
 *
 * @author ds_C
 * @since 2023-07-20
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常流程"})
@ApiSupport(order = 28)
public class AdminExceptionProcessController extends BaseController {

    private final ExceptionProcessService exceptionProcessService;

    @PostMapping("/admin/exception/process")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常流程创建")
    public R<Long> create(@Validated @RequestBody ExceptionProcessCreateRequest request) {
        return R.data(exceptionProcessService.create(request.init()));
    }

    @PostMapping("/admin/exception/process/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常流程删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        exceptionProcessService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/exception/process/delete/batch")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常流程批量删除")
    public R<Boolean> deleteBatch(@Validated @RequestBody IdListRequest request) {
        exceptionProcessService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/exception/process/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常流程更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody ExceptionProcessUpdateRequest request) {
        return R.data(exceptionProcessService.update(id, request.init()));
    }

    @GetMapping("/admin/exception/process/{id}/detail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常流程详情")
    public R<ExceptionProcessResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(exceptionProcessService.detail(id));
    }

    @GetMapping("/admin/exception/process/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常流程分页列表")
    public R<IPage<ExceptionProcessResponse>> page(@Validated ExceptionProcessQueryRequest request) {
        return R.data(exceptionProcessService.page(request));
    }

    @PostMapping("/admin/exception/process/tag")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常流程标签新增")
    public R<Long> createTag(@Validated @RequestBody ExceptionProcessTagCreateRequest request) {
        return R.data(exceptionProcessService.createTag(request));
    }

    @PostMapping("/admin/exception/process/tag/relation")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常流程标签关系新增")
    public R<Long> createTagRelation(@Validated @RequestBody ExceptionProcessTagRelationCreateRequest request) {
        return R.data(exceptionProcessService.createTagRelation(request.init()));
    }

    @PostMapping("/admin/exception/process/tag/delete")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常流程标签删除")
    public R<Boolean> deleteTag(@Validated @RequestBody ExceptionProcessTagDeleteRequest request) {
        exceptionProcessService.deleteTag(request);
        return R.status(true);
    }

    @GetMapping("/admin/exception/process/{id}/submit")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常流程响应数据")
    public R<ExceptionProcessSubmitResponse> detailSubmit(@PathVariable(value = "id") Long id) {
        return R.data(exceptionProcessService.detailSubmit(id));
    }

}
