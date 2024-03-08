package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.formfield.request.*;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfield.service.FormFieldService;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 异常表单字段表 管理平台控制层
 *
 * @author gaojia
 * @since 2023-07-28
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常表单字段表"})
@ApiSupport(order = 30)
public class AdminFormFieldController extends BaseController {

    private final FormFieldService formFieldService;

    @PostMapping("/admin/form/field")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常表单字段表创建")
    public R<Long> create(@RequestBody @Validated FormFieldCreateRequest request) {
        return R.data(formFieldService.create(request));
    }

    @DeleteMapping("/admin/form/field/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常表单字段表删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        formFieldService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/form/field/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常表单字段表更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody FormFieldUpdateRequest request) {
        return R.data(formFieldService.update(id, request));
    }

    @GetMapping("/admin/form/field/{id}/detail/{carryDictData}")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常表单字段表详情")
    public R<FormFieldResponse> detail(@PathVariable(value = "id") Long id, @PathVariable(value = "carryDictData") Boolean carryDictData) throws JsonProcessingException {
        return R.data(formFieldService.detail(id, carryDictData));
    }

    @GetMapping("/admin/form/field/page/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常表单字段表分页列表")
    public R<IPage<FormFieldResponse>> page(@Validated FormFieldQueryRequest request) {
        return R.data(formFieldService.page(request));
    }

    @GetMapping("/admin/form/field/list")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常表单字段表列表")
    public R<List<FormFieldResponse>> list(@Validated FormFieldListQueryRequest request) {
        return R.data(formFieldService.list(request));
    }

    @PostMapping("/admin/form/field/update/status/{id}")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常表单字段表更新状态")
    public R<Long> updateStatus(@PathVariable(value = "id") Long id, @Validated @RequestBody FormFieldUpdateRequest request) {
        return R.data(formFieldService.updateStatus(id, request));
    }
}
