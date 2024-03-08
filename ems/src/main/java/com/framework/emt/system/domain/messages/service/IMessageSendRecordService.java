package com.framework.emt.system.domain.messages.service;


import com.framework.emt.system.domain.login.request.DingTalkMessageRequest;
import com.framework.emt.system.domain.messages.MessageSendRecord;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

/**
 * 消息发送记录 服务层
 *
 * @author yankunw
 * @since 2023-07-19
 */
public interface IMessageSendRecordService extends BaseService<MessageSendRecord> {

    void process(MessageToBeSendResponse sendResponse);

    void start();

    boolean suspend();

    void sendByMessageId(Long id);

    /**
     * 钉钉消息发送
     *
     * @param request 参数
     * @return 消息是否成功
     */
    String dingTalkMessage(DingTalkMessageRequest request);
}
