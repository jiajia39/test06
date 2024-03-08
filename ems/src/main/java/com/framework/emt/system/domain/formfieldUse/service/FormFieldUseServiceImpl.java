package com.framework.emt.system.domain.formfieldUse.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.json.utils.JsonUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.formfield.request.SelectListRequest;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.FormFieldUse;
import com.framework.emt.system.domain.formfieldUse.constant.FormFieldUseErrorCode;
import com.framework.emt.system.domain.formfieldUse.convert.FormFieldUseConvertor;
import com.framework.emt.system.domain.formfieldUse.mapper.FormFieldUseMapper;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseCreateRequest;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseQuery;
import com.framework.emt.system.domain.formfieldUse.request.FormFieldUseUpdateRequest;
import com.framework.emt.system.domain.formfieldUse.response.FormFieldUseResponse;
import com.framework.emt.system.domain.formfieldUse.response.InputResponse;
import com.framework.emt.system.domain.formfieldUse.response.MultiSelectResponse;
import com.framework.emt.system.domain.formfieldUse.response.SelectResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 异常单字段使用表 服务实现类
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@RequiredArgsConstructor
@Service
public class FormFieldUseServiceImpl extends BaseServiceImpl<FormFieldUseMapper, FormFieldUse> implements IFormFieldUseService {

    @Override
    public Long create(FormFieldUseCreateRequest request) {
        FormFieldUse formFieldUse = FormFieldUseConvertor.INSTANCE.requestToEntity(request);
        this.create(formFieldUse);
        return formFieldUse.getId();
    }

    @Override
    public void update(Long id, FormFieldUseUpdateRequest request) {
        FormFieldUse formFieldUse = getById(id);
        FormFieldUseConvertor.INSTANCE.requestToUpdate(formFieldUse, request);
        this.update(formFieldUse);
    }

    @Override
    public FormFieldUseResponse info(Long id) {
        FormFieldUse formFieldUse = this.findByIdThrowErr(id, FormFieldUseErrorCode.NOT_FOUND);
        return FormFieldUseConvertor.INSTANCE.entityToResponse(formFieldUse);
    }

    @Override
    public IPage<FormFieldUseResponse> page(FormFieldUseQuery query) {
        IPage<FormFieldUse> page = Condition.getPage(query);
        LambdaQueryWrapper<FormFieldUse> queryWrapper = new LambdaQueryWrapper<>();
        IPage<FormFieldUse> formFieldUseIPage = this.page(page, queryWrapper);
        return FormFieldUseConvertor.INSTANCE.pageVo(formFieldUseIPage);
    }


    @Override
    public void delete(Long id) {
        Assert.isTrue(this.removeById(id),
                () -> new ServiceException(FormFieldUseErrorCode.DELETE_NOT_EXIST));
    }

    @Override
    public void deleteByKeyIdAndBusinessType(Long bizId, BusinessTypeEnum businessType) {
        LambdaQueryWrapper<FormFieldUse> lambda = new LambdaQueryWrapper<>();
        lambda.eq(FormFieldUse::getBizId, bizId);
        lambda.eq(FormFieldUse::getBusinessType, businessType);
        this.remove(lambda);
    }

    @Override
    public void createFormFieldUser(List<FormFieldResponse> extendDataList, Long bizId, BusinessTypeEnum businessType) {
        if (ObjectUtil.isNotEmpty(extendDataList)) {
            List<FormFieldUse> formFieldUseList = new ArrayList<>();
            extendDataList.forEach(extendData -> {
                FormFieldUse formFieldUse = new FormFieldUse();
                formFieldUse.setFieldsVersion(extendData.getFieldsVersion());
                formFieldUse.setBizId(bizId);
                formFieldUse.setBusinessType(businessType);
                formFieldUse.setFieldsCode(extendData.getCode());
                formFieldUse.setRequired(extendData.getRequired());
                FormFieldTypeEnum fieldTypeEnum = extendData.getType();
                switch (fieldTypeEnum) {
                    case INPUT:
                    case DATETIME:
                    case SLIDER:
                        InputResponse response = new InputResponse();
                        response.setContent(extendData.getKey());
                        formFieldUse.setFieldsValue(JsonUtil.toJson(response));
                        break;
                    case SELECT:
                    case RADIO:
                    case SWITCH:
                        SelectResponse selectResponse = new SelectResponse();
                        selectResponse.setKey(extendData.getKey());
                        selectResponse.setValue(extendData.getValue());
                        formFieldUse.setFieldsValue(JsonUtil.toJson(selectResponse));
                        break;
                    case CHECKBOX:
                        List<SelectResponse> selectResponseList = new ArrayList<>();
                        String substring = extendData.getKey().substring(1, extendData.getKey().length() - 1);
                        List<String> keyList = Arrays.asList(substring.split(","));
                        if (ObjectUtil.isEmpty(extendData.getValue())) {
                            throw new ServiceException("value值不能为空");
                        }
                        List<String> valueList = Arrays.asList(extendData.getValue().split(","));
                        for (int i = 0; i < keyList.size(); i++) {
                            SelectResponse checkSelectResponse = new SelectResponse();
                            checkSelectResponse.setKey(keyList.get(i));
                            checkSelectResponse.setValue(valueList.get(i));
                            selectResponseList.add(checkSelectResponse);
                        }
                        MultiSelectResponse multiSelectResponse = new MultiSelectResponse();
                        multiSelectResponse.setSelectList(selectResponseList);
                        formFieldUse.setFieldsValue(JsonUtil.toJson(selectResponseList));
                        break;
                    case UPLOAD:
                        String substringFile = extendData.getKey();
                        if (StringUtils.isNotBlank(substringFile)) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                List<FileRequest> formFields = objectMapper.readValue(substringFile, new TypeReference<List<FileRequest>>() {
                                });
                                formFieldUse.setFieldsValue(JsonUtil.toJson(formFields));
                            } catch (JsonProcessingException e) {
                            }
                        }
                        break;
                }
                formFieldUseList.add(formFieldUse);
            });
            this.saveBatch(formFieldUseList);
        }
    }


    public FormFieldUse getById(Long id) {
        return this.findByIdThrowErr(id, FormFieldUseErrorCode.NOT_FOUND);
    }
}
