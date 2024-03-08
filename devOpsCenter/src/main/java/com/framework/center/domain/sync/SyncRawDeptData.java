package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mss_sync_raw_dept_data")
@Data
public class SyncRawDeptData extends TenantEntity implements Serializable {
    /**
     * 组织GUID
     */
    @TableField(value = "department_guid")
    private String departmentGuid;
    /**
     * 组织ID
     */
    @TableField(value = "department_id")
    private String departmentId;
    /**
     * 组织名称
     */
    @TableField(value = "department_name")
    private String departmentName;
    /**
     * 上级部门GUID
     */
    @TableField(value = "parent_unit_guid")
    private String parentUnitGuid;
    /**
     * 上级部门ID
     */
    @TableField(value = "parent_unit_id")
    private String parentUnitId;
    /**
     * 上级部门名称
     */
    @TableField(value = "parent_unit_name")
    private String parentUnitName;
    /**
     * 层级
     */
    @TableField(value = "department_grade")
    private String departmentGrade;
    /**
     * 部门/公司级别
     */
    @TableField(value = "department_level")
    private String departmentLevel;
    /**
     * 公司代码
     */
    @TableField(value = "company_code")
    private String companyCode;
    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    private String companyName;
    /**
     * 负责人
     */
    @TableField(value = "leader")
    private String leader;
    /**
     * 状态1(启用)/0(停用)
     */
    @TableField(value = "dept_status")
    private String deptStatus;
    /**
     * 部门编制
     */
    @TableField(value = "insitution")
    private String insitution;
    /**
     * 是否是公司1(公司)/0(部门)
     */
    @TableField(value = "is_company")
    private String isCompany;
    /**
     * 操作类型insert/update/disable
     */
    @TableField(value = "operation")
    private String operation;
    /**
     * 秘钥
     */
    @TableField(value = "secret_key")
    private String secretKey;
    /**
     * 备用字段1
     */
    @TableField(value = "placeholder1")
    private String placeholder1;
    /**
     * 备用字段2
     */
    @TableField(value = "placeholder2")
    private String placeholder2;
    /**
     * 备用字段3
     */
    @TableField(value = "placeholder3")
    private String placeholder3;
    /**
     * 备用字段4
     */
    @TableField(value = "placeholder4")
    private String placeholder4;
    /**
     * 备用字段5
     */
    @TableField(value = "placeholder5")
    private String placeholder5;
    /**
     * 备用字段6
     */
    @TableField(value = "placeholder6")
    private String placeholder6;
    /**
     * 备用字段7
     */
    @TableField(value = "placeholder7")
    private String placeholder7;
    /**
     * 备用字段8
     */
    @TableField(value = "placeholder8")
    private String placeholder8;
    /**
     * 备用字段9
     */
    @TableField(value = "placeholder9")
    private String placeholder9;
    /**
     * 备用字段10
     */
    @TableField(value = "placeholder10")
    private String placeholder10;

    /**
     * 处理状态 0 未处理 1 已处理 2 待重试
     */
    @TableField(value = "process_status")
    private String processStatus;

    /**
     * 批次号码
     */
    @TableField(value = "batch_number")
    private String batchNumber;
}
