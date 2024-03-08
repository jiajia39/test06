package com.framework.emt.system.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常任务节点枚举
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Getter
@AllArgsConstructor
public enum ExceptionTaskNode {

    /**
     * 异常任务节点枚举 提报、响应、处理、协同、验收
     */
    SUBMIT("submit", "提报节点"),
    RESPONSE("response", "响应节点"),
    HANDING("handing", "处理节点"),
    COOPERATION("cooperation", "协同节点"),
    CHECK("check", "验收节点");

    final String bizId;

    final String desc;

}
