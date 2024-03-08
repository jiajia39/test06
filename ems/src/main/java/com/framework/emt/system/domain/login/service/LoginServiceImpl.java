package com.framework.emt.system.domain.login.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.util.UserInfo;
import com.framework.common.api.constant.ApiConstant;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.api.utils.WebUtil;
import com.framework.common.auth.constant.TokenConstant;
import com.framework.common.auth.entity.TokenInfo;
import com.framework.core.secure.constant.SecureConstant;
import com.framework.core.secure.provider.IClientDetails;
import com.framework.core.secure.utils.SecureUtil;
import com.framework.emt.system.domain.login.ThirdPartyInfo;
import com.framework.emt.system.domain.login.request.DingTalkBindRequest;
import com.framework.emt.system.domain.login.request.WxCpBindRequest;
import com.framework.emt.system.infrastructure.config.DingTalkConfig;
import com.framework.emt.system.infrastructure.constant.enums.PlatformType;
import com.framework.wechat.cp.service.IWxCpCustomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUserDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements ILoginService {
    private final IWxCpCustomService wxCpCustomService;
    private final IUserDao userDao;

    private final IThirdPartyInfoService thirdPartyInfoService;


    private final DingTalkConfig dingTalkConfig;

    @Override
    public Kv wxMplogin(String unionId, String openId) {
        return login(null, unionId, openId, PlatformType.WX_ENTERPRISE_APPLICATION.getCode());
    }

    @Override
    public Kv wxMpBind(String unionId, String openId, String account, String password, String tenantId, Integer platformType) {
        return thirdPartyInfoBind(unionId, openId, account, password, tenantId, platformType, null, null, null, null);
    }

    /**
     * 绑定已存在，登录，不存在新增绑定信息
     *
     * @param unionId      平台用户唯一标识
     * @param openId       应用用户唯一标识
     * @param account      账户
     * @param password     密码
     * @param tenantId     租户Id
     * @param platformType 平台类型 1 微信公众号 2微信小程序3 微信企业应用4 钉钉
     * @param clientId     平台应用唯一标识
     * @param nickName     昵称
     * @param headImgUrl   头像
     * @param phone        电话
     * @return 用户信息
     */

    public Kv thirdPartyInfoBind(String unionId, String openId, String account, String password, String tenantId, Integer platformType, String clientId, String nickName, String headImgUrl, String phone) {
        Kv kv;
        ThirdPartyInfo thirdPartyInfo = thirdPartyInfoService.getThirdPartyInfo(unionId, openId, platformType);
        UserInfo userInfo;
        if (ObjectUtil.isNotNull(thirdPartyInfo) && ObjectUtil.isNotNull(thirdPartyInfo.getUserId())) {
            userInfo = userDao.userInfo(thirdPartyInfo.getUserId());
            kv = loginByUser(userInfo);
            kv.put("unionid", unionId);
            kv.put("openid", openId);
            kv.put("isBindWeChatOfficialAccount", "true");
        } else {
            ThirdPartyInfo thirdPartyInfoNew = new ThirdPartyInfo();
            String digestPwd = DigestUtil.sha1Hex(password);
            userInfo = this.userDao.userInfo(tenantId, account, digestPwd);
            if (ObjectUtil.isNotNull(userInfo) && ObjectUtil.isNotNull(userInfo.getUser()) && ObjectUtil.isNotNull(userInfo.getUser().getId())) {
                if (ObjectUtil.isNotNull(thirdPartyInfoService.getThirdPartyInfoByUserId(userInfo.getUser().getId(), platformType))) {
                    if (ObjectUtil.equal(platformType, PlatformType.WX_ENTERPRISE_APPLICATION.getCode())) {
                        throw new ServiceException("该账户已经绑定其他微信公众号！");
                    } else if (ObjectUtil.equal(platformType, PlatformType.DING_TALK.getCode())) {
                        throw new ServiceException("该账户已经绑定其他钉钉账号！");
                    }
                }
                thirdPartyInfoNew.setUnionId(unionId);
                thirdPartyInfoNew.setUserId(userInfo.getUser().getId());
                thirdPartyInfoNew.setOpenId(openId);
                thirdPartyInfoNew.setPlatformType(platformType);
                thirdPartyInfoNew.setClientId(clientId);
                thirdPartyInfoNew.setPhone(phone);
                thirdPartyInfoNew.setNickName(nickName);
                thirdPartyInfoNew.setHeadImgUrl(headImgUrl);
                thirdPartyInfoNew.setAuthorizationStatus("0");
                boolean isBindWeChatOfficialAccount = thirdPartyInfoService.save(thirdPartyInfoNew);
                kv = loginByUser(userInfo);
                kv.put("unionid", unionId);
                kv.put("openid", openId);
                kv.put("isBindWeChatOfficialAccount", Convert.toStr(isBindWeChatOfficialAccount));
            } else {
                throw new ServiceException("账户密码错误！");
            }
        }
        return kv;
    }

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

    private Kv loginByUser(UserInfo user) {
        IClientDetails client = SecureUtil.clientDetails("admin");
        Map<String, Object> params = new HashMap<>(16);
        params.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        params.put(TokenConstant.TENANT_ID, ApiConstant.ADMIN_TENANT_ID);
        params.put(TokenConstant.USER_ID, user.getUser().getId());
        if (ObjectUtil.isNotNull(user.getUser()) && ObjectUtil.isNotEmpty(user.getUser().getRoleIdList())) {
            String roleIdStr = user.getUser().getRoleIdList().stream().map(id -> Convert.toStr(id, "")).filter(StrUtil::isNotBlank).distinct().collect(Collectors.joining(StrPool.COMMA));
            params.put(TokenConstant.ROLE_ID, roleIdStr);
        }
        String roles = "";
        if (ObjectUtil.isNotEmpty(user.getRoles())) {
            roles = user.getRoles().stream().filter(Objects::nonNull).collect(Collectors.joining(StrPool.COMMA));
        }
        params.put(TokenConstant.ROLE_NAME, roles);

        params.put(TokenConstant.ACCOUNT, user.getUser().getAccount());
        params.put(TokenConstant.USER_NAME, user.getUser().getName());
        params.put(TokenConstant.NICK_NAME, user.getUser().getName());
        params.put(TokenConstant.CLIENT_ID, client.getClientId());
        Kv kv = Kv.create();
        params.put(TokenConstant.DETAIL, kv);
        setAuthorization(Convert.toStr(params.get(TokenConstant.CLIENT_ID)), client.getClientSecret());
        TokenInfo accessToken = SecureUtil.createJwt(params, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
        Kv authInfo = Kv.create();
        Long deptId = null;
        List<Long> deptIdList = user.getUser().getDeptIdList();
        if (ObjectUtil.isNotEmpty(deptIdList)) {
            deptId = deptIdList.get(0);
        }
        authInfo.set(TokenConstant.TENANT_ID, Convert.toStr(params.get(TokenConstant.TENANT_ID)))
                .set(TokenConstant.USER_ID, Convert.toStr(params.get(TokenConstant.USER_ID)))
                .set(TokenConstant.ACCOUNT, Convert.toStr(params.get(TokenConstant.ACCOUNT)))
                .set(TokenConstant.USER_NAME, Convert.toStr(params.get(TokenConstant.USER_NAME)))
                .set(TokenConstant.NICK_NAME, Convert.toStr(params.get(TokenConstant.NICK_NAME)))
                .set(TokenConstant.ROLE_NAME, Convert.toStr(params.get(TokenConstant.ROLE_NAME)))
                .set(TokenConstant.AVATAR, Convert.toStr(user.getUser().getAvatar(), TokenConstant.DEFAULT_AVATAR))
                .set(TokenConstant.ACCESS_TOKEN, accessToken.getToken())
                .set(TokenConstant.TOKEN_TYPE, TokenConstant.BEARER)
                .set(TokenConstant.EXPIRES_IN, accessToken.getExpire())
                .set(TokenConstant.DEPT_ID, deptId)
                .set(TokenConstant.LICENSE, TokenConstant.LICENSE_NAME);
        if (WebUtil.getRequest() != null) {
            authInfo.set(SecureConstant.BASIC_HEADER_KEY, WebUtil.getRequest().getAttribute(SecureConstant.BASIC_HEADER_KEY));
            WebUtil.getRequest().removeAttribute(SecureConstant.BASIC_HEADER_KEY);
        }
        return authInfo;
    }

    public static Client authClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    @Override
    public Kv dingTalkBind(DingTalkBindRequest request) {
        return thirdPartyInfoBind(request.getUnionId(), request.getOpenId(), request.getAccount(), request.getPassword(), request.getTenantId(), PlatformType.DING_TALK.getCode(), request.getClientId(), request.getNickName(), request.getHeadImgUrl(), request.getPhone());
    }

    @Override
    public Kv dingTalkLogin(String code) throws Exception {
        OapiV2UserGetResponse.UserGetResponse userGetResponse = getDTAccessToken(code);
        if (ObjectUtil.isEmpty(userGetResponse)) {
            throw new ServiceException("临时授权代码错误，未获取到用户信息");
        }
        String unionId = userGetResponse.getUnionid();
        String openId = userGetResponse.getUserid();
        Kv kv = login(dingTalkConfig.getClientId(), unionId, openId, PlatformType.DING_TALK.getCode());
        kv.put("phone", userGetResponse.getMobile());
        kv.put("headImgUrl", userGetResponse.getAvatar());
        kv.put("nickName", userGetResponse.getName());
        return kv;
    }

    public Kv login(String clientId, String unionId, String openId, Integer platformType) {
        Kv kv = Kv.create();
        ThirdPartyInfo thirdPartyInfo;
        if (StrUtil.isNotBlank(clientId)) {
            thirdPartyInfo = thirdPartyInfoService.getThirdPartyInfoByClientId(clientId, openId, platformType);
        } else {
            thirdPartyInfo = thirdPartyInfoService.getThirdPartyInfo(unionId, openId, platformType);
        }
        if (ObjectUtil.isNotNull(thirdPartyInfo) && ObjectUtil.isNotNull(thirdPartyInfo.getUserId())) {
            UserInfo userInfo = userDao.userInfo(thirdPartyInfo.getUserId());
            kv = loginByUser(userInfo);
            kv.set("unionid", unionId).set("clientId", clientId).set("openid", openId).set("isBindWeChatOfficialAccount", "true");
        } else {
            kv.set("unionid", unionId).set("clientId", clientId).set("openid", openId).set("isBindWeChatOfficialAccount", "false");
        }
        return kv;
    }


    @Override
    public OapiV2UserGetResponse.UserGetResponse getDTAccessToken(@RequestParam(value = "authCode") String authCode) throws Exception {
        //1.获取accessToken
        String accessToken = getAccessToken();
        DingTalkClient dingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
        req.setCode(authCode);
        OapiV2UserGetuserinfoResponse rsp = dingTalkClient.execute(req, accessToken);
        System.out.println(rsp.getBody());
        JSONObject UserGetUserInfoJsonObject = JSONUtil.parseObj(rsp.getBody());
        if (!ObjectUtil.equal(Convert.toInt(UserGetUserInfoJsonObject.get("errcode")), 0)) {
            throw new ServiceException(UserGetUserInfoJsonObject.get("errmsg").toString());
        }
        String userid = rsp.getResult().getUserid();
        //3根据 ueserId和accessToken 获取用户信息
        DingTalkClient dingTalkClient1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest oapiV2UserGetRequest = new OapiV2UserGetRequest();
        oapiV2UserGetRequest.setUserid(userid);
        OapiV2UserGetResponse oapiV2UserGetResponse = dingTalkClient1.execute(oapiV2UserGetRequest, accessToken);
        System.out.println(oapiV2UserGetResponse.getBody());
        JSONObject jsonObject = JSONUtil.parseObj(oapiV2UserGetResponse.getBody());
        if (!ObjectUtil.equal(Convert.toInt(jsonObject.get("errcode")), 0)) {
            throw new ServiceException(jsonObject.get("errmsg").toString());
        }
        OapiV2UserGetResponse.UserGetResponse oapiV2UserGetResponseResult = oapiV2UserGetResponse.getResult();
        log.info("用户信息" + oapiV2UserGetResponseResult);
        return oapiV2UserGetResponseResult;
    }

    @Override
    public String getAccessToken() {
        //1.获取accessToken
        try {
            Client client = authClient();
            GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                    //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
                    .setAppKey(dingTalkConfig.getClientId())
                    //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
                    .setAppSecret(dingTalkConfig.getClientSecret());

            GetAccessTokenResponse getAccessTokenResponse = client.getAccessToken(getAccessTokenRequest);
            //2.根据accessToken和authCode 获取用户的userid
            return getAccessTokenResponse.getBody().getAccessToken();
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.info("报错信息" + err);
                throw new ServiceException(err.message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Kv wxCplogin(Integer agentId, String code) {
        WxCpService wxcpService = wxCpCustomService.getWxCpService(agentId);
        WxCpOauth2UserInfo userInfo;
        WxCpUserDetail userDetail;
        try {
            userInfo = wxcpService.getOauth2Service().getUserInfo(agentId, code);
            userDetail = wxcpService.getOauth2Service().getUserDetail(userInfo.getUserTicket());

        } catch (WxErrorException e) {
            log.error("获取企业微信用户失败：", e);
            throw new ServiceException("获取企业微信用户失败!");
        }
        Kv kv = login(Convert.toStr(agentId), null, userInfo.getUserId(), PlatformType.WX_OFFICIAL_ACCOUNT.getCode());
        kv.put("phone", userDetail.getMobile());
        kv.put("headImgUrl", userDetail.getAvatar());
        kv.put("nickName", userDetail.getName());
        return kv;
    }

    @Override
    public Kv wxCpBind(WxCpBindRequest request) {
        return thirdPartyInfoBind(null, request.getOpenId(), request.getAccount(), request.getPassword(), request.getTenantId(), PlatformType.WX_OFFICIAL_ACCOUNT.getCode(), request.getClientId(), request.getNickName(), request.getHeadImgUrl(), request.getPhone());
    }

}

