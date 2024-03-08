package com.framework.emt.system.domain.task.submit.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常提报 查询条件
 *
 * @author ds_C
 * @since 2023-08-09
 */
@Getter
@Setter
public class TaskSubmitQueryRequest extends Query implements Serializable {

    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @Length(max = 30, message = "异常编号长度限制{max}")
    @ApiModelProperty(value = "异常编号")
    private String code;

    @EnumValidator(enumClazz = ExceptionStatus.class, message = "异常提报状态错误")
    @ApiModelProperty(value = "异常提报状态")
    private Integer taskStatus;

    @EnumValidator(enumClazz = ExceptionSubStatus.class, message = "异常提报子状态错误")
    @ApiModelProperty(value = "异常提报子状态")
    private Integer taskSubStatus;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度类型")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度类型")
    private Integer severity;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提报开始时间")
    private LocalDateTime submitStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提报结束时间")
    private LocalDateTime submitEndTime;

    @ApiModelProperty(value = "异常提报状态列表")
    private List<Integer> taskStatusList;

    @ApiModelProperty(value = "异常项，异常编号模糊搜索")
    private String content;
}
