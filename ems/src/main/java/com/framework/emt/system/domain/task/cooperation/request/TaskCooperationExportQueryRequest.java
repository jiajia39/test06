package com.framework.emt.system.domain.task.cooperation.request;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.Choice;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationSubStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常协同-导出 查询条件
 *
 * @author ds_C
 * @since 2023-08-17
 */
@Getter
@Setter
public class TaskCooperationExportQueryRequest implements Serializable {

    @Length(max = 30, message = "协同编号长度限制{max}")
    @ApiModelProperty(value = "协同编号")
    private String code;

    @Length(max = 20, message = "协同任务长度限制{max}")
    @ApiModelProperty(value = "协同任务")
    private String title;

    @Pattern(regexp = "1|3|4|5", message = "状态错误 1:待协同 3:协同中 4:已完成 5：所有")
    @ApiModelProperty(value = "状态 1:待协同 3:协同中 4:已完成")
    private String status;

    @EnumValidator(enumClazz = CooperationStatus.class, message = "协同状态错误 1:待协同 2:已撤销 3:协同中 4:已完成")
    @ApiModelProperty(value = "协同状态")
    private Integer cooperationStatus;

    @EnumValidator(enumClazz = CooperationSubStatus.class, message = "协同子状态错误 11:待协同 12:已转派 21:已撤销 31:协同中 41:已完成")
    @ApiModelProperty(value = "协同子状态")
    private Integer cooperationSubStatus;

    @Length(max = 30, message = "异常编号长度限制{max}")
    @ApiModelProperty(value = "异常编号")
    private String exceptionCode;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命")
    private Integer severity;

    @EnumValidator(enumClazz = Choice.class, message = "选择类型错误 0:否 1:是 2:全部")
    @ApiModelProperty(value = "协同超时 0:否 1:是 2:全部")
    private Integer isTimeOut;

    @ApiModelProperty(value = "异常任务处理id")
    private Long exceptionTaskHandingId;

    @Size(max = 10000, message = "异常协同id数目最大{max}条")
    @UniqueElementsValidator(message = "异常协同id不能重复")
    @ApiModelProperty(value = "异常协同id列表")
    private List<Long> ids;

    @ApiModelProperty(value = "需要展示的附加字段 1提报2.响应3处理4协同5验收6挂起")
    private List<Integer> isShowExtendField;

}
