package com.framework.emt.system.infrastructure.exception.task.task;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 异常任务表 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emt_exception_task")
public class ExceptionTask extends TenantEntity {

    /**
     * 异常任务配置id
     */
    private Long exceptionTaskSettingId;

    /**
     * 异常编号 自动生成: YC+年月日时分秒+毫秒+随机四位数
     */
    private String code;

    /**
     * 异常状态
     */
    private ExceptionStatus taskStatus;

    /**
     * 异常子状态
     */
    private ExceptionSubStatus taskSubStatus;

    /**
     * 提报版本号 重新提报自增1
     */
    private Integer submitVersion;

    /**
     * 异常关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 响应最后期限
     */
    private LocalDateTime responseDeadline;

    /**
     * 处理最后期限
     */
    private LocalDateTime handingDeadline;

    /**
     * 响应版本号
     */
    private Integer responseVersion;

    /**
     * 响应转派次数
     */
    private Integer responseOtherCount;

    /**
     * 响应总时长 秒
     */
    private Long responseTotalSecond;

    /**
     * 响应驳回次数
     */
    private Integer responseRejectNum;

    /**
     * 处理版本号
     */
    private Integer handingVersion;

    /**
     * 处理转派次数
     */
    private Integer handingOtherCount;

    /**
     * 处理挂起次数
     */
    private Integer handingSuspendNum;

    /**
     * 处理挂起总时长
     */
    private long handingSuspendTotalSecond;

    /**
     * 处理总时长 秒
     */
    private long handingTotalSecond;

    /**
     * 处理驳回次数
     */
    private Integer handingRejectNum;

    /**
     * 验收版本号
     */
    private Integer checkVersion;

    /**
     * 验收总时长 秒
     */
    private long checkTotalSecond;

    /**
     * 待验收数目
     */
    private Integer checkCount;

    /**
     * 已验收数目
     */
    private Integer checkedCount;

    /**
     * 验收结束时间
     */
    private LocalDateTime finishTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 异常任务创建
     *
     * @param taskSettingId 异常任务配置id
     * @param status        异常状态
     * @param subStatus     异常子状态
     * @return
     */
    public ExceptionTask init(Long taskSettingId, ExceptionStatus status, ExceptionSubStatus subStatus) {
        this.exceptionTaskSettingId = taskSettingId;
        this.taskStatus = status;
        this.taskSubStatus = subStatus;
        this.submitVersion = 1;
        return this;
    }


    /**
     * 初始化提交数据
     *
     * @param responseDurationTime 响应时限 （分钟）
     * @param handlingDurationTime 处理时限 （分钟）
     * @param submitDate           提交时间
     */
    public void submit(Integer responseDurationTime, Integer handlingDurationTime, LocalDateTime submitDate) {
        taskStatus = ExceptionStatus.PRE_RESPONSE;
        taskSubStatus = ExceptionSubStatus.PRE_RESPONSE;
        responseVersion++;
        // 更新响应时限/ 处理时限 当前时间+响应时限
        responseDeadline = LocalDateTimeUtil.offset(submitDate, responseDurationTime, ChronoUnit.MINUTES);
        handingDeadline = LocalDateTimeUtil.offset(submitDate, handlingDurationTime, ChronoUnit.MINUTES);
    }

    public ExceptionTask close(@NonNull LocalDateTime closeDate) {
        taskStatus = ExceptionStatus.CLOSE;
        taskSubStatus = ExceptionSubStatus.CLOSE;
        closeTime = closeDate;
        return this;
    }

    //提报撤销
    public ExceptionTask cancel() {
        taskStatus = ExceptionStatus.CANCEL;
        taskSubStatus = ExceptionSubStatus.CANCEL;
        return this;
    }

    // 响应驳回
    public void responseReject() {
        taskStatus = ExceptionStatus.SUBMIT_REJECT;
        taskSubStatus = ExceptionSubStatus.SUBMIT_REJECT_RESPONSE;
        submitVersion++;
    }

