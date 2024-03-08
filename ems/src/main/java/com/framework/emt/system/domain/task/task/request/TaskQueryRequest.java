package com.framework.emt.system.domain.task.task.request;

import cn.hutool.core.date.DatePattern;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.Choice;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务 查询条件
 *
 * @author ds_C
 * @since 2023-08-20
 */
@Getter
@Setter
public class TaskQueryRequest extends Query implements Serializable {

    @ApiModelProperty(value = "异常流程id")
    private Long processId;

    @ApiModelProperty(value = "异常分类id")
    private Long categoryId;

    @ApiModelProperty(value = "异常项id")
    private Long itemId;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceId;

    @ApiModelProperty(value = "提报部门id")
    private Long deptId;

    @Length(max = 30, message = "异常编号长度限制{max}")
    @ApiModelProperty(value = "异常编号")
    private String code;

    @EnumValidator(enumClazz = ExceptionStatus.class, message = "异常状态类型错误")
    @ApiModelProperty(value = "异常状态")
    private Integer status;

    @EnumValidator(enumClazz = ExceptionSubStatus.class, message = "异常子状态类型错误")
    @ApiModelProperty(value = "异常子状态")
    private Integer subStatus;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命")
    private Integer severity;

    @EnumValidator(enumClazz = Choice.class, message = "响应超时类型错误 0:否 1:是 2:全部")
    @ApiModelProperty(value = "响应超时 0:否 1:是 2:全部")
    private Integer responseIsTimeOut;

    @EnumValidator(enumClazz = Choice.class, message = "处理超时类型错误 0:否 1:是 2:全部")
    @ApiModelProperty(value = "处理超时 0:否 1:是 2:全部")
    private Integer handingIsTimeOut;

    @ApiModelProperty(value = "响应时长是否正序  true:正序 false:倒叙")
    private Boolean responseDurationIsAsc;

    @ApiModelProperty(value = "处理时长是否正序  true:正序 false:倒叙")
    private Boolean handingDurationIsAsc;

    @ApiModelProperty(value = "驳回次数是否正序  true:正序 false:倒叙")
    private Boolean rejectNumIsAsc;

    @ApiModelProperty(value = "转派次数是否正序  true:正序 false:倒叙")
    private Boolean otherCountIsAsc;

    @ApiModelProperty(value = "挂起次数是否正序  true:正序 false:倒叙")
    private Boolean handingSuspendNumIsAsc;

    @ApiModelProperty(value = "挂起时长是否正序  true:正序 false:倒叙")
    private Boolean handingSuspendTotalSecondIsAsc;

    @ApiModelProperty(value = "时间类型 1.提报2响应3处理4验收")
    private Integer timeType;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "异常状态列表")
    private List<Integer> statusList;
}
