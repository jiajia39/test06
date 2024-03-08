package com.framework.emt.system.domain.task.handing.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 异常处理-导出 响应体
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskHandingExportResponse implements Serializable {

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
    @ExcelProperty("响应人")
    private String responseUserName;

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
    @ExcelProperty("处理时限")
    private String expire;

    @ColumnWidth(20)
    @ExcelProperty("接受时间")
    private Date acceptTime;

    @ColumnWidth(20)
    @ExcelProperty("挂起次数")
    private Integer suspendNum;

    @ColumnWidth(20)
    @ExcelProperty("挂起时长")
    private String suspendSecondStr;

    @ColumnWidth(20)
    @ExcelProperty("提交时间")
    private Date submitTime;

    @ColumnWidth(20)
    @ExcelProperty("处理人")
    private String handingUserName;

    @ColumnWidth(20)
    @ExcelProperty("处理时间")
    private String handingTime;

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
    @ExcelProperty("处理附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> handingExtendField;

    @ColumnWidth(30)
    @ExcelProperty("处理附加信息")
    private String handingExtendDatas;

    @ExcelIgnore
    @ExcelProperty("挂起附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> suspendExtendField;
    @ColumnWidth(30)
    @ExcelProperty("挂起附加信息")
    private String suspendExtendDatas;

    @ExcelIgnore
    @ApiModelProperty(value = "处理时限")
    private Date handingDeadline;

    @ExcelIgnore
    @ApiModelProperty(value = "异常状态枚举")
    private ExceptionStatus taskStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "挂起时长")
    private Integer suspendSecond;
    @ExcelIgnore
    @ApiModelProperty(value = "紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ApiModelProperty(value = "严重程度枚举")
    private SeverityEnum severity;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ExcelIgnore
    @ApiModelProperty(value = "最后更新人id")
    private Long updateUser;

    @ExcelIgnore
    @ApiModelProperty(value = "实际响应用户id")
    private Long responseUserId;

    @ExcelIgnore
    @ApiModelProperty(value = "实际处理用户id")
    private Long handingUserId;

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

    public List<Long> initUserList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(createUser)) {
            userIdList.add(createUser);
        }
        if (DataUtils.isId(updateUser)) {
            userIdList.add(updateUser);
        }
        if (DataUtils.isId(responseUserId)) {
            userIdList.add(responseUserId);
        }
        if (DataUtils.isId(handingUserId)) {
            userIdList.add(handingUserId);
        }
        return userIdList;
    }

    public void init(Date now,
                     Map<Long, User> userMap,
                     Map<Long, Dept> deptMap,
                     Map<Long, ExceptionCategory> categoryMap,
                     Map<Long, ExceptionItem> itemMap,
                     Map<Long, WorkspaceLocationResponse> workspaceMap) {
        if (CollectionUtil.isEmpty(userMap)) {
            return;
        }
        createUserName = DataUtils.getUserName(userMap, createUser);
        updateUserName = DataUtils.getUserName(userMap, updateUser);
        responseUserName = DataUtils.getUserName(userMap, responseUserId);
        handingUserName = DataUtils.getUserName(userMap, handingUserId);
        deptName = deptMap.get(deptId) == null ? "" : deptMap.get(deptId).getDeptName();
        categoryName = categoryMap.get(categoryId) == null ? "" : categoryMap.get(categoryId).getTitle();
        itemName = itemMap.get(itemId) == null ? "" : itemMap.get(itemId).getTitle();
        WorkspaceLocationResponse workspaceLocationResponse = workspaceMap.get(workspaceId);
        if (workspaceLocationResponse == null) {
            workspaceName = "";
        }else {
            if (StrUtil.isBlank(workspaceLocationResponse.getParentTitle())) {
                workspaceName = workspaceLocationResponse.getTitle();
            } else {
                workspaceName = workspaceLocationResponse.getParentTitle() + "," + workspaceLocationResponse.getTitle();
            }
        }

        this.taskStatusName = Optional.ofNullable(taskStatus).map(ExceptionStatus::getName).orElse(StrUtil.EMPTY);
        this.priorityName = Optional.ofNullable(priority).map(PriorityEnum::getName).orElse(StrUtil.EMPTY);
        this.severityName = Optional.ofNullable(severity).map(SeverityEnum::getName).orElse(StrUtil.EMPTY);
        if (handingDeadline == null) {
            expire = StringConstant.UNKNOWN;
            return;
        }
        if (submitTime == null) {
            expire = now.after(handingDeadline) ? StringConstant.TIME_OUT : StringConstant.NOT_TIME_OUT;
        } else {
            expire = submitTime.after(handingDeadline) ? StringConstant.TIME_OUT : StringConstant.NOT_TIME_OUT;
        }
        // 计算挂起时长
        if (suspendSecond != null) {
            suspendSecondStr = getHourMinutes(suspendSecond);
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
