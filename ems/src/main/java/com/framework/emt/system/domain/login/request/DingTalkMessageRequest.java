package com.framework.emt.system.domain.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "钉钉消息参数", description = "钉钉消息参数")
public class DingTalkMessageRequest implements Serializable {


    @ApiModelProperty(value = "钉钉接收消息的用户列表")
    @NotEmpty(message = "钉钉接收消息的用户列表不能为空")
    private List<String> dingTalkUserIds;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;

    @NotBlank(message = "accessToken不能为空")
    @ApiModelProperty(value = "accessToken")
    private String accessToken;
}
