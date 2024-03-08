package com.framework.emt.system.controller.app;

import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationDetailRequest;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作业单元 移动端控制层
 *
 * @author ds_C
 * @since 2023-07-12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"移动端-作业单元"})
@ApiSupport(order = 24)
public class AppWorkspaceLocationController extends BaseController {

    private final WorkspaceLocationService workspaceLocationService;

    @GetMapping("/app/workspace/location/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "通过二维码查询作业单元详情")
    public R<WorkspaceLocationResponse> detailApp(@Validated WorkspaceLocationDetailRequest request) {
        return R.data(workspaceLocationService.detailApp(request));
    }

}
