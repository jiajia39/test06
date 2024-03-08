package com.framework.center.domain.account.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 账户列表查询参数
 *
 * @author yankunw
 * @since 2023-04-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "账户列表查询参数", description = "账户列表查询参数")
public class AccountQueryRequest extends Query implements Serializable {
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String account;

    /**
     * 账户类型 0 集团成员 1 子公司成员
     */
    @ApiModelProperty(value = "账户类型 0 集团成员 1 子公司成员")
    private Integer type;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 所属公司id
     */
    @ApiModelProperty(value = "所属公司id")
    private Long companyId;
}
