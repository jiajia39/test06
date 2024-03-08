package com.framework.emt.system.domain.task.task.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常任务配置验收数据 响应体
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Getter
@Setter
public class SettingCheckResponse implements Serializable {

    @ApiModelProperty(value = "异常任务配置id")
    private Long id;

    @ApiModelProperty(value = "验收模式 0:不指定 1:固定人员 2:多个人员 3:填报同验收 4:填报同验收多人")
    private CheckModeEnum checkMode;

    @ApiModelProperty(value = "验收人id列表")
    private List<Long> checkUserIds;

    @ApiModelProperty(value = "验收人id和姓名列表")
    private List<UserInfoResponse> checkUserInfoList;

}
