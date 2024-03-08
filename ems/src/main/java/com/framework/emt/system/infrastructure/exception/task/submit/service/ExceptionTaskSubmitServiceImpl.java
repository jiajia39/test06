package com.framework.emt.system.infrastructure.exception.task.submit.service;

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
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.exception.service.ExceptionProcessService;
import com.framework.emt.system.domain.formfield.response.FormFieldListResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.submit.convert.TaskSubmitConvert;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitCreateRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitExportQueryRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitQueryRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitUpdateRequest;
import com.framework.emt.system.domain.task.submit.response.TaskSubmitExportResponse;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.constant.code.ExceptionTaskSubmitErrorCode;
import com.framework.emt.system.infrastructure.exception.task.submit.mapper.ExceptionTaskSubmitMapper;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitDetailResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitResponse;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常任务提报 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskSubmitServiceImpl extends BaseServiceImpl<ExceptionTaskSubmitMapper, ExceptionTaskSubmit> implements ExceptionTaskSubmitService {

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskSettingService exceptionTaskSettingService;

    private final DeptService deptService;

    private final ExceptionItemService exceptionItemService;

    private final ExceptionProcessService exceptionProcessService;

    @Override
    public ExceptionTaskSubmit create(Long userId, TaskSubmitCreateRequest request, Long exceptionProcessId, String exceptionProcessTitle,
                                      Long exceptionCategoryId, Long exceptionTaskId, Integer submitVersion, List<FormFieldResponse> extendDataList) {
        ExceptionTaskSubmit exceptionTaskSubmit = TaskSubmitConvert.INSTANCE.createRequestToEntity(userId, request,
                exceptionProcessId, exceptionProcessTitle, exceptionCategoryId, exceptionTaskId, submitVersion, extendDataList);
        this.create(exceptionTaskSubmit);
        return exceptionTaskSubmit;
    }

    @Override
    public Long copy(ExceptionTaskSubmit sourceSubmit, TaskRejectRequest taskRejectRequest, Integer submitVersion) {
        return this.create(TaskSubmitConvert.INSTANCE.createRequestToEntity(sourceSubmit, taskRejectRequest, submitVersion));
    }

    @Override
    public void update(ExceptionTaskSubmit exceptionTaskSubmit, TaskSubmitUpdateRequest request,
                       ExceptionProcessResponse exceptionProcess, List<FormFieldResponse> extendDataList) {
        this.update(TaskSubmitConvert.INSTANCE.updateRequestToEntity(exceptionTaskSubmit, request, exceptionProcess, extendDataList));
    }

    @Override
    public TaskResponse detail(Long id, Long userId) {
        TaskResponse detailResult = this.baseMapper.detail(id, userId);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.NOT_FOUND);
        }
        exceptionTaskService.loadDetail(detailResult);
        this.loadReportProcessName(detailResult);
        return detailResult;
    }

    @Override
    public IPage<TaskResponse> page(IPage<TaskResponse> page, TaskSubmitQueryRequest request, Long userId) {
        IPage<TaskResponse> pageResult = this.baseMapper.page(page, request, userId, null);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        exceptionTaskService.loadList(taskList);
        return pageResult;
    }

    @Override
    public ExceptionTaskSubmit findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionTaskSubmitErrorCode.NOT_FOUND);
    }

    @Override
    public ExceptionTaskSubmit validateUser(@NonNull Long id, @NonNull Long currentUserId) {
        ExceptionTaskSubmit exceptionTaskSubmit = findByIdThrowErr(id);
        if (!ObjectUtil.equals(currentUserId, exceptionTaskSubmit.getCreateUser())) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.ONLY_CREATE_USER_CAN_DELETE_UPDATE_CLOSE_OR_SUBMIT);
        }
        return exceptionTaskSubmit;
    }

    @Override
    public List<TaskSubmitExportResponse> findExportData(TaskSubmitExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskSubmitExportResponse> exportResult = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isEmpty(exportResult)) {
            return exportResult;
        }
        exportResult.forEach(item -> {
            item.init();
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
                if (!request.getIsShowExtendField().contains(NumberConstant.THREE)) {
                    item.setHandingExtendField(null);
                } else {
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
                } else {

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
            }
        });
        return DataHandleUtils.loadExportResult(exportResult);
    }

    @Override
    public ExceptionTaskSubmit findUniqueInfo(Long exceptionTaskId, Integer submitVersion) {
        LambdaQueryWrapper<ExceptionTaskSubmit> wrapper = new LambdaQueryWrapper<ExceptionTaskSubmit>()
                .eq(ExceptionTaskSubmit::getExceptionTaskId, exceptionTaskId)
                .eq(ExceptionTaskSubmit::getSubmitVersion, submitVersion);
        ExceptionTaskSubmit exceptionTaskSubmit = getOne(wrapper);
        if (exceptionTaskSubmit == null) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.NOT_FOUND);
        }
        return exceptionTaskSubmit;
    }

    @Override
    public List<StatisticsProportionResponse> submitReject(StatisticsTimeQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.submitReject(queryRequest, deptIds);
    }

    @Override
    public TaskSubmitDetailResponse findDetailById(Long id) {
        return this.baseMapper.findDetailById(id);
    }

    @Override
    public List<Long> findById(Long itemId, Long categoryId, Long workSpaceId, Long processId) {
        LambdaQueryWrapper<ExceptionTaskSubmit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ExceptionTaskSubmit::getId, ExceptionTaskSubmit::getExceptionTaskId);
        if (itemId != null) {
            queryWrapper.eq(ExceptionTaskSubmit::getExceptionItemId, itemId);
        }
        if (categoryId != null) {
            queryWrapper.eq(ExceptionTaskSubmit::getExceptionCategoryId, categoryId);
        }
        if (workSpaceId != null) {
            queryWrapper.eq(ExceptionTaskSubmit::getWorkspaceLocationId, workSpaceId);
        }
        if (processId != null) {
            queryWrapper.eq(ExceptionTaskSubmit::getExceptionProcessId, processId);
        }
        return this.list(queryWrapper).stream().map(ExceptionTaskSubmit::getExceptionTaskId).collect(Collectors.toList());
    }

    @Override
    public List<ExceptionItemResponse> processOfItems(Long processId) {
        ExceptionProcess exceptionProcess = exceptionProcessService.findByIdThrowErr(processId);
        return exceptionItemService.findItemsByCategoryId(exceptionProcess.getExceptionCategoryId());
    }

    /**
     * 装载响应超时上报流程名称和处理超时上报流程名称
     *
     * @param detail 异常提报详情
     * @return
     */
    private void loadReportProcessName(TaskResponse detail) {
        ExceptionTaskSetting taskSetting = exceptionTaskSettingService.findByTaskId(detail.getTaskId());
        TaskSubmitResponse submitResponse = detail.getSubmit();
        if (taskSetting != null && submitResponse != null) {
            submitResponse.setResponseReportProcessId(taskSetting.getResponseReportNoticeProcessId());
            submitResponse.setResponseReportProcessName(taskSetting.getResponseReportNoticeProcessName());
            submitResponse.setHandingReportProcessId(taskSetting.getHandingReportNoticeProcessId());
            submitResponse.setHandingReportProcessName(taskSetting.getHandingReportNoticeProcessName());
        }
    }


}
