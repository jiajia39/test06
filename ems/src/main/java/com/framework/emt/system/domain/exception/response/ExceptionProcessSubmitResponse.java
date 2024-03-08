package com.framework.emt.system.domain.exception.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常流程响应数据 响应体
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Getter
@Setter
public class ExceptionProcessSubmitResponse implements Serializable {

    @ApiModelProperty(value = "异常流程id")
    private Long id;

    @ApiModelProperty(value = "提报附加字段")
    private List<FormFieldResponse> submitExtendFieldList;

    @ApiModelProperty(value = "响应模式 0:不指定 1:固定人员 2:多个人员")
    private ResponseModeEnum responseMode;

    @ApiModelProperty(value = "响应人id列表")
    private List<Long> responseUserIds;

    @ApiModelProperty(value = "响应人id和姓名列表")
    private List<UserInfoResponse> responseUserInfoList;

}
