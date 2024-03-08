package com.framework.center.infrastructure.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账户类型枚举
 *
 * @author yankunw
 */

@Getter
@AllArgsConstructor
public enum AccountType implements BaseEnum<AccountType> {

    /**
     * 账户类型枚举
     */
    ORDINARY_MEMBERS_OF_THE_GROUP(0, "集团成员",1697183541349060609L),
    ORDINARY_MEMBERS_OF_SUBSIDIARIES(1, "子公司成员",1697183541349060609L),
    ADMINISTRATOR(2, "administrator",1123598816738675201L);
    
    /**
     * code编码
     */
    @EnumValue
    final Integer code;
    /**
     * 中文信息描述
     */
    final String name;

    final Long roleId;

    public static AccountType getAccountTypeByCode(Integer code){
        for (AccountType value : AccountType.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        return null;
    }

}
