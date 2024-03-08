package com.framework.center.domain.sync.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.service.IAccountService;
import com.framework.center.domain.login.request.LoginRequest;
import com.framework.center.domain.login.service.ILoginService;
import com.framework.center.domain.sync.Dept;
import com.framework.center.domain.sync.SyncRawDeptData;
import com.framework.center.domain.sync.SyncRawUserData;
import com.framework.center.domain.sync.UserRole;
import com.framework.center.domain.sync.config.SsoConfig;
import com.framework.center.domain.sync.convert.SyncRawDeptDataConvertor;
import com.framework.center.domain.sync.convert.SyncRawUserDataConvertor;
import com.framework.center.domain.sync.request.*;
import com.framework.center.domain.sync.response.SyncDeptItem;
import com.framework.center.domain.sync.response.SyncResponse;
import com.framework.center.domain.sync.response.SyncUserItem;
import com.framework.center.domain.sync.service.IDeptService;
import com.framework.center.domain.sync.service.ISyncRawDeptDataService;
import com.framework.center.domain.sync.service.ISyncRawUserDataService;
import com.framework.center.domain.sync.service.ISyncService;
import com.framework.center.infrastructure.constant.code.BusinessErrorCode;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.json.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode", "SqlDialectInspection", "SqlNoDataSourceInspection"})
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SyncServiceImpl implements ISyncService {
    private final ILoginService loginService;
    private final SsoConfig ssoConfig;

    private final ISyncRawUserDataService syncRawUserDataService;

    private final ISyncRawDeptDataService syncRawDeptDataService;

    private final JdbcTemplate jdbcTemplate;


    private final IAccountService accountService;

    private final IDeptService deptService;


    @Override
    @Transactional
    public SyncResponse<SyncUserItem> submitUsers(UserSyncRequest param) {
        String batchNumber= UUID.randomUUID().toString();
        log.info(JsonUtil.toJson(param));
        SyncResponse<SyncUserItem> response=new SyncResponse<>();
        response.setIstat("1");
        response.setSysid(param.getSysid());
        response.setMessage("操作成功");
        response.setSendid(param.getSendid());
        response.setItfid(param.getItfid());
        List<SyncUserItem> list=new ArrayList<>();
        if(ObjectUtil.isNotEmpty(param.getItem())){
            for (UserContentRequest userContentRequest : param.getItem()) {
                SyncUserItem item=new SyncUserItem();
                item.setUid(userContentRequest.getUid());
                item.setIstat("1");
                item.setMessage("操作成功");
                list.add(item);
            }
            List<SyncRawUserData> datas = SyncRawUserDataConvertor.INSTANCE.requestList2List(param.getItem());
            datas=datas.stream().peek(data->{data.setBatchNumber(batchNumber);data.setProcessStatus(0);}).collect(Collectors.toList());
            syncRawUserDataService.saveBatch(datas);
        }
        response.setSyncItemList(list);
        return response;
    }
    @Override
    public SyncResponse<SyncDeptItem> submitDepts(DeptSyncRequest param) {
        String batchNumber= UUID.randomUUID().toString();
        log.info(JsonUtil.toJson(param));
        SyncResponse<SyncDeptItem> response=new SyncResponse<>();
        response.setIstat("1");
        response.setSysid(param.getSysid());
        response.setMessage("操作成功");
        response.setSendid(param.getSendid());
        response.setItfid(param.getItfid());
        List<SyncDeptItem> list=new ArrayList<>();
        if(ObjectUtil.isNotEmpty(param.getItem())){
            for (DeptContentRequest userContentRequest : param.getItem()) {
                SyncDeptItem item=new SyncDeptItem();
                item.setUid(userContentRequest.getDepartmentguid());
                item.setIstat("1");
                item.setMessage("操作成功");
                list.add(item);
            }
            List<SyncRawDeptData> datas = SyncRawDeptDataConvertor.INSTANCE.requestList2List(param.getItem());
            datas=datas.stream().peek(data->data.setBatchNumber(batchNumber)).collect(Collectors.toList());
            syncRawDeptDataService.saveBatch(datas);
        }
        response.setSyncItemList(list);
        return response;
    }
    @Override
    public SyncResponse<SyncUserItem> submitUserPasswd(UserPasswdSyncRequest param) {
        log.info(JsonUtil.toJson(param));
        SyncResponse<SyncUserItem> response=new SyncResponse<>();
        response.setIstat("1");
        response.setSysid(param.getSysid());
        response.setMessage("操作成功");
        response.setSendid(param.getSendid());
        response.setItfid(param.getItfid());
        List<SyncUserItem> list=new ArrayList<>();
        if(ObjectUtil.isNotEmpty(param.getItem())){
            for (UserPasswdRequest userContentRequest : param.getItem()) {
                SyncUserItem item=new SyncUserItem();
                item.setUid(userContentRequest.getUid());
                item.setIstat("1");
                item.setMessage("操作成功");
                list.add(item);
                String password= DigestUtil.sha1Hex(userContentRequest.getPassword());
                String sql="update mss_account set `password`='"+password+"'  ,process_status =1    where uid ='"+userContentRequest.getUid()+"' and is_deleted=0";
                jdbcTemplate.update(sql);
            }
        }
        response.setSyncItemList(list);
        return response;
    }

    @Override
    public Kv login(String account) {
        if(StrUtil.isNotBlank(account)){
            LoginRequest param=new LoginRequest();
            param.setAccount(account);
            return loginService.login(param, "admin", false);
        }else{
            throw new ServiceException(BusinessErrorCode.LOGIN_FAILED);
        }
    }

    @Override
    public Account getAccount(String token, String ipAddress) {
        String body = getLoginBody(token, ipAddress);
        log.info("访问统一门户登录响应信息："+body);
        String result=null;
        try {
            JSONObject json = JSONUtil.parseObj(body);
            if(json.containsKey("state") && json.getBool("state")){
                if(json.containsKey("data") && json.getJSONObject("data")!=null){
                    JSONObject object = json.getJSONObject("data");
                    result= object.getStr("ResponseBody");
                }
            }
        }catch (Exception ignored){}
        String account=null;
        if(StrUtil.isNotBlank(result)){
            Map<String, String> resultMap = getResultMap(result);
            if(resultMap.containsKey("message") && StrUtil.equals(resultMap.get("message"),"success")){
                account=resultMap.get("outid");
            }else{
                throw new ServiceException(BusinessErrorCode.LOGIN_FAILED);
            }
        }
        if(StrUtil.isNotBlank(account)){
            LambdaQueryWrapper<Account> query=new LambdaQueryWrapper<>();
            query.eq(Account::getIsDeleted,0);
            query.eq(Account::getAccount,account);
            query.last(" limit 1");
            return accountService.getBaseMapper().selectOne(query);
        }
        return null;
    }

    private String getLoginBody(String token, String ipAddress) {
        Map<String, List<String>> header=new HashMap<>();
        List<String> value=new ArrayList<>();
        value.add(ssoConfig.getAppKey());
        header.put("appKey",value);
        Map<String,String> param=new HashMap<>();
        param.put("ltpaToken", token);
        param.put("systemID",ssoConfig.getSystemId());
        param.put("ipAddress", ipAddress);
        log.info("统一认证请求参数："+JsonUtil.toJson(param));
        log.info("统一认证请求头："+JsonUtil.toJson(header));
        log.info("统一认证请求地址："+ssoConfig.getUrl());
        HttpResponse response = HttpUtil.createPost(ssoConfig.getUrl()).header(header).body(JsonUtil.toJson(param)).execute();
        if(response==null || response.getStatus()!= 200 || StrUtil.isBlank(response.body())){
            throw new ServiceException(BusinessErrorCode.LOGIN_FAILED);
        }
        return response.body();
    }

    private  Map<String, String> getResultMap(String data) {
        Map<String,String> map=new HashMap<>();
        List<String> list = StrUtil.splitTrim(data, ";");
        for (String content : list) {
            List<String> array = StrUtil.splitTrim(content, "=");
            String key=array.get(0);
            String value="";
            if(array.size()==2){
                value=array.get(1);
            }
            if(StrUtil.equals("validityDate",key) && StrUtil.isNotBlank(value)){
                value=value.substring(0,10) +" "+value.substring(11,19);
            }
            map.put(key,value);
        }
        return map;
    }

    public Dept getDeptByThirdPartyId(String thirdPartyId){
        LambdaQueryWrapper<Dept> query=new LambdaQueryWrapper<>();
        query.eq(Dept::getThirdPartyId,thirdPartyId);
        query.eq(Dept::getIsDeleted,0);
        query.last(" limit 1");
        return deptService.getBaseMapper().selectOne(query);
    }

    private List<SyncRawUserData> getSyncRawUserData(String batchNumber) {
        LambdaQueryWrapper<SyncRawUserData> query=new LambdaQueryWrapper<>();
        query.eq(SyncRawUserData::getProcessStatus,0);
        query.eq(SyncRawUserData::getIsDeleted,0);
        query.eq(SyncRawUserData::getBatchNumber,batchNumber);
        query.last(" limit 1000");
        return syncRawUserDataService.getBaseMapper().selectList(query);
    }

    public String getAncestors(Dept dept,Map<Long, List<Dept>> idMap){
        if(ObjectUtil.isNull(dept.getParentId()) || dept.getParentId()==0){
            return "0";
        }
        List<String> strList=new ArrayList<>();
        Dept parentDept=dept;
        do{
           if(idMap.containsKey(parentDept.getParentId()) && idMap.get(parentDept.getParentId())!=null){
               strList.add(Convert.toStr(parentDept.getParentId()));
               parentDept=idMap.get(parentDept.getParentId()).get(0);
           }else{
               strList.add("0");
               parentDept=null;
           }

        }while (parentDept!=null);
        return String.join(StrPool.COMMA, ListUtil.reverse(strList));
    }
    private List<SyncRawDeptData> getSyncRawDeptData(String batchNumber) {
        LambdaQueryWrapper<SyncRawDeptData> query=new LambdaQueryWrapper<>();
        query.eq(SyncRawDeptData::getProcessStatus,0);
        query.eq(SyncRawDeptData::getIsDeleted,0);
        query.eq(SyncRawDeptData::getBatchNumber, batchNumber);
        return syncRawDeptDataService.getBaseMapper().selectList(query);
    }
    private List<SyncRawDeptData> findRootNodes(Map<String, List<SyncRawDeptData>> parentMap,Map<String, List<SyncRawDeptData>> map) {
        List<SyncRawDeptData> list=new ArrayList<>();
        for (String deptId : parentMap.keySet()) {
            if(!map.containsKey(deptId)){
                list.addAll(parentMap.get(deptId));
            }
        }
        return list;
    }

    @Override
    @DS("#dsName")
    public  void syncDept(List<Dept> list,String dsName) {
        jdbcTemplate.execute("truncate table ft_dept");
        if(ObjectUtil.isNotEmpty(list)){
            for (Dept dept : list) {
                deptService.getBaseMapper().insert(dept);
            }
        }
    }

    @Override
    public Map<Long, List<Map<String, Object>>> getRoleByUserId(Long userId) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                " SELECT r.id, r.role_alias, r.role_name, case when e.exception_management is null then 0 else e.exception_management end as exception_management FROM ft_user_role ur LEFT JOIN ft_role r ON ur.role_id = r.id and r.is_deleted=0 LEFT JOIN emt_role_ext e ON r.id= e.role_id and e.is_deleted=0 WHERE ur.is_deleted = 0 AND ur.user_id=" + Convert.toStr(userId)
        );
        return list.stream().collect(Collectors.groupingBy(row -> Convert.toLong(row.get("id"))));
    }


    @Override
    public List<Dept> selectDeptAll() {
        LambdaQueryWrapper<Dept> query=new LambdaQueryWrapper<>();
        query.eq(Dept::getIsDeleted,0);
        return deptService.getBaseMapper().selectList(query);
    }

    @Override
    public void updateAccount(List<Account> accountList){
        if(ObjectUtil.isNotEmpty(accountList)){
            this.accountService.updateBatchById(accountList);
        }
    }





    private static UserRole getUserRole(Long id ,Long userId,Long roleId) {
        UserRole userRole=new UserRole();
        userRole.setId(id);
        userRole.setStatus(1);
        userRole.setIsDeleted(0);
        userRole.setUpdateTime(new Date());
        userRole.setUpdateTime(new Date());
        userRole.setTenantId("000000");
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        return userRole;
    }
    private static Map<Long, List<Map<String, Object>>> getUserStatusMap(List<Map<String, Object>> userStatusList) {
        Map<Long, List<Map<String, Object>>> userStatusMap =new HashMap<>();
        if(ObjectUtil.isNotEmpty(userStatusList)){
            userStatusMap = userStatusList.stream().collect(Collectors.groupingBy(map -> Convert.toLong(map.get("id"))));
        }
        return userStatusMap;
    }

    @Override
    public Boolean userStatusEnable(UserStatusRequest request) {
        return updateUserByStatus(request,1);
    }
    @Override
    public Boolean userStatusDisable(UserStatusRequest request) {
        return updateUserByStatus(request,2);
    }
    private boolean updateUserByStatus(UserStatusRequest request,int status) {
        LambdaUpdateWrapper<Account> query=new LambdaUpdateWrapper<>();
        query.in(Account::getId, request.getUserIdList());
        query.set(Account::getStatus,status);
        return accountService.update(query);
    }



}
