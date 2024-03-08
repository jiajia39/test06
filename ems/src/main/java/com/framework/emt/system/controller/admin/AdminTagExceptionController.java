package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionQueryRequest;
import com.framework.emt.system.domain.tagexception.response.TagExceptionResponse;
import com.framework.emt.system.domain.tagexception.service.TagExceptionService;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 异常标签关联 管理平台控制层
 *
 * @author gaojia
 * @since 2023-08-02
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常标签关联"})
@ApiSupport(order = 31)
public class AdminTagExceptionController extends BaseController {

    private final TagExceptionService tagExceptionService;

    @PostMapping("/admin/tag/exception")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常标签创建")
    public R<Long> create(@Validated @RequestBody TagExceptionCreateRequest request) {
        return R.data(tagExceptionService.create(request));
    }

    @DeleteMapping("/admin/tag/exception/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常标签删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        tagExceptionService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/tag/exception/add/list")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常标签新增")
    public R<Long> update(@Validated @RequestBody TagExceptionCreateListRequest request) {
        tagExceptionService.createList(request);
        return R.status(true);
    }

    @GetMapping("/admin/tag/exception/{id}/detail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常标签详情")
    public R<TagExceptionResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(tagExceptionService.detail(id));
    }

    @GetMapping("/admin/tag/exception/page/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常标签分页列表")
    public R<IPage<TagExceptionResponse>> page(@Validated TagExceptionQueryRequest request) {
        return R.data(tagExceptionService.page(request));
    }

}
