package com.framework.emt.system.controller.admin;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryImportExcel;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryQueryRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryUpdateRequest;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryTreeResponse;
import com.framework.emt.system.domain.exception.service.ExceptionCategoryImportServiceImpl;
import com.framework.emt.system.domain.exception.service.ExceptionCategoryService;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 异常分类 控制层
 *
 * @author ds_C
 * @since 2023-07-12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常分类"})
@ApiSupport(order = 22)
public class AdminExceptionCategoryController extends BaseController {

    private final ExceptionCategoryService exceptionCategoryService;

    @PostMapping("/admin/exception/category")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常分类创建")
    public R<Long> create(@Validated @RequestBody ExceptionCategoryCreateRequest request) {
        return R.data(exceptionCategoryService.create(request));
    }

    @PostMapping("/admin/exception/category/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常分类删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        exceptionCategoryService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/exception/category/{id}")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常分类更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody ExceptionCategoryUpdateRequest request) {
        return R.data(exceptionCategoryService.update(id, request));
    }

    @GetMapping("/admin/exception/category/{id}/detail")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常分类详情")
    public R<ExceptionCategoryResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(exceptionCategoryService.detail(id));
    }

    @GetMapping("/admin/exception/category/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常分类分页列表")
    public R<IPage<ExceptionCategoryResponse>> page(@Validated ExceptionCategoryQueryRequest request) {
        return R.data(exceptionCategoryService.page(request));
    }

    @GetMapping("/admin/exception/category/tree")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常分类树状图")
    public R<List<ExceptionCategoryTreeResponse>> tree() {
        return R.data(exceptionCategoryService.tree());
    }

    @PostMapping("/admin/exception/category/import")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "异常分类导入")
    public R<Boolean> importExceptionCategory(@ApiParam("上传文件") @RequestPart @RequestParam MultipartFile file) {
        ExceptionCategoryImportServiceImpl importExceptionCategory = new ExceptionCategoryImportServiceImpl(exceptionCategoryService);
        Integer maxImportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_IMPORT_NUM, AuthUtil.getTenantId()), BusinessConstant.MAX_IMPORT_NUM);
        ExcelUtil.save(file, importExceptionCategory, ExceptionCategoryImportExcel.class, BusinessConstant.BATCH_IMPORT_NUM, maxImportNum, true);
        return R.success("操作成功");
    }

}
