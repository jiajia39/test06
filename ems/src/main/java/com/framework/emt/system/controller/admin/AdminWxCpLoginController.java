package com.framework.emt.system.controller.admin;

import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.login.request.WxCpBindRequest;
import com.framework.emt.system.domain.login.service.ILoginService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-企业微信登录"})
@ApiSupport(order = 35)
@Slf4j
public class AdminWxCpLoginController extends BaseController {
    private final ILoginService loginService;

    @GetMapping("/admin/wx/cp/login/{agentId}")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "企业微信登录")
    public R<Kv> wxMpLogin(@PathVariable Integer agentId, @Valid @NotBlank(message = "企业微信授权代码不能为空") @ApiParam(value = "企业微信授权代码") String code) {
        Kv kv = loginService.wxCplogin(agentId,code);
        return R.data(kv);
    }

    @PostMapping("/admin/wx/cp/bind")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "企业微信绑定")
    public R<Kv> wxMpBind(@Valid @RequestBody WxCpBindRequest request) {
        return R.data(loginService.wxCpBind(request));
    }
}
