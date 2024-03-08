package com.framework.center.domain.account.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户列表查询参数
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Data
@ApiModel(value = "账户响应信息", description = "账户响应信息")
public class AccountVo implements Serializable {
    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    private Long id;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer sex;


    @ApiModelProperty(value = "uid")
    private String uid;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    @ApiModelProperty("创建人")
    private Long createUser;


    @ApiModelProperty("创建人姓名")
    private String createUserName;


    @ApiModelProperty("更新时间")
    private Date updateTime;
}
