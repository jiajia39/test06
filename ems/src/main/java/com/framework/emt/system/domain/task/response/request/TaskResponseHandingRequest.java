package com.framework.emt.system.domain.task.response.request;


import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 异常任务选择处理人参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskResponseHandingRequest implements Serializable {

    @ApiModelProperty(value = "提交处理人id")
    private Long submitHandingUserId;

    @ApiModelProperty(value = "提交附加数据")
    private List<@Valid ExtendFieldsRequest> submitExtendDatas;
}
