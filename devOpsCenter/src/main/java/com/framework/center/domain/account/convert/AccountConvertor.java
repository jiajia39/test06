package com.framework.center.domain.account.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.account.request.AccountCreateRequest;
import com.framework.center.domain.account.request.AccountUpdateRequest;
import com.framework.center.domain.account.response.AccountVo;
import com.framework.center.infrastructure.constant.enums.AccountType;
import com.framework.center.infrastructure.constant.enums.BaseEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 账户转换类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Mapper
public interface AccountConvertor {
    AccountConvertor INSTANCE = Mappers.getMapper(AccountConvertor.class);


    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return 实体
     */
    Account createRequest2Entity(AccountCreateRequest request);

    /**
     * 实体列表转换成vo列表
     *
     * @param formConfList 实体列表
     * @return vo列表
     */
    List<AccountVo> entityList2Vo(List<Account> formConfList);

    /**
     * @param account 转换的实体类
     * @return AccountVo
     */
    AccountVo entity2Vo(Account account);


    /**
     * 更新参数转换成实体
     *
     * @param id      账户id
     * @param request 更新参数
     * @return 实体
     */
    @Mapping(target = "id", source = "id")
    Account updateRequest2Entity(Long id, AccountUpdateRequest request);


    default IPage<AccountVo> pageVo(IPage<Account> pages) {
        IPage<AccountVo> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(AccountConvertor.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }

    /**
     * 账户类型换成枚举
     *
     * @param type 账户类型
     * @return 账户类型枚举
     */
    default AccountType typeToEnum(Integer type) {
        return BaseEnum.parseByCode(AccountType.class, type);
    }
}

