package com.framework.emt.system.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.login.request.DingTalkBindRequest;
import com.framework.emt.system.domain.login.request.WxMpBindRequest;
import com.framework.emt.system.domain.login.service.ILoginService;
import com.framework.emt.system.infrastructure.constant.enums.PlatformType;
import com.framework.wechat.mp.config.WxMpConfig;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-微信公众号登录"})
@ApiSupport(order = 33)
@Slf4j
public class AdminWxLoginController extends BaseController {
    private final WxMpConfig wxMpConfig;
    private final WxMpService wxMpService;
    private final ILoginService loginService;

    @GetMapping("/admin/wx/mp/login")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "微信公众号登录")
    public R<Kv> wxMpLogin(@Valid @NotBlank(message = "微信授权代码不能为空") @ApiParam(value = "微信授权代码") String code) {
        if (ObjectUtil.isNotNull(wxMpConfig) && ObjectUtil.isNotEmpty(wxMpConfig.getConfigs())) {
            wxMpService.switchover(wxMpConfig.getConfigs().get(0).getAppId());
            try {
                WxOAuth2AccessToken token = wxMpService.getOAuth2Service().getAccessToken(code);
                Kv kv = loginService.wxMplogin(token.getUnionId(), token.getOpenId());
                return R.data(kv);
            } catch (WxErrorException e) {
                log.info(e.getMessage());
                throw new ServiceException("获取微信公众号令牌失败！");
            }
        } else {
            throw new ServiceException("尚未配置微信公众号基础信息");
        }
    }

    @PostMapping("/admin/wx/mp/bind")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "微信公众号绑定")
    public R<Kv> wxMpBind(@Valid @RequestBody WxMpBindRequest request) {
        return R.data(loginService.wxMpBind(request.getUnionId(), request.getOpenId(), request.getAccount(), request.getPassword(), request.getTenantId(), PlatformType.WX_ENTERPRISE_APPLICATION.getCode()));
    }
}
