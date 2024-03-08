package com.framework.center.infrastructure.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 基础返回对象
 * 默认带创建、编辑用户信息
 *
 * @author jiaXue
 * date 2023/3/14
 */

@Setter
@Getter
@ToString
public class BaseUserVo {

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("修改人姓名")
    private String updateUserName;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}
