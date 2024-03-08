package com.framework.center.controller;

import cn.hutool.core.util.ObjectUtil;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.login.service.ILoginService;
import com.framework.center.domain.sync.request.*;
import com.framework.center.domain.sync.response.SyncDeptItem;
import com.framework.center.domain.sync.response.SyncResponse;
import com.framework.center.domain.sync.response.SyncUserItem;
import com.framework.center.domain.sync.service.ISyncService;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.api.utils.WebUtil;
import com.framework.common.json.utils.JsonUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "数据同步管理", tags = "数据同步管理")
@ApiSupport(order = 5)
@Slf4j
public class SyncController extends BaseController {
    private final ISyncService syncService;

    private final ILoginService loginService;

    /**
     * 用户数据同步
     */
    @PostMapping("/admin/sync/user")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户数据同步", notes = "用户数据同步")
    public SyncResponse<SyncUserItem> submit(@Valid @RequestBody UserSyncRequest param) {
        return syncService.submitUsers(param);
    }

    /**
     * 用户密码同步
     */
    @PostMapping("/admin/sync/userPasswd")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户密码同步", notes = "用户密码同步")
    public SyncResponse<SyncUserItem> submit(@Valid @RequestBody UserPasswdSyncRequest param) {
        return syncService.submitUserPasswd(param);
    }

    /**
     * 部门数据同步
     */
    @PostMapping("/admin/sync/dept")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "部门数据同步", notes = "部门数据同步")
    public SyncResponse<SyncDeptItem> submit(@Valid @RequestBody DeptSyncRequest param) {
        return syncService.submitDepts(param);
    }

    /**
     * 统一门户登录
     */
    @PostMapping("/admin/sso/login")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "统一门户登录", notes = "统一门户登录")
    public R<Kv> sso(@RequestBody @Validated SsoLoginRequest param) {
        log.info("统一认证请求token:" + JsonUtil.toJson(param));
        String ip = WebUtil.getIp(WebUtil.getRequest());
        log.info("统一认证请求Ip:" + ip);
        Account account = syncService.getAccount(param.getToken(), ip);
        if (ObjectUtil.isNull(account)) {
            throw new ServiceException("账户不存在！");
        }
        Kv kv = syncService.login(account.getAccount());
        return R.data(kv);
    }

    /**
     * 用户批量启用
     */
    @PostMapping("/admin/sync/user/status/enable")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "用户批量启用", notes = "用户批量启用")
    public R<Boolean> userStatusEnable(@RequestBody @Valid UserStatusRequest request) {
        return R.data(syncService.userStatusEnable(request));
    }

    /**
     * 用户批量禁用
     */
    @PostMapping("/admin/sync/user/status/disable")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "用户批量启用", notes = "用户批量启用")
    public R<Boolean> userStatusDisable(@RequestBody @Valid UserStatusRequest request) {
        return R.data(syncService.userStatusDisable(request));
    }

}
