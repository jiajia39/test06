package com.framework.center.infrastructure.common;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * id列表请求参数
 *
 * @author yankunw
 */
@Getter
@Setter
public class IdsRequest {


    @NotEmpty(message = "不能为空！")
    @Size(max = 1000, message = "id数目最大1000条")
    private List<Long> ids;
}
