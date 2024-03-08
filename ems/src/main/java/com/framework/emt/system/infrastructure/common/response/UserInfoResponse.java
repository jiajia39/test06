package com.framework.emt.system.infrastructure.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户id和用户名称 响应体
 *
 * @author ds_C
 * @since 2023-07-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserInfoResponse implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户id 文本类型")
    private String strId;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    public UserInfoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserInfoResponse(String strId, String name) {
        this.strId = strId;
        this.name = name;
    }

}
