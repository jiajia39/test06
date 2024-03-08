package com.framework.emt.system.domain.formfieldUse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.FormFieldUse;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseCreateRequest;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseUpdateRequest;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseQuery;
import com.framework.emt.system.domain.formfieldUse.response.FormFieldUseResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 异常单字段使用表 服务接口
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Validated
public interface IFormFieldUseService extends BaseService<FormFieldUse> {

    /**
     * 新增异常单字段使用表
     *
     * @param request 新增参数
     * @return 异常单字段使用表id
     */
    Long create(@Valid FormFieldUseCreateRequest request);

    /**
     * 更新异常单字段使用表
     *
     * @param id      异常单字段使用表id
     * @param request 更新参数
     */
    void update(@NotNull(message = "异常单字段使用表id不能为空") Long id, @Valid FormFieldUseUpdateRequest request);

    /**
     * 查看异常单字段使用表
     *
     * @param id 异常单字段使用表id
     * @return 异常单字段使用表
     */
    FormFieldUseResponse info(@NotNull(message = "异常单字段使用表id不能为空") Long id);

    /**
     * 异常单字段使用表分页
     *
     * @param pageQuery 分页查询条件
     * @return 查询结果
     */
    IPage<FormFieldUseResponse> page(@Valid FormFieldUseQuery pageQuery);

    /**
     * 删除异常单字段使用表
     *
     * @param id 异常单字段使用表id
     */
    void delete(@NotNull(message = "异常单字段使用表id不能为空") Long id);


    /**
     * 删除异常单字段使用表
     *
     * @param bizId 关联id
     */
    void deleteByKeyIdAndBusinessType(Long bizId, BusinessTypeEnum businessType);

    /**
     * 新增异常单字段使用信息
     *
     * @param extendDataList 附加字段
     * @param bizId          关联id
     */
    void createFormFieldUser(List<FormFieldResponse> extendDataList, Long bizId, BusinessTypeEnum businessType);
}
