package com.framework.emt.system.domain.reportnoticeprocess.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcess;
import com.framework.emt.system.domain.reportnoticeprocess.constant.code.ReportNoticeProcessErrorCode;
import com.framework.emt.system.domain.reportnoticeprocess.convert.ReportNoticeProcessConvert;
import com.framework.emt.system.domain.reportnoticeprocess.mapper.ReportNoticeProcessMapper;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessCreateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUpdateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportAndChildResponse;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 上报流程 实现类
 *
 * @author ds_C
 * @since 2023-07-17
 */
@Service
@RequiredArgsConstructor
public class ReportNoticeProcessServiceImpl extends BaseServiceImpl<ReportNoticeProcessMapper, ReportNoticeProcess> implements ReportNoticeProcessService {

    private final ReportNoticeProcessUserService reportNoticeProcessUserService;

    private final UserService userService;

    @Override
    public Long create(ReportNoticeProcessCreateRequest request) {
        // 校验名称必须唯一
        checkNameUnique(null, request.getName());
        return this.create(ReportNoticeProcessConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    @DSTransactional
    public void delete(Long id) {
        // 校验上报流程是否被引用
        ReportAndChildResponse response = this.baseMapper.findReportAndExistChild(id);
        if (ObjectUtil.isNull(response)) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        if (response.getHasChildren()) {
            throw new ServiceException(ReportNoticeProcessErrorCode.REPORT_PROCESS_BE_EXCEPTION_PROCESS_CITE_CAN_NOT_DELETE);
        }
        this.deleteById(id);
        // 删除上报流程下的上报人及上报消息
        reportNoticeProcessUserService.deleteByProcessId(id);
    }

    @Override
    @DSTransactional
    public void deleteBatch(List<Long> ids) {
        // 校验上报流程是否被引用
        ReportAndChildResponse response = this.baseMapper.findReportCountAndExistChild(ids);
        if (response.getReportCount() != ids.size()) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        if (response.getExceptionProcessCount() > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ReportNoticeProcessErrorCode.REPORT_PROCESS_BE_EXCEPTION_PROCESS_CITE_CAN_NOT_DELETE);
        }
        this.removeByIds(ids);
        // 删除上报流程下的上报人及上报消息
        reportNoticeProcessUserService.deleteByProcessIds(ids);
    }

    @Override
    public Long update(Long id, ReportNoticeProcessUpdateRequest request) {
        ReportNoticeProcess reportNoticeProcess = findByIdThrowErr(id);
        // 校验名称必须唯一
        checkNameUnique(id, request.getName());

        return this.update(ReportNoticeProcessConvert.INSTANCE.updateRequestToEntity(reportNoticeProcess, request));
    }

    @Override
    public ReportNoticeProcessResponse detail(Long id) {
        return this.baseMapper.detail(findByIdThrowErr(id).getId());
    }

    @Override
    public IPage<ReportNoticeProcessResponse> page(ReportNoticeProcessQueryRequest request) {
        return this.loadUserInfoList(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public void changeEnableFlag(Integer enableFlag, List<Long> ids) {
        long count = this.count(new LambdaQueryWrapper<ReportNoticeProcess>().in(ReportNoticeProcess::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.STATE_CHANGE_FAIL_DATA_NOT_EXIST_CAN_NOT_CHANGE);
        }
        this.update(new LambdaUpdateWrapper<ReportNoticeProcess>().set(ReportNoticeProcess::getEnableFlag, enableFlag).in(ReportNoticeProcess::getId, ids));
    }

    @Override
    public ReportNoticeProcess findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ReportNoticeProcessErrorCode.REPORT_NOTICE_PROCESS_INFO_NOT_FIND);
    }

    @Override
    public void checkExist(Long responseReportNoticeProcessId, Long handingReportNoticeProcessId) {
        this.findByIdThrowErr(responseReportNoticeProcessId, ReportNoticeProcessErrorCode.RESPONSE_REPORT_NOTICE_PROCESS_INFO_NOT_FIND);
        this.findByIdThrowErr(handingReportNoticeProcessId, ReportNoticeProcessErrorCode.HANDING_REPORT_NOTICE_PROCESS_INFO_NOT_FIND);
    }

    @Override
    public IPage<ReportNoticeProcessResponse> loadUserInfoList(IPage<ReportNoticeProcessResponse> pageResult) {
        // 分页结果集为空，则不装载上报人列表
        List<ReportNoticeProcessResponse> pageList = pageResult.getRecords();
        if (CollectionUtil.isEmpty(pageList)) {
            return pageResult;
        }
        // 上报人id列表为空，则不装载上报人列表
        List<String> allReceiveUserIds = pageList.stream()
                .flatMap(report -> Arrays.stream(report.getReceiveUserIds().split(StrUtil.COMMA)))
                .distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(allReceiveUserIds)) {
            return DataHandleUtils.loadUserName(pageResult);
        }
        // 装载每条上报流程对应的上报人列表
        Map<String, String> userInfoMap = userService.findStrIdNameMap(allReceiveUserIds);
        pageList.forEach(report -> {
            List<String> receiveUserIds = DataHandleUtils.splitStr(report.getReceiveUserIds());
            report.setUserInfoList(reportNoticeProcessUserService.getUserInfoList(receiveUserIds, userInfoMap));
        });
        return DataHandleUtils.loadUserName(pageResult);
    }

    /**
     * 校验上报流程名称在数据库中不能存在
     *
     * @param id   主键id
     * @param name 上报流程名称
     */
    private void checkNameUnique(Long id, String name) {
        LambdaQueryWrapper<ReportNoticeProcess> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(ReportNoticeProcess::getId, id);
        }
        queryWrapper.eq(ReportNoticeProcess::getName, name);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ReportNoticeProcessErrorCode.REPORT_NOTICE_PROCESS_NAME_IS_EXIST);
        }
    }

}
