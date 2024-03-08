package com.framework.emt.system.infrastructure.exception.task.record.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 操作类型
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum TaskRecordType implements BaseEnum<TaskRecordType> {

    /**
     * 履历节点 提报、响应、处理、验收
     */
    CREATE(0, "创建"),

    UPDATE(1, "编辑"),

    DELETE(2, "删除"),

    SUBMIT(3, "提交"),

    CLOSE(4, "关闭"),

    TRANSFER(5, "转派"),

    REJECT(6, "驳回"),

    ACCEPT(7, "接受"),

    CREATE_SUB(8, "设置协同"),

    SUSPEND(9, "挂起"),

    SUSPEND_DELAY(10, "挂起延期"),

    RESUME_HAND(11, "手动恢复"),

    RESUME_AUTO(12, "自动恢复"),

    NOTICE(13, "上报通知"),

    EXPIRED(14, "超时"),

    COLLABORATIVE_PROCESSING(15, "协同处理"),

    COLLABORATIVE_TRANSFER(16, "协同转派"),

    COLLABORATIVE_SUBMISSION_PROCESSING(17, "协同创建"),

    SET_HANDLER(18, "设置处理人"),

    FINISH(19, "已完成"),

    CANCEL(20, "撤销"),

    RESPONSE_TIMEOUT_NOTICE(21, "响应超时上报"),

    RESPONSE_TIMEOUT(22, "响应超时"),

    HANDING_TIMEOUT_NOTICE(23, "处理超时上报"),

    HANDING_TIMEOUT(24, "处理超时"),

    COOPERATION_TIMEOUT_NOTICE(25, "协同超时上报"),

    COOPERATION_TIMEOUT(26, "协同超时");


    /**
     * code编码
     */
    @EnumValue
    @JsonValue
    final Integer code;

    /**
     * 中文信息描述
     */
    final String name;

    /**
     * 获取异常挂起相关枚举code
     *
     * @return
     */
    public static List<Integer> getSuspendCodes() {
        return Lists.newArrayList(SUSPEND.getCode(), SUSPEND_DELAY.getCode(), RESUME_HAND.getCode(), RESUME_AUTO.getCode());
    }

    /**
     * 是否属于转派类型
     *
     * @param type 操作类型
     * @return
     */
    public static boolean isTransfer(Integer type) {
        return TRANSFER.getCode().equals(type) || COLLABORATIVE_TRANSFER.getCode().equals(type);
    }

    /**
     * 是否属于超时、转派、撤销
     *
     * @param type 操作类型
     * @return
     */
    public static boolean isTimeoutRejectCancel(Integer type) {
        return EXPIRED.getCode().equals(type) || RESPONSE_TIMEOUT_NOTICE.getCode().equals(type) || RESPONSE_TIMEOUT.getCode().equals(type)
                || HANDING_TIMEOUT_NOTICE.getCode().equals(type) || HANDING_TIMEOUT.getCode().equals(type) || COOPERATION_TIMEOUT_NOTICE.getCode().equals(type)
                || COOPERATION_TIMEOUT.getCode().equals(type) || REJECT.getCode().equals(type) || CANCEL.getCode().equals(type);
    }

}
