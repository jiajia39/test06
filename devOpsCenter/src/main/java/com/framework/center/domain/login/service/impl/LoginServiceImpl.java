package com.framework.center.domain.login.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.service.IAccountService;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.service.ICompanyService;
import com.framework.center.domain.login.request.LoginRequest;
import com.framework.center.domain.login.response.MyCompanyVo;
import com.framework.center.domain.login.service.ILoginService;
import com.framework.center.infrastructure.config.PreviewConfig;
import com.framework.common.api.constant.ApiConstant;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.exception.SecureException;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.api.utils.WebUtil;
import com.framework.common.auth.constant.TokenConstant;
import com.framework.common.auth.entity.TokenInfo;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.common.jwt.utils.JwtUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.core.mybatisplus.support.Query;
import com.framework.core.secure.constant.SecureConstant;
import com.framework.core.secure.provider.IClientDetails;
import com.framework.core.secure.utils.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class LoginServiceImpl implements ILoginService {
    private final IAccountService accountService;
    private final PreviewConfig previewConfig;
    private final ICompanyService companyService;

    private void setAuthorization(String clientId, String clientSecret) {
        if (WebUtil.getRequest() != null) {
            String authentication =
                    SecureConstant.BASIC_HEADER_PREFIX
                            + Base64.encode
                            (
                                    Convert.toStr(clientId, "")
                                            .concat(StrPool.COLON)
                                            .concat(Convert.toStr(clientSecret, ""))
                            );
            WebUtil.getRequest().setAttribute(SecureConstant.BASIC_HEADER_KEY, authentication);
        }
    }

    @Override
    public Account getUserIdByAccount(String accountName) {
        return getUserByAccount(accountName);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public Kv login(LoginRequest param, String clientId, boolean checkPasswd) {
        IClientDetails client = SecureUtil.clientDetails(clientId);
        if (ObjectUtil.isNull(client)) {
            throw new ServiceException("未找到对应的终端信息！");
        }
        Account account = getUserByAccount(param.getAccount());
        if (checkPasswd) {
            if (!StrUtil.equals(account.getPassword(), DigestUtil.sha1Hex(param.getPassword()))) {
                throw new ServiceException("登录失败，密码不正确！");
            }
        }
        if (!ObjectUtil.equals(account.getStatus(), 1)) {
            throw new ServiceException("登录失败，账户已被禁用，请您联系管理员！");
        }
        if (ObjectUtil.equals(account.getStatus(), 0)) {
            throw new ServiceException("登录失败，该用户已经离职，请您联系管理员！");
        }
        Map<String, Object> params = new HashMap<>(16);
        String roleId = "";
        boolean exceptionManagement = false;
        params.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        params.put(TokenConstant.TENANT_ID, ApiConstant.ADMIN_TENANT_ID);
        params.put(TokenConstant.USER_ID, account.getId());
        params.put(TokenConstant.ROLE_ID, roleId);
        params.put(TokenConstant.ACCOUNT, account.getAccount());
        params.put(TokenConstant.USER_NAME, account.getName());
        params.put(TokenConstant.NICK_NAME, account.getName());
        params.put(TokenConstant.CLIENT_ID, clientId);
        Kv kv = Kv.create();
        params.put(TokenConstant.DETAIL, kv);
        setAuthorization(Convert.toStr(params.get(TokenConstant.CLIENT_ID)), client.getClientSecret());
        TokenInfo accessToken = SecureUtil.createJwt(params, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
        Kv authInfo = Kv.create();
        authInfo.set(TokenConstant.TENANT_ID, Convert.toStr(params.get(TokenConstant.TENANT_ID)))
                .set(TokenConstant.USER_ID, Convert.toStr(params.get(TokenConstant.USER_ID)))
                .set(TokenConstant.ROLE_ID, roleId)
                .set(TokenConstant.ACCOUNT, Convert.toStr(params.get(TokenConstant.ACCOUNT)))
                .set(TokenConstant.USER_NAME, Convert.toStr(params.get(TokenConstant.USER_NAME)))
                .set(TokenConstant.NICK_NAME, Convert.toStr(params.get(TokenConstant.NICK_NAME)))
                .set(TokenConstant.ROLE_NAME, Convert.toStr(params.get(TokenConstant.ROLE_NAME)))
                .set(TokenConstant.ACCESS_TOKEN, accessToken.getToken())
                .set(TokenConstant.TOKEN_TYPE, TokenConstant.BEARER)
                .set(TokenConstant.EXPIRES_IN, accessToken.getExpire())
                .set(TokenConstant.LICENSE, TokenConstant.LICENSE_NAME);

        if (WebUtil.getRequest() != null) {
            authInfo.set(SecureConstant.BASIC_HEADER_KEY, WebUtil.getRequest().getAttribute(SecureConstant.BASIC_HEADER_KEY));
            WebUtil.getRequest().removeAttribute(SecureConstant.BASIC_HEADER_KEY);
        }
        authInfo.set("previewUrl", previewConfig.getUrl());
        authInfo.set("exceptionManagement", exceptionManagement);
        return authInfo;
    }

    @NotNull
    private Account getUserByAccount(String accountName) {
        LambdaQueryWrapper<Account> query = new LambdaQueryWrapper<>();
        query.eq(Account::getAccount, accountName);
        query.eq(Account::getIsDeleted, ApiConstant.DB_NOT_DELETED);
        Account account = accountService.getBaseMapper().selectOne(query);
        if (ObjectUtil.isNull(account)) {
            throw new ServiceException("账户不存在！");
        }
        return account;
    }

    @Override
    public Boolean logout() {
        if (ObjectUtil.isNotEmpty(AuthUtil.getUser())) {
            JwtUtil.removeAccessToken(AuthUtil.getTenantId(), Convert.toStr(Objects.requireNonNull(AuthUtil.getUser()).getUserId()));
        }
        return true;
    }

    @Override
    public Boolean kickOutUsers(List<String> userIdList) {
        if (ObjectUtil.isNotEmpty(userIdList)) {
            for (String userId : userIdList) {
                JwtUtil.removeAccessToken("000000", userId);
            }
            return true;
        }
        return false;
    }


    @Override
    public IPage<MyCompanyVo> loadMyCompany(Query query) {
        Long accountId = AuthUtil.getUserId();
        Account account = accountService.getById(accountId);
        if (ObjectUtil.isNotNull(accountId) && ObjectUtil.isNotNull(account)) {
            LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();

            log.info("查询我的公司，进入公司//////////////////////////////////////////////////////////////////");
            IPage<Company> page = companyService.page(Condition.getPage(query), wrapper);
            IPage<MyCompanyVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            if (ObjectUtil.isNotEmpty(page.getRecords())) {
                TypeReference<List<MyCompanyVo>> type = new TypeReference<List<MyCompanyVo>>() {
                };
                List<MyCompanyVo> list = JsonUtil.parse(JsonUtil.toJson(page.getRecords()), type);
                result.setRecords(list);
            }
            return result;
        }
        throw new SecureException("您尚未登录系统或者登录信息已过期");
    }
}
