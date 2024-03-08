package com.framework.emt.system.domain.messages.service;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.common.auth.entity.FtUser;
import com.framework.core.template.ITemplateService;
import com.framework.emt.system.domain.messages.MessageTemplate;
import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.constant.enums.NoticeLevel;
import com.framework.emt.system.domain.messages.request.MessageCreate;
import com.framework.emt.system.domain.messages.request.MessageCreateRequest;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.TemplateCodeConstant;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常任务消息推送 实现类
 *
 * @author ds_C
 * @since 2023-08-22
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessagePushServiceImpl implements MessagePushService {

    private final IMessageService messageService;

    private final IMessageTemplateService messageTemplateService;

    private final ITemplateService templateService;

    @Override
    public void submitSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.SUBMIT);

        // 装载消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.SUBMIT.getCode();

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        List<MessageCreate> list = new ArrayList<>();
        for (Long receiveUserId :
                receiveUserIds) {
            MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);
            list.add(messageCreate);

        }
        messageCreateRequest.setMessageCreateList(list);
        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void responseRejectSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.RESPONSE_REJECT);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.RESPONSE.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void responseToOtherSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.RESPONSE_TO_OTHER);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.RESPONSE.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void responseSubmitSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.RESPONSE_SUBMIT);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.RESPONSE.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingAcceptSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_ACCEPT);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.HANDING.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingRejectToSubmitNodeSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_REJECT_TO_SUBMIT_NODE);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.HANDING.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingRejectToResponseNodeSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_REJECT_TO_RESPONSE_NODE);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.HANDING.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingToOtherSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_TO_OTHER);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.HANDING.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingCreateCooperationSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_CREATE_COOPERATION);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.HANDING.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingSubmitSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId) {
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            return;
        }

        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_SUBMIT);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        List<MessageCreate> messageCreateList = new ArrayList<>();
        for (Long receiveUserId : receiveUserIds) {
            Long sendUserId = user.getUserId();
            Long templateId = messageTemplate.getId();
            Integer level = NoticeLevel.NORMAL.getCode();
            Integer businessType = ExceptionType.HANDING.getCode();
            MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);
            messageCreateList.add(messageCreate);
        }

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(messageCreateList);

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void handingSuspendSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId) {
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            return;
        }

        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.HANDING_SUSPEND);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        List<MessageCreate> messageCreateList = new ArrayList<>();
        for (Long receiveUserId : receiveUserIds) {
            Long sendUserId = user.getUserId();
            Long templateId = messageTemplate.getId();
            Integer level = NoticeLevel.NORMAL.getCode();
            Integer businessType = ExceptionType.HANDING.getCode();
            MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);
            messageCreateList.add(messageCreate);
        }

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(messageCreateList);

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void checkRejectSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.CHECK_REJECT);

        // 创建消息模板
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(null, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.CHECK.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void checkPassSend(FtUser user, Long taskId, String itemTitle, List<Long> receiveUserIds, Long businessId) {
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            return;
        }

        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.CHECK_PASS);

        // 创建消息模板
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(null, code, itemTitle, template);

        List<MessageCreate> messageCreateList = new ArrayList<>();
        for (Long receiveUserId : receiveUserIds) {
            Long sendUserId = user.getUserId();
            Long templateId = messageTemplate.getId();
            Integer level = NoticeLevel.NORMAL.getCode();
            Integer businessType = ExceptionType.CHECK.getCode();
            MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);
            messageCreateList.add(messageCreate);
        }

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(messageCreateList);

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void cooperationTransferSend(FtUser user, Long taskId, String itemTitle, Long receiveUserId, Long businessId) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(TemplateCodeConstant.COOPERATION_TRANSFER);

        // 创建消息模板
        String operator = user.getUserName();
        String code = String.valueOf(taskId);
        String template = messageTemplate.getContent();
        String message = loadTemplate(operator, code, itemTitle, template);

        Long sendUserId = user.getUserId();
        Long templateId = messageTemplate.getId();
        Integer level = NoticeLevel.NORMAL.getCode();
        Integer businessType = ExceptionType.COOPERATION.getCode();
        MessageCreate messageCreate = init(receiveUserId, businessId, message, sendUserId, templateId, level, businessType);

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送消息
        messageService.send(messageCreateRequest);
    }

    /**
     * 创建消息模板
     *
     * @param operator        操作人
     * @param code            异常编号
     * @param itemTitle       异常项
     * @param templateContent 消息模板
     * @return
     */
    private String loadTemplate(String operator, String code, String itemTitle, String templateContent) {
        Map<String, String> hashMap = new HashMap<>(NumberConstant.THREE);
        if (operator != null) {
            hashMap.put("operator", operator);
        }
        if (code != null) {
            hashMap.put("code", code);
        }
        if (itemTitle != null) {
            hashMap.put("item", itemTitle);
        }
        return templateService.render(hashMap, templateContent);
    }

    /**
     * 初始化消息创建参数
     *
     * @param receiveUserId 接收人id
     * @param businessId    业务id
     * @param message       消息内容
     * @param sendUserId    发送人id
     * @param templateId    模版id
     * @param level         通知级别
     * @param businessType  业务类型
     * @return
     */
    private MessageCreate init(Long receiveUserId, Long businessId, String message, Long sendUserId,
                               Long templateId, Integer level, Integer businessType) {
        MessageCreate messageCreate = new MessageCreate();
        messageCreate.setMessageTemplateId(templateId);
        messageCreate.setContent(message);
        messageCreate.setSendUserId(sendUserId);
        messageCreate.setReceiveUserId(receiveUserId);
        messageCreate.setBusinessId(businessId);
        messageCreate.setLevel(level);
        messageCreate.setBusinessType(businessType);
        return messageCreate;
    }

}
