package com.framework.emt.system.domain.workspacelocation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 作业单元 移动端查询条件
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class WorkspaceLocationDetailRequest implements Serializable {

    @NotBlank(message = "二维码密文不能为空")
    @ApiModelProperty(value = "二维码密文", required = true)
    private String cipherText;

}
