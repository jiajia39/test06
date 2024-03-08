package com.framework.emt.system.domain.task.task.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 异常列表-导出 响应体
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskExportResponse implements Serializable {
    @ColumnWidth(20)
    @ExcelProperty("开始时间")
    private Date startTime;

    @ColumnWidth(20)
    @ExcelProperty("关闭时间")
    private Date endTime;

    @ColumnWidth(20)
    @ExcelProperty("总时长(分)")
    private String totalDuration;

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
    @ExcelProperty("提报时间")
    @ContentStyle(dataFormat = 14)
    private Date submitTime;

    @ColumnWidth(30)
    @ExcelProperty("提报附加信息 产品型号")
    private String submitExtendModelDatas;

    @ColumnWidth(30)
    @ExcelProperty("提报附加信息 故障是否造成主传动停止，进空匣钵(只勾选窑炉工序选择)")
    private String submitExtendDriveStopDatas;

    @ColumnWidth(30)
    @ExcelProperty("提报附加信息 影响批次")
    private String submitExtendBatchDatas;

    @ColumnWidth(20)
    @ExcelProperty("作业单元")
    private String workspaceName;

    @ColumnWidth(20)
    @ExcelProperty("故障描述")
    private String problemDesc;

    @ColumnWidth(20)
    @ExcelProperty("提报人")
    private String submitUserName;

    @ColumnWidth(20)
    @ExcelProperty("异常状态")
    private String taskStatusName;

    @ColumnWidth(20)
    @ExcelProperty("实际响应人")
    private String responseUserName;

//    @ColumnWidth(20)
//    @ExcelProperty("紧急状态")
//    private String priorityName;
//
//    @ColumnWidth(20)
//    @ExcelProperty("严重程度")
//    private String severityName;

    @ColumnWidth(20)
    @ExcelProperty("响应时长(分)")
    private String responseDuration;

    @ColumnWidth(20)
    @ExcelProperty("响应时间")
    private Date responseSubmitTime;

    @ColumnWidth(20)
    @ExcelProperty("响应期限")
    private String responseLimit;

    @ColumnWidth(20)
    @ExcelProperty("实际处理人")
    private String handingUserName;

    @ColumnWidth(20)
    @ExcelProperty("处理时间")
    private Date handingSubmitTime;

    @ColumnWidth(20)
    @ExcelProperty("异常原因项")
    private String handingReasons;

    @ColumnWidth(30)
    @ExcelProperty("原因分析")
    private String handingReasonAnalysis;

    @ColumnWidth(30)
    @ExcelProperty("解决方案")
    private String handingSolution;

    @ColumnWidth(30)
    @ExcelProperty("处理结果")
    private String handingResult;

    @ColumnWidth(30)
    @ExcelProperty("处理附加信息 处置方案")
    private String handingExtendDisposalPlanDatas;

    @ColumnWidth(30)
    @ExcelProperty("处理附加信息 维修内容")
    private String handingExtendMaintenanceContentDatas;

    @ColumnWidth(20)
    @ExcelProperty("处理期限")
    private String handingLimit;

    @ColumnWidth(20)
    @ExcelProperty("处理时长(分)")
    private String handingDuration;

    @ColumnWidth(20)
    @ExcelProperty("驳回次数")
    private Integer rejectNum;

    @ColumnWidth(20)
    @ExcelProperty("转派次数")
    private Integer otherCount;

    @ColumnWidth(20)
    @ExcelProperty("挂起次数")
    private Integer handingSuspendNum;

    @ColumnWidth(20)
    @ExcelProperty("挂起时长(分)")
    private String suspendDuration;

    @ColumnWidth(30)
    @ExcelProperty("挂起附加信息 原因分析")
    private String suspendExtendCauseAnalysisDatas;

//    @ColumnWidth(20)
//    @ExcelProperty("提报部门")
//    private String deptName;

    @ColumnWidth(20)
    @ExcelProperty("验收时间")
    @ContentStyle(dataFormat = 14)
    private Date closeTime;

    @ColumnWidth(20)
    @ExcelProperty("验收人")
    private String checkUser;

    //    @ColumnWidth(20)
//    @ExcelProperty("创建人")
//    private String createUserName;
//
    @ExcelIgnore
    @ExcelProperty("创建时间")
    @ContentStyle(dataFormat = 14)
    private Date createTime;
//
//    @ColumnWidth(20)
//    @ExcelProperty("最后更新人")
//    private String updateUserName;
//
//    @ColumnWidth(20)
//    @ExcelProperty("最后更新时间")
//    private Date updateTime;

    @ExcelIgnore
    @ExcelProperty("提报附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendField;

    @ExcelIgnore
    @ExcelProperty("响应附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> responseExtendField;

    @ExcelIgnore
    @ApiModelProperty("异常原因项列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TagInfo> handingReasonItems;

    @ExcelIgnore
    @ExcelProperty("处理附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> handingExtendField;

    @ExcelIgnore
    @ExcelProperty("挂起附加信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> suspendExtendField;

    @ExcelIgnore
    @ExcelProperty("异常id")
    private Long exceptionTaskId;

    @ExcelIgnore
    @ApiModelProperty(value = "异常状态枚举")
    private ExceptionStatus taskStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ApiModelProperty(value = "响应总时长")
    private Integer responseTotalSecond;

    @ExcelIgnore
    @ApiModelProperty(value = "处理总时长")
    private Integer handingTotalSecond;

    @ExcelIgnore
    @ApiModelProperty(value = "处理挂起总时长")
    private Integer handingSuspendTotalSecond;

    @ExcelIgnore
    @ApiModelProperty(value = "验收总时长")
    private Integer checkTotalSecond;

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

    @ExcelIgnore
    @ApiModelProperty(value = "提报人用户id")
    private Long submitUserId;

    @ExcelIgnore
    @ApiModelProperty(value = "实际响应用户id")
    private Long responseUserId;

    @ExcelIgnore
    @ApiModelProperty(value = "实际处理用户id")
    private Long handingUserId;

    @ExcelIgnore
    @ApiModelProperty(value = "响应最后期限")
    private Date responseDeadline;

    @ExcelIgnore
    @ApiModelProperty(value = "处理最后期限")
    private Date handingDeadline;

    @ExcelIgnore
    @ApiModelProperty(value = "响应是否超时")
    private Integer responseExpire;

    @ExcelIgnore
    @ApiModelProperty(value = "处理是否超时")
    private Integer handingExpire;


    public List<Long> initUserList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(createUser)) {
            userIdList.add(createUser);
        }
        if (DataUtils.isId(updateUser)) {
            userIdList.add(updateUser);
        }
        if (DataUtils.isId(submitUserId)) {
            userIdList.add(submitUserId);
        }
        if (DataUtils.isId(responseUserId)) {
            userIdList.add(responseUserId);
        }
        if (DataUtils.isId(handingUserId)) {
            userIdList.add(handingUserId);
        }
        return userIdList;
    }

    /**
     * 初始化数据
     *
     * @param now 当前时间
     */
    public void init(Date now,
                     Map<Long, User> userMap,
                     Map<Long, Dept> deptMap,
                     Map<Long, ExceptionCategory> categoryMap,
                     Map<Long, ExceptionItem> itemMap,
                     Map<Long, WorkspaceLocationResponse> workspaceMap) {
        if (CollectionUtil.isEmpty(userMap)) {
            return;
        }
//        createUserName = DataUtils.getUserName(userMap, createUser);
//        updateUserName = DataUtils.getUserName(userMap, updateUser);
        submitUserName = DataUtils.getUserName(userMap, submitUserId);
        responseUserName = DataUtils.getUserName(userMap, responseUserId);
        handingUserName = DataUtils.getUserName(userMap, handingUserId);
//        deptName = deptMap.get(deptId) == null ? "" : deptMap.get(deptId).getDeptName();
        categoryName = categoryMap.get(categoryId) == null ? "" : categoryMap.get(categoryId).getTitle();
        itemName = itemMap.get(itemId) == null ? "" : itemMap.get(itemId).getTitle();
        WorkspaceLocationResponse workspaceLocationResponse = workspaceMap.get(workspaceId);
        if (workspaceLocationResponse == null) {
            workspaceName = "";
        } else {
            if (StrUtil.isBlank(workspaceLocationResponse.getParentTitle())) {
                workspaceName = workspaceLocationResponse.getTitle();
            } else {
                workspaceName = workspaceLocationResponse.getParentTitle() + "," + workspaceLocationResponse.getTitle();
            }
        }
        // 获取枚举name
        this.taskStatusName = Optional.ofNullable(taskStatus).map(ExceptionStatus::getName).orElse(StrUtil.EMPTY);
//        this.priorityName = Optional.ofNullable(priority).map(PriorityEnum::getName).orElse(StrUtil.EMPTY);
//        this.severityName = Optional.ofNullable(severity).map(SeverityEnum::getName).orElse(StrUtil.EMPTY);
        // 获取响应期限和处理期限
        SimpleDateFormat dateFormat = new SimpleDateFormat(StringConstant.SIMPLE_DATE_FORMAT);
        responseExpire = getExpire(now, responseSubmitTime, responseDeadline);
        if (NumberUtils.INTEGER_ONE.equals(responseExpire)) {
            responseLimit = StringConstant.TIME_OUT;
        } else {
            responseLimit = Optional.ofNullable(responseDeadline).map(dateFormat::format).orElse(StrUtil.EMPTY);
        }
        handingExpire = getExpire(now, handingSubmitTime, handingDeadline);
        if (NumberUtils.INTEGER_ONE.equals(handingExpire)) {
            if (ExceptionStatus.PRE_RESPONSE.equals(taskStatus)) {
                handingLimit = StrUtil.EMPTY;
            } else {
                handingLimit = StringConstant.TIME_OUT;
            }
        } else {
            handingLimit = Optional.ofNullable(handingDeadline).map(dateFormat::format).orElse(StrUtil.EMPTY);
        }
        handingDuration = secToMinutes(handingTotalSecond);
        responseDuration = secToMinutes(responseTotalSecond);
        suspendDuration = secToMinutes(handingSuspendTotalSecond);

        if (ObjectUtil.isNotEmpty(submitTime) && ObjectUtil.isNotEmpty(closeTime)) {
            totalDuration = secToMinutes((Convert.toInt(closeTime.getTime() - submitTime.getTime())) / 1000);
        }
    }

    private String secToMinutes(Integer seconds) {
        if (seconds == null || seconds <= 0) {
            return "0";
        }
        return String.valueOf(seconds/60);
    }

    /**
     * 获取响应是否超时或处理是否超时
     *
     * @param now  当前时间
     * @param time 响应时间或处理时间
     * @param time 响应最后期限或处理最后期限
     * @return
     */
    private Integer getExpire(Date now, Date time, Date deadline) {
        if (time == null) {
            if (deadline == null) {
                return NumberUtils.INTEGER_ZERO;
            }
            return now.after(deadline) ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
        } else {
            return time.after(deadline) ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
        }
    }

}
