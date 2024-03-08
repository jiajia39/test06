package com.framework.emt.system.domain.task.cooperation.request;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 异常协同 提交参数
 *
 * @author ds_C
 * @since 2023-08-12
 */
@Getter
@Setter
public class TaskCooperationSubmitRequest implements Serializable {

    @NotBlank(message = "原因分析不能为空")
    @Length(max = 500, message = "原因分析长度限制{max}")
    @ApiModelProperty(value = "原因分析", required = true)
    private String submitReasonAnalysis;

    @NotBlank(message = "处理方案不能为空")
    @Length(max = 500, message = "处理方案长度限制{max}")
    @ApiModelProperty(value = "处理方案", required = true)
    private String submitSolution;

    @NotBlank(message = "处理结果不能为空")
    @Length(max = 500, message = "处理结果长度限制{max}")
    @ApiModelProperty(value = "处理结果", required = true)
    private String submitResult;

    @ApiModelProperty(value = "协同附件")
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "协同附加内容")
    private List<ExtendFieldsRequest> submitExtendDatas;

    public TaskCooperationSubmitRequest init() {
        if (CollectionUtil.isEmpty(submitFiles)) {
            submitFiles = Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(submitExtendDatas)) {
            submitExtendDatas = Collections.emptyList();
        }
        return this;
    }

}
