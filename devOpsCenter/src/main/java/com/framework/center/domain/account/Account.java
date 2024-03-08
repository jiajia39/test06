package com.framework.center.domain.account;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName mss_account
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mss_account")
@Data
public class Account extends TenantEntity implements Serializable {

    /**
     * 账户
     */
    @TableField(value = "account")
    private String account;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 生日
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 工号
     */
    @TableField(value = "uid")
    private String uid;
}