package com.framework.emt.system.domain.task.response.response;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 异常响应-导出 响应体
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskResponseExportResponse implements Serializable {

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
    @ExcelProperty("作业单元")
    private String workspaceName;

    @ColumnWidth(20)
    @ExcelProperty("提报人")
    private String subSubmitUserName;

    @ColumnWidth(20)
    @ExcelProperty("提报时间")
    private Date subSubmitTime;

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
    @ExcelProperty("响应人")
    private String responseUserName;

    @ColumnWidth(20)
    @ExcelProperty("响应时间")
    private Date responseTime;

    @ColumnWidth(20)
    @ExcelProperty("响应时限")
    private String expire;

    @ColumnWidth(20)
    @ExcelProperty("响应时长")
    private String responseDuration;

    @ColumnWidth(20)
    @ExcelProperty("接受时间")
    private Date acceptTime;

    @ColumnWidth(20)
    @ExcelProperty("提交时间")
    private Date submitTime;

    @ColumnWidth(20)
    @ExcelProperty("处理人")
    private String submitHandingUserName;

    @ColumnWidth(20)
    @ExcelProperty("提报部门")
    private String deptName;

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
    @ApiModelProperty(value = "异常状态枚举")
    private ExceptionStatus taskStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ApiModelProperty(value = "严重程度枚举")
    private SeverityEnum severity;

    @ExcelIgnore
    @ApiModelProperty(value = "响应时限")
    private Date responseDeadline;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ExcelIgnore
    @ApiModelProperty(value = "最后更新人id")
    private Long updateUser;

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

    public void init(Date now) {
        this.taskStatusName = Optional.ofNullable(taskStatus).map(ExceptionStatus::getName).orElse(StrUtil.EMPTY);
        this.priorityName = Optional.ofNullable(priority).map(PriorityEnum::getName).orElse(StrUtil.EMPTY);
        this.severityName = Optional.ofNullable(severity).map(SeverityEnum::getName).orElse(StrUtil.EMPTY);
        if (responseDeadline == null) {
            expire = StringConstant.UNKNOWN;
            return;
        }
        if (submitTime == null) {
            expire = now.after(responseDeadline) ? StringConstant.NOT_TIME_OUT : StringConstant.TIME_OUT;
        } else {
            expire = submitTime.after(responseDeadline) ? StringConstant.NOT_TIME_OUT : StringConstant.TIME_OUT;
        }

        // 计算响应和处理时长
        if (responseTime != null) {
            if (subSubmitTime != null) {
                responseDuration = calculationDuration(subSubmitTime, responseTime);
            }
        }
    }

    /**
     * 时长计算
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    private String calculationDuration(Date startTime, Date endTime) {
        long startTimeNum = startTime.getTime();
        long endTimeNum = endTime.getTime();
        if (startTimeNum > NumberUtils.LONG_ZERO && endTimeNum > NumberUtils.LONG_ZERO) {
            long timeDifferenceInMillis = endTimeNum - startTimeNum;
            return getHourMinutes(timeDifferenceInMillis);
        } else {
            return StrUtil.EMPTY;
        }
    }

    private String getHourMinutes(long timeDifferenceInMillis) {
        long seconds = timeDifferenceInMillis / NumberConstant.THOUSAND;
        long days = seconds / NumberConstant.EIGHTY_SIX_THOUSAND_AND_FOUR_HUNDRED;
        long hours = (seconds % NumberConstant.EIGHTY_SIX_THOUSAND_AND_FOUR_HUNDRED) / NumberConstant.THREE_THOUSAND_SIX;
        long minutes = (seconds % NumberConstant.THREE_THOUSAND_SIX) / NumberConstant.SIXTY;
        StringBuilder formattedTimeDifference = new StringBuilder();
        if (days > NumberUtils.INTEGER_ZERO) {
            formattedTimeDifference.append(days).append(StringConstant.DAY);
        }
        if (hours > NumberUtils.INTEGER_ZERO) {
            formattedTimeDifference.append(hours).append(StringConstant.HOUR);
        }
        if (minutes > NumberUtils.INTEGER_ZERO) {
            formattedTimeDifference.append(minutes).append(StringConstant.MINUTE);
        }
        return formattedTimeDifference.toString();
    }

}
