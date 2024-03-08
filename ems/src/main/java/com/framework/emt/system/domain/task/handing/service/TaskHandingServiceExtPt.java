package com.framework.emt.system.domain.task.handing.service;

import com.alibaba.cola.extension.ExtensionPointI;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.handing.request.*;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import org.springframework.lang.NonNull;

/**
 * 异常处理服务接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
public interface TaskHandingServiceExtPt extends ExtensionPointI {

    /**
     * 处理驳回
     *
     * @param exceptionHandingId 异常处理id
     * @param rejectRequest      驳回请求参数
     */
    void reject(@NonNull Long exceptionHandingId, HandingRejectRequest rejectRequest);

    /**
     * 处理转派
     *
     * @param exceptionHandingId 异常处理id
     * @param otherUserId        转派接受人id
     * @param otherRemark        转派备注
     */
    void toOther(@NonNull Long exceptionHandingId, Long otherUserId, String otherRemark);

    /**
     * 管理员进行处理转派
     *
     * @param taskId      任务id
     * @param userId      转派用户id
     * @param otherRemark 转派备注
     */
    void adminToOther(Long taskId, Long userId, String otherRemark);

    /**
     * 处理接受
     *
     * @param exceptionHandingId 异常处理id
     */
    void accept(@NonNull Long exceptionHandingId);

    /**
     * 申请验收
     *
     * @param exceptionHandingId 处理id
     * @param request            提交处理请求参数
     */
    void update(@NonNull Long exceptionHandingId, HandingUpdateRequest request);

    /**
     * 提交验收
     *
     * @param exceptionHandingId 处理id
     */
    void submit(@NonNull Long exceptionHandingId, HandingSubmitRequest request);

    /**
     * 处理挂起
     *
     * @param exceptionHandingId 异常处理id
     * @param request            挂起请求参数
     */
    void suspend(@NonNull Long exceptionHandingId, HandingSuspendRequest request);

    /**
     * 处理挂起延期
     *
     * @param exceptionHandingId 异常处理id
     * @param request            挂起延期请求参数
     */
    void suspendDelay(@NonNull Long exceptionHandingId, HandingSuspendDelayRequest request);

    /**
     * 处理挂起恢复
     *
     * @param exceptionHandingId 异常处理id
     * @param resumeType         挂起恢复类型
     */
    void resume(@NonNull Long exceptionHandingId, TaskResumeType resumeType);

    /**
     * 异常协同创建
     *
     * @param request
     * @param user    当前登录用户
     * @return
     */
    void createCooperation(TaskCooperationCreateRequest request, FtUser user);

    /**
     * 管理员--申请验收
     *
     * @param exceptionHandingId 处理id
     * @param request            提交处理请求参数
     */
    void administratorUpdate(@NonNull Long exceptionHandingId, HandingUpdateRequest request);


    /**
     * 管理员--提交验收
     *
     * @param exceptionHandingId 处理id
     */
    void administratorSubmit(@NonNull Long exceptionHandingId, HandingSubmitRequest request);

    /**
     * 自动恢复挂起
     *
     * @param id 处理id
     */
    void updateSuspendByHanding(Long id);
}
