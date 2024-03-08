package com.framework.emt.system.domain.messages.service;

import com.framework.common.auth.entity.FtUser;

import java.util.List;

/**
 * 异常任务消息推送 服务层
 *
 * @author ds_C
 * @since 2023-08-22
 */
public interface MessagePushService {

    /**
     * 异常提报-提报
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void submitSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserId, Long businessId);

    /**
     * 异常响应-驳回
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void responseRejectSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常响应-转派
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void responseToOtherSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常响应-设置处理人
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void responseSubmitSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-接受
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void handingAcceptSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-驳回/异常提报节点
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void handingRejectToSubmitNodeSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-驳回/异常响应节点
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void handingRejectToResponseNodeSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-转派
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void handingToOtherSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-设置协同
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void handingCreateCooperationSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常处理-提交验收
     *
     * @param user           当前用户
     * @param taskId         异常任务id
     * @param itemTitle      异常项名称
     * @param receiveUserIds 接收人id列表
     * @param businessId     业务id
     */
    void handingSubmitSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId);

    /**
     * 异常处理-申请挂起
     *
     * @param user           当前用户
     * @param taskId         异常任务id
     * @param itemTitle      异常项名称
     * @param receiveUserIds 接收人id列表
     * @param businessId     业务id
     */
    void handingSuspendSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId);

    /**
     * 异常验收-驳回
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void checkRejectSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

    /**
     * 异常验收-验收
     *
     * @param user           当前用户
     * @param taskId         异常任务id
     * @param itemTitle      异常项名称
     * @param receiveUserIds 接收人id列表
     * @param businessId     业务id
     */
    void checkPassSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId);

    /**
     * 异常协同-转派
     *
     * @param user          当前用户
     * @param taskId        异常任务id
     * @param itemTitle     异常项名称
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     */
    void cooperationTransferSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId);

}
