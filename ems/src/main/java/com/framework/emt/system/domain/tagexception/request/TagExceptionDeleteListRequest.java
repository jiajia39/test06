package com.framework.emt.system.domain.tagexception.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常标签关联 删除参数
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagExceptionDeleteListRequest implements Serializable {

    @NotEmpty(message = "关联表id列表不能为空")
    @ApiModelProperty(value = "关联表id列表", required = true)
    @Size(max = 100, message = "关联表id列表数目最多100个")
    private List<Long> sourceIdList;

}
