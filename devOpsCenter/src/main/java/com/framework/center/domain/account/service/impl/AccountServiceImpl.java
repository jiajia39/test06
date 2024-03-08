package com.framework.center.domain.account.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.convert.AccountConvertor;
import com.framework.center.domain.account.mapper.AccountMapper;
import com.framework.center.domain.account.request.AccountCreateRequest;
import com.framework.center.domain.account.request.AccountQueryRequest;
import com.framework.center.domain.account.request.AccountUpdatePassWordRequest;
import com.framework.center.domain.account.request.AccountUpdateRequest;
import com.framework.center.domain.account.response.AccountVo;
import com.framework.center.domain.account.service.IAccountService;
import com.framework.center.infrastructure.constant.code.BusinessErrorCode;
import com.framework.center.infrastructure.service.BaseServiceImpl;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.entity.BaseEntity;
import com.framework.core.mybatisplus.support.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 账户管理
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    public Long submit(AccountCreateRequest param) {
        checkParams(param, null);
        Account entity = AccountConvertor.INSTANCE.createRequest2Entity(param);
        return create(entity);
    }

    private void checkParams(AccountCreateRequest param, Long id) {
        valid(param.getAccount(), id);
        param.setPassword(DigestUtil.sha1Hex(param.getPassword()));
    }

    @Override
    public Boolean edit(Long id, AccountUpdateRequest param) {
        Account account = this.getById(id);
        if (ObjectUtil.isEmpty(account)) {
            throw new ServiceException(BusinessErrorCode.ACCOUNT_DOES_NOT_EXIST);
        }
        checkUpdateParams(param, id);
        account = AccountConvertor.INSTANCE.updateRequest2Entity(id, param);
        update(account);
        return true;
    }

    private void checkUpdateParams(AccountUpdateRequest param, Long id) {
        valid(param.getAccount(), id);
    }

    private void valid(String accountInfo, Long id) {
        LambdaQueryWrapper<Account> query = new LambdaQueryWrapper<>();
        query.eq(Account::getAccount, accountInfo);
        query.last("limit 1");
        if (ObjectUtil.isNotNull(id)) {
            query.ne(Account::getId, id);
        }
        Account account = this.baseMapper.selectOne(query);
        if (ObjectUtil.isNotNull(account)) {
            throw new ServiceException(BusinessErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
    }

    @Override
    public AccountVo detail(Long id) {
        Account entity = this.baseMapper.selectById(id);
        AccountVo result = AccountConvertor.INSTANCE.entity2Vo(entity);
        loadList(ListUtil.toList(result));
        return result;
    }

    @Override
    public IPage<AccountVo> queryList(AccountQueryRequest param) {
        IPage<Account> page = Condition.getPage(param);
        LambdaQueryWrapper<Account> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(param.getAccount())) {
            query.like(Account::getAccount, param.getAccount());
        }
        if (StrUtil.isNotBlank(param.getName())) {
            query.like(Account::getName, param.getName());
        }
        query.orderByDesc(Account::getUpdateTime);
        page = this.page(page, query);
        IPage<AccountVo> result = AccountConvertor.INSTANCE.pageVo(page);
        loadList(result.getRecords());
        return result;

    }

    public void loadList(List<AccountVo> list) {
        TypeReference<String> type = new TypeReference<String>() {
        };
        IAccountService accountService = cn.hutool.extra.spring.SpringUtil.getBean(IAccountService.class);
        accountService.loadData(list, "createUser", Account::getId, "createUserName", "name", type);
        accountService.loadData(list, "updateUser", Account::getId, "updateUserName", "name", type);
    }


    public boolean resetPassword(String userIds) {
        List<Long> userIdList = Convert.toList(Long.class, StrUtil.splitTrim(userIds, ','));
        LambdaUpdateWrapper<Account> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Account::getPassword, DigestUtil.sha1Hex(DigestUtil.md5Hex("123456")));
        lambdaUpdateWrapper.in(Account::getId, userIdList);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updatePassword(AccountUpdatePassWordRequest request) {
        LambdaUpdateWrapper<Account> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Account::getPassword, DigestUtil.sha1Hex(DigestUtil.md5Hex(request.getPassword())));
        lambdaUpdateWrapper.eq(Account::getId, request.getAccountId());
        return this.update(lambdaUpdateWrapper);
    }

}
