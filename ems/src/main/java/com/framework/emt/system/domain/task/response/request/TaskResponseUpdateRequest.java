package com.framework.emt.system.domain.task.response.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务响应 更新参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskResponseUpdateRequest implements Serializable {

    @ApiModelProperty(value = "异常任务表id")
    private Long exceptionTaskId;


    @ApiModelProperty(value = "响应版本号")
    private Integer responseVersion;

    @ApiModelProperty(value = "被转派人id")
    private Long userId;

    @Length(max = 500, message = "转派备注长度限制{max}")
    @ApiModelProperty(value = "转派备注")
    private String otherRemark;

    @ApiModelProperty(value = "被转派时间")
    private LocalDateTime otherTime;
    @ApiModelProperty(value = "接受人id")
    private Long acceptUserId;


    @ApiModelProperty(value = "接受时间")
    private LocalDateTime acceptTime;


    @ApiModelProperty(value = "提交处理人id")
    private Long submitHandingUserId;


    @ApiModelProperty(value = "提交附加数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @EnumValidator(enumClazz = TaskRejectNode.class, message = "驳回节点 0未驳回  1响应  2处理")
    @ApiModelProperty(value = "驳回节点")
    private Integer rejectNode;


    @ApiModelProperty(value = "驳回来源")
    private Long rejectSourceId;


    @ApiModelProperty(value = "驳回时间")
    private LocalDateTime rejectTime;

    @Length(max = 500, message = "驳回原因长度限制{max}")
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;


    @ApiModelProperty(value = "驳回人")
    private Long rejectUserId;


    // 响应转派
    public void responseTransfer(Long userId, String otherRemark, LocalDateTime otherTime) {
        this.userId = userId;
        this.otherRemark = otherRemark;
        this.otherTime = otherTime;
    }

    //接受响应
    public void responseAccept(Long userId, LocalDateTime acceptTime) {
        this.acceptUserId = userId;
        this.acceptTime = acceptTime;
    }

    //响应 -提交处理人
    public void responseHanding(Long submitHandingUserId, List<FormFieldResponse> submitExtendDatas) {
        this.submitHandingUserId = submitHandingUserId;
        this.submitTime = LocalDateTime.now();
        this.submitExtendDatas = submitExtendDatas;
    }

}
