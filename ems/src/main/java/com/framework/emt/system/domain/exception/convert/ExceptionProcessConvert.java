package com.framework.emt.system.domain.exception.convert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.exception.request.ExceptionProcessCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionProcessUpdateRequest;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessSubmitResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.ExtendFieldRequest;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常流程 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface ExceptionProcessConvert {

    ExceptionProcessConvert INSTANCE = Mappers.getMapper(ExceptionProcessConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @param formFieldMap key为附加字段id，value为附加字段对象的map列表
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum.ENABLE)")
    @Mapping(target = "responseMode", expression = "java(responseModeToEnum(request.getResponseMode()))")
    @Mapping(target = "handingMode", expression = "java(handingModeToEnum(request.getHandingMode()))")
    @Mapping(target = "checkMode", expression = "java(checkModeToEnum(request.getCheckMode()))")
    @Mapping(target = "checkCondition", expression = "java(checkConditionToEnum(request.getCheckCondition()))")
    @Mapping(target = "submitExtendField", expression = "java(getExtendFieldList(request.getSubmitExtendFieldList(), formFieldMap))")
    @Mapping(target = "responseExtendField", expression = "java(getExtendFieldList(request.getResponseExtendFieldList(), formFieldMap))")
    @Mapping(target = "handingExtendField", expression = "java(getExtendFieldList(request.getHandingExtendFieldList(), formFieldMap))")
    @Mapping(target = "pendingExtendField", expression = "java(getExtendFieldList(request.getPendingExtendFieldList(), formFieldMap))")
    @Mapping(target = "cooperateExtendField", expression = "java(getExtendFieldList(request.getCooperateExtendFieldList(), formFieldMap))")
    @Mapping(target = "checkExtendField", expression = "java(getExtendFieldList(request.getCheckExtendFieldList(), formFieldMap))")
    ExceptionProcess createRequestToEntity(ExceptionProcessCreateRequest request,
                                           @Context Map<Long, FormFieldResponse> formFieldMap);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @param formFieldMap key为附加字段id，value为附加字段对象的map列表
     * @return
     */
    @Mapping(target = "responseMode", expression = "java(responseModeToEnum(request.getResponseMode()))")
    @Mapping(target = "handingMode", expression = "java(handingModeToEnum(request.getHandingMode()))")
    @Mapping(target = "checkMode", expression = "java(checkModeToEnum(request.getCheckMode()))")
    @Mapping(target = "checkCondition", expression = "java(checkConditionToEnum(request.getCheckCondition()))")
    @Mapping(target = "submitExtendField", expression = "java(getExtendFieldList(request.getSubmitExtendFieldList(), formFieldMap))")
    @Mapping(target = "responseExtendField", expression = "java(getExtendFieldList(request.getResponseExtendFieldList(), formFieldMap))")
    @Mapping(target = "handingExtendField", expression = "java(getExtendFieldList(request.getHandingExtendFieldList(), formFieldMap))")
    @Mapping(target = "pendingExtendField", expression = "java(getExtendFieldList(request.getPendingExtendFieldList(), formFieldMap))")
    @Mapping(target = "cooperateExtendField", expression = "java(getExtendFieldList(request.getCooperateExtendFieldList(), formFieldMap))")
    @Mapping(target = "checkExtendField", expression = "java(getExtendFieldList(request.getCheckExtendFieldList(), formFieldMap))")
    ExceptionProcess updateRequestToEntity(@MappingTarget ExceptionProcess entity,
                                           ExceptionProcessUpdateRequest request,
                                           @Context Map<Long, FormFieldResponse> formFieldMap);

    /**
     * map键对应的值转响应体
     *
     * @param response
     * @param userInfoMap
     * @return
     */
    @Mapping(target = "responseUserIdOfNameList", expression = "java(loadUserInfoList(response.getResponseUserIds(), userInfoMap))")
    @Mapping(target = "handingUserIdOfNameList", expression = "java(loadUserInfoList(response.getHandingUserIds(), userInfoMap))")
    @Mapping(target = "checkUserIdOfNameList", expression = "java(loadUserInfoList(response.getCheckUserIds(), userInfoMap))")
    ExceptionProcessResponse mapValueToResponse(ExceptionProcessResponse response,
                                                @Context Map<Long, String> userInfoMap);

    /**
     * map键对应的值转响应体
     *
     * @param response
     * @param userInfoMap
     * @return
     */
    @Mapping(target = "responseUserInfoList", expression = "java(loadUserInfoList(response.getResponseUserIds(), userInfoMap))")
    ExceptionProcessSubmitResponse mapValueToResponse(ExceptionProcessSubmitResponse response,
                                                      @Context Map<Long, String> userInfoMap);

    /**
     * 异常表单字段对象拷贝
     *
     * @param formFieldResponse
     * @return
     */
    FormFieldResponse copyFormFieldResponse(FormFieldResponse formFieldResponse);

    /**
     * 响应模式Code转换成枚举
     *
     * @param code 响应模式Code
     * @return 响应模式枚举
     */
    default ResponseModeEnum responseModeToEnum(Integer code) {
        return BaseEnum.parseByCode(ResponseModeEnum.class, code);
    }

    /**
     * 处理模式Code转换成枚举
     *
     * @param code 处理模式Code
     * @return 处理模式枚举
     */
    default HandingModeEnum handingModeToEnum(Integer code) {
        return BaseEnum.parseByCode(HandingModeEnum.class, code);
    }

    /**
     * 验收模式Code转换成枚举
     *
     * @param code 验收模式Code
     * @return 验收模式枚举
     */
    default CheckModeEnum checkModeToEnum(Integer code) {
        return BaseEnum.parseByCode(CheckModeEnum.class, code);
    }

    /**
     * 验收模式Code转换成枚举
     *
     * @param code 验收模式Code
     * @return 验收模式枚举
     */
    default CheckConditionEnum checkConditionToEnum(Integer code) {
        return BaseEnum.parseByCode(CheckConditionEnum.class, code);
    }


    /**
     * 通过附加字段id列表得到对应的附加字段对象
     *
     * @param fieldList    附加字段列表
     * @param formFieldMap key为附加字段id，value为附加字段对象的map列表
     * @return
     */
    default List<FormFieldResponse> getExtendFieldList(List<ExtendFieldRequest> fieldList, @Context Map<Long, FormFieldResponse> formFieldMap) {
        return fieldList.stream().map(request -> {
            FormFieldResponse formField = formFieldMap.get(request.getId());
            if (formField != null) {
                FormFieldResponse formFieldResponse = ExceptionProcessConvert.INSTANCE.copyFormFieldResponse(formField);
                formFieldResponse.setRequired(request.getRequired() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
                return formFieldResponse;
            }
            return null;
        }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
    }

    /**
     * 通过响应、处理、验收人id对应的name的map列表
     * 装载对应的用户id和用户名称列表
     *
     * @param userIds     用户id列表
     * @param userInfoMap key为用户id，value为用户姓名的map列表
     * @return
     */
    default List<UserInfoResponse> loadUserInfoList(List<Long> userIds, @Context Map<Long, String> userInfoMap) {
        if (CollectionUtil.isEmpty(userInfoMap)) {
            return Collections.emptyList();
        }
        return userIds.stream().filter(userInfoMap::containsKey).map(userId -> new UserInfoResponse(userId, userInfoMap.get(userId))).collect(Collectors.toList());
    }

}
