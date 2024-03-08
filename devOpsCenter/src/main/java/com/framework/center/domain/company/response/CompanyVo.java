package com.framework.center.domain.company.response;

import com.framework.center.infrastructure.common.FileRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司响应信息
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Data
@ApiModel(value = "公司响应信息", description = "公司响应信息")
public class CompanyVo implements Serializable {
    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id")
    private Long id;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "公司Logo")
    private FileRequest logo;

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
