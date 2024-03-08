package com.framework.emt.system.domain.knowledge.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 知识库 移动端查询条件
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class KnowledgeBaseAppQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "关键词长度限制{max}")
    @ApiModelProperty(value = "关键词  知识库、异常项或关键词")
    private String keyWord;

}
