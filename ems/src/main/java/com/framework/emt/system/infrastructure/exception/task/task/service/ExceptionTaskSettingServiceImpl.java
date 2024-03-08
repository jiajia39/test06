package com.framework.emt.system.infrastructure.exception.task.task.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.json.utils.JsonUtil;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionProcessErrorCode;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.task.task.convert.TaskSettingConvert;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.domain.task.task.response.TaskSettingResponse;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.constant.code.ExceptionTaskSubmitErrorCode;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.constant.code.ExceptionTaskErrorCode;
import com.framework.emt.system.infrastructure.exception.task.task.mapper.ExceptionTaskSettingMapper;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 异常任务配置 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskSettingServiceImpl extends BaseServiceImpl<ExceptionTaskSettingMapper, ExceptionTaskSetting> implements ExceptionTaskSettingService {

    private final UserService userService;

    @Override
    public ExceptionTaskSetting create(ExceptionProcessResponse exceptionProcess, List<TagInfo> reasonItems) {
        ExceptionTaskSetting exceptionTaskSetting = TaskSettingConvert.INSTANCE.createRequestToEntity(exceptionProcess, reasonItems);
        create(exceptionTaskSetting);
        return exceptionTaskSetting;
    }

    @Override
    public Long update(ExceptionProcessResponse exceptionProcess, Long id, List<TagInfo> reasonItems) {
        return this.update(TaskSettingConvert.INSTANCE.updateRequestToEntity(exceptionProcess, id, reasonItems));
    }

    @Override
    public ExceptionTaskSetting info(Long exceptionSettingId) {
        return findByIdThrowErr(exceptionSettingId, ExceptionTaskErrorCode.SETTING_NOT_FOUND);
    }


    @Override
    public List<FormFieldResponse> validateExtendFields(List<FormFieldResponse> formList, List<ExtendFieldsRequest> extendDataList) {
        if (CollectionUtil.isEmpty(formList)) {
            // 附加字段未配置 无需校验
            return Collections.emptyList();
        }
        if (CollectionUtil.isNotEmpty(formList) && CollectionUtil.isEmpty(extendDataList)) {
            throw new ServiceException(ExceptionTaskErrorCode.ADDITIONAL_FIELDS_ARE_MISSING_PLEASE_CHECK);
        }
        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
        };
        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(formList), type);
        List<String> forms = formFields.stream().map(FormFieldResponse::getProp).collect(Collectors.toList());
        List<String> extendDatas = extendDataList.stream().map(ExtendFieldsRequest::getProp).collect(Collectors.toList());
        // 附加字段不能为空
        boolean isEqual = CollectionUtils.isEqualCollection(forms, extendDatas);
        if (!isEqual) {
            throw new ServiceException(ExceptionTaskErrorCode.ADDITIONAL_FIELDS_ARE_MISSING_PLEASE_CHECK);
        }
        formFields.forEach(formFieldResponse -> {
            extendDataList.forEach(extendFieldsRequest -> {
                if (formFieldResponse.getProp().equals(extendFieldsRequest.getProp())) {
                    String key = extendFieldsRequest.getKey() == null ? "" : extendFieldsRequest.getKey();
                    String value = extendFieldsRequest.getValue() == null ? "" : extendFieldsRequest.getValue();
                    formFieldResponse.validate(key, value);
                }
            });
        });
        return formFields;
    }

    @Override
    public TaskSettingResponse getTaskById(Long id) {
        return this.baseMapper.getTaskById(id);
    }

    @Override
    public List<Long> validateResponseUser(ExceptionTaskSetting exceptionTaskSetting, Long responseUserId) {
        // 不指定人员
        if (ResponseModeEnum.NOT_SPECIFIED.equals(exceptionTaskSetting.getResponseMode())) {
            if (responseUserId == null) {
                throw new ServiceException(ExceptionTaskSubmitErrorCode.RESPONDENT_ID_CANNOT_BE_EMPTY);
            }
            userService.findUserByIdThrowErr(responseUserId, ExceptionProcessErrorCode.RESPONSE_USER_NOT_FOUND);
            return Lists.newArrayList(responseUserId);
        }
        // 固定人员 或多个人员
        List<Long> responseUserIds = exceptionTaskSetting.getResponseUserIds();
        if (ObjectUtil.isEmpty(responseUserIds)) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.RESPONDENT_ID_CANNOT_BE_EMPTY);
        }
        return responseUserIds;
    }

    @Override
    public List<Long> validateHandingUser(@NonNull ExceptionTaskSetting exceptionTaskSetting, Long userId, Long handingUserId) {
        List<Long> handingUserIds = exceptionTaskSetting.getHandingUserIds();
        switch (exceptionTaskSetting.getHandingMode()) {
            case NOT_SPECIFIED:
                if (handingUserId == null) {
                    throw new ServiceException(ExceptionTaskErrorCode.RESPONSE_HANDLER_CANNOT_BE_EMPTY);
                }
                userService.findUserByIdThrowErr(handingUserId, ExceptionProcessErrorCode.RESPONSE_USER_NOT_FOUND);
                return Lists.newArrayList(handingUserId);
            case FIXED_PERSONNEL:
            case MULTIPLE_PERSONNEL:
                if (ObjectUtil.isEmpty(handingUserIds)) {
                    throw new ServiceException(ExceptionTaskErrorCode.RESPONSE_HANDLER_CANNOT_BE_EMPTY);
                }
                return handingUserIds;
            case RESPONSE_AND_HANDING:
                return Lists.newArrayList(userId);
            default:
                return null;
        }
    }

    @Override
    public List<Long> validateCheckUser(ExceptionTaskSetting setting, List<Long> checkUserIds, ExceptionTaskSubmit exceptionTaskSubmit) {
        CheckModeEnum checkModeEnum = CheckModeEnum.getEnum(setting.getCheckMode().getCode());
        switch (checkModeEnum) {
            case NOT_SPECIFIED:
                if (ObjectUtil.isEmpty(checkUserIds)) {
                    throw new ServiceException(ExceptionTaskErrorCode.CHECK_USER_LIST_NOT_NULL);
                }
                userService.checkUserExist(checkUserIds, ExceptionTaskErrorCode.CHECK_USER_PART_NOT_EXIST);
                return checkUserIds;
            case FIXED_PERSONNEL:
            case MULTIPLE_PERSONNEL:
                return setting.getCheckUserIds();
            case REPORT_AND_CHECK:
            case REPORT_AND_CHECK_MULTIPLE_PEOPLE:
                List<Long> ids = new ArrayList<>();
                ids.add(exceptionTaskSubmit.getSubmitUserId());
                ids.addAll(setting.getCheckUserIds());
                return ids;
            default:
                return null;
        }
    }

    @Override
    public SettingCheckResponse findCheckDataById(Long id) {
        // 查询异常任务配置下的验收数据
        SettingCheckResponse settingCheckData = this.baseMapper.findCheckDataById(id);
        if (ObjectUtil.isNull(settingCheckData)) {
            throw new ServiceException(ExceptionTaskErrorCode.CHECK_DATA_NOT_FOUND);
        }
        // 装载验收人id和姓名列表
        Map<Long, String> userInfoMap = userService.findIdOfNameMap(settingCheckData.getCheckUserIds());
        if (CollectionUtil.isNotEmpty(userInfoMap)) {
            List<UserInfoResponse> userInfoList = settingCheckData.getCheckUserIds().stream()
                    .filter(userInfoMap::containsKey)
                    .map(userId -> new UserInfoResponse(userId, userInfoMap.get(userId)))
                    .collect(Collectors.toList());
            settingCheckData.setCheckUserInfoList(userInfoList);
        }
        return settingCheckData;
    }

    @Override
    public SettingHandingResponse findCheckHandingById(Long id) {
        // 查询异常任务配置下的处理数据
        SettingHandingResponse settingHandingData = this.baseMapper.findCheckHandingById(id);
        if (ObjectUtil.isNull(settingHandingData)) {
            throw new ServiceException(ExceptionTaskErrorCode.HANDING_DATA_NOT_FOUND);
        }
        // 装载处理人id和姓名列表
        Map<Long, String> userInfoMap = userService.findIdOfNameMap(settingHandingData.getHandingUserIds());
        if (CollectionUtil.isNotEmpty(userInfoMap)) {
            List<UserInfoResponse> userInfoList = settingHandingData.getHandingUserIds().stream()
                    .filter(userInfoMap::containsKey)
                    .map(userId -> new UserInfoResponse(userId, userInfoMap.get(userId)))
                    .collect(Collectors.toList());
            settingHandingData.setHandingUserInfoList(userInfoList);
        }
        return settingHandingData;
    }

    @Override
    public ExceptionTaskSetting findByTaskId(Long taskId) {
        return this.baseMapper.findByTaskId(taskId);
    }

    @Override
    public ExceptionTaskSetting findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionTaskErrorCode.SETTING_NOT_FOUND);
    }

}
