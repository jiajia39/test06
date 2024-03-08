package com.framework.center.domain.account.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 修改密码参数
 *
 * @author gao
 * @since 2024-02-04
 */

@Data
@ApiModel(value = "修改密码参数", description = "修改密码参数")
public class AccountUpdatePassWordRequest implements Serializable {
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private Long accountId;

    /**
     * 账户类型 0 集团成员 1 子公司成员
     */
    @ApiModelProperty(value = "账户类型 0 集团成员 1 子公司成员")
    private String password;

}
