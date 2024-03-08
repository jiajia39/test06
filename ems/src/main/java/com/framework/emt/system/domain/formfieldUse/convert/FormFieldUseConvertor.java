package com.framework.emt.system.domain.formfieldUse.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.admin.system.entity.DictBiz;
import com.framework.emt.system.domain.formfield.FormField;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.FormFieldUse;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseCreateRequest;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseUpdateRequest;
import com.framework.emt.system.domain.formfieldUse.response.FormFieldUseResponse;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常单字段使用表 转换器
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Mapper
public interface FormFieldUseConvertor {

    FormFieldUseConvertor INSTANCE = Mappers.getMapper(FormFieldUseConvertor.class);

    /**
     * 异常单字段使用表新增参数转换为实体
     *
     * @param request 新增参数
     * @return 实体
     */
    @Mapping(target = "businessType", expression = "java(businessTypeEnumCodeToEnum(request.getBusinessType()))")
    FormFieldUse requestToEntity(FormFieldUseCreateRequest request);

    /**
     * 异常单字段使用表更新参数转换为实体
     *
     * @param formFieldUse 实体
     * @param request      更新参数
     */
    @Mapping(target = "businessType", expression = "java(businessTypeEnumCodeToEnum(request.getBusinessType()))")
    void requestToUpdate(@MappingTarget FormFieldUse formFieldUse, FormFieldUseUpdateRequest request);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default BusinessTypeEnum businessTypeEnumCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(BusinessTypeEnum.class, code);
    }

    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<FormFieldUseResponse> pageVo(IPage<FormFieldUse> pages) {
        IPage<FormFieldUseResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(FormFieldUseConvertor.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }

    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<FormFieldUseResponse> entityList2Vo(List<FormFieldUse> itemList);

    /**
     * 实体转换成响应体
     *
     * @param entity
     * @return
     */
    FormFieldUseResponse entityToResponse(FormFieldUse entity);
}
