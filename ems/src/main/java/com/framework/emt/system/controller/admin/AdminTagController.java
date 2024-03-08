package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.tag.request.TagCreateRequest;
import com.framework.emt.system.domain.tag.request.TagQueryRequest;
import com.framework.emt.system.domain.tag.request.TagUpdateRequest;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 异常标签 管理平台控制层
 *
 * @author gaojia
 * @since 2023-08-02
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常标签"})
@ApiSupport(order = 31)
public class AdminTagController extends BaseController {

    private final TagService tagService;

    @PostMapping("/admin/tag")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常标签创建")
    public R<Long> create(@Validated @RequestBody TagCreateRequest request) {
        return R.data(tagService.create(request));
    }

    @DeleteMapping("/admin/tag/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常标签删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        tagService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/tag/{id}")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常标签更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody TagUpdateRequest request) {
        return R.data(tagService.update(id, request));
    }

    @GetMapping("/admin/tag/{id}/detail")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常标签详情")
    public R<TagResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(tagService.detail(id));
    }

    @GetMapping("/admin/tag/page/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常标签分页列表")
    public R<IPage<TagResponse>> page(@Validated TagQueryRequest request) {
        return R.data(tagService.page(request));
    }

}
