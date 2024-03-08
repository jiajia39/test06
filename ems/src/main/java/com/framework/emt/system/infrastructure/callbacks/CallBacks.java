package com.framework.emt.system.infrastructure.callbacks;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.framework.admin.system.callbacks.LoginCallBack;
import com.framework.admin.system.callbacks.RoleCallBack;
import com.framework.admin.system.vo.RoleVO;
import com.framework.common.api.entity.Kv;
import com.framework.emt.system.domain.role.RoleExt;
import com.framework.emt.system.domain.role.service.IRoleExtService;
import com.framework.emt.system.infrastructure.config.PreviewConfig;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CallBacks {



    private final LoginCallBack loginCallBack;

    private final PreviewConfig previewConfig;


    private final RoleCallBack roleCallBack;

    private final IRoleExtService roleExtService;

    @PostConstruct
    public void initCallBacks(){
        loginCallBack.setAdminLoginCallBack(kv -> {
            kv.put("previewUrl",previewConfig.getUrl());
            return kv;
        });

        roleCallBack.setBeforeSaveRoleCallBack(kv->{
                Integer exceptionManagement = 0;
                if(ObjectUtil.isNotEmpty(kv) && kv.containsKey(BusinessConstant.EXCEPTION_MANAGEMENT)){
                    exceptionManagement= Convert.toInt(kv.get(BusinessConstant.EXCEPTION_MANAGEMENT),0);
                }
                Long roleId = kv.get("id", null);
                LambdaQueryWrapper<RoleExt> query=new LambdaQueryWrapper<>();
                query.eq(RoleExt::getRoleId, roleId);
                query.last(" limit 1 ");
                RoleExt entity= roleExtService.getBaseMapper().selectOne(query);
                if(ObjectUtil.isNull(entity)){
                    entity=new RoleExt();
                    entity.setRoleId(roleId);
                    entity.setExceptionManagement(exceptionManagement);
                    roleExtService.save(entity);
                }else{
                    entity.setRoleId(roleId);
                    entity.setExceptionManagement(exceptionManagement);
                    roleExtService.updateById(entity);
                }
            return true;
        });

        roleCallBack.setAfterSelectRoleCallBack(list->{
            if(ObjectUtil.isNotEmpty(list)){
                List<Long> roleIdList = list.stream().map(RoleVO::getId).collect(Collectors.toList());
                if(ObjectUtil.isNotEmpty(roleIdList)){
                    LambdaQueryWrapper<RoleExt> query=new LambdaQueryWrapper<>();
                    query.in(RoleExt::getRoleId, roleIdList);
                    query.eq(RoleExt::getIsDeleted, 0);
                    List<RoleExt> roleExtList = roleExtService.getBaseMapper().selectList(query);
                    Map<Long, List<RoleExt>> map=new HashMap<>();
                    if(ObjectUtil.isNotEmpty(roleExtList)){
                        map = roleExtList.stream().collect(Collectors.groupingBy(RoleExt::getRoleId));
                    }
                    for (RoleVO roleVO : list) {
                         if(ObjectUtil.isNotEmpty(map) && map.containsKey(roleVO.getId())){
                             if(ObjectUtil.isNotEmpty(map.get(roleVO.getId()))){
                                 roleVO.setExtendedParameters(Kv.create().set(BusinessConstant.EXCEPTION_MANAGEMENT,map.get(roleVO.getId()).get(0).getExceptionManagement()));
                             }
                         }else {
                             roleVO.setExtendedParameters(Kv.create().set(BusinessConstant.EXCEPTION_MANAGEMENT, 0));
                         }
                    }
                }

            }
            return list;
        });
    }
}
