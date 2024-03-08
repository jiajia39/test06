package com.framework.emt.system.domain.formfield.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.admin.system.entity.DictBiz;
import com.framework.emt.system.domain.formfield.FormField;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldSubTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.formfield.request.FormFieldCreateRequest;
import com.framework.emt.system.domain.formfield.request.FormFieldListQueryRequest;
import com.framework.emt.system.domain.formfield.request.FormFieldUpdateRequest;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常表单 转换类
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Mapper
public interface FormFieldConvert {

    FormFieldConvert INSTANCE = Mappers.getMapper(FormFieldConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "businessType", expression = "java(businessTypeEnumCodeToEnum(request.getBusinessType()))")
    @Mapping(target = "type", expression = "java(formFieldTypeEnumCodeToEnum(request.getType()))")
    @Mapping(target = "subtype", expression = "java(formFieldSubTypeEnumCodeToEnum(request.getSubtype()))")
    FormField createRequestToEntity(FormFieldCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "businessType", expression = "java(businessTypeEnumCodeToEnum(request.getBusinessType()))")
    @Mapping(target = "type", expression = "java(formFieldTypeEnumCodeToEnum(request.getType()))")
    @Mapping(target = "subtype", expression = "java(formFieldSubTypeEnumCodeToEnum(request.getSubtype()))")
    FormField updateRequestToEntity(FormFieldUpdateRequest request);

    /**
     * 参数转换成request
     *
     * @param extendFieldIdList 附加字段id列表
     * @param carryDictData     是否携带字典数据
     * @return
     */
    @Mapping(target = "idList", source = "extendFieldIdList")
    @Mapping(target = "carryDictData", source = "carryDictData")
    FormFieldListQueryRequest paramsToQueryRequest(List<Long> extendFieldIdList,
                                                   Boolean carryDictData);

    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<FormFieldResponse> pageVo(IPage<FormField> pages) {
        IPage<FormFieldResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(FormFieldConvert.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }

    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<FormFieldResponse> entityList2Vo(List<FormField> itemList);

    /**
     * 实体转换成响应体
     *
     * @param entity
     * @param dictList
     * @return
     */
    FormFieldResponse entityToResponse(FormField entity, List<DictBiz> dictList);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default BusinessTypeEnum businessTypeEnumCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(BusinessTypeEnum.class, code);
    }

    default FormFieldTypeEnum formFieldTypeEnumCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(FormFieldTypeEnum.class, code);
    }

    default FormFieldSubTypeEnum formFieldSubTypeEnumCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(FormFieldSubTypeEnum.class, code);
    }

}
