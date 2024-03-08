package com.framework.emt.system.domain.task.cooperation.response;

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
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 异常协同-导出 响应体
 *
 * @author ds_C
 * @since 2023-08-17
 */
@Getter
@Setter
public class TaskCooperationExportResponse implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty("异常编号")
    private String taskId;

    @ColumnWidth(20)
    @ExcelProperty("协同编号")
    private String cooperationId;

    @ColumnWidth(20)
    @ExcelProperty("异常流程")
    private String exceptionProcessTitle;

    @ColumnWidth(20)
    @ExcelProperty("协同任务")
    private String title;

    @ColumnWidth(20)
    @ExcelProperty("协同时限")
    private String expire;

    @ColumnWidth(20)
    @ExcelProperty("作业单元")
    private String workspaceName;

    @ColumnWidth(20)
    @ExcelProperty("接收时间")
    private Date acceptTime;

    @ColumnWidth(20)
    @ExcelProperty("提交时间")
    private Date submitTime;

    @ColumnWidth(20)
    @ExcelProperty("协同人")
    private String cooperationUserName;

    @ColumnWidth(20)
    @ExcelProperty("协同时间")
    
    private Date cooperationTime;

    @ColumnWidth(20)
    @ExcelProperty("协同状态")
    private String cooperationStatusName;

    @ColumnWidth(20)
    @ExcelProperty("紧急程度")
    private String priorityName;

    @ColumnWidth(20)
    @ExcelProperty("严重程度")
    private String severityName;

    @ColumnWidth(20)
    @ExcelProperty("创建人")
    private String createUserName;

    @ColumnWidth(20)
    @ExcelProperty("创建时间")
    private Date createTime;

    @ColumnWidth(20)
    @ExcelProperty("最后更新人")
    private String updateUserName;

    @ColumnWidth(20)
    @ExcelProperty("最后更新时间")
    private Date updateTime;

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
    @ExcelProperty("挂起附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> cooperationExtendField;
    @ColumnWidth(30)
    @ExcelProperty("挂起附加信息")
    private String cooperationExtendDatas;

    @ExcelIgnore
    @ApiModelProperty(value = "协同状态枚举")
    private CooperationStatus cooperationStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ApiModelProperty(value = "严重程度枚举")
    private SeverityEnum severity;

    @ExcelIgnore
    @ApiModelProperty(value = "处理最后期限")
    private Date finishDeadline;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ExcelIgnore
    @ApiModelProperty(value = "修改人id")
    private Long updateUser;

    @ExcelIgnore
    @ApiModelProperty(value = "作业单元id")
    private Long workspaceId;

    public void init(Date now) {
        this.cooperationStatusName = Optional.ofNullable(cooperationStatus).map(CooperationStatus::getName).orElse(StrUtil.EMPTY);
        this.priorityName = Optional.ofNullable(priority).map(PriorityEnum::getName).orElse(StrUtil.EMPTY);
        this.severityName = Optional.ofNullable(severity).map(SeverityEnum::getName).orElse(StrUtil.EMPTY);
        if (finishDeadline == null) {
            expire = StringConstant.UNKNOWN;
            return;
        }
        if (submitTime == null) {
            expire = now.after(finishDeadline) ? StringConstant.TIME_OUT : StringConstant.NOT_TIME_OUT;
        } else {
            expire = submitTime.after(finishDeadline) ? StringConstant.TIME_OUT : StringConstant.NOT_TIME_OUT;
        }
    }

}
