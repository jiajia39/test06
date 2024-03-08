package com.framework.emt.system.controller.admin;

import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.login.request.DingTalkBindRequest;
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
@Api(tags = {"管理平台-钉钉登录"})
@ApiSupport(order = 34)
@Slf4j
public class AdminDingTalkLoginController extends BaseController {
    private final ILoginService loginService;

    @GetMapping("/admin/ding-talk/get/user/info")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "钉钉获取用户信息")
    public OapiV2UserGetResponse.UserGetResponse getAccessToken(@RequestParam(value = "authCode") String authCode) throws Exception {
        return loginService.getDTAccessToken(authCode);
    }

    @GetMapping("/admin/ding-talk/login")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "钉钉登录")
    public R<Kv> dingTalkLogin(@Valid @NotBlank(message = "临时授权代码不能为空") @ApiParam(value = "临时授权代码") String code) throws Exception {
        return R.data(loginService.dingTalkLogin(code));
    }

    @PostMapping("/admin/ding-talk/mp/bind")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "钉钉绑定")
    public R<Kv> dingTalkBind(@Valid @RequestBody DingTalkBindRequest request) {
        return R.data(loginService.dingTalkBind(request));
    }
}
