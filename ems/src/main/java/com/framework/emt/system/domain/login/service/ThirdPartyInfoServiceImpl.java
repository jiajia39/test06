package com.framework.emt.system.domain.login.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.framework.emt.system.domain.login.ThirdPartyInfo;
import com.framework.emt.system.domain.login.mapper.ThirdPartyInfoMapper;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.PlatformType;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThirdPartyInfoServiceImpl extends BaseServiceImpl<ThirdPartyInfoMapper, ThirdPartyInfo> implements IThirdPartyInfoService {

    @Override
    public ThirdPartyInfo getThirdPartyInfo(String unionId, String openId, Integer platformType) {
        LambdaQueryWrapper<ThirdPartyInfo> query = getThirdPartyInfoLambdaQueryWrapper(null,unionId,openId,platformType);
        query.last(" limit 1");
        return this.getBaseMapper().selectOne(query);
    }

    @Override
    public ThirdPartyInfo getThirdPartyInfoByClientId(String client, String openId, Integer platformType) {
        LambdaQueryWrapper<ThirdPartyInfo> query = getThirdPartyInfoLambdaQueryWrapper(client,null ,openId, platformType);
        query.last(" limit 1");
        return this.getBaseMapper().selectOne(query);
    }

    @NotNull
    private static LambdaQueryWrapper<ThirdPartyInfo> getThirdPartyInfoLambdaQueryWrapper(String client,String unionId, String openId, Integer platformType) {
        LambdaQueryWrapper<ThirdPartyInfo> query = new LambdaQueryWrapper<>();
        query.eq(StrUtil.isNotBlank(client),ThirdPartyInfo::getClientId, client);
        query.eq(StrUtil.isNotBlank(unionId),ThirdPartyInfo::getUnionId, unionId);
        query.eq(StrUtil.isNotBlank(openId),ThirdPartyInfo::getOpenId, openId);
        query.eq(ObjectUtil.isNotNull(platformType),ThirdPartyInfo::getPlatformType, platformType);
        query.eq(ThirdPartyInfo::getIsDeleted, 0);
        return query;
    }

    @Override
    public ThirdPartyInfo getThirdPartyInfoByUserId(Long userId, Integer platformType) {
        LambdaQueryWrapper<ThirdPartyInfo> query = new LambdaQueryWrapper<>();
        query.eq(ThirdPartyInfo::getPlatformType, platformType);
        query.eq(ThirdPartyInfo::getUserId, userId);
        query.eq(ThirdPartyInfo::getIsDeleted, 0);
        query.eq(ThirdPartyInfo::getAuthorizationStatus, 0);
        query.last(" limit 1");
        return this.getBaseMapper().selectOne(query);
    }

    @Override
    public Map<Long, String> getChannelsByUserIdList(List<Long> userIdList) {
        Map<Long, String> result =new  HashMap<>();
        if (ObjectUtil.isNotNull(userIdList) && ObjectUtil.isNotEmpty(userIdList)) {
            LambdaQueryWrapper<ThirdPartyInfo> query = new LambdaQueryWrapper<>();
            query.in(ThirdPartyInfo::getUserId, userIdList);
            query.eq(ThirdPartyInfo::getIsDeleted, 0);
            List<ThirdPartyInfo> searchList = this.getBaseMapper().selectList(query);
            if (ObjectUtil.isNotEmpty(searchList)) {
                Map<Long, List<ThirdPartyInfo>> map = searchList.stream().collect(Collectors.groupingBy(ThirdPartyInfo::getUserId));
                if(ObjectUtil.isNotNull(map) && ObjectUtil.isNotEmpty(map.keySet())){
                    for (Long userId : map.keySet()) {
                        List<ThirdPartyInfo> infoList=map.get(userId);
                        if(ObjectUtil.isNotEmpty(infoList)){
                            String channels = infoList.stream()
                                    .filter(i->ObjectUtil.isNotNull(i.getPlatformType()))
                                    .map(i -> Convert.toStr(BaseEnum.parseByCode(PlatformType.class, i.getPlatformType()).getChannel().getCode())).collect(Collectors.joining(","));
                            result.put(userId,channels);
                        }
                    }
                }
            }
        }
        return result;
    }
}