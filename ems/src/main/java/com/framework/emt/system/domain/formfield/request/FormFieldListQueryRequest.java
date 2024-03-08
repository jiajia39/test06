package com.framework.emt.system.domain.formfield.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常表单字段表 查询条件
 *
 * @author 高佳
 * @since 2023-07-28
 */
@Getter
@Setter
@Accessors(chain = true)
public class FormFieldListQueryRequest extends Query implements Serializable {

    @EnumValidator(enumClazz = BusinessTypeEnum.class, message = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    @ApiModelProperty(value = "业务类型 0:异常提报1:异常响应2:异常处理3:异常挂起4:异常协同 5:异常验收")
    private Integer businessType;

    @ApiModelProperty(value = "表单id列表")
    @NotEmpty(message = "表单id列表不能为空")
    @UniqueElementsValidator(message = "表单id不能重复")
    @Size(max = 100, message = "id列表数目最大100条")
    private List<Long> idList = new ArrayList<>();

    @NotNull(message = "是否携带字典数据不能为空")
    @ApiModelProperty(value = "是否携带字典数据")
    private Boolean carryDictData;

}
