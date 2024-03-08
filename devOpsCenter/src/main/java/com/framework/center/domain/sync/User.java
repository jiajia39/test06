package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.common.api.entity.Kv;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 说明：用户表
 * 对象名：User
 * 描述：实体类
 *
 * @author yankunw
 * @date 2021-10-09 11:41:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ft_user")
@ApiModel(value = "User对象", description = "用户表")
public class User extends TenantEntity {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    private String code;
    /**
     * 用户平台
     */
    @ApiModelProperty(value = "用户平台")
    private Integer userType;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String name;
    /**
     * 真名
     */
    @ApiModelProperty(value = "真名")
    private String realName;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
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

    @TableField(exist = false)
    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门id列表")
    private List<Long> deptIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色id列表")
    private List<Long> postIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "扩展参数")
    private Kv extendedParameters;


}