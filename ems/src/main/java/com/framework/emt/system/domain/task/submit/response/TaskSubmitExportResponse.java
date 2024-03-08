package com.framework.emt.system.domain.task.submit.response;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 异常提报导出 响应体
 *
 * @author ds_C
 * @since 2023-08-10
 */
@Getter
@Setter
public class TaskSubmitExportResponse implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty("异常编号")
    private String taskId;

    @ColumnWidth(20)
    @ExcelProperty("异常分类")
    private String categoryName;

    @ColumnWidth(20)
    @ExcelProperty("异常流程")
    private String exceptionProcessTitle;

    @ColumnWidth(20)
    @ExcelProperty("异常项")
    private String itemName;

    @ColumnWidth(20)
    @ExcelProperty("异常状态")
    private String taskStatusName;

    @ColumnWidth(20)
    @ExcelProperty("紧急程度")
    private String priorityName;

    @ColumnWidth(20)
    @ExcelProperty("严重程度")
    private String severityName;

    @ColumnWidth(20)
    @ExcelProperty("响应时限")
    private String responseDuration;

    @ColumnWidth(20)
    @ExcelProperty("处理时限")
    private String handingDuration;

    @ColumnWidth(20)
    @ExcelProperty("提报部门")
    private String deptName;

    @ColumnWidth(20)
    @ExcelProperty("作业单元")
    private String workspaceName;

    @ColumnWidth(20)
    @ExcelProperty("响应人")
    private String submitResponseUserName;

    @ColumnWidth(20)
    @ExcelProperty("创建时间")
    private Date createTime;

    @ColumnWidth(20)
    @ExcelProperty("提报时间")
    private Date submitTime;

    @ColumnWidth(30)
    @ExcelProperty("提报附加信息")
    private String submitExtendDatas;

    @ExcelIgnore
    @ExcelProperty("提报附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendField;

    @ExcelIgnore
    @ExcelProperty("响应附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> responseExtendField;

    @ColumnWidth(30)
    @ExcelProperty("响应附加信息")
    private String responseExtendDatas;

    @ExcelIgnore
    @ExcelProperty("处理附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> handingExtendField;

    @ColumnWidth(30)
    @ExcelProperty("处理附加信息")
    private String handingExtendDatas;

    @ColumnWidth(30)
    @ExcelProperty("挂起附加信息")
    private String suspendExtendDatas;

    @ExcelIgnore
    @ExcelProperty("挂起附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> suspendExtendField;

    @ExcelIgnore
    @ApiModelProperty(value = "异常状态枚举")
    private ExceptionStatus taskStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ApiModelProperty(value = "严重程度枚举")
    private SeverityEnum severity;

    @ExcelIgnore
    @ApiModelProperty(value = "响应时限 数字")
    private Integer responseDurationTime;

    @ExcelIgnore
    @ApiModelProperty(value = "处理时限 数字")
    private Integer handingDurationTime;

    @ExcelIgnore
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ExcelIgnore
    @ApiModelProperty(value = "异常分类id")
    private Long categoryId;

    @ExcelIgnore
    @ApiModelProperty(value = "异常项id")
    private Long itemId;

    @ExcelIgnore
    @ApiModelProperty(value = "作业单元id")
    private Long workspaceId;

    public void init() {
        this.taskStatusName = Optional.ofNullable(taskStatus).map(ExceptionStatus::getName).orElse(StrUtil.EMPTY);
        this.priorityName = Optional.ofNullable(priority).map(PriorityEnum::getName).orElse(StrUtil.EMPTY);
        this.severityName = Optional.ofNullable(severity).map(SeverityEnum::getName).orElse(StrUtil.EMPTY);
        this.responseDuration = this.responseDurationTime + StringConstant.MINUTES;
        this.handingDuration = this.handingDurationTime + StringConstant.MINUTES;
    }

}
