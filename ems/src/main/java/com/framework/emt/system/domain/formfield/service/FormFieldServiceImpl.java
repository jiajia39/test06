package com.framework.emt.system.domain.formfield.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.admin.system.dao.IDictBizDao;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.DictBiz;
import com.framework.admin.system.entity.User;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.json.utils.JsonUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.service.ExceptionProcessService;
import com.framework.emt.system.domain.formfield.FormField;
import com.framework.emt.system.domain.formfield.constant.code.FormFieldErrorCode;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.formfield.convert.FormFieldConvert;
import com.framework.emt.system.domain.formfield.mapper.FormFieldMapper;
import com.framework.emt.system.domain.formfield.request.*;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常表单字段表 实现类
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Service
@RequiredArgsConstructor
public class FormFieldServiceImpl extends BaseServiceImpl<FormFieldMapper, FormField> implements FormFieldService {

    private final IDictBizDao dictDao;

    private final IUserDao userDao;

    @Override
    public Long create(FormFieldCreateRequest request) {
        checkCodeUnique(null, request.getCode(), null);
        checkCodeUnique(null, null, request.getProp());
        validIsNull(request);
        FormField formField = FormFieldConvert.INSTANCE.createRequestToEntity(request);
        if (ObjectUtil.isNotEmpty(request.getSelectListInfo())) {
            List<SelectListRequest> selectListRequests = extracted(request.getSelectListInfo());
            formField.setSelectList(JsonUtil.toJson(selectListRequests));
        }
        formField.setFieldsVersion(1);
        formField.setIsLastVersion(1);
        return this.create(formField);
    }

    private static List<SelectListRequest> extracted(List<String> selectListInfo) {
        List<SelectListRequest> selectListRequests = new ArrayList<>();
        for (int i = 1; i <= selectListInfo.size(); i++) {
            SelectListRequest selectListRequest = new SelectListRequest();
            selectListRequest.setKey(i);
            selectListRequest.setValue(selectListInfo.get(i - 1));
            selectListRequests.add(selectListRequest);
        }
        return selectListRequests;
    }

    /**
     * 验证参数中选择某些类型后，对应的某个字段不能为空
     *
     * @param request 参数
     */
    private void validIsNull(FormFieldCreateRequest request) {
        valid(request.getType(), request.getSelectListInfo(), request.getSliderMin(), request.getSliderMax(), request.getFileMaxNum(), request.getFileMaxSize(), request.getSubtype());
    }

