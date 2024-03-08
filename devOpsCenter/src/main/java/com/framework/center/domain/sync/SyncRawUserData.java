package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mss_sync_raw_user_data")
@Data
public class SyncRawUserData extends TenantEntity implements Serializable {
    /**
     * 是否创建账号 0(销户)/1(开户）
     */
    @TableField(value = "account_status")
    private String accountStatus;
    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    private String birthday;
    /**
     * 卡号
     */
    @TableField(value = "card_number")
    private String cardNumber;
    /**
     * 姓名
     */
    @TableField(value = "cn")
    private String cn;
    /**
     * 公司GUID
     */
    @TableField(value = "company_guid")
    private String companyGuid;
    /**
     * 公司ID
     */
    @TableField(value = "company_id")
    private String companyId;
    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    private String companyName;
    /**
     * 部门GUID
     */
    @TableField(value = "department_guid")
    private String departmentGuid;
    /**
     * 部门ID
     */
    @TableField(value = "department_id")
    private String departmentId;
    /**
     * 部门名称
     */
    @TableField(value = "department_name")
    private String departmentName;
    /**
     * 现居住地
     */
    @TableField(value = "domicile")
    private String domicile;
    /**
     * 学历
     */
    @TableField(value = "education")
    private String education;
    /**
     * 员工状态
     */
    @TableField(value = "employee_status")
    private String employeeStatus;
    /**
     * 入亨通日期
     */
    @TableField(value = "enterht_date")
    private String enterhtDate;
    /**
     * 毕业院校
     */
    @TableField(value = "graduated_from")
    private String graduatedFrom;
    /**
     * 员工GUID
     */
    @TableField(value = "guid")
    private String guid;
    /**
     * 直接上级（工号）
     */
    @TableField(value = "high_level")
    private String highLevel;
    /**
     * 光电邮箱
     */
    @TableField(value = "htgd_mail")
    private String htgdMail;
    /**
     * 集团邮箱
     */
    @TableField(value = "ht_group_mail")
    private String htGroupMail;
    /**
     * 身份证号
     */
    @TableField(value = "identity_card")
    private String identityCard;
    /**
     * 级别
     */
    @TableField(value = "job_level")
    private String jobLevel;
    /**
     * 邮箱
     */
    @TableField(value = "mail")
    private String mail;
    /**
     * 专业
     */
    @TableField(value = "major")
    private String major;
    /**
     * 婚姻状况
     */
    @TableField(value = "marital_status")
    private String maritalStatus;
    /**
     * 手机号码
     */
    @TableField(value = "mobile")
    private String mobile;
    /**
     * 籍贯
     */
    @TableField(value = "nativeplace")
    private String nativeplace;
    /**
     * 操作类型 insert/update/disable
     */
    @TableField(value = "operation")
    private String operation;
    /**
     * 密码 md5
     */
    @TableField(value = "password")
    private String password;
    /**
     * 户籍地址
     */
    @TableField(value = "permanent_address")
    private String permanentAddress;
    /**
     * 政治面貌
     */
    @TableField(value = "political_status")
    private String politicalStatus;
    /**
     * 岗位GUID
     */
    @TableField(value = "post_guid")
    private String postGuid;
    /**
     * 岗位ID
     */
    @TableField(value = "post_id")
    private String postId;
    /**
     * 岗位名称
     */
    @TableField(value = "post_name")
    private String postName;
    /**
     * 岗位系列GUID
     */
    @TableField(value = "post_series_guid")
    private String postSeriesGuid;
    /**
     * 岗位系列ID
     */
    @TableField(value = "post_series_id")
    private String postSeriesId;
    /**
     * 岗位系统名称
     */
    @TableField(value = "post_series_name")
    private String postSeriesName;
    /**
     * 岗位类型GUID
     */
    @TableField(value = "post_type_guid")
    private String postTypeGuid;
    /**
     * 岗位类型ID
     */
    @TableField(value = "post_type_id")
    private String postTypeId;
    /**
     * 岗位类型名称
     */
    @TableField(value = "post_type_name")
    private String postTypeName;
    /**
     * 是否外派
     */
    @TableField(value = "send_out_staff")
    private String sendOutStaff;
    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;
    /**
     * 社保号码
     */
    @TableField(value = "sscn")
    private String sscn;
    /**
     * 岗位到岗时间
     */
    @TableField(value = "start_post_date")
    private String startPostDate;
    /**
     * 工号
     */
    @TableField(value = "uid")
    private String uid;
    /**
     * 1(在职)/0(离职)
     */
    @TableField(value = "user_status")
    private String userStatus;
    /**
     * 单位电话
     */
    @TableField(value = "wordtel")
    private String wordtel;


    /**
     * 处理状态 0 未处理 1 已处理 2 待重试
     */
    @TableField(value = "process_status")
    private Integer processStatus = 0;


    /**
     * 批次号码
     */
    @TableField(value = "batch_number")
    private String batchNumber;
}
