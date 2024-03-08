package com.framework.emt.system.infrastructure.exception.task.cooperation.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.response.FormFieldListResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationExportQueryRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationQueryRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;
import com.framework.emt.system.domain.task.cooperation.response.TaskCooperationExportResponse;
import com.framework.emt.system.domain.task.handing.request.HandingCooperationQueryRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.code.ExceptionTaskCooperationErrorCode;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.mapper.ExceptionTaskCooperationMapper;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 异常任务协同 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskCooperationServiceImpl extends BaseServiceImpl<ExceptionTaskCooperationMapper, ExceptionTaskCooperation> implements ExceptionTaskCooperationService {

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    @Override
    public ExceptionTaskCooperation create(TaskCooperationCreateRequest request, ExceptionTaskHanding exceptionTaskHanding,
                                           LocalDateTime currentTime, String reportNoticeProcessName, List<FormFieldResponse> cooperateExtendFields) {
        ExceptionTaskCooperation exceptionTaskCooperation = new ExceptionTaskCooperation();
        this.create(exceptionTaskCooperation.createInit(request, exceptionTaskHanding, currentTime, reportNoticeProcessName, cooperateExtendFields));
        return exceptionTaskCooperation;
    }

    @Override
    public CooperationStatusNumResponse statistics(Long currentUserId) {
        return this.baseMapper.statisticsStatus(currentUserId);
    }

    @Override
    public TaskResponse detail(Long id, Integer version) {
        TaskResponse detailResult = this.baseMapper.detail(id, version);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.NOT_FOUND);
        }
        exceptionTaskService.loadDetail(detailResult);
        return detailResult;
    }

    @Override
    public IPage<TaskResponse> page(IPage<TaskResponse> page, TaskCooperationQueryRequest request, Long userId) {
        IPage<TaskResponse> pageResult = this.baseMapper.page(page, request, userId);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        exceptionTaskService.loadList(taskList);
        return pageResult;
    }

    @Override
    public IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query) {
        Integer timeOutType = TimeOutType.COOPERATION_REPORT.getCode();
        return exceptionTaskScheduleService.timeOutReportPage(timeOutType, taskId, query);
    }

    @Override
    public IPage<TaskResponse> handingCooperationPage(IPage<TaskResponse> page, HandingCooperationQueryRequest request, Long userId) {
        IPage<TaskResponse> pageResult = baseMapper.handingCooperationPage(page, request, userId);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        exceptionTaskService.loadList(taskList);
        return pageResult;
    }

    @Override
    public List<TaskCooperationExportResponse> findExportData(TaskCooperationExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskCooperationExportResponse> exportResult = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isEmpty(exportResult)) {
            return exportResult;
        }
        Date now = new Date();
        exportResult.forEach(item -> {
            item.init(now);
            if (ObjectUtil.isNotEmpty(request.getIsShowExtendField()) && request.getIsShowExtendField().size() > 0) {
                if (!request.getIsShowExtendField().contains(NumberConstant.ONE)) {
                    item.setSubmitExtendField(null);
                }else {
                    List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                    if (ObjectUtil.isNotEmpty(item.getSubmitExtendField()) && item.getSubmitExtendField().size() > 0) {
                        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                        };
                        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getSubmitExtendField()), type);
                        formFields.forEach(responseExtendField -> {
                            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                            formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                        });

                    }
                    item.setSubmitExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.TWO)) {
                    item.setResponseExtendField(null);
                }else {
                    List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                    if (ObjectUtil.isNotEmpty(item.getResponseExtendField()) && item.getResponseExtendField().size() > 0) {
                        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                        };
                        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getResponseExtendField()), type);
                        formFields.forEach(responseExtendField -> {
                            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                            formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                        });

                    }
                    item.setResponseExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.THREE)) {
                    item.setHandingExtendField(null);
                }else {
                    List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                    if (ObjectUtil.isNotEmpty(item.getHandingExtendField()) && item.getHandingExtendField().size() > 0) {
                        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                        };
                        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getHandingExtendField()), type);
                        formFields.forEach(responseExtendField -> {
                            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                            formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                        });

                    }
                    item.setHandingExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.SIX)) {
                    item.setSuspendExtendField(null);
                }else {

                    List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                    if (ObjectUtil.isNotEmpty(item.getSuspendExtendField()) && item.getSuspendExtendField().size() > 0) {
                        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                        };
                        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getSuspendExtendField()), type);
                        formFields.forEach(responseExtendField -> {
                            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                            formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                        });

                    }
                    item.setSuspendExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.FOUR)) {
                    item.setCooperationExtendField(null);
                }else {

                    List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                    if (ObjectUtil.isNotEmpty(item.getCooperationExtendField()) && item.getCooperationExtendField().size() > 0) {
                        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                        };
                        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getCooperationExtendField()), type);
                        formFields.forEach(responseExtendField -> {
                            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                            formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                        });

                    }
                    item.setCooperationExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                }
            }
        });
        return DataHandleUtils.loadUserName(exportResult);

    }

    @Override
    public ExceptionTaskCooperation findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionTaskCooperationErrorCode.NOT_FOUND);
    }

    @Override
    public ExceptionTaskCooperation validateStatusAccept(@NonNull Long id, Long currentUserId) {
        ExceptionTaskCooperation exceptionTaskCooperation = this.findByIdThrowErr(id);
        if (!CooperationStatus.WAIT_COOPERATION.equals(exceptionTaskCooperation.getCooperationStatus())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.THE_COOPERATION_INFORMATION_STATUS_MUST_BE_WAIT_COOPERATION);
        }
        //  校验当前用户是否和异常协同人相同
        if (currentUserId != null && !currentUserId.equals(exceptionTaskCooperation.getUserId())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.YOU_NOT_IS_THIS_COOPERATION_INFORMATION_OF_COOPERATION_USER);
        }
        return exceptionTaskCooperation;
    }

    @Override
    public ExceptionTaskCooperation validateStatusTransfer(@NonNull Long id, Long transferUserId, Long currentUserId) {
        ExceptionTaskCooperation exceptionTaskCooperation = this.findByIdThrowErr(id);
        if (!CooperationStatus.WAIT_COOPERATION.equals(exceptionTaskCooperation.getCooperationStatus())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.THE_COOPERATION_INFORMATION_STATUS_MUST_BE_WAIT_COOPERATION);
        }
        // 校验当前用户是否和异常协同人相同
        if (currentUserId != null && !currentUserId.equals(exceptionTaskCooperation.getUserId())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.YOU_NOT_IS_THIS_COOPERATION_INFORMATION_OF_COOPERATION_USER);
        }
        // 校验转派人不能是自己
        if (ObjectUtil.equals(transferUserId, currentUserId)) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.COOPERATION_TASK_CAN_NOT_TRANSFER_FOR_ONESELF);
        }
        return exceptionTaskCooperation;
    }

    @Override
    public ExceptionTaskCooperation validateSubmit(@NonNull Long id, Long currentUserId) {
        ExceptionTaskCooperation taskCooperation = this.findByIdThrowErr(id);
        if (!CooperationStatus.COOPERATION_ING.equals(taskCooperation.getCooperationStatus())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.THE_COOPERATION_INFORMATION_STATUS_MUST_BE_COOPERATION_ING);
        }
        // 校验当前用户是否和异常协同人相同
        if (currentUserId != null && !currentUserId.equals(taskCooperation.getUserId())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.YOU_NOT_IS_THIS_COOPERATION_INFORMATION_OF_COOPERATION_USER);
        }
        return taskCooperation;
    }

    @Override
    public Long count(@NonNull Long exceptionTaskHandingId, @NonNull Integer handingVersion, CooperationStatus status) {
        LambdaQueryWrapper<ExceptionTaskCooperation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExceptionTaskCooperation::getExceptionTaskHandingId, exceptionTaskHandingId);
        queryWrapper.eq(ExceptionTaskCooperation::getHandingVersion, handingVersion);
        queryWrapper.ne(ExceptionTaskCooperation::getCooperationStatus, CooperationStatus.ALREADY_REVOKED);
        if (ObjectUtil.isNotNull(status)) {
            queryWrapper.eq(ExceptionTaskCooperation::getCooperationStatus, status);
        }
        return this.count(queryWrapper);
    }

    @Override
    public void validExceedMaxVal(Long handingId) {
        long count = this.count(new LambdaQueryWrapper<ExceptionTaskCooperation>().eq(ExceptionTaskCooperation::getExceptionTaskHandingId, handingId));
        Integer cooperationMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.HANDING_USER_SUBMIT_COOPERATION_TASK_MAX_COUNT, AuthUtil.getTenantId()), ExceptionTaskConstant.HANDING_USER_SUBMIT_COOPERATION_TASK_DEFAULT_MAX_COUNT);
        if (count >= cooperationMaxCount) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.COOPERATION_TASK_CAN_NOT_TRANSFER_FOR_ONESELF);
        }
    }

    @Override
    public ExceptionTaskCooperation validateStatusWaitCooperation(Long id) {
        ExceptionTaskCooperation exceptionTaskCooperation = this.findByIdThrowErr(id);
        if (!CooperationStatus.WAIT_COOPERATION.equals(exceptionTaskCooperation.getCooperationStatus())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.THE_COOPERATION_INFORMATION_STATUS_MUST_BE_WAIT_COOPERATION);
        }
        return exceptionTaskCooperation;
    }

    @Override
    public ExceptionTaskCooperation validateStatusCancel(Long id) {
        ExceptionTaskCooperation exceptionTaskCooperation = this.findByIdThrowErr(id);
        if (!CooperationStatus.ALREADY_REVOKED.equals(exceptionTaskCooperation.getCooperationStatus())) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.COLLABORATION_INFORMATION_STATUS_MUST_BE_REVOKED);
        }
        return exceptionTaskCooperation;
    }

    @Override
    public  List<ExceptionTaskCooperation> updateStatus(Long taskId, Integer HandingVersion) {
        LambdaQueryWrapper<ExceptionTaskCooperation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExceptionTaskCooperation::getExceptionTaskId, taskId);
        queryWrapper.eq(ExceptionTaskCooperation::getHandingVersion, HandingVersion);
        List<ExceptionTaskCooperation> cooperationList = this.list(queryWrapper);
        if (cooperationList.size() > 0) {
            cooperationList.forEach(cooperation -> {
                cooperation.updateStatus();
            });
        }
        this.updateBatchById(cooperationList);
        return cooperationList;
    }

    @Override
    public void validIsExist(Long exceptionTaskHandingId, Long planUserId) {
        LambdaQueryWrapper<ExceptionTaskCooperation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExceptionTaskCooperation::getExceptionTaskHandingId, exceptionTaskHandingId);
        queryWrapper.eq(ExceptionTaskCooperation::getUserId, planUserId);
        queryWrapper.ne(ExceptionTaskCooperation::getCooperationStatus, CooperationStatus.ALREADY_REVOKED);
        List<ExceptionTaskCooperation> list = this.list(queryWrapper);
        if (ObjectUtil.isNotEmpty(list) && list.size() > 0) {
            throw new ServiceException(ExceptionTaskCooperationErrorCode.THE_COLLABORATIVE_TASK_OF_THE_PLAN_COLLABORATOR_ALREADY_EXISTS);
        }
    }
}
