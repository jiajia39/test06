package com.framework.emt.system.domain.task.task.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常任务配置处理数据 响应体
 *
 * @author ds_C
 * @since 2023-08-17
 */
@Getter
@Setter
public class SettingHandingResponse implements Serializable {

    @ApiModelProperty(value = "异常任务配置id")
    private Long id;

    @ApiModelProperty(value = "处理模式 0:不指定 1:固定人员 2:多个人员 3:响应同处理")
    private HandingModeEnum handingMode;

    @ApiModelProperty(value = "处理人id列表")
    private List<Long> handingUserIds;

    @ApiModelProperty(value = "处理人id和姓名列表")
    private List<UserInfoResponse> handingUserInfoList;

}