    //响应转派
    public void responseTransfer() {
        responseOtherCount++;
    }

    //接受响应
    public void responseAccept() {
        taskStatus = ExceptionStatus.RESPONDING;
        taskSubStatus = ExceptionSubStatus.RESPONDING;
    }

    // 响应提交
    public void responseSubmit(LocalDateTime createTime, LocalDateTime submitTime) {
        taskStatus = ExceptionStatus.PRE_HANDING;
        taskSubStatus = ExceptionSubStatus.PRE_HANDING;
        responseTotalSecond += LocalDateTimeUtil.between(createTime, submitTime, ChronoUnit.SECONDS);
        handingVersion++;
    }

    /**
     * 处理驳回信息
     *
     * @param isSubmitNode 是否驳回到提报节点
     */
    public ExceptionTask handingReject(boolean isSubmitNode) {
        if (isSubmitNode) {
            taskStatus = ExceptionStatus.SUBMIT_REJECT;
            taskSubStatus = ExceptionSubStatus.SUBMIT_REJECT_HANDING;
            submitVersion++;
        } else {
            taskStatus = ExceptionStatus.RESPONSE_REJECT;
            taskSubStatus = ExceptionSubStatus.RESPONSE_REJECT;
            responseVersion++;
            responseRejectNum++;
        }
        return this;
    }


    // 处理转派更新
    public ExceptionTask handingToOther() {
        handingOtherCount++;
        return this;
    }

    // 处理接受
    public ExceptionTask handingAccept() {
        taskStatus = ExceptionStatus.HANDING;
        taskSubStatus = ExceptionSubStatus.HANDING;
        return this;
    }

    // 处理挂起
    public ExceptionTask handingSuspend() {
        taskStatus = ExceptionStatus.SUSPEND;
        taskSubStatus = ExceptionSubStatus.SUSPEND;
        handingSuspendNum++;
        return this;
    }

    // 处理挂起恢复
    public ExceptionTask handingResume(long suspendSeconds) {
        taskStatus = ExceptionStatus.HANDING;
        taskSubStatus = ExceptionSubStatus.HANDING;
        // 挂起总时长新增
        handingSuspendTotalSecond += suspendSeconds;
        // 处理最后时限 在原有时限基础上 延迟本次挂起的总时间
        handingDeadline = LocalDateTimeUtil.offset(handingDeadline, suspendSeconds, ChronoUnit.SECONDS);
        return this;
    }


    // 处理申请验收
    public void handingSubmit(Long handingSecond, int checkNumber) {
        checkVersion++;
        taskStatus = ExceptionStatus.CHECK;
        taskSubStatus = ExceptionSubStatus.CHECK;
        checkCount = checkNumber;
        checkedCount = 0;
        this.handingTotalSecond += handingSecond;
    }

    //验收驳回
    public void checkReject() {
        handingVersion++;
        handingRejectNum++;
        taskStatus = ExceptionStatus.HANDING_REJECT;
        taskSubStatus = ExceptionSubStatus.HANDING_REJECT;
    }

    //某一个验收信息验收通过
    public void checkPassOne() {
        checkedCount++;
        checkCount--;
    }

    //所有的验收信息都验收通过
    public void checkAllPass(LocalDateTime createTime, LocalDateTime submitTime) {
        this.checkedCount++;
        this.checkCount--;
        finishTime = submitTime;
        taskStatus = ExceptionStatus.FINISH;
        taskSubStatus = ExceptionSubStatus.FINISH;
        closeTime = submitTime;
        this.checkTotalSecond += LocalDateTimeUtil.between(createTime, submitTime, ChronoUnit.SECONDS);
    }

    // 异常关闭
    public ExceptionTask discontinue() {
        taskStatus = ExceptionStatus.CLOSE;
        taskSubStatus = ExceptionSubStatus.CLOSE;
        return this;
    }
}
