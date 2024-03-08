package com.framework.emt.system.domain.formfield.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.framework.emt.system.domain.formfield.FormField;
import com.framework.emt.system.domain.formfield.request.*;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 异常表单字段表 服务层
 *
 * @author gaojia
 * @since 2023-07-28
 */
public interface FormFieldService extends BaseService<FormField> {

    /**
     * 异常表单字段表创建
     *
     * @param request 新增内容
     * @return 主键id
     */
    Long create(FormFieldCreateRequest request);

    /**
     * 删除异常表单字段表对应id的数据
     *
     * @param id 异常表单字段表id
     */
    void delete(Long id);

    /**
     * 更新异常表单字段表的数据
     *
     * @param id      异常表单字段表id
     * @param request 更新内容
     * @return 主键id
     */
    Long update(Long id, FormFieldUpdateRequest request);

    /**
     * 查询异常表单字段表详情
     *
     * @param id            异常表单字段表id
     * @param carryDictData 是否携带字典数据
     * @return 异常表单字段表信息
     */
    FormFieldResponse detail(Long id,
                             boolean carryDictData) throws JsonProcessingException;

    /**
     * 查询异常表单字段表分页列表
     *
     * @param request 查询条件
     * @return 符合条件的异常表单字段表内容
     */
    IPage<FormFieldResponse> page(FormFieldQueryRequest request);

    /**
     * 查询异常表单字段表列表
     *
     * @param request 查询条件
     * @return 符合条件的异常表单字段表内容
     */
    List<FormFieldResponse> list(FormFieldListQueryRequest request);

    /**
     * 启用禁用接口
     * @param id 表单id
     * @param request 参数
     * @return
     */
    Long updateStatus(Long id, FormFieldUpdateRequest request);
}
