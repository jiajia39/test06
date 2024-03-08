package com.framework.emt.system.domain.formfield.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.formfield.FormField;
import com.framework.emt.system.domain.formfield.request.FormFieldListQueryRequest;
import com.framework.emt.system.domain.formfield.request.FormFieldQueryRequest;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常表单字段表 Mapper层
 *
 * @author gaojia
 * @since 2023-07-28
 */
public interface FormFieldMapper extends BaseMapper<FormField> {

    /**
     * 查询需要查询字典的异常表单字段列表
     *
     * @param request 查询条件
     * @return 符合条件的异常表单字段表内容
     */
    List<FormFieldResponse> formFieldList( @Param("request") FormFieldListQueryRequest request);
}
