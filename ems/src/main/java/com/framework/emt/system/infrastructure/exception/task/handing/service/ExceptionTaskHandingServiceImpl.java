package com.framework.emt.system.infrastructure.exception.task.handing.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.service.ExceptionCategoryService;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.handing.convert.TaskHandingConvert;
import com.framework.emt.system.domain.task.handing.request.TaskHandingExportQueryRequest;
import com.framework.emt.system.domain.task.handing.request.TaskHandingQueryRequest;
import com.framework.emt.system.domain.task.handing.response.HandingStatusNumResponse;
import com.framework.emt.system.domain.task.handing.response.TaskHandingExportResponse;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationService;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.code.ExceptionTaskHandingErrorCode;
import com.framework.emt.system.infrastructure.exception.task.handing.mapper.ExceptionTaskHandingMapper;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 异常任务处理 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskHandingServiceImpl extends BaseServiceImpl<ExceptionTaskHandingMapper, ExceptionTaskHanding> implements ExceptionTaskHandingService {

    private final DeptService deptService;

    private final UserService userService;

    private final ExceptionCategoryService exceptionCategoryService;

    private final ExceptionItemService exceptionItemService;

    private final WorkspaceLocationService workspaceLocationService;

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskSettingService settingService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    @Override
    public IPage<TaskResponse> page(IPage<TaskResponse> page, Long userId, TaskHandingQueryRequest request) {
        IPage<TaskResponse> handingPage = baseMapper.page(page, userId, request);
        List<TaskResponse> taskList = handingPage.getRecords();
        if (CollectionUtil.isEmpty(taskList)) {
            return handingPage;
        }
        exceptionTaskService.loadList(taskList);
        return handingPage;
    }

    @Override
    public IPage<TaskScheduleResponse> timeOutReportPage(Long taskId, Query query) {
        Integer timeOutType = TimeOutType.HANDING_REPORT.getCode();
        return exceptionTaskScheduleService.timeOutReportPage(timeOutType, taskId, query);
    }

    @Override
    public void create(@NonNull Long exceptionTaskId, @NonNull Integer handingVersion, @NonNull List<Long> planUserIds,
                       List<FormFieldResponse> handingExtendFields, List<FormFieldResponse> pendingExtendFields) {
        List<ExceptionTaskHanding> exceptionTaskHandingList = new ArrayList<>();
        for (Long planUserId :
                planUserIds) {
            ExceptionTaskHanding exceptionTaskHanding = new ExceptionTaskHanding();
            exceptionTaskHanding.init(exceptionTaskId, handingVersion, planUserId, handingExtendFields, pendingExtendFields);
            exceptionTaskHandingList.add(exceptionTaskHanding);

        }
        saveBatch(exceptionTaskHandingList);
    }

    @Override
    public void deleteHanding(Long taskId, Long id, Integer handingVersion) {
        LambdaQueryWrapper<ExceptionTaskHanding> wrapper = new LambdaQueryWrapper<ExceptionTaskHanding>()
                .ne(ExceptionTaskHanding::getId, id)
                .eq(ExceptionTaskHanding::getExceptionTaskId, taskId)
                .eq(ExceptionTaskHanding::getHandingVersion, handingVersion);
        this.removeBatchByIds(this.list(wrapper));

    }

    @Override
    public List<Long> getUserIds(Long taskId, Long id, Integer handingVersion) {
        LambdaQueryWrapper<ExceptionTaskHanding> wrapper = new LambdaQueryWrapper<ExceptionTaskHanding>()
                .ne(ExceptionTaskHanding::getId, id)
                .eq(ExceptionTaskHanding::getExceptionTaskId, taskId)
                .eq(ExceptionTaskHanding::getHandingVersion, handingVersion);
        return this.list(wrapper).stream().map(ExceptionTaskHanding::getUserId).collect(Collectors.toList());
    }

    @Override
    public ExceptionTaskHanding info(@NonNull Long id, Long userId) {
        ExceptionTaskHanding exceptionTaskHanding = findByIdThrowErr(id, ExceptionTaskHandingErrorCode.NOT_FOUND);
        if (userId != null && !ObjectUtil.equals(userId, exceptionTaskHanding.getUserId())) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.NOT_ACCEPT_USER);
        }
        return exceptionTaskHanding;
    }

    @Override
    public ExceptionTaskHanding validateSubmit(@NonNull Long id, Long userId) {
        ExceptionTaskHanding exceptionTaskHanding = info(id, userId);
        exceptionTaskHanding.validateSubmit();
        return exceptionTaskHanding;
    }

    @Override
    public ExceptionTaskHanding findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionTaskHandingErrorCode.NOT_FOUND);
    }


    @Override
    public TaskResponse detail(Long id, Long userId) {
        TaskResponse detailResult = this.baseMapper.detail(id);
        if (ObjectUtil.isNull(detailResult)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.NOT_FOUND);
        }
        exceptionTaskService.loadDetail(detailResult);
        return detailResult;
    }

    @Override
    public HandingStatusNumResponse statistics(Long userId) {
        return baseMapper.statistics(userId);
    }

    @Override
    public SettingCheckResponse accepted(Long taskId) {
        ExceptionTask task = exceptionTaskService.findByIdThrowErr(taskId);
        return settingService.findCheckDataById(task.getExceptionTaskSettingId());
    }

    @Override
    public ExceptionTaskHanding findUniqueInfo(Long taskId, Integer version) {
        LambdaQueryWrapper<ExceptionTaskHanding> wrapper = new LambdaQueryWrapper<ExceptionTaskHanding>()
                .eq(ExceptionTaskHanding::getExceptionTaskId, taskId)
                .eq(ExceptionTaskHanding::getHandingVersion, version);
        List<ExceptionTaskHanding> handingList = list(wrapper);
        if (CollectionUtil.isEmpty(handingList)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.NOT_FOUND);
        }
        return handingList.get(0);
    }

    @Override
    public Long copy(ExceptionTaskHanding taskHanding, TaskRejectRequest taskRejectRequest, Integer handingVersion, Integer handingRejectNum) {
        return this.create(TaskHandingConvert.INSTANCE.createRequestToEntity(taskHanding, taskRejectRequest, handingVersion, handingRejectNum));

    }

    @Override
    public ExceptionTaskHanding validateTransferNumber(Long exceptionTaskHandingId) {
        ExceptionTaskHanding handing = findByIdThrowErr(exceptionTaskHandingId);
        //验证转派次数是否已超过最大值
        Integer transferMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.TASK_HAND_COOPERATION_MAX_COUNT,
                AuthUtil.getTenantId()), ExceptionTaskConstant.TASK_HAND_COOPERATION_DEFAULT_MAX_COUNT);
        if (handing.getCooperationOtherCount() >= transferMaxCount) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.THE_NUMBER_OF_COLLABORATIVE_TRANSFERS_HAS_REACHED_THE_MAXIMUM_VALUE);
        }
        return handing;
    }

    @Override
    public List<TaskHandingExportResponse> findExportData(TaskHandingExportQueryRequest request, Long userId) {
        Long exportCount = this.baseMapper.findExportDataCount(request, request.getIds(), userId);
        Integer maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM,
                AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        List<TaskHandingExportResponse> exportResponseList = this.baseMapper.findExportData(request, request.getIds(), userId);
        if (CollectionUtil.isEmpty(exportResponseList)) {
            return exportResponseList;
        }
        // 用户信息
        List<Long> userIdList = new ArrayList<>();
        exportResponseList.forEach(item -> userIdList.addAll(item.initUserList()));
        Map<Long, User> userMap = userService.getMapByIds(userIdList.stream().distinct().collect(toList()));
        // 提报部门
        List<Long> deptIds = exportResponseList.stream().map(TaskHandingExportResponse::getDeptId)
                .filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, Dept> deptMap = deptService.getMapByIds(deptIds);
        // 异常分类
        List<Long> categoryIds = exportResponseList.stream().map(TaskHandingExportResponse::getCategoryId)
                .filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, ExceptionCategory> categoryMap = exceptionCategoryService.getMapByIds(categoryIds);
        // 异常项
        List<Long> itemIds = exportResponseList.stream()
                .map(TaskHandingExportResponse::getItemId).filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, ExceptionItem> itemMap = exceptionItemService.getMapByIds(itemIds);
        // 作业单元
        List<Long> workspaceIds = exportResponseList.stream()
                .map(TaskHandingExportResponse::getWorkspaceId).filter(Objects::nonNull).distinct().collect(toList());
        Map<Long, WorkspaceLocationResponse> workspaceMap = workspaceLocationService.getMapByIds(workspaceIds);
        Date now = new Date();
        exportResponseList.forEach(item -> {
            item.init(now, userMap, deptMap, categoryMap, itemMap, workspaceMap);
            if (ObjectUtil.isNotEmpty(request.getIsShowExtendField())) {
                if (!request.getIsShowExtendField().contains(NumberConstant.ONE)) {
                    item.setSubmitExtendField(null);
                } else {
                    item.setSubmitExtendDatas(DataUtils.convertFormField(item.getSubmitExtendField()));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.TWO)) {
                    item.setResponseExtendField(null);
                } else {
                    item.setResponseExtendDatas(DataUtils.convertFormField(item.getResponseExtendField()));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.THREE)) {
                    item.setHandingExtendField(null);
                } else {
                    item.setHandingExtendDatas(DataUtils.convertFormField(item.getHandingExtendField()));
                }
                if (!request.getIsShowExtendField().contains(NumberConstant.SIX)) {
                    item.setSuspendExtendField(null);
                } else {
                    item.setSuspendExtendDatas(DataUtils.convertFormField(item.getSuspendExtendField()));
                }
            }
        });
        return exportResponseList;
    }

}