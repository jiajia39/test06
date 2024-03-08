package com.framework.center.domain.sync;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "ft_user_ext", autoResultMap = true)
public class UserExt extends TenantEntity {
    /**
     * 工号
     */
    private String uid;
    /**
     * 岗位id
     */
    private String postId;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位类型id
     */
    private String postTypeId;
    /**
     * 岗位类型名称
     */
    private String postTypeName;
    /**
     * 上级工号
     */
    private String parentUid;
    /**
     * 身份证号
     */
    private String identityCard;
    /**
     * 办公状态 1(在职)/0(离职)
     */
    private String officeStatus;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 创建人工号
     */
    private String createUid;
    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 修改人工号
     */
    private String updateUid;
    /**
     * 修改人姓名
     */
    private String updateUserName;

    /**
     * 用户id
     */
    private Long userId;
}
