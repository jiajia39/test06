package com.framework.emt.system.domain.task.check.request;

import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
public class TaskCheckRequest implements Serializable {
    @ApiModelProperty(value = "提交附加数据")
    private List<ExtendFieldsRequest> submitExtendDatas;

    @ApiModelProperty(value = "提交附件列表")
    private List<FileRequest> submitFiles;
}
