package com.framework.emt.system.infrastructure.exception.task.check.service;

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
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.formfield.response.FormFieldListResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.check.convert.TaskCheckConvert;
import com.framework.emt.system.domain.task.check.request.TaskCheckCreateRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckExportQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckUpdateRequest;
import com.framework.emt.system.domain.task.check.response.CheckStatusNumResponse;
import com.framework.emt.system.domain.task.check.response.TaskCheckExportResponse;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.check.constant.code.TaskCheckErrorCode;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import com.framework.emt.system.infrastructure.exception.task.check.mapper.ExceptionTaskCheckMapper;
import com.framework.emt.system.infrastructure.exception.task.response.constant.code.TaskResponseErrorCode;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常任务验收 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskCheckServiceImpl extends BaseServiceImpl<ExceptionTaskCheckMapper, ExceptionTaskCheck> implements ExceptionTaskCheckService {

    private final ExceptionTaskService exceptionTaskService;

    private final DeptService deptService;


    @Override
    public IPage<TaskResponse> page(IPage<TaskResponse> page, Long userId, TaskCheckQueryRequest request) {
        IPage<TaskResponse> handingPage = baseMapper.page(page, userId, request);
        List<TaskResponse> taskList = handingPage.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return handingPage;
        }
        exceptionTaskService.loadList(taskList);
        return handingPage;
    }

    @Override
    public void createCheck(TaskCheckCreateRequest request) {
        List<Long> userIdList =
                request.getUserIdList().stream()
                        .distinct()
                        .collect(Collectors.toList());
        List<TaskCheckCreateRequest> requestList = new ArrayList<>();
        userIdList.forEach(userId -> {
            TaskCheckCreateRequest createRequest = TaskCheckConvert.INSTANCE.copy(request);
            createRequest.setUserId(userId);
            requestList.add(createRequest);
        });
        this.saveBatch(TaskCheckConvert.INSTANCE.createRequestToEntity(requestList));
    }

    @Override
    public void deleteCheck(Long id) {
        findById(id, TaskResponseErrorCode.NOT_EXIST_DATA_CAN_NOT_DELETE);
        this.deleteById(id);
    }

    @Override
    public Long updateCheck(Long id, TaskCheckUpdateRequest request) {
        ExceptionTaskCheck taskCheck = findById(id, null);
        return this.update(TaskCheckConvert.INSTANCE.updateRequestToEntity(taskCheck, request));
    }

    @Override
    public TaskResponse detail(Long id, Long userId, Integer version) {
        TaskResponse detailResult = this.baseMapper.detail(id, userId, version);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(TaskCheckErrorCode.TASK_CHECK_INFO_NOT_FIND);
        }
        exceptionTaskService.loadDetail(detailResult);
        return detailResult;
    }

    @Override
    public ExceptionTaskCheck findById(Long id, IResultCode errorMessage) {
        return this.findByIdThrowErr(id, ObjectUtil.isNull(errorMessage) ? TaskCheckErrorCode.TASK_CHECK_INFO_NOT_FIND : errorMessage);
    }

    @Override
    public Boolean AllCheckPass(Long exceptionTaskId, Integer checkVersion) {
        long count = this.count(new LambdaQueryWrapper<ExceptionTaskCheck>().eq(ExceptionTaskCheck::getExceptionTaskId, exceptionTaskId).eq(ExceptionTaskCheck::getCheckVersion, checkVersion).ne(ExceptionTaskCheck::getCheckStatus, CheckStatus.CHECK_PASSED.getCode()));
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int checkPassOrNoPassCount(Long exceptionTaskId, Integer checkVersion, Boolean pass) {
        LambdaQueryWrapper<ExceptionTaskCheck> query = new LambdaQueryWrapper<>();
        query.eq(ExceptionTaskCheck::getExceptionTaskId, exceptionTaskId);
        query.eq(ExceptionTaskCheck::getCheckVersion, checkVersion);
        if (pass) {
            query.eq(ExceptionTaskCheck::getCheckStatus, CheckStatus.CHECK_PASSED.getCode());
        } else {
            query.ne(ExceptionTaskCheck::getCheckStatus, CheckStatus.CHECK_PASSED.getCode());
        }
        return (int) this.count(query);
    }

    @Override
    public void setExpire(Long taskId, Long checkId) {
        List<ExceptionTaskCheck> taskChecks = this.list(new LambdaQueryWrapper<ExceptionTaskCheck>().eq(ExceptionTaskCheck::getExceptionTaskId, taskId).ne(ExceptionTaskCheck::getId, checkId));
        for (ExceptionTaskCheck taskCheck : taskChecks) {
            taskCheck.setCheckSubstatus(CheckSubStatus.WAIT_CHECK_EXPIRED);
        }
        this.updateBatchById(taskChecks);
    }

    @Override
    public void deleteByTaskId(Long taskId, Long checkId, Integer version) {
        List<ExceptionTaskCheck> taskChecks = this.list(new LambdaQueryWrapper<ExceptionTaskCheck>().eq(ExceptionTaskCheck::getExceptionTaskId, taskId).ne(ExceptionTaskCheck::getId, checkId).eq(ExceptionTaskCheck::getCheckVersion, version));
        this.removeBatchByIds(taskChecks);
    }


    @Override
    public ExceptionTaskCheck checkUser(Long id, Long userId) {
        ExceptionTaskCheck check = findById(id, null);
        if (!ObjectUtil.equal(check.getUserId(), userId)) {
            throw new ServiceException(TaskCheckErrorCode.THE_INSPECTOR_AND_OPERATOR_DO_NOT_MATCH);
        }
        if (ObjectUtil.equal(check.getCheckSubstatus(), CheckSubStatus.WAIT_CHECK_EXPIRED)) {
            throw new ServiceException(TaskCheckErrorCode.ACCEPTANCE_INFORMATION_MUST_BE_VALID);
        }
        if (!ObjectUtil.equal(check.getCheckStatus(), CheckStatus.WAIT_CHECK) || !ObjectUtil.equal(check.getCheckSubstatus(), CheckSubStatus.WAIT_CHECK)) {
            throw new ServiceException(TaskCheckErrorCode.THE_ACCEPTANCE_INFORMATION_STATUS_MUST_BE_PENDING_ACCEPTANCE);
        }
        return check;
    }

    @Override
    public CheckStatusNumResponse statistics(Long userId) {
        return this.baseMapper.statistics(userId);
    }

    @Override
    public List<StatisticsProportionResponse> checkReject(StatisticsTimeQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.checkReject(queryRequest, deptIds);
    }

    @Override
    public List<TaskCheckExportResponse> findExportData(TaskCheckExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskCheckExportResponse> exportResult = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isEmpty(exportResult)) {
            return exportResult;
        }
        exportResult.forEach(item -> {
                    item.init();
                    if (ObjectUtil.isNotEmpty(request.getIsShowExtendField()) && request.getIsShowExtendField().size() > 0) {
                        if (!request.getIsShowExtendField().contains(NumberConstant.ONE)) {
                            item.setSubmitExtendDatas(null);
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
                            item.setResponseExtendDatas(null);
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
                            item.setHandingExtendDatas(null);
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
                            item.setSuspendExtendDatas(null);
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
                        if (!request.getIsShowExtendField().contains(NumberConstant.FIVE)) {
                            item.setCheckExtendDatas(null);
                        } else {
                            List<FormFieldListResponse> formFieldListResponseList = new ArrayList<>();
                            if (ObjectUtil.isNotEmpty(item.getCheckExtendField()) && item.getCheckExtendField().size() > 0) {
                                TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
                                };
                                List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(item.getCheckExtendField()), type);
                                formFields.forEach(responseExtendField -> {
                                    FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
                                    formFieldListResponseList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
                                });

                            }
                            item.setCheckExtendDatas(JSONUtil.toJsonPrettyStr(formFieldListResponseList));
                        }
                    }
                }
        );
        return DataHandleUtils.loadExportResult(exportResult);
    }

    @Override
    public void updateStatus(Long taskId, Integer checkVersion) {
        LambdaQueryWrapper<ExceptionTaskCheck> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExceptionTaskCheck::getExceptionTaskId, taskId);
        queryWrapper.eq(ExceptionTaskCheck::getCheckVersion, checkVersion);
        List<ExceptionTaskCheck> checkList = this.list(queryWrapper);
        if (checkList.size() > 0) {
            checkList.forEach(check -> {
                check.updateStatus();
            });
        }
        this.updateBatchById(checkList);
    }


    public Map<Long, List<Long>> loadCheckUserId(List<Long> taskIds) {
        if (ObjectUtil.isEmpty(taskIds)) {
            return null;
        }
        LambdaQueryWrapper<ExceptionTaskCheck> lambda = new LambdaQueryWrapper<>();
        lambda.in(ExceptionTaskCheck::getExceptionTaskId, taskIds);
        lambda.eq(ExceptionTaskCheck::getCheckStatus, CheckStatus.CHECK_PASSED);
        lambda.ne(ExceptionTaskCheck::getCheckSubstatus, CheckSubStatus.WAIT_CHECK_EXPIRED);
        lambda.apply("check_version = (SELECT MAX(check_version) FROM emt_exception_task_check task_check WHERE task_check.exception_task_id = emt_exception_task_check.exception_task_id)");
        List<ExceptionTaskCheck> latestDataList = this.list(lambda);
        return latestDataList.stream().collect(Collectors.groupingBy(ExceptionTaskCheck::getExceptionTaskId, Collectors.mapping(ExceptionTaskCheck::getUserId, Collectors.toList())));
    }

}