    @Override
    public void delete(Long id) {
        FormField formField = findById(id, FormFieldErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        if (ObjectUtil.equal(formField.getStatus(), 1)) {
            throw new ServiceException(FormFieldErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        ExceptionProcessService processService = SpringUtil.getBean(ExceptionProcessService.class);
        List<FormField> formFieldList = this.list(new LambdaQueryWrapper<FormField>().eq(FormField::getCode, formField.getCode()));
        List<Long> formFieldIds = formFieldList.stream().map(FormField::getId).collect(Collectors.toList());
        Integer formFieldNumber = processService.findByFormField(formFieldIds);
        if (formFieldNumber > 0) {
            throw new ServiceException(FormFieldErrorCode.THIS_FORM_HAS_BEEN_REFERENCED_AND_CANNOT_BE_DELETED);
        }
        this.removeBatchByIds(formFieldIds);
    }

    @Override
    @DSTransactional
    public Long update(Long id, FormFieldUpdateRequest request) {
        FormField formField = findById(id, FormFieldErrorCode.THE_FORM_DOES_NOT_EXIST);
        checkCodeUnique(id, null, request.getProp());
        //更新 是否最后版本 = 否
        updateIsLastVersion(formField.getCode());
        //获取最新版本号
        LambdaQueryWrapper<FormField> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FormField::getCode, formField.getCode());
        Integer count = Convert.toInt(this.count(lambdaQueryWrapper));
        validIsNull(request);
        FormField formFieldUpdate = FormFieldConvert.INSTANCE.updateRequestToEntity(request);
        formFieldUpdate.setCode(formField.getCode());
        formFieldUpdate.setIsLastVersion(1);
        formFieldUpdate.setFieldsVersion(count + 1);
        formFieldUpdate.setCreateTime(formField.getCreateTime());
        if (ObjectUtil.isNotEmpty(request.getSelectListInfo())) {
            List<SelectListRequest> selectListRequests = extracted(request.getSelectListInfo());
            formFieldUpdate.setSelectList(JsonUtil.toJson(selectListRequests));
        }
        return this.create(formFieldUpdate);
    }

    public void updateIsLastVersion(String code) {
        LambdaUpdateWrapper<FormField> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(FormField::getCode, code);
        lambdaQueryWrapper.set(FormField::getIsLastVersion, 0);
        this.update(lambdaQueryWrapper);
    }

    /**
     * 验证参数中选择某些类型后，对应的某个字段不能为空
     *
     * @param request 参数
     */
    private void validIsNull(FormFieldUpdateRequest request) {
        valid(request.getType(), request.getSelectListInfo(), request.getSliderMin(), request.getSliderMax(), request.getFileMaxNum(), request.getFileMaxSize(), request.getSubtype());
    }

    /**
     * 验证参数中选择某些类型后，对应的某个字段不能为空
     *
     * @param type           字段类型
     * @param selectListInfo 选择项
     * @param sliderMin      滑块最小值
     * @param sliderMax      滑块最大值
     * @param fileMaxNum     文件最大数目
     * @param fileMaxSize    文件大小限制
     * @param subtype        子类型
     */
    private void valid(Integer type, List<String> selectListInfo, BigDecimal sliderMin, BigDecimal sliderMax, Integer fileMaxNum, Integer fileMaxSize, Integer subtype) {
        if (ObjectUtil.equal(type, FormFieldTypeEnum.CHECKBOX.getCode()) || ObjectUtil.equal(type, FormFieldTypeEnum.SELECT.getCode()) || ObjectUtil.equal(type, FormFieldTypeEnum.RADIO.getCode())) {
            if (ObjectUtil.isEmpty(selectListInfo) || selectListInfo.size() < 2) {
                throw new ServiceException(FormFieldErrorCode.THE_SELECTION_BOX_OPTION_CANNOT_BE_EMPTY_AND_MUST_HAVE_AT_LEAST_TWO_OPTIONS);
            }
        }
        if (ObjectUtil.equal(type, FormFieldTypeEnum.SLIDER.getCode())) {
            if (ObjectUtil.isNull(sliderMin) || ObjectUtil.isNull(sliderMax)) {
                throw new ServiceException(FormFieldErrorCode.THE_MAXIMUM_AND_MINIMUM_VALUES_OF_THE_SLIDER_CANNOT_BE_EMPTY);
            }
        }
        if (ObjectUtil.equal(type, FormFieldTypeEnum.UPLOAD.getCode())) {
            if (ObjectUtil.isNull(fileMaxNum) || ObjectUtil.isNull(fileMaxSize)) {
                throw new ServiceException(FormFieldErrorCode.THE_MAXIMUM_NUMBER_OF_FILES_AND_FILE_SIZE_LIMIT_CANNOT_BE_EMPTY);
            }
        }
        if (ObjectUtil.equal(type, FormFieldTypeEnum.UPLOAD.getCode()) || ObjectUtil.equal(type, FormFieldTypeEnum.SELECT.getCode()) || ObjectUtil.equal(type, FormFieldTypeEnum.INPUT.getCode())) {
            if (ObjectUtil.isNull(subtype)) {
                throw new ServiceException(FormFieldErrorCode.THE_SUB_TYPE_OF_A_PARAGRAPH_CANNOT_BE_EMPTY);
            }
        }
    }

    @Override
    public FormFieldResponse detail(Long id, boolean carryDictData) throws JsonProcessingException {
        FormField formField = findById(id, FormFieldErrorCode.THE_FORM_DOES_NOT_EXIST);
        FormFieldResponse formFieldResponse = FormFieldConvert.INSTANCE.entityToResponse(formField, carryDictData ? dictDao.getList(formField.getProp()) : Lists.newArrayList());
        if (StrUtil.isNotBlank(formFieldResponse.getSelectList())) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<SelectListRequest> formFields = objectMapper.readValue(formFieldResponse.getSelectList(), new TypeReference<List<SelectListRequest>>() {
            });
            formFieldResponse.setSelectListInfo(formFields);
        }
        formFieldResponse.load();
        loadName(ListUtil.toList(formFieldResponse));
        return formFieldResponse;
    }

    @Override
    public IPage<FormFieldResponse> page(FormFieldQueryRequest request) {
        IPage<FormField> page = Condition.getPage(request);
        LambdaQueryWrapper<FormField> query = new LambdaQueryWrapper<>();
        query.eq(FormField::getIsDeleted, 0);
        query.eq(ObjectUtil.isNotNull(request.getStatus()), FormField::getStatus, request.getStatus());
        query.like(StrUtil.isNotBlank(request.getLabel()), FormField::getLabel, request.getLabel());
        query.like(StrUtil.isNotBlank(request.getProp()), FormField::getProp, request.getProp());
        query.eq(ObjectUtil.isNotNull(request.getCode()), FormField::getCode, request.getCode());
        query.eq(ObjectUtil.isNotNull(request.getType()), FormField::getType, request.getType());
        query.eq(ObjectUtil.isNotNull(request.getBusinessType()), FormField::getBusinessType, request.getBusinessType());
        query.eq(FormField::getIsLastVersion, 1);
        query.orderByDesc(FormField::getSort);
        query.orderByDesc(FormField::getCreateTime);
        page = this.page(page, query);
        IPage<FormFieldResponse> result = FormFieldConvert.INSTANCE.pageVo(page);
        loadName(result.getRecords());
        loadSelectList(result.getRecords());
        if (request.isCarryDictData()) {
            if (ObjectUtil.isNotEmpty(result.getRecords())) {
                result.getRecords().forEach(formFieldResponse -> {
                    if (!ObjectUtil.equal(formFieldResponse.getType(), 0) && !ObjectUtil.equal(formFieldResponse.getType(), 2)) {
                        List<DictBiz> dictBizList = dictDao.getList(formFieldResponse.getProp());
                        formFieldResponse.setDictList(dictBizList);
                    }
                });
            }
        }
        return result;
    }

    private static void loadSelectList(List<FormFieldResponse> result) {
        result.forEach(formFieldResponse -> {
            formFieldResponse.load();
            if (StrUtil.isNotBlank(formFieldResponse.getSelectList())) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<SelectListRequest> formFields;
                try {
                    formFields = objectMapper.readValue(formFieldResponse.getSelectList(), new TypeReference<List<SelectListRequest>>() {
                    });
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                formFieldResponse.setSelectListInfo(formFields);
            }
        });
    }

    public void loadName(List<FormFieldResponse> result) {
        TypeReference<String> type = new TypeReference<String>() {
        };
        userDao.loadData(result, "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(result, "updateUser", User::getId, "updateUserName", "name", type);
    }

    @Override
    public List<FormFieldResponse> list(FormFieldListQueryRequest request) {
        checkIDExist(request.getIdList());
        List<FormFieldResponse> formFieldResponses = this.baseMapper.formFieldList(request);
        formFieldResponses.forEach(FormFieldResponse::load);
        loadName(formFieldResponses);
        return formFieldResponses;
    }

    @Override
    public Long updateStatus(Long id, FormFieldUpdateRequest request) {
        FormField formField = findById(id, FormFieldErrorCode.THE_FORM_DOES_NOT_EXIST);
        formField.setStatus(request.getStatus());
        return this.update(formField);
    }

    /**
     * 检测表单列表中的id是否存在
     *
     * @param list id列表
     */
    private void checkIDExist(List<Long> list) {
        long count = this.count(new LambdaQueryWrapper<FormField>().in(FormField::getId, list));
        if (count != list.size()) {
            throw new ServiceException(FormFieldErrorCode.THE_LIST_OF_ABNORMAL_FORM_IDS_CONTAINS_IDS_THAT_DO_NOT_EXIST_IN_THE_FORM);
        }
    }

    /**
     * 根据id查询此条知识库
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return 表单信息
     */
    private FormField findById(Long id, IResultCode errorMessage) {
        return this.findByIdThrowErr(id, ObjectUtil.isNull(errorMessage) ? FormFieldErrorCode.FORM_FIELD_INFO_NOT_FIND : errorMessage);
    }

    /**
     * 校验表单编号在数据库中不能存在
     *
     * @param id   主键id
     * @param code 知识库编号
     */
    private void checkCodeUnique(Long id, String code, String prop) {
        LambdaQueryWrapper<FormField> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(FormField::getId, id);
        }
        if (StringUtils.isNotBlank(prop)) {
            queryWrapper.eq(FormField::getProp, prop);
        }
        queryWrapper.eq(FormField::getCode, code);
        if (this.count(queryWrapper) > 0L) {
            throw new ServiceException(FormFieldErrorCode.FORM_FIELD_CODE_EXIST);
        }
    }
}
