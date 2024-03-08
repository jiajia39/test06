package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("SpellCheckingInspection")
@Data
@ApiModel(value = "部门信息内容参数", description = "部门信息内容参数")
public class DeptContentRequest {
    @ApiModelProperty(value = "组织GUID")
    private String departmentguid;
    @ApiModelProperty(value = "组织ID")
    private String departmentid;
    @ApiModelProperty(value = "组织名称")
    private String departmentname;
    @ApiModelProperty(value = "上级部门GUID")
    private String parentunitguid;
    @ApiModelProperty(value = "上级部门ID")
    private String parentunitid;
    @ApiModelProperty(value = "上级部门名称")
    private String parentunitname;
    @ApiModelProperty(value = "层级")
    private String departmentgrade;
    @ApiModelProperty(value = "部门/公司级别")
    private String departmentlevel;
    @ApiModelProperty(value = "公司代码")
    private String companycode;
    @ApiModelProperty(value = "公司名称")
    private String companyname;
    @ApiModelProperty(value = "负责人")
    private String leader;
    @ApiModelProperty(value = "状态1(启用)/0(停用)")
    private String deptstatus;
    @ApiModelProperty(value = "部门编制")
    private String insitution;
    @ApiModelProperty(value = "是否是公司1(公司)/0(部门)")
    private String iscompany;
    @ApiModelProperty(value = "操作类型insert/update/disable")
    private String operation;
    @ApiModelProperty(value = "秘钥")
    private String key;
    @ApiModelProperty(value = "备用字段1")
    private String placeholder1;
    @ApiModelProperty(value = "备用字段2")
    private String placeholder2;
    @ApiModelProperty(value = "备用字段3")
    private String placeholder3;
    @ApiModelProperty(value = "备用字段4")
    private String placeholder4;
    @ApiModelProperty(value = "备用字段5")
    private String placeholder5;
    @ApiModelProperty(value = "备用字段6")
    private String placeholder6;
    @ApiModelProperty(value = "备用字段7")
    private String placeholder7;
    @ApiModelProperty(value = "备用字段8")
    private String placeholder8;
    @ApiModelProperty(value = "备用字段9")
    private String placeholder9;
    @ApiModelProperty(value = "备用字段10")
    private String placeholder10;
}
