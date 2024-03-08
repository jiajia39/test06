package com.framework.emt.system.domain.reportnoticeprocess.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcessUser;
import com.framework.emt.system.domain.reportnoticeprocess.constant.code.ReportNoticeProcessUserErrorCode;
import com.framework.emt.system.domain.reportnoticeprocess.convert.ReportNoticeProcessUserConvert;
import com.framework.emt.system.domain.reportnoticeprocess.mapper.ReportNoticeProcessUserMapper;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserCreateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserUpdateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.domain.user.code.UserErrorCode;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
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
 * 上报流程推送 实现类
 *
 * @author ds_C
 * @since 2023-07-24
 */
@Service
@RequiredArgsConstructor
public class ReportNoticeProcessUserServiceImpl extends BaseServiceImpl<ReportNoticeProcessUserMapper, ReportNoticeProcessUser> implements ReportNoticeProcessUserService {

    private final UserService userService;

    @Override
    public Long create(ReportNoticeProcessUserCreateRequest request) {
        // 校验上报人数量
        checkReceiveUserNum(request.getReceiveUserIds().size());
        // 校验上报流程必须存在
        ReportNoticeProcessService reportNoticeProcessService = SpringUtil.getBean(ReportNoticeProcessService.class);
        reportNoticeProcessService.findByIdThrowErr(request.getReportNoticeProcessId());
        // 校验同一个上报流程下的上报时限必须唯一
        checkSameReportTimeLimitUnique(null, request.getReportNoticeProcessId(), request.getTimeLimit());
        // 校验所有上报人都存在
        userService.checkUserExist(request.getReceiveUserIds(), UserErrorCode.RECEIVER_USER_INFO_NOT_FIND);

        return this.create(ReportNoticeProcessUserConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    public void delete(Long id) {
        findByIdThrowErr(id);
        this.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 校验上报人及上报消息都存在
        long count = this.count(new LambdaQueryWrapper<ReportNoticeProcessUser>()
                .in(ReportNoticeProcessUser::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.DELETE_FAIL_NOT_EXIST_DATA_CAN_NOT_DELETE);
        }
        this.removeByIds(ids);
    }

    @Override
    public Long update(Long id, ReportNoticeProcessUserUpdateRequest request) {
        ReportNoticeProcessUser reportNoticeProcessUser = findByIdThrowErr(id);
        // 校验上报人数量
        checkReceiveUserNum(request.getReceiveUserIds().size());
        // 校验同一个上报流程下的上报时限必须唯一
        checkSameReportTimeLimitUnique(id, reportNoticeProcessUser.getReportNoticeProcessId(), request.getTimeLimit());
        // 校验所有上报人都存在
        userService.checkUserExist(request.getReceiveUserIds(), UserErrorCode.RECEIVER_USER_INFO_NOT_FIND);

        return this.update(ReportNoticeProcessUserConvert.INSTANCE.updateRequestToEntity(reportNoticeProcessUser, request));
    }

    @Override
    public ReportNoticeProcessResponse detail(Long id) {
        ReportNoticeProcessResponse detailResult = this.baseMapper.detail(findByIdThrowErr(id).getId());
        List<String> userIds = DataHandleUtils.splitStr(detailResult.getReceiveUserIds());
        if (CollectionUtil.isEmpty(userIds)) {
            return detailResult;
        }
        detailResult.setUserInfoList(getUserInfoList(userIds, userService.findStrIdNameMap(userIds)));
        return detailResult;
    }

    @Override
    public IPage<ReportNoticeProcessResponse> page(ReportNoticeProcessUserQueryRequest request) {
        ReportNoticeProcessService reportNoticeProcessService = SpringUtil.getBean(ReportNoticeProcessService.class);
        return reportNoticeProcessService.loadUserInfoList(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public void deleteByProcessId(Long processId) {
        this.remove(new LambdaQueryWrapper<ReportNoticeProcessUser>().eq(ReportNoticeProcessUser::getReportNoticeProcessId, processId));
    }

    @Override
    public void deleteByProcessIds(List<Long> processIds) {
        this.remove(new LambdaQueryWrapper<ReportNoticeProcessUser>().in(ReportNoticeProcessUser::getReportNoticeProcessId, processIds));
    }

    @Override
    public List<UserInfoResponse> getUserInfoList(List<String> userIds, Map<String, String> userInfoMap) {
        return userIds.stream().filter(userInfoMap::containsKey).map(userId -> new UserInfoResponse(userId, userInfoMap.get(userId))).collect(Collectors.toList());
    }

    @Override
    public List<ReportNoticeProcessResponse> findByProcessId(Long processId) {
        return this.baseMapper.findByReportNoticeProcessId(processId);
    }

    @Override
    public List<Long> findUserIdsByProcessId(Long processId) {
        List<ReportNoticeProcessUser> reportUserList = this.list(new LambdaQueryWrapper<ReportNoticeProcessUser>()
                .select(ReportNoticeProcessUser::getReceiveUserIds)
                .eq(ReportNoticeProcessUser::getReportNoticeProcessId, processId));
        return reportUserList.stream().flatMap(user -> Arrays.stream(user.getReceiveUserIds().split(StrUtil.COMMA)))
                .distinct().map(Long::parseLong).collect(Collectors.toList());
    }

    /**
     * 根据主键id查询上报流程推送信息
     * 如果未查询相关数据则抛出异常信息
     *
     * @param id 主键id
     * @return
     */
    private ReportNoticeProcessUser findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ReportNoticeProcessUserErrorCode.REPORT_NOTICE_PROCESS_USER_INFO_NOT_FIND);
    }

    /**
     * 校验上报人数量不能超过系统指定的数量
     *
     * @param receiveUserNum 上报人数量
     */
    private void checkReceiveUserNum(int receiveUserNum) {
        Integer userMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.REPORT_NOTICE_PROCESS_SETTING_REPORT_USER_MAX_COUNT,
                AuthUtil.getTenantId()), ExceptionTaskConstant.REPORT_NOTICE_PROCESS_SETTING_REPORT_USER_DEFAULT_MAX_COUNT);
        if (receiveUserNum >= userMaxCount) {
            throw new ServiceException(ReportNoticeProcessUserErrorCode.THE_NUMBER_OF_REPORTED_PERSONNEL_HAS_EXCEEDED_THE_MAXIMUM_VALUE);
        }
    }

    /**
     * 校验同一个上报流程下的上报时限必须唯一
     *
     * @param id              上报流程推送主键id
     * @param reportProcessId 上报流程id
     * @param timeLimit       超出时限不能为空
     */
    private void checkSameReportTimeLimitUnique(Long id, Long reportProcessId, Integer timeLimit) {
        LambdaQueryWrapper<ReportNoticeProcessUser> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(ReportNoticeProcessUser::getId, id);
        }
        queryWrapper.eq(ReportNoticeProcessUser::getTimeLimit, timeLimit);
        queryWrapper.eq(ReportNoticeProcessUser::getReportNoticeProcessId, reportProcessId);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ReportNoticeProcessUserErrorCode.SAME_REPORT_NOTICE_PROCESS_OF_TIME_LIMIT_MUST_UNIQUE);
        }
    }

}
