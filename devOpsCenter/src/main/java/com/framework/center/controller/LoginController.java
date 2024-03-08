package com.framework.center.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.login.request.LoginRequest;
import com.framework.center.domain.login.response.MyCompanyVo;
import com.framework.center.domain.login.service.ILoginService;
import com.framework.center.domain.sync.service.ISyncService;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.core.mybatisplus.support.Query;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "运维中心账户登录", tags = "运维中心账户登录")
@ApiSupport(order = 3)
@Slf4j
public class LoginController extends BaseController {
    private final ILoginService loginService;
    private final ISyncService syncService;

    @PostMapping("/admin/center/login/token")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取认证令牌", notes = "获取认证令牌")
    public R<Kv> token(@RequestBody @Valid LoginRequest param) {
        Account account = loginService.getUserIdByAccount(param.getAccount());
        return R.data(loginService.login(param, "admin", true));
    }

    @GetMapping("/admin/center/logout")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "退出系统", notes = "退出系统")
    public R<Boolean> logout() {
        return R.data(loginService.logout());
    }

    @GetMapping("/admin/center/my/company")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询本账户有权访问的公司信息", notes = "查询本账户有权访问的公司信息")
    public R<IPage<MyCompanyVo>> loadMyCompany(Query query) {
        return R.data(loginService.loadMyCompany(query));
    }
}
