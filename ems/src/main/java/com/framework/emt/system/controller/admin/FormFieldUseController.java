package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseCreateRequest;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseQuery;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseUpdateRequest;
import com.framework.emt.system.domain.formfieldUse.response.FormFieldUseResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 异常单字段使用表 接口层
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Api(tags = "异常单字段使用表管理")
@Validated
@RestController
@RequiredArgsConstructor
public class FormFieldUseController extends BaseController {

    private final IFormFieldUseService IFormFieldUseService;

    @ApiOperation(value = "新增异常单字段使用表")
    @PostMapping("/formFieldUse")
    public R<Long> create(@RequestBody FormFieldUseCreateRequest request) {
        return R.data(IFormFieldUseService.create(request));
    }

    @ApiOperation(value = "更新异常单字段使用表")
    @PostMapping("/formFieldUse/{id}")
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody FormFieldUseUpdateRequest request) {
        IFormFieldUseService.update(id, request);
        return R.status(true);
    }

    @ApiOperation(value = "异常单字段使用表分页")
    @GetMapping("/formFieldUse/page")
    public R<IPage<FormFieldUseResponse>> page(@Valid FormFieldUseQuery query) {
        return R.data(IFormFieldUseService.page(query));
    }

    @ApiOperation(value = "查看异常单字段使用表")
    @GetMapping("/formFieldUse/{id}")
    public R<FormFieldUseResponse> info(@PathVariable("id") Long id) {
        return R.data(IFormFieldUseService.info(id));
    }

    @ApiOperation(value = "删除异常单字段使用表")
    @DeleteMapping("/formFieldUse/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        IFormFieldUseService.delete(id);
        return R.status(true);
    }

}
