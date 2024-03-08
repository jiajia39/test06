package com.framework.emt.system.domain.task.handing.request;

import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常任务提交处理
 *
 * @author gaojia
 * @since 2023-08-16
 */
@Getter
@Setter
public class HandingUpdateRequest implements Serializable {

    @NotEmpty(message = "异常原因项不能为空")
    @ApiModelProperty(value = "异常原因项", required = true)
    @Size(max = 30, message = "异常原因项最多{max}个")
    private List<@Valid TagInfo> reasonItems;

    @ApiModelProperty(value = "原因分析", required = true)
    @Length(max = 500, message = "内容长度限制{max}")
    @NotBlank(message = "原因分析不能为空")
    private String submitReasonAnalysis;

    @ApiModelProperty(value = "处理方案", required = true)
    @Length(max = 500, message = "处理方案长度限制{max}")
    @NotBlank(message = "处理方案不能为空")
    private String submitSolution;

    @ApiModelProperty(value = "处理结果", required = true)
    @Length(max = 500, message = "处理结果限制{max}")
    @NotBlank(message = "处理结果不能为空")
    private String submitResult;

    @ApiModelProperty(value = "提交附加数据")
    @Size(max = 50, message = "提交附加数据最多{max}个")
    private List<@Valid ExtendFieldsRequest> submitExtendDatas;

    @ApiModelProperty(value = "提交附件列表")
    private List<FileRequest> submitFiles;
}
