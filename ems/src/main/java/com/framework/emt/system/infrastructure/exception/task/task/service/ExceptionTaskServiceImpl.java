package com.framework.emt.system.infrastructure.exception.task.task.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.cache.ParamCache;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.service.ExceptionCategoryService;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.statistics.constant.enums.StatisticsType;
import com.framework.emt.system.domain.statistics.request.*;
import com.framework.emt.system.domain.statistics.response.*;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.task.handing.response.HandingSuspendResponse;
import com.framework.emt.system.domain.task.response.response.StatisticsQuantityResponse;
import com.framework.emt.system.domain.task.task.request.TaskExportQueryRequest;
import com.framework.emt.system.domain.task.task.request.TaskQueryRequest;
import com.framework.emt.system.domain.task.task.response.TaskExportResponse;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationService;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.check.constant.code.TaskCheckErrorCode;
import com.framework.emt.system.infrastructure.exception.task.check.service.ExceptionTaskCheckService;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.code.ExceptionTaskHandingErrorCode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import com.framework.emt.system.infrastructure.exception.task.record.response.RejectResponse;
import com.framework.emt.system.infrastructure.exception.task.record.response.TransferResponse;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.response.constant.code.TaskResponseErrorCode;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.constant.code.ExceptionTaskSubmitErrorCode;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.constant.code.ExceptionTaskErrorCode;
import com.framework.emt.system.infrastructure.exception.task.task.constant.code.SubmitExtendConstant;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import com.framework.emt.system.infrastructure.exception.task.task.mapper.ExceptionTaskMapper;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskRecordResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 异常任务 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExceptionTaskServiceImpl extends BaseServiceImpl<ExceptionTaskMapper, ExceptionTask> implements ExceptionTaskService {

    private final DeptService deptService;

    private final UserService userService;

    private final ExceptionCategoryService exceptionCategoryService;

    private final ExceptionItemService exceptionItemService;

    private final WorkspaceLocationService workspaceLocationService;

    private final ExceptionTaskRecordService exceptionTaskRecordService;

    private final IUserDao userDao;

    private final ExceptionTaskScheduleService scheduleService;

    private final IMessageService messageService;
    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    @Override
    public ExceptionTask create(Long taskSettingId, ExceptionStatus status, ExceptionSubStatus subStatus) {
        ExceptionTask exceptionTask = new ExceptionTask();
        this.create(exceptionTask.init(taskSettingId, status, subStatus));
        return exceptionTask;
    }

    @Override
    public IPage<TaskResponse> page(TaskQueryRequest request) {
        IPage<TaskResponse> pageResult = this.baseMapper.page(Condition.getPage(request), request);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        this.loadList(taskList);
        return pageResult;
    }

    @Override
    public TaskResponse detail(Long id) {
        TaskResponse detailResult = this.baseMapper.detail(id);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(ExceptionTaskErrorCode.TASK_INFO_NOT_FIND);
        }
        this.loadDetail(detailResult);
        return detailResult;
    }

    @Override
    public IPage<RejectResponse> submitRejectPage(Long id, Query query) {
        List<Integer> recordNodeCodes = TaskRecordNode.getResponseHandingCodes();
        Integer recordTypeCode = TaskRecordType.REJECT.getCode();
        return this.baseMapper.taskRejectPage(Condition.getPage(query), recordNodeCodes, recordTypeCode, id);
    }

    @Override
    public IPage<TransferResponse> responseTransferPage(Long id, Query query) {
        Integer recordNodeCode = TaskRecordNode.RESPONSE.getCode();
        Integer recordTypeCode = TaskRecordType.TRANSFER.getCode();
        return this.baseMapper.taskTransferPage(Condition.getPage(query), recordNodeCode, recordTypeCode, id);
    }

    @Override
    public IPage<RejectResponse> responseRejectPage(Long id, Query query) {
        List<Integer> recordNodeCodes = Lists.newArrayList(TaskRecordNode.HANDING.getCode());
        Integer recordTypeCode = TaskRecordType.REJECT.getCode();
        return this.baseMapper.taskRejectPage(Condition.getPage(query), recordNodeCodes, recordTypeCode, id);
    }

    @Override
    public IPage<TaskResponse> handingPage(Long id, Query query) {
        IPage<TaskResponse> pageResult = this.baseMapper.handingPage(Condition.getPage(query), id);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        this.loadList(taskList);
        return pageResult;
    }

    @Override
    public IPage<TransferResponse> handingTransferPage(Long id, Query query) {
        Integer recordNodeCode = TaskRecordNode.HANDING.getCode();
        Integer recordTypeCode = TaskRecordType.TRANSFER.getCode();
        return this.baseMapper.taskTransferPage(Condition.getPage(query), recordNodeCode, recordTypeCode, id);
    }

    @Override
    public IPage<HandingSuspendResponse> handingSuspendPage(Long id, Query query) {
        Integer recordNodeCode = TaskRecordNode.HANDING.getCode();
        List<Integer> recordTypeCodes = TaskRecordType.getSuspendCodes();
        return this.baseMapper.handingSuspendPage(Condition.getPage(query), recordNodeCode, recordTypeCodes, id);
    }

    @Override
    public IPage<RejectResponse> handingRejectPage(Long id, Query query) {
        List<Integer> recordNodeCodes = Lists.newArrayList(TaskRecordNode.CHECK.getCode());
        Integer recordTypeCode = TaskRecordType.REJECT.getCode();
        return this.baseMapper.taskRejectPage(Condition.getPage(query), recordNodeCodes, recordTypeCode, id);
    }

    @Override
    public IPage<TaskResponse> cooperationPage(Long id, Query query) {
        IPage<TaskResponse> pageResult = this.baseMapper.cooperationPage(Condition.getPage(query), id);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        this.loadList(taskList);
        return pageResult;
    }

    @Override
    public TaskResponse cooperationDetail(Long cooperationId, Integer version) {
        return SpringUtil.getBean(ExceptionTaskCooperationService.class).detail(cooperationId, version);
    }

    @Override
    public IPage<TaskResponse> checkPage(Long id, Query query) {
        IPage<TaskResponse> pageResult = this.baseMapper.checkPage(Condition.getPage(query), id);
        List<TaskResponse> taskList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return pageResult;
        }
        this.loadList(taskList);
        return pageResult;
    }

    @Override
    public TaskResponse checkDetail(Long checkId, Integer version) {
        return SpringUtil.getBean(ExceptionTaskCheckService.class).detail(checkId, null, version);
    }

    @Override
    public IPage<TaskRecordResponse> recordPage(Long id, String keyWord, Query query) {
        IPage<TaskRecordResponse> pageResult = this.baseMapper.recordPage(Condition.getPage(query), id, keyWord);
        return Optional.of(pageResult).map(IPage::getRecords).filter(CollectionUtil::isNotEmpty).map(records -> {
            records.forEach(TaskRecordResponse::init);
            return DataHandleUtils.loadUserName(pageResult);
        }).orElse(pageResult);
    }

    @Override
    public List<TaskExportResponse> findExportData(TaskExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskExportResponse> exportResponseList = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isEmpty(exportResponseList)) {
            return exportResponseList;
        }
        // 用户信息
        List<Long> userIdList = new ArrayList<>();
        exportResponseList.forEach(item -> {
            userIdList.addAll(item.initUserList());
        });
        Map<Long, User> userMap = userService.getMapByIds(userIdList.stream().distinct().collect(toList()));
        // 提报部门
        List<Long> deptIds = exportResponseList.stream().map(TaskExportResponse::getDeptId)
                .filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, Dept> deptMap = deptService.getMapByIds(deptIds);
        // 异常分类
        List<Long> categoryIds = exportResponseList.stream().map(TaskExportResponse::getCategoryId).filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, ExceptionCategory> categoryMap = exceptionCategoryService.getMapByIds(categoryIds);
        // 异常项
        List<Long> itemIds = exportResponseList.stream().map(TaskExportResponse::getItemId).filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, ExceptionItem> itemMap = exceptionItemService.getMapByIds(itemIds);
        // 作业单元
        List<Long> workspaceIds = exportResponseList.stream().map(TaskExportResponse::getWorkspaceId).filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, WorkspaceLocationResponse> workspaceMap = workspaceLocationService.getMapByIds(workspaceIds);
        Date now = new Date();
        List<Long> taskIds = exportResponseList.stream().filter(response -> response.getTaskStatus() == ExceptionStatus.FINISH).map(TaskExportResponse::getExceptionTaskId).collect(toList());
        Map<Long, User> mapByIds;
        ExceptionTaskCheckService checkService = SpringUtil.getBean(ExceptionTaskCheckService.class);
        Map<Long, List<Long>> checkUserIds = checkService.loadCheckUserId(taskIds);
        if (ObjectUtil.isNotEmpty(checkUserIds) && !checkUserIds.isEmpty()) {
            List<Long> userIds = checkUserIds.values().stream().flatMap(List::stream).collect(Collectors.toList());
            mapByIds = userService.getMapByIds(userIds.stream().distinct().collect(toList()));

        } else {
            mapByIds = null;
        }
        exportResponseList.forEach(item -> {
            item.init(now, userMap, deptMap, categoryMap, itemMap, workspaceMap);
            item.setStartTime(item.getSubmitTime());
            item.setEndTime(item.getCloseTime());
            item.setSubmitExtendDriveStopDatas(convertTaskFormField(item.getSubmitExtendField(), SubmitExtendConstant.DID_CAUSE_DRIVE_STOP));
            item.setSubmitExtendModelDatas(convertTaskFormField(item.getSubmitExtendField(), SubmitExtendConstant.MODEL));
            item.setSubmitExtendBatchDatas(convertTaskFormField(item.getSubmitExtendField(), SubmitExtendConstant.IMPACT_BATCH));
            item.setHandingExtendDisposalPlanDatas(convertTaskFormField(item.getHandingExtendField(), SubmitExtendConstant.DISPOSAL_PLAN));
            item.setHandingExtendMaintenanceContentDatas(convertTaskFormField(item.getHandingExtendField(), SubmitExtendConstant.MAINTENANCE_CONTENT));
            item.setSuspendExtendCauseAnalysisDatas(convertTaskFormField(item.getSuspendExtendField(), SubmitExtendConstant.CAUSE_ANALYSIS));
            if (ObjectUtil.isNotEmpty(checkUserIds) && !checkUserIds.isEmpty()) {
                List<Long> checkIdList = checkUserIds.get(item.getExceptionTaskId());
                if (ObjectUtil.isNotEmpty(checkIdList) && checkIdList.size() > NumberConstant.ZERO && ObjectUtil.isNotNull(mapByIds)) {
                    item.setCheckUser(checkIdList.stream().map(mapByIds::get).map(User::getName).collect(Collectors.joining(StrPool.COMMA)));
                }
            }
            if (CollectionUtil.isEmpty(item.getHandingReasonItems())) {
                item.setHandingReasons("");
            }else {
                TypeReference<List<TagInfo>> type = new TypeReference<List<TagInfo>>() {
                };
                List<TagInfo> reasonItems = JsonUtil.parse(JsonUtil.toJson(item.getHandingReasonItems()), type);
                item.setHandingReasons(String.join(",", reasonItems.stream().map(TagInfo::getContent).collect(toList())));
            }
        });
        return exportResponseList;
    }


    /**
     * 异常列表附加字段
     *
     * @param extendResponseList 附加字段信息
     * @param prop               英文描述
     * @return
     */
    public static String convertTaskFormField(List<FormFieldResponse> extendResponseList, String prop) {
        if (ObjectUtil.isEmpty(extendResponseList)) {
            return "";
        }
        String result = "";
        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {
        };
        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(extendResponseList), type);
        for (FormFieldResponse responseExtendField : formFields) {
            if (ObjectUtil.equal(responseExtendField.getProp(), prop)) {
                if (responseExtendField.getType().equals(FormFieldTypeEnum.SWITCH) || responseExtendField.getType().equals(FormFieldTypeEnum.CHECKBOX) || responseExtendField.getType().equals(FormFieldTypeEnum.RADIO)) {
                    if (StringUtils.isNotBlank(responseExtendField.getValue())) {
                        if (StringUtils.isNotBlank(result)) {
                            result += "、" + responseExtendField.getValue();
                        } else {
                            result += responseExtendField.getValue();
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(responseExtendField.getKey())) {
                        if (StringUtils.isNotBlank(result)) {
                            result += "、" + responseExtendField.getKey();
                        } else {
                            result += responseExtendField.getKey();
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override

    public ExceptionTask findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionTaskErrorCode.TASK_INFO_NOT_FIND);
    }

    @Override
    public ExceptionTask validateStatusUpdate(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!ExceptionStatus.DRAFT.equals(exceptionTask.getTaskStatus()) && !ExceptionStatus.CANCEL.equals(exceptionTask.getTaskStatus()) && !ExceptionStatus.SUBMIT_REJECT.equals(exceptionTask.getTaskStatus())) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.STATUS_MUST_DRAFT_OR_CANCEL_OR_SUBMIT_REJECT_CAN_UPDATE);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusDelete(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!ExceptionStatus.DRAFT.equals(exceptionTask.getTaskStatus()) && !ExceptionStatus.CANCEL.equals(exceptionTask.getTaskStatus())
                && !ExceptionStatus.CLOSE.equals(exceptionTask.getTaskStatus())) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.EXCEPTION_TASK_SUBMIT_STATUS_MUST_DRAFT_CAN_DELETE);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusClose(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!ExceptionStatus.SUBMIT_REJECT.equals(exceptionTask.getTaskStatus())) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.EXCEPTION_TASK_SUBMIT_STATUS_MUST_SUBMIT_REJECT_CAN_CLOSE);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusSubmit(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!ExceptionStatus.DRAFT.equals(exceptionTask.getTaskStatus())
                && !ExceptionStatus.CANCEL.equals(exceptionTask.getTaskStatus())
                && !ExceptionStatus.SUBMIT_REJECT.equals(exceptionTask.getTaskStatus())) {
            throw new ServiceException(ExceptionTaskSubmitErrorCode.STATUS_MUST_DRAFT_OR_CANCEL_OR_SUBMIT_REJECT_CAN_SUBMIT);
        }
        return exceptionTask;
    }


    @Override
    public ExceptionTask validateStatusHandingReject(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.PRE_HANDING)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_PRE_HANDING);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusHandingToOther(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.PRE_HANDING)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_PRE_HANDING);
        }
        //验证转派次数是否已超过最大值
        Integer toOtherMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.TASK_HANDING_MAX_COUNT, AuthUtil.getTenantId()), ExceptionTaskConstant.TASK_HANDING_DEFAULT_MAX_COUNT);
        if (exceptionTask.getHandingOtherCount() >= toOtherMaxCount) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.THE_MAXIMUM_NUMBER_OF_TRANSFERS_PROCESSED_HAS_BEEN_REACHED);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusHandingToAccept(Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.PRE_HANDING)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_PRE_HANDING);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateHandingSuspend(Long id, long suspendSeconds) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.HANDING) && !Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.HANDING_REJECT)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_HANDING);
        }
        // 校验挂起总次数
        Integer suspendMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.SUSPEND_MAX_COUNT, AuthUtil.getTenantId()), ExceptionTaskConstant.SUSPEND_DEFAULT_MAX_COUNT);
        if (exceptionTask.getHandingSuspendNum() >= suspendMaxCount) {
            throw new ServiceException(TaskResponseErrorCode.ABOVE_SUSPEND_MAX_COUNT);
        }
        // 校验挂起总时长
        Integer suspendMaxMinutes = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.SUSPEND_MAX_MINUTES, AuthUtil.getTenantId()), ExceptionTaskConstant.SUSPEND_DEFAULT_MAX_MINUTES);
        // 已挂起总时间+预计要挂起的时间 >= 挂起总时长限制
        if (exceptionTask.getCheckTotalSecond() + suspendSeconds >= suspendMaxMinutes * 60) {
            throw new ServiceException(TaskResponseErrorCode.ABOVE_SUSPEND_MAX_MINUTES);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateHandingSuspendDelay(Long id, long suspendSeconds, long suspendDelaySeconds) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.SUSPEND)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_SUSPEND);
        }
        // 校验挂起总时长
        Integer suspendMaxMinutes = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.SUSPEND_MAX_MINUTES, AuthUtil.getTenantId()), ExceptionTaskConstant.SUSPEND_DEFAULT_MAX_MINUTES);
        // 已挂起总时间+当前挂起总时间+预计延期的秒数 >= 挂起总时长限制
        if (exceptionTask.getCheckTotalSecond() + suspendSeconds + suspendDelaySeconds >= suspendMaxMinutes * 60) {
            throw new ServiceException(TaskResponseErrorCode.ABOVE_SUSPEND_MAX_MINUTES);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateHandingResume(Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.SUSPEND)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_SUSPEND);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateHandingCooperationTask(Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        if (!Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.HANDING) && !Objects.equals(exceptionTask.getTaskStatus(), ExceptionStatus.HANDING_REJECT)) {
            throw new ServiceException(TaskResponseErrorCode.NOT_HANDING);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusResponse(@NonNull Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        boolean flag = !ObjectUtil.equal(exceptionTask.getTaskStatus(), ExceptionStatus.PRE_RESPONSE) && (!ObjectUtil.equal(exceptionTask.getTaskSubStatus(), ExceptionSubStatus.PRE_RESPONSE) || !ObjectUtil.equal(exceptionTask.getTaskSubStatus(), ExceptionSubStatus.PRE_RESPONSE_OTHER));
        if (flag) {
            throw new ServiceException(TaskResponseErrorCode.WHEN_RESPONDING_TO_REJECTION_THE_ABNORMAL_STATUS_MUST_BE_EITHER_PENDING_RESPONSE_OR_TRANSFERRED);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validResponseToOther(Long id) {
        ExceptionTask exceptionTask = findByIdThrowErr(id);
        boolean flag = !ObjectUtil.equal(exceptionTask.getTaskStatus(), ExceptionStatus.PRE_RESPONSE) && (!ObjectUtil.equal(exceptionTask.getTaskSubStatus(), ExceptionSubStatus.PRE_RESPONSE) || !ObjectUtil.equal(exceptionTask.getTaskSubStatus(), ExceptionSubStatus.PRE_RESPONSE_OTHER));
        if (flag) {
            throw new ServiceException(TaskResponseErrorCode.WHEN_RESPONDING_TO_REJECTION_THE_ABNORMAL_STATUS_MUST_BE_EITHER_PENDING_RESPONSE_OR_TRANSFERRED);
        }
        //验证转派次数是否已超过最大值
        Integer transferMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.TASK_TRANSFER_MAX_COUNT, AuthUtil.getTenantId()), ExceptionTaskConstant.TASK_TRANSFER_DEFAULT_MAX_COUNT);
        if (exceptionTask.getResponseOtherCount() >= transferMaxCount) {
            throw new ServiceException(TaskResponseErrorCode.THE_MAXIMUM_NUMBER_OF_RESPONSE_TRANSFERS_HAS_BEEN_REACHED);
        }
        return exceptionTask;
    }

    @Override
    public ExceptionTask validateStatusResponseHanding(Long id) {
        ExceptionTask task = findByIdThrowErr(id);
        if (!ExceptionStatus.RESPONDING.equals(task.getTaskStatus()) && !ExceptionStatus.RESPONSE_REJECT.equals(task.getTaskStatus())) {
            throw new ServiceException(TaskResponseErrorCode.WHEN_SETTING_THE_HANDLER_THE_EXCEPTION_STATE_MUST_BE_RESPONDING);
        }
        return task;
    }

    @Override
    public ExceptionTask validateStatusCheck(Long id) {
        ExceptionTask task = findByIdThrowErr(id);
        if (!ObjectUtil.equal(task.getTaskStatus(), ExceptionStatus.CHECK)) {
            throw new ServiceException(TaskCheckErrorCode.THE_ACCEPTANCE_INFORMATION_STATUS_MUST_BE_PENDING_ACCEPTANCE);
        }
        return task;
    }

    @Override
    public ExceptionTask validationProcessing(Long id) {
        ExceptionTask task = findByIdThrowErr(id);
        if (!ObjectUtil.equal(task.getTaskStatus(), ExceptionStatus.HANDING) && !ObjectUtil.equal(task.getTaskStatus(), ExceptionStatus.HANDING_REJECT)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.PROCESSING_INFORMATION_STATUS_MUST_BE_HANDING);
        }
        return task;
    }

    @Override
    public void loadList(List<TaskResponse> taskList) {
        Map<Long, Dept> deptMap = getDeptMap(taskList);
        Map<Long, User> userMap = getUserMap(taskList);
        Map<Long, ExceptionCategory> categoryMap = getCategoryMap(taskList);
        Map<Long, ExceptionItem> itemMap = getItemMap(taskList);
        Map<Long, WorkspaceLocationResponse> workspaceMap = getWorkspaceMap(taskList);
        LocalDateTime now = LocalDateTime.now();
        taskList.forEach(item -> {
            item.init(now);
            item.update(userMap, deptMap, categoryMap, itemMap, workspaceMap);
        });
    }

    @Override
    public void loadDetail(TaskResponse task) {
        Map<Long, Dept> deptMap = getDeptMap(Lists.newArrayList(task));
        Map<Long, User> userMap = getUserMap(Lists.newArrayList(task));
        Map<Long, ExceptionCategory> categoryMap = getCategoryMap(Lists.newArrayList(task));
        Map<Long, ExceptionItem> itemMap = getItemMap(Lists.newArrayList(task));
        Map<Long, WorkspaceLocationResponse> workspaceMap = getWorkspaceMap(Lists.newArrayList(task));
        LocalDateTime now = LocalDateTime.now();
        task.init(now);
        task.update(userMap, deptMap, categoryMap, itemMap, workspaceMap);
    }

    @Override
    public StatisticsSummaryResponse statisticsSummary(StatisticsSummaryQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.statisticsSummary(deptIds);
    }

    @Override
    public StatisticsRealTimeResponse realTime(StatisticsSummaryQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.statisticsRealTime(deptIds);
    }


    @Override
    public StatisticsTrendResponse trend(StatisticsTrendQueryRequest request) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(request.getDeptId())) {
            deptIds = deptService.findByParentId(request.getDeptId());
        }
        StatisticsTrendResponse response = new StatisticsTrendResponse();
        StatisticsType sourceTypeEnum = StatisticsType.getEnum(request.getType());
        switch (sourceTypeEnum) {
            case DAY:
                response.setSubmitData(this.baseMapper.statisticsDayTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setFinishData(this.baseMapper.statisticsDayTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setPendingData(exceptionTaskRecordService.statisticsDayTrend("suspend", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
            case WEEK:
                response.setSubmitData(this.baseMapper.statisticsWeekTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setFinishData(this.baseMapper.statisticsWeekTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));

                response.setPendingData(exceptionTaskRecordService.statisticsWeekTrend("suspend", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
            case MONTH:
                response.setSubmitData(this.baseMapper.statisticsMonthTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setFinishData(this.baseMapper.statisticsMonthTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setPendingData(exceptionTaskRecordService.statisticsMonthTrend("suspend", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
            case YEAR:
                response.setSubmitData(this.baseMapper.statisticsYearTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, null, null, null, null));
                response.setFinishData(this.baseMapper.statisticsYearTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, null, null, null, null));
                response.setPendingData(exceptionTaskRecordService.statisticsYearTrend("suspend", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
        }
        return response;
    }


    @Override
    public StatisticsTrendTimeoutResponse trendTimeout(StatisticsTrendQueryRequest request) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(request.getDeptId())) {
            deptIds = deptService.findByParentId(request.getDeptId());
        }
        StatisticsTrendTimeoutResponse response = new StatisticsTrendTimeoutResponse();
        StatisticsType sourceTypeEnum = StatisticsType.getEnum(request.getType());
        switch (sourceTypeEnum) {
            case DAY:
                response.setResponseTimeOutCount(exceptionTaskScheduleService.statisticsDayTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setHandingTimeOutCount(exceptionTaskScheduleService.statisticsDayTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setCooperationTimeOutCount(exceptionTaskScheduleService.statisticsDayTimeout("cooperation", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
            case MONTH:
                response.setResponseTimeOutCount(exceptionTaskScheduleService.statisticsMonthTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setHandingTimeOutCount(exceptionTaskScheduleService.statisticsMonthTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setCooperationTimeOutCount(exceptionTaskScheduleService.statisticsMonthTimeout("cooperation", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
            case YEAR:
                response.setResponseTimeOutCount(exceptionTaskScheduleService.statisticsYearTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setHandingTimeOutCount(exceptionTaskScheduleService.statisticsYearTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                response.setCooperationTimeOutCount(exceptionTaskScheduleService.statisticsYearTimeout("cooperation", request.getStartDate(), request.getEndDate(), deptIds, null, null, null));
                break;
        }
        return response;
    }


    @Override
    public StatisticsProportionPieResponse categoryProportion(StatisticsTimeQueryRequest queryRequest) {
        StatisticsProportionPieResponse pieResponse = new StatisticsProportionPieResponse();
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        List<StatisticsProportionResponse> statisticsProportion = this.baseMapper.categoryProportion(queryRequest, deptIds);
        pieResponse.init(statisticsProportion, calculateExceptionTotal(statisticsProportion));
        return pieResponse;
    }

    @Override
    public StatisticsProportionPieResponse processProportion(StatisticsTimeQueryRequest queryRequest) {
        StatisticsProportionPieResponse pieResponse = new StatisticsProportionPieResponse();
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        List<StatisticsProportionResponse> statisticsProportion = this.baseMapper.processProportion(queryRequest, deptIds);
        pieResponse.init(statisticsProportion, calculateExceptionTotal(statisticsProportion));
        return pieResponse;
    }

    @Override
    public List<StatisticsProportionResponse> itemProportion(StatisticsTimeQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.itemProportion(queryRequest, deptIds);
    }

    @Override
    public StatisticsExceptionTrendResponse exceptionTrend(StatisticsExceptionTrendQueryRequest request) {
        StatisticsExceptionTrendResponse response = new StatisticsExceptionTrendResponse();
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(request.getDeptId())) {
            deptIds = deptService.findByParentId(request.getDeptId());
        }
        StatisticsType sourceTypeEnum = StatisticsType.getEnum(request.getType());
        switch (sourceTypeEnum) {
            case DAY:
                response.setSubmitData(this.baseMapper.statisticsDayTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setFinishData(this.baseMapper.statisticsDayTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setResponseTimeOutCount(exceptionTaskRecordService.statisticsDayTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setHandingTimeOutCount(exceptionTaskRecordService.statisticsDayTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                break;
            case WEEK:
                response.setSubmitData(this.baseMapper.statisticsWeekTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setFinishData(this.baseMapper.statisticsWeekTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setResponseTimeOutCount(exceptionTaskRecordService.statisticsWeekTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setHandingTimeOutCount(exceptionTaskRecordService.statisticsWeekTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                break;
            case MONTH:
                response.setSubmitData(this.baseMapper.statisticsMonthTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setFinishData(this.baseMapper.statisticsMonthTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setResponseTimeOutCount(exceptionTaskRecordService.statisticsMonthTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setHandingTimeOutCount(exceptionTaskRecordService.statisticsMonthTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
            case YEAR:
                response.setSubmitData(this.baseMapper.statisticsYearTrend("submit", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId(), "exceptionTrend"));
                response.setFinishData(this.baseMapper.statisticsYearTrend("finish", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId(), "exceptionTrend"));
                response.setResponseTimeOutCount(exceptionTaskRecordService.statisticsMonthTimeout("response", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                response.setHandingTimeOutCount(exceptionTaskRecordService.statisticsMonthTimeout("handing", request.getStartDate(), request.getEndDate(), deptIds, request.getExceptionCategoryId(), request.getExceptionProcessId(), request.getWorkspaceLocationId()));
                break;
        }
        return response;
    }

    @Override
    public StatisticsProportionPieResponse exceptionCategoryProportion(StatisticsTimeQueryRequest queryRequest) {
        StatisticsProportionPieResponse pieResponse = new StatisticsProportionPieResponse();
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        List<StatisticsProportionResponse> statisticsProportion = this.baseMapper.exceptionCategoryProportion(queryRequest, deptIds);
        pieResponse.init(statisticsProportion, calculateExceptionTotal(statisticsProportion));
        return pieResponse;
    }

    /**
     * 计算异常共计
     *
     * @param statisticsProportion 分类或者流程的个数
     * @return 计算异常共计
     */
    private Integer calculateExceptionTotal(List<StatisticsProportionResponse> statisticsProportion) {
        Integer exceptionTotal = 0;
        for (StatisticsProportionResponse proportion :
                statisticsProportion) {
            exceptionTotal = exceptionTotal + proportion.getCount();
        }
        return exceptionTotal;
    }

    @Override
    public List<StatisticsProportionResponse> exceptionPendingTop(StatisticsTimeQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.exceptionPendingTop(queryRequest, deptIds);
    }

    @Override
    public StatisticsCompleteRateResponse completeRate(StatisticsCompleteQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.completeRate(queryRequest, deptIds);
    }

    @Override
    public List<StatisticsAvgDetailResponse> avgEnergyConsumption(StatisticsAvgRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        if (ObjectUtil.equal(NumberConstant.ONE, queryRequest.getType())) {
            return this.baseMapper.avgResponseTime(queryRequest, deptIds);
        }
        if (ObjectUtil.equal(NumberConstant.TWO, queryRequest.getType())) {
            return this.baseMapper.avgHandingTime(queryRequest, deptIds);
        }
        if (ObjectUtil.equal(NumberConstant.THREE, queryRequest.getType())) {
            return this.baseMapper.avgCheckTime(queryRequest, deptIds);
        }
        return null;
    }

    @Override
    public StatisticsDeptBoardResponse deptBoardSummary(StatisticsDeptBoardRequest queryRequest) {
        List<Long> deptIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        StatisticsDeptBoardResponse statisticsDeptBoardResponse = new StatisticsDeptBoardResponse();
        StatisticsDeptBoardResponse response = this.baseMapper.deptBoardSummary(deptIds, queryRequest.getProcessIdList());
        if (ObjectUtil.isNotEmpty(response)) {
            statisticsDeptBoardResponse = response;
        }
        statisticsDeptBoardResponse.setPreCooperationQuantity(this.baseMapper.suspendQuantity(deptIds, queryRequest.getProcessIdList()));
        statisticsDeptBoardResponse.setTimeoutQuantity(this.baseMapper.timeoutQuantity(deptIds, queryRequest.getProcessIdList()));
        return statisticsDeptBoardResponse;
    }

    @Override
    public IPage<StatisticsDeptBoardListResponse> deptBoardPage(StatisticsDeptBoardRequest queryRequest) {
        List<Long> deptIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        IPage<StatisticsDeptBoardListResponse> responseIPage = this.baseMapper.deptBoardPage(Condition.getPage(queryRequest), deptIds, queryRequest.getProcessIdList());
        List<StatisticsDeptBoardListResponse> records = responseIPage.getRecords();
        Map<Long, WorkspaceLocationResponse> workspaceMap = getWorkspaceMapByBoards(records);
        if (ObjectUtil.isNotEmpty(records)) {
            List<Long> userIdList = new ArrayList<>();
            records.forEach(item -> {
                userIdList.addAll(item.initUserList());
            });
            Map<Long, User> mapByIds = userService.getMapByIds(userIdList.stream().distinct().collect(toList()));
            records.forEach(record -> {
                record.init(records);
                LocalDateTime now = LocalDateTime.now();
                record.loadRemainderAndTimeout(now);
                record.getWorkspaceLocationName(workspaceMap);
                record.update(mapByIds);
            });
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(records, "userId", User::getId, "userName", "name", type);
            exceptionItemService.loadData(records, "exceptionItemId", ExceptionItem::getId, "exceptionItemName", "title", type);
            exceptionCategoryService.loadData(records, "exceptionCategoryId", ExceptionCategory::getId, "exceptionCategoryName", "title", type);
        }
        return responseIPage;
    }


    /**
     * 获取作业单元map列表
     *
     * @param taskResponseList 任务列表
     * @return
     */
    private Map<Long, WorkspaceLocationResponse> getWorkspaceMapByBoards(List<StatisticsDeptBoardListResponse> taskResponseList) {
        List<Long> workspaceIds = taskResponseList.stream().filter(item -> item.getWorkspaceLocationId() != null).map(item -> item.getWorkspaceLocationId()).distinct().collect(toList());
        return workspaceLocationService.getMapByIds(workspaceIds);
    }

    @Override
    @DSTransactional
    public void discontinue(Long id) {
        //判断是否有转派权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(ExceptionTaskErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_CLOSE_EXCEPTION_MANAGEMENT);
        }
        //修改状态为 3 已关闭
        ExceptionTask task = findByIdThrowErr(id);
        if (ObjectUtil.equal(task.getTaskStatus().getCode(), ExceptionStatus.FINISH.getCode())) {
            throw new ServiceException(ExceptionTaskErrorCode.THE_TASK_HAS_BEEN_COMPLETED_AND_CANNOT_BE_ABORTED);
        }
        ExceptionTaskSubmitService submitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
        ExceptionTaskSubmit submit = submitService.findUniqueInfo(task.getId(), task.getSubmitVersion());
        //协同任务修改为已撤销 2  验收任务修改为已过期
        if (task.getTaskStatus().getCode() > ExceptionStatus.PRE_HANDING.getCode()) {
            ExceptionTaskCooperationService exceptionTaskCooperationService = SpringUtil.getBean(ExceptionTaskCooperationService.class);
            List<ExceptionTaskCooperation> exceptionTaskCooperation = exceptionTaskCooperationService.updateStatus(id, task.getHandingVersion());
            // 取消发送协同、协同上报超时定时消息
            if (ObjectUtil.isNotNull(exceptionTaskCooperation) && exceptionTaskCooperation.size() > 0) {
                for (ExceptionTaskCooperation taskCooperation :
                        exceptionTaskCooperation) {
                    scheduleService.cooperationTimeOutMessageSendCancel(taskCooperation, LocalDateTime.now());
                }
            }
        }
        if (ObjectUtil.equal(task.getTaskStatus().getCode(), ExceptionStatus.CHECK.getCode())) {
            ExceptionTaskCheckService checkService = SpringUtil.getBean(ExceptionTaskCheckService.class);
            checkService.updateStatus(id, task.getCheckVersion());
        }
        //更新任务
        this.update(task.discontinue());
        //    取消发送响应、响应上报超时和处理、处理上报超时定时消息
        scheduleService.timeOutMessageSendCancel(submit, LocalDateTime.now());
    }



    @Override
    public long findFinishCountByIds(List<Long> ids) {
        return this.count(new LambdaQueryWrapper<ExceptionTask>()
                .ne(ExceptionTask::getTaskStatus, ExceptionStatus.FINISH)
                .ne(ExceptionTask::getTaskStatus, ExceptionStatus.CLOSE)
                .in(ExceptionTask::getId, ids));
    }

    /**
     * 获取作业单元map列表
     *
     * @param taskResponseList 任务列表
     * @return
     */
    private Map<Long, WorkspaceLocationResponse> getWorkspaceMap(List<TaskResponse> taskResponseList) {
        List<Long> workspaceIds = taskResponseList.stream().filter(item -> item.getSubmit() != null)
                .map(item -> item.getSubmit().getWorkspaceLocationId()).distinct().collect(toList());
        return workspaceLocationService.getMapByIds(workspaceIds);
    }

    /**
     * 获取异常项map列表
     *
     * @param taskResponseList 任务列表
     * @return
     */
    private Map<Long, ExceptionItem> getItemMap(List<TaskResponse> taskResponseList) {
        List<Long> itemIds = taskResponseList.stream().filter(item -> item.getSubmit() != null)
                .map(item -> item.getSubmit().getExceptionItemId()).distinct().collect(toList());
        return exceptionItemService.getMapByIds(itemIds);
    }

    /**
     * 获取异常分类map列表
     *
     * @param taskResponseList 任务列表
     * @return
     */
    private Map<Long, ExceptionCategory> getCategoryMap(List<TaskResponse> taskResponseList) {
        List<Long> categoryIds = taskResponseList.stream().filter(item -> item.getSubmit() != null)
                .map(item -> item.getSubmit().getExceptionCategoryId()).distinct().collect(toList());
        return exceptionCategoryService.getMapByIds(categoryIds);
    }

    /**
     * 获取部门map列表
     *
     * @param taskResponseList 任务响应体列表
     * @return
     */
    private Map<Long, Dept> getDeptMap(List<TaskResponse> taskResponseList) {
        List<Long> deptIds = taskResponseList.stream().filter(item -> item.getSubmit() != null)
                .map(item -> item.getSubmit().getDeptId()).distinct().collect(toList());
        return deptService.getMapByIds(deptIds);
    }

    /**
     * 获取用户map列表
     *
     * @param taskResponseList 任务响应体列表
     * @return
     */
    private Map<Long, User> getUserMap(List<TaskResponse> taskResponseList) {
        List<Long> userIdList = new ArrayList<>();
        taskResponseList.forEach(item -> {
            userIdList.addAll(item.initUserList());
        });
        return userService.getMapByIds(userIdList.stream().distinct().collect(toList()));
    }

    @Override
    public StatisticsQuantityResponse quantity(Long userId) {
        return this.baseMapper.quantity(userId);
    }
}
