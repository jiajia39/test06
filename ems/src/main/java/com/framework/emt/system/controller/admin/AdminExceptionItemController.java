package com.framework.emt.system.controller.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionItemExportResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.service.ExceptionItemImportServiceImpl;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.infrastructure.common.request.ChangeEnableFlagRequest;
import com.framework.emt.system.infrastructure.common.request.IdListRequest;
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

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 异常项 控制层
 *
 * @author ds_C
 * @since 2023-07-12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-异常项"})
@ApiSupport(order = 23)
public class AdminExceptionItemController extends BaseController {

    private final ExceptionItemService exceptionItemService;

    @PostMapping("/admin/exception/item")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "异常项创建")
    public R<Long> create(@Validated @RequestBody ExceptionItemCreateRequest request) {
        return R.data(exceptionItemService.create(request));
    }

    @PostMapping("/admin/exception/item/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "异常项删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        exceptionItemService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/exception/item/delete/batch")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "异常项批量删除")
    public R<Boolean> deleteBatch(@Validated @RequestBody IdListRequest request) {
        exceptionItemService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/exception/item/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "异常项更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody ExceptionItemUpdateRequest request) {
        return R.data(exceptionItemService.update(id, request));
    }

    @GetMapping("/admin/exception/item/{id}/detail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常项详情")
    public R<ExceptionItemResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(exceptionItemService.detail(id));
    }

    @GetMapping("/admin/exception/item/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "异常项分页列表")
    public R<IPage<ExceptionItemResponse>> page(@Validated ExceptionItemQueryRequest request) {
        return R.data(exceptionItemService.page(request));
    }

    @PostMapping("/admin/exception/item/import")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "异常项导入")
    public R<Boolean> importExceptionItem(@ApiParam("上传文件") @RequestPart @RequestParam MultipartFile file) {
        ExceptionItemImportServiceImpl importExceptionItem = new ExceptionItemImportServiceImpl(exceptionItemService);
        Integer maxImportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_IMPORT_NUM, AuthUtil.getTenantId()), BusinessConstant.MAX_IMPORT_NUM);
        ExcelUtil.save(file, importExceptionItem, ExceptionItemImportExcel.class, BusinessConstant.BATCH_IMPORT_NUM, maxImportNum, true);
        return R.success("操作成功");
    }

    @PostMapping("/admin/exception/item/export")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "异常项导出")
    public void export(@Validated @RequestBody ExceptionItemExportQueryRequest request, HttpServletResponse response) {
        ExcelUtil.export(response, "异常项" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "异常项数据表", exceptionItemService.export(request), ExceptionItemExportResponse.class);
    }

    @PostMapping("/admin/exception/item/enableFlag")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "异常项禁用启用")
    public R<Boolean> changeEnableFlag(@Validated @RequestBody ChangeEnableFlagRequest request) {
        exceptionItemService.changeEnableFlag(request.getEnableFlag(), request.getIdList());
        return R.status(true);
    }

}
