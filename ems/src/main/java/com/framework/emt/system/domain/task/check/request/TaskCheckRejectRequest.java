package com.framework.emt.system.domain.task.check.request;

import com.framework.emt.system.infrastructure.common.request.FileRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 异常任务验收 更新参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskCheckRejectRequest implements Serializable {
    @NotBlank(message = "拒绝原因不能为空")
    @ApiModelProperty(value = "拒绝原因", required = true)
    private String rejectReason;

    @ApiModelProperty(value = "提交附件列表")
    private List<FileRequest> submitFiles;
}
