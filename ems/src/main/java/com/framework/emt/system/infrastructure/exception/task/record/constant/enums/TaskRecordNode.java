package com.framework.emt.system.infrastructure.exception.task.record.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.code.ExceptionTaskHandingErrorCode;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 履历节点 枚举
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum TaskRecordNode implements BaseEnum<TaskRecordNode> {

    /**
     * 履历节点
     */
    SUBMIT(0, "提报节点"),
    RESPONSE(1, "响应节点"),
    HANDING(2, "处理节点"),
    CHECK(3, "验收节点");

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
     * 获取处理驳回节点
     *
     * @param code 驳回节点编码
     * @return 处理驳回后的节点
     */
    public static TaskRecordNode getHandingRejectNode(Integer code) {
        TaskRecordNode taskRecordNode = BaseEnum.parseByCode(TaskRecordNode.class, code);
        if (taskRecordNode == null) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.REJECT_NODE_ERROR);
        }
        if (!taskRecordNode.equals(SUBMIT) && !taskRecordNode.equals(RESPONSE)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.REJECT_NODE_ERROR);
        }
        return taskRecordNode;
    }

    /**
     * 获取响应节点和处理节点code
     *
     * @return
     */
    public static List<Integer> getResponseHandingCodes() {
        return Lists.newArrayList(RESPONSE.getCode(), HANDING.getCode());
    }

}
