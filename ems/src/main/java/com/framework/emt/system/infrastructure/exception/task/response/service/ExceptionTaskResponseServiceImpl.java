package com.framework.emt.system.infrastructure.exception.task.response.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.formfield.response.FormFieldListResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import com.framework.emt.system.domain.task.response.convert.TaskResponseConvert;
import com.framework.emt.system.domain.task.response.request.TaskResponseExportQueryRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseQueryRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseUpdateRequest;
import com.framework.emt.system.domain.task.response.response.ResponseStatusNumResponse;
import com.framework.emt.system.domain.task.response.response.TaskResponseExportResponse;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.response.constant.code.TaskResponseErrorCode;
import com.framework.emt.system.infrastructure.exception.task.response.mapper.ExceptionTaskResponseMapper;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.constant.code.ExceptionTaskSubmitErrorCode;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常任务响应表 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskResponseServiceImpl extends BaseServiceImpl<ExceptionTaskResponseMapper, ExceptionTaskResponse> implements ExceptionTaskResponseService {

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskSettingService settingService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    private final IFormFieldUseService formFieldUseService;
    @Override
    public IPage<TaskResponse> page(IPage<TaskResponse> page, Long currentUserId, TaskResponseQueryRequest request) {
        IPage<TaskResponse> responsePage = baseMapper.page(page, currentUserId, request);
        List<TaskResponse> taskList = responsePage.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return responsePage;
        }
        exceptionTaskService.loadList(taskList);
        return responsePage;
    }

    @Override
    public IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query) {
        Integer timeOutType = TimeOutType.RESPONSE_REPORT.getCode();
        return exceptionTaskScheduleService.timeOutReportPage(timeOutType, taskId, query);
    }

    @Override
    public TaskResponse detail(Long id, Long userId) {
        TaskResponse detailResult = this.baseMapper.detail(id, userId);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.NOT_FOUND);
        }
        exceptionTaskService.loadDetail(detailResult);
        return detailResult;
    }

    @Override
    public void create(@NonNull Long exceptionTaskId, @NonNull Integer responseVersion, @NonNull List<Long> planUserIds, List<FormFieldResponse> responseExtendFields) {
        List<ExceptionTaskResponse> exceptionTaskResponseList = new ArrayList<>();
        for (Long planUserId :
                planUserIds) {
            ExceptionTaskResponse exceptionTaskResponse = new ExceptionTaskResponse();
            exceptionTaskResponse.createResponse(exceptionTaskId, responseVersion, planUserId, responseExtendFields);
            exceptionTaskResponseList.add(exceptionTaskResponse);
        }
        this.saveBatch(exceptionTaskResponseList);
    }

    @Override
    public Long updateResponse(ExceptionTaskResponse taskResponse, TaskResponseUpdateRequest request) {
        return this.update(TaskResponseConvert.INSTANCE.updateRequestToEntity(taskResponse, request));
    }

    @Override
    public void deleteResponse(Long taskId, Long id, Integer responseVersion) {
        LambdaQueryWrapper<ExceptionTaskResponse> wrapper = new LambdaQueryWrapper<ExceptionTaskResponse>()
                .ne(ExceptionTaskResponse::getId, id)
                .eq(ExceptionTaskResponse::getExceptionTaskId, taskId)
                .eq(ExceptionTaskResponse::getResponseVersion, responseVersion);
        List<ExceptionTaskResponse> responseList = this.list(wrapper);
        if (ObjectUtil.isNotEmpty(responseList) && responseList.size() > 0) {
            this.removeBatchByIds(responseList);
        }
    }

    @Override
    public List<Long> getRespondent(Long taskId, Long id, Integer responseVersion) {
        LambdaQueryWrapper<ExceptionTaskResponse> wrapper = new LambdaQueryWrapper<ExceptionTaskResponse>()
                .ne(ExceptionTaskResponse::getId, id)
                .eq(ExceptionTaskResponse::getExceptionTaskId, taskId)
                .eq(ExceptionTaskResponse::getResponseVersion, responseVersion);
        return this.list(wrapper).stream().map(ExceptionTaskResponse::getUserId).collect(Collectors.toList());
    }

    @Override
    public ExceptionTaskResponse infoByTask(Long exceptionTaskId, Integer responseVersion) {
        LambdaQueryWrapper<ExceptionTaskResponse> wrapper = new LambdaQueryWrapper<ExceptionTaskResponse>()
                .eq(ExceptionTaskResponse::getExceptionTaskId, exceptionTaskId)
                .eq(ExceptionTaskResponse::getResponseVersion, responseVersion);
        List<ExceptionTaskResponse> exceptionTaskResponseList = list(wrapper);
        if (CollectionUtil.isEmpty(exceptionTaskResponseList)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_FOUND);
        }
        return exceptionTaskResponseList.get(0);
    }

    /**
     * 根据id查询此条异常响应
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    public ExceptionTaskResponse findById(Long id, IResultCode errorMessage) {
        return this.findByIdThrowErr(id, ObjectUtil.isNull(errorMessage) ? TaskResponseErrorCode.TASK_RESPONSE_INFO_NOT_FIND : errorMessage);
    }

    @Override
    public ExceptionTaskResponse getByIdAndUserId(Long id, Long userId) {
        ExceptionTaskResponse taskResponse = findById(id, null);
        if (!ObjectUtil.equal(taskResponse.getUserId(), userId)) {
            throw new ServiceException(TaskResponseErrorCode.THE_RESPONSE_INFORMATION_OF_THE_OPERATOR_DOES_NOT_EXIST);
        }
        return taskResponse;
    }

    @Override
    public ResponseStatusNumResponse statistics(Long userId) {
        return baseMapper.statistics(userId);
    }

    @Override
    public SettingHandingResponse handingInfo(Long taskId) {
        ExceptionTask task = exceptionTaskService.findByIdThrowErr(taskId);
        return settingService.findCheckHandingById(task.getExceptionTaskSettingId());
    }

    @Override
    public Long copy(ExceptionTaskResponse sourceResponse, TaskRejectRequest taskRejectRequest, Integer responseVersion, Integer responseRejectNum) {
        return this.create(TaskResponseConvert.INSTANCE.createRequestToEntity(sourceResponse, taskRejectRequest, responseVersion, responseRejectNum));
    }

    @Override
    public List<TaskResponseExportResponse> findExportData(TaskResponseExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskResponseExportResponse> exportResult = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isNotEmpty(exportResult)) {
            Date now = new Date();
            exportResult.forEach(item -> {
                item.init(now);
                if (ObjectUtil.isNotEmpty(request.getIsShowExtendField()) && request.getIsShowExtendField().size() > 0) {
                    if (!request.getIsShowExtendField().contains(NumberConstant.ONE)) {
                        item.setSubmitExtendField(null);
                    } else {
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
                    } else {
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
                }
            });
            DataHandleUtils.loadExportResult(exportResult);
        }
        return exportResult;
    }

}
