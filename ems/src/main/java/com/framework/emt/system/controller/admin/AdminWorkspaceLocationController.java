package com.framework.emt.system.controller.admin;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.emt.system.domain.workspacelocation.request.*;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationImportServiceImpl;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationService;
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
 * 作业单元 管理平台控制层
 *
 * @author ds_C
 * @since 2023-07-12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-作业单元"})
@ApiSupport(order = 24)
public class AdminWorkspaceLocationController extends BaseController {

    private final WorkspaceLocationService workspaceLocationService;

    @PostMapping("/admin/workspace/location")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "作业单元创建")
    public R<Long> create(@Validated @RequestBody WorkspaceLocationCreateRequest request) {
        return R.data(workspaceLocationService.create(request));
    }

    @PostMapping("/admin/workspace/location/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "作业单元删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        workspaceLocationService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/workspace/location/{id}")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "作业单元更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody WorkspaceLocationUpdateRequest request) {
        return R.data(workspaceLocationService.update(id, request));
    }

    @GetMapping("/admin/workspace/location/{id}/detail")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "作业单元详情")
    public R<WorkspaceLocationResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(workspaceLocationService.detail(id));
    }

    @GetMapping("/admin/workspace/location/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "作业单元分页列表")
    public R<IPage<WorkspaceLocationResponse>> page(@Validated WorkspaceLocationQueryRequest request) {
        return R.data(workspaceLocationService.page(request));
    }

    @GetMapping("/admin/workspace/location/tree")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "作业单元树状图")
    public R<List<WorkspaceLocationTreeResponse>> tree() {
        return R.data(workspaceLocationService.tree());
    }

    @PostMapping("/admin/workspace/location/import")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "作业单元导入")
    public R<Boolean> importWorkspaceLocation(@ApiParam("上传文件") @RequestPart @RequestParam MultipartFile file) {
        WorkspaceLocationImportServiceImpl importWorkspaceLocation = new WorkspaceLocationImportServiceImpl(workspaceLocationService);
        Integer maxImportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_IMPORT_NUM, AuthUtil.getTenantId()), BusinessConstant.MAX_IMPORT_NUM);
        ExcelUtil.save(file, importWorkspaceLocation, WorkspaceLocationImportExcel.class, BusinessConstant.BATCH_IMPORT_NUM, maxImportNum, true);
        return R.success("操作成功");
    }

}
