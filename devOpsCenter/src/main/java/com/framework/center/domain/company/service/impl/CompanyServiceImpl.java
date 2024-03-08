package com.framework.center.domain.company.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.service.IAccountService;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.convert.CompanyConvertor;
import com.framework.center.domain.company.mapper.CompanyMapper;
import com.framework.center.domain.company.request.CompanyCreateRequest;
import com.framework.center.domain.company.request.CompanyQueryRequest;
import com.framework.center.domain.company.response.CompanyVo;
import com.framework.center.domain.company.service.ICompanyService;
import com.framework.center.infrastructure.service.BaseServiceImpl;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公司信息服务实现类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompanyServiceImpl extends BaseServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Override
    public boolean submit(CompanyCreateRequest param) {
        Company entity = CompanyConvertor.INSTANCE.createRequest2Entity(param);
        checkNameUnique(null, entity.getName());
        create(entity);
        return true;
    }

    @Override
    public boolean checkParamToCreateDb(Company entity) {
        boolean result = !ObjectUtil.isNull(entity);
        if (result) {
            String[] checkColumns = {"microservices", "databaseHost", "databasePort", "databaseAccount", "databasePassword", "databaseDriver", "databaseName"};
            for (String checkColumn : checkColumns) {
                String value = Convert.toStr(ReflectUtil.getFieldValue(entity, checkColumn), "");
                if (StrUtil.isBlank(value)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean edit(Long id, CompanyCreateRequest param) {
        Company entity = this.getById(id);
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("公司不存在");
        }
        entity = CompanyConvertor.INSTANCE.updateRequest2Entity(id, param);
        checkNameUnique(id, entity.getName());
        update(entity);
        return true;
    }

    @Override
    public CompanyVo detail(Long id) {
        Company entity = this.baseMapper.selectById(id);
        CompanyVo result = CompanyConvertor.INSTANCE.entity2Vo(entity);
        loadDetail(result);
        return result;
    }

    @Override
    public IPage<CompanyVo> queryList(CompanyQueryRequest param) {
        IPage<Company> page = Condition.getPage(param);
        LambdaQueryWrapper<Company> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(param.getName())) {
            query.like(Company::getName, param.getName());
        }
        query.orderByDesc(Company::getUpdateTime);
        page = this.page(page, query);
        IPage<CompanyVo> result = CompanyConvertor.INSTANCE.pageVo(page);
        loadList(result.getRecords());
        return result;
    }

    public void loadList(List<CompanyVo> list) {
        TypeReference<String> type = new TypeReference<String>() {
        };
        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        accountService.loadData(list, "createUser", Account::getId, "createUserName", "name", type);
        accountService.loadData(list, "updateUser", Account::getId, "updateUserName", "name", type);
    }

    public void loadDetail(CompanyVo res) {
        TypeReference<String> type = new TypeReference<String>() {
        };
        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        accountService.loadData(ListUtil.toList(res), "createUser", Account::getId, "createUserName", "name", type);
        accountService.loadData(ListUtil.toList(res), "updateUser", Account::getId, "updateUserName", "name", type);
    }

    /**
     * 校验公司名称是否唯一
     *
     * @param id   主键id
     * @param name 公司名
     */
    private void checkNameUnique(Long id, String name) {
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(Company::getId, id);
        }
        queryWrapper.eq(Company::getName, name);
        if (this.count(queryWrapper) > 0L) {
            throw new ServiceException("公司名称已存在");
        }
    }

}
