package com.framework.emt.system.infrastructure.common.request;

import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 主键id列表 request
 *
 * @author ds_C
 * @since 2023-07-11
 */
@Getter
@Setter
public class IdListRequest implements Serializable {

    @NotEmpty(message = "主键id列表不能为空")
    @Size(max = 1000, message = "主键id数目最大{max}条")
    @UniqueElementsValidator(message = "主键id不能重复")
    @ApiModelProperty(value = "主键id列表", required = true)
    private List<Long> idList;

}
