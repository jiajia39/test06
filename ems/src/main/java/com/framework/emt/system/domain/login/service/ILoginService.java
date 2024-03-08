package com.framework.emt.system.domain.login.service;

import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.framework.common.api.entity.Kv;
import com.framework.emt.system.domain.login.request.DingTalkBindRequest;
import com.framework.emt.system.domain.login.request.DingTalkMessageRequest;
import com.framework.emt.system.domain.login.request.WxCpBindRequest;

public interface ILoginService {
    Kv wxMplogin(String unionId, String openId);

    Kv wxMpBind(String unionId, String openId, String account, String password, String tenantId, Integer platformType);

    /**
     * 获取钉钉的用户信息
     *
     * @param authCode 临时编码
     * @return
     * @throws Exception
     */
    UserGetResponse getDTAccessToken(String authCode) throws Exception;

    Kv dingTalkBind(DingTalkBindRequest request);

    /**
     * 根据临时授权代码登录
     *
     * @param code 临时授权代码
     * @return 用户信息
     */
    Kv dingTalkLogin(String code) throws Exception;


    String getAccessToken();

    Kv wxCplogin(Integer agentId, String code);


    Kv wxCpBind(WxCpBindRequest request);

}
