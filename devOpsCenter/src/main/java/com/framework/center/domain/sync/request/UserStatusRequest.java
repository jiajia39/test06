package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "用户启用禁用参数", description = "用户启用禁用参数")
public class UserStatusRequest {
    @ApiModelProperty(value = "用户id列表")
    private List<Long> userIdList;
}
