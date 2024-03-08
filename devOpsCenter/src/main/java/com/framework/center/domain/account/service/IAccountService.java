package com.framework.center.domain.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.request.AccountCreateRequest;
import com.framework.center.domain.account.request.AccountQueryRequest;
import com.framework.center.domain.account.request.AccountUpdatePassWordRequest;
import com.framework.center.domain.account.request.AccountUpdateRequest;
import com.framework.center.domain.account.response.AccountVo;
import com.framework.center.infrastructure.service.BaseService;

/**
 * @author yankunw
 * @description 针对表【mss_account】的数据库操作Service
 * @createDate 2023-03-08 16:20:38
 */
public interface IAccountService extends BaseService<Account> {

    /**
     * 新增账户
     *
     * @param param 新增参数
     * @return 是否成功
     */
    Long submit(AccountCreateRequest param);

    /**
     * 编辑账户信息
     *
     * @param id    账户id
     * @param param 修改参数
     * @return 是否成功
     */
    Boolean edit(Long id, AccountUpdateRequest param);

    /**
     * 账户详情
     *
     * @param id 账户id
     * @return 账户详情
     */
    AccountVo detail(Long id);

    /**
     * 查询账户列表
     *
     * @param param 查询参数
     * @return 账户分页信息
     */
    IPage<AccountVo> queryList(AccountQueryRequest param);

    /**
     * 重置面膜
     *
     * @param accountIds 账户id
     */

    boolean resetPassword(String accountIds);

    /**
     * 修改密码
     *
     * @param request 参数
     * @return 结果
     */
    boolean updatePassword(AccountUpdatePassWordRequest request);
}
