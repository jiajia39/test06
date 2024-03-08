package com.framework.center.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.account.request.AccountCreateRequest;
import com.framework.center.domain.account.request.AccountQueryRequest;
import com.framework.center.domain.account.request.AccountUpdatePassWordRequest;
import com.framework.center.domain.account.request.AccountUpdateRequest;
import com.framework.center.domain.account.response.AccountVo;
import com.framework.center.domain.account.service.IAccountService;
import com.framework.center.infrastructure.common.IdsRequest;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "运维中心账户管理", tags = "运维中心账户管理")
@ApiSupport(order = 2)
public class AccountController extends BaseController {
    private final IAccountService accountService;

    /**
     * 账户创建
     */
    @PostMapping("/admin/account")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "账户创建", notes = "账户创建")
    public R<Long> submit(@Valid @RequestBody AccountCreateRequest param) {
        return R.data(accountService.submit(param));
    }

    /**
     * 账户编辑
     */
    @PostMapping("/admin/account/{id}")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "账户编辑", notes = "账户编辑")
    public R<Boolean> edit(@PathVariable(value = "id") Long id, @Valid @RequestBody AccountUpdateRequest param) {
        return R.status(accountService.edit(id, param));
    }

    /**
     * 账户详情
     */
    @GetMapping("/admin/account/{id}/detail")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "账户详情", notes = "账户详情")
    public R<AccountVo> edit(@PathVariable(value = "id") Long id) {
        AccountVo res = accountService.detail(id);
        return R.data(res);
    }

    /**
     * 批量删除账户
     *
     * @param idsRequest id 列表
     */
    @DeleteMapping("/admin/account/delete")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "批量删除账户", notes = "批量删除账户")
    public R<Boolean> delete(@RequestBody @Validated IdsRequest idsRequest) {
        String ids = idsRequest.getIds().stream().map(Convert::toStr).collect(Collectors.joining(StrPool.COMMA));
        Boolean result = accountService.deleteByIds(ids);
        return R.status(result);
    }

    /**
     * 账户列表
     */
    @GetMapping("/admin/account/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "账户列表", notes = "账户列表")
    public R<IPage<AccountVo>> list(AccountQueryRequest param) {
        IPage<AccountVo> result = accountService.queryList(param);
        return R.data(result);
    }

    @PostMapping({"/admin/account/reset-password"})
    @ApiOperationSupport(
            order = 6
    )
    @ApiOperation(
            value = "初始化密码",
            notes = "传入accountId集合"
    )
    public R<Boolean> resetPassword(@ApiParam(value = "accountId集合", required = true) @RequestParam String userIds) {
        return R.updateStatus(accountService.resetPassword(userIds));
    }

    @PostMapping({"/admin/account/update-password"})
    @ApiOperationSupport(
            order = 7
    )
    @ApiOperation(
            value = "修改密码"
    )
    public R<Boolean> updatePassword(@RequestBody AccountUpdatePassWordRequest request) {
        return R.updateStatus(accountService.updatePassword(request));
    }
}
