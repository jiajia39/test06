package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("SpellCheckingInspection")
@Data
@ApiModel(value = "用户信息内容参数", description = "用户信息内容参数")
public class UserContentRequest {
    @ApiModelProperty(value = "是否创建账号 0(销户)/1(开户）")
    private String accountstatus;
    @ApiModelProperty(value = "出生日期")
    private String birthday;
    @ApiModelProperty(value = "卡号")
    private String cardnumber;
    @ApiModelProperty(value = "姓名")
    private String cn;
    @ApiModelProperty(value = "公司GUID")
    private String companyguid;
    @ApiModelProperty(value = "公司ID")
    private String companyid;
    @ApiModelProperty(value = "公司名称")
    private String companyname;
    @ApiModelProperty(value = "部门GUID")
    private String departmentguid;
    @ApiModelProperty(value = "部门ID")
    private String departmentid;
    @ApiModelProperty(value = "部门名称")
    private String departmentname;
    @ApiModelProperty(value = "现居住地")
    private String domicile;
    @ApiModelProperty(value = "学历")
    private String education;
    @ApiModelProperty(value = "员工状态")
    private String employeestatus;
    @ApiModelProperty(value = "入亨通日期")
    private String enterhtdate;
    @ApiModelProperty(value = "毕业院校")
    private String graduatedfrom;
    @ApiModelProperty(value = "员工GUID")
    private String guid;
    @ApiModelProperty(value = "直接上级（工号）")
    private String highlevel;
    @ApiModelProperty(value = "光电邮箱")
    private String htgdmail;
    @ApiModelProperty(value = "集团邮箱")
    private String htgroupmail;
    @ApiModelProperty(value = "身份证号")
    private String identitycard;
    @ApiModelProperty(value = "级别")
    private String joblevel;
    @ApiModelProperty(value = "邮箱")
    private String mail;
    @ApiModelProperty(value = "专业")
    private String major;
    @ApiModelProperty(value = "婚姻状况")
    private String maritalstatus;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "籍贯")
    private String nativeplace;
    @ApiModelProperty(value = "操作类型 insert/update/disable")
    private String operation;
    @ApiModelProperty(value = "密码 md5")
    private String password;
    @ApiModelProperty(value = "户籍地址")
    private String permanentaddress;
    @ApiModelProperty(value = "政治面貌")
    private String politicalstatus;
    @ApiModelProperty(value = "岗位GUID")
    private String postguid;
    @ApiModelProperty(value = "岗位ID")
    private String postid;
    @ApiModelProperty(value = "岗位名称")
    private String postname;
    @ApiModelProperty(value = "岗位系列GUID")
    private String postseriesguid;
    @ApiModelProperty(value = "岗位系列ID")
    private String postseriesid;
    @ApiModelProperty(value = "岗位系统名称")
    private String postseriesname;
    @ApiModelProperty(value = "岗位类型GUID")
    private String posttypeguid;
    @ApiModelProperty(value = "岗位类型ID")
    private String posttypeid;
    @ApiModelProperty(value = "岗位类型名称")
    private String posttypename;
    @ApiModelProperty(value = "是否外派")
    private String sendoutstaff;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "社保号码")
    private String sscn;
    @ApiModelProperty(value = "岗位到岗时间")
    private String startpostdate;
    @ApiModelProperty(value = "工号")
    private String uid;
    @ApiModelProperty(value = "1(在职)/0(离职)")
    private String userstatus;
    @ApiModelProperty(value = "单位电话")
    private String wordtel;
}
