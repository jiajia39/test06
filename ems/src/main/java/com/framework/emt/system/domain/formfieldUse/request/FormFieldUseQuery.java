package com.framework.emt.system.domain.formfieldUse.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 异常单字段使用表 查询参数
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@ApiModel(value = "异常单字段使用表查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FormFieldUseQuery extends Query implements Serializable {

}


