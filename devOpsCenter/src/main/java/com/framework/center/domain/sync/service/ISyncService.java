package com.framework.center.domain.sync.service;

import com.framework.center.domain.account.Account;
import com.framework.center.domain.sync.Dept;
import com.framework.center.domain.sync.request.DeptSyncRequest;
import com.framework.center.domain.sync.request.UserPasswdSyncRequest;
import com.framework.center.domain.sync.request.UserStatusRequest;
import com.framework.center.domain.sync.request.UserSyncRequest;
import com.framework.center.domain.sync.response.SyncDeptItem;
import com.framework.center.domain.sync.response.SyncResponse;
import com.framework.center.domain.sync.response.SyncUserItem;
import com.framework.common.api.entity.Kv;

import java.util.List;
import java.util.Map;

public interface ISyncService {
    SyncResponse<SyncUserItem> submitUsers(UserSyncRequest param);

    SyncResponse<SyncDeptItem> submitDepts(DeptSyncRequest param);

    SyncResponse<SyncUserItem> submitUserPasswd(UserPasswdSyncRequest param);

    /**
     * 统一门户登录
     * @param account 账户 门票
     * @param role 角色
     * @return 登录成功之后的参数
     */
    Kv login(String account);


    void syncDept(List<Dept> list,String dsName);

    List<Dept> selectDeptAll();

    void updateAccount(List<Account> accountList);

    Account getAccount(String token, String ipAddress);
    Map<Long, List<Map<String, Object>>> getRoleByUserId(Long userId);

    Boolean userStatusEnable(UserStatusRequest request);

    Boolean userStatusDisable(UserStatusRequest request);
}
