package com.framework.emt.system.domain.task.check.request;

import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常验收-导出 查询条件
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskCheckExportQueryRequest implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String categoryTitle;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "异常项名称")
    private String itemTitle;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "作业单元名称")
    private String workspaceLocationTitle;

    @ApiModelProperty(value = "提报部门id")
    private String deptId;

    @ApiModelProperty(value = "提报部门名称")
    private String deptName;

    @ApiModelProperty(value = "异常编号")
    private String code;

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "验收状态")
    @Range(max = 3, min = 1, message = "验证状态的值最小为1，最大为3")
    @EnumValidator(enumClazz = CheckStatus.class, message = "验收状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "是否过期")
    private Boolean isTimeOut;

    @ApiModelProperty(value = "任务状态 全部：0")
    private Integer taskStatus;

    @Size(max = 10000, message = "异常验收id数目最大{max}条")
    @UniqueElementsValidator(message = "异常验收id不能重复")
    @ApiModelProperty(value = "异常验收id列表")
    private List<Long> ids;

    @ApiModelProperty(value = "需要展示的附加字段 1提报2.响应3处理4协同5验收6挂起")
    private List<Integer> isShowExtendField;

}
