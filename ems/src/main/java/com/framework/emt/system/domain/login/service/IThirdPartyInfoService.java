package com.framework.emt.system.domain.login.service;

import com.framework.emt.system.domain.login.ThirdPartyInfo;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

public interface IThirdPartyInfoService extends BaseService<ThirdPartyInfo> {
    ThirdPartyInfo getThirdPartyInfo(String unionId, String openId, Integer platformType);

    ThirdPartyInfo getThirdPartyInfoByClientId(String client, String openId, Integer platformType);

    ThirdPartyInfo getThirdPartyInfoByUserId(Long userId, Integer platformType);

    Map<Long, String> getChannelsByUserIdList(List<Long> userIdList);
}
