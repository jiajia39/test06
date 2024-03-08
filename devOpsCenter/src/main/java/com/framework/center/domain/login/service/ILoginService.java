package com.framework.center.domain.login.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.login.request.LoginRequest;
import com.framework.center.domain.login.response.MyCompanyVo;
import com.framework.common.api.entity.Kv;
import com.framework.core.mybatisplus.support.Query;

import java.util.List;
import java.util.Map;

public interface ILoginService {

    Account getUserIdByAccount(String account);
    /**
     * 登录
     *
     * @param param    登录参数
     * @param clientId 登录终端
     * @param checkPasswd 是否校验密码
     * @return 登录返回的token信息
     */
    Kv login(LoginRequest param, String clientId,boolean checkPasswd);

    /**
     * 退出登录
     *
     * @return 是否成功
     */
    Boolean logout();

    /**
     * 查询当前用户拥有登录权限的公司
     *
     * @param query 分页信息
     * @return 公司信息
     */
    IPage<MyCompanyVo> loadMyCompany(Query query);

    /**
     * 踢出用户
     * @param userIdList 用户id列表
     * @return 是否成功
     */
    Boolean kickOutUsers(List<String> userIdList);
}
