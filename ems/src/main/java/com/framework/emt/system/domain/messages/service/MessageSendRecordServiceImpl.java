package com.framework.emt.system.domain.messages.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.login.ThirdPartyInfo;
import com.framework.emt.system.domain.login.request.DingTalkMessageRequest;
import com.framework.emt.system.domain.login.service.ILoginService;
import com.framework.emt.system.domain.login.service.IThirdPartyInfoService;
import com.framework.emt.system.domain.messages.Message;
import com.framework.emt.system.domain.messages.MessageSendRecord;
import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.constant.enums.NoticeLevel;
import com.framework.emt.system.domain.messages.mapper.MessageSendRecordMapper;
import com.framework.emt.system.domain.messages.request.UniPushMessageRequest;
import com.framework.emt.system.domain.messages.request.UniPushTransparentMessage;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.infrastructure.config.DingTalkConfig;
import com.framework.emt.system.infrastructure.config.MessageConfig;
import com.framework.emt.system.infrastructure.config.MessagePushConfig;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.PlatformType;
import com.framework.emt.system.infrastructure.constant.enums.SendChannelEnum;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.UniPushUtil;
import com.framework.wechat.cp.config.WxCpConfig;
import com.framework.wechat.cp.service.IWxCpCustomService;
import com.framework.wechat.mp.config.WxMpConfig;
import com.framework.wechat.mp.service.IWxMpCustomService;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpMessageService;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息发送记录 实现类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
//开启定时任务
@EnableScheduling
@Slf4j
public class MessageSendRecordServiceImpl extends BaseServiceImpl<MessageSendRecordMapper, MessageSendRecord> implements IMessageSendRecordService {
    private ExecutorService threadPool;
    private boolean isRunning = false;

    private final MessageConfig messageConfig;
    private final MessagePushConfig messagePushConfig;

    private final IWxMpCustomService wxMpCustomService;

    private final WxMpConfig wxMpConfig;

    private final IThirdPartyInfoService thirdPartyInfoService;

    private final DingTalkConfig dingTalkConfig;

    private final IWxCpCustomService wxCpCustomService;

    private final WxCpConfig wxCpConfig;

    public synchronized boolean suspend() {
        isRunning = false;
        return true;
    }

    @Override
    public synchronized void start() {
        if (!isRunning) {
            isRunning = true;
            if (threadPool == null) {
                log.info("开始启动消息推送的处理进程");
                if (messageConfig.getThreadCount() == null || messageConfig.getThreadCount() < 5) {
                    messageConfig.setThreadCount(5);
                }
                log.info("开始启动消息推送的处理进程的线程池大小设置为：" + messageConfig.getThreadCount());
                threadPool = Executors.newFixedThreadPool(messageConfig.getThreadCount());
                log.info("启动消息推送的处理进程，成功！");
            }
        }
    }

    public synchronized void stop() {
        log.info("消息推送的处理进程，准备停止中......");
        isRunning = false;
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdownNow();
        threadPool = null;
        log.info("消息推送的处理进程，已经停止！");
    }

    @Async
    @Order
    @EventListener({WebServerInitializedEvent.class})
    public void afterStart(final WebServerInitializedEvent event) {
        start();
    }


    @Scheduled(fixedRate = 500)
    public void watch() {
        if (isRunning && messageConfig.isThreadRun()) {
            send();
        }
    }

    @Override
    public void process(MessageToBeSendResponse sendResponse) {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        log.info("处理消息" + sendResponse.getMessageId() + "：" + JSONUtil.toJsonPrettyStr(sendResponse));
        MessageSendRecord record = this.getById(sendResponse.getId());
        if (ObjectUtil.isNotNull(record)) {
            if (ObjectUtil.isNotNull(sendResponse)) {
                SendChannelEnum channel = BaseEnum.parseByCode(SendChannelEnum.class, record.getSendChannel());
                switch (channel) {
                    case UNI_PUSH:
                        sendMessageByUniPush(sendResponse, record);
                        break;
                    case WX_MP:
                        sendMessageByWxMp(sendResponse, record);
                        break;
                    case WX_QX:
                        sendMessageByWxQy(sendResponse, record);
                        break;
                    case TT_PUSH:
                        sendMessageByTtPush(sendResponse, record);
                        break;
                    default:
                        break;
                }
            }
            if (this.updateById(record)) {
                // 修改消息状态为已发送
                messageService.changeStatusToSend(ListUtil.toList(sendResponse.getMessageId()));
            }
            Message message = messageService.findByIdThrowErr(sendResponse.getMessageId());
            if (ObjectUtil.equal(message.getSendState(), NumberConstant.TWO)) {
                //  修改异常任务定时计划的状态为已执行
                SpringUtil.getBean(ExceptionTaskScheduleService.class).updateExecuteStatus(sendResponse);
                //  给超时、超时上报消息添加履历
                this.createTaskRecord(sendResponse);
            }
            log.info("处理消息" + sendResponse.getMessageId() + "：已经成功！");
        } else {
            log.info("处理消息" + sendResponse.getMessageId() + "：未找到对应记录");
        }
    }

    private void sendMessageByTtPush(MessageToBeSendResponse sendResponse, MessageSendRecord record) {
        log.info("钉钉发送消息开始+" + sendResponse);
        ThirdPartyInfo info = thirdPartyInfoService.getThirdPartyInfoByUserId(sendResponse.getReceiveUserId(), PlatformType.DING_TALK.getCode());
        if (ObjectUtil.isNotNull(info)) {
            ILoginService loginService = SpringUtil.getBean(ILoginService.class);
            String accessToken = loginService.getAccessToken();
            DingTalkMessageRequest dingTalkMessageRequest = new DingTalkMessageRequest();
            dingTalkMessageRequest.setDingTalkUserIds(ListUtil.toList(info.getOpenId()));
            dingTalkMessageRequest.setContent(sendResponse.getContent());
            dingTalkMessageRequest.setAccessToken(accessToken);
            String dingTalkMessageResult = dingTalkMessage(dingTalkMessageRequest);
            JSONObject tTJsonObject = JSONUtil.parseObj(dingTalkMessageResult);
            log.info("钉钉发送消息结果" + tTJsonObject);
            if (!ObjectUtil.equal(Convert.toInt(tTJsonObject.get("errcode")), 0)) {
                record.setSendState(4);
                record.setRemark((String) tTJsonObject.get("errmsg"));
            } else {
                record.setSendState(3);
            }
        } else {
            record.setSendState(4);
            record.setRemark("微信模板消息发送失败,没有绑定过钉钉");
        }

    }

    private void sendMessageByWxQy(MessageToBeSendResponse sendResponse, MessageSendRecord record) {
        ThirdPartyInfo info = thirdPartyInfoService.getThirdPartyInfoByUserId(sendResponse.getReceiveUserId(), PlatformType.WX_OFFICIAL_ACCOUNT.getCode());
        if (ObjectUtil.isNotNull(info) && StrUtil.isNotBlank(info.getClientId())) {
//            String url= wxCpConfig.getAppConfigs().stream().filter(o-> Objects.equals(o.getAgentId(), Convert.toInt(info.getClientId()))).map(WxCpConfig.AppConfig::getUrl).findFirst().orElse("");
//            WxCpMessageService messageService = this.wxCpCustomService.getWxCpService(Convert.toInt(info.getClientId())).getMessageService();
//            WxCpMessage msg = new WxCpMessage();
//            msg.setAgentId(Convert.toInt(info.getClientId()));
//            msg.setContent("<a href='"+url+"'>"+sendResponse.getContent()+"</a>");
//            msg.setMsgType("text");
//            msg.setToUser(info.getOpenId());
//            msg.setUrl(url);
//            try {
//                WxCpMessageSendResult result = messageService.send(msg);
//                if (result.getErrCode() == 0) {
//                    record.setSendState(3);
//                } else {
//                    record.setSendState(4);
//                    record.setRemark("企业微信消息发送失败，" + result.getErrMsg());
//                }
//            } catch (WxErrorException e) {
//                record.setSendState(4);
//                record.setRemark("企业微信消息发送失败，" + StrUtil.subPre(e.getMessage(), 400));
//            }
        }
    }


    @Override
    public String dingTalkMessage(DingTalkMessageRequest request) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(dingTalkConfig.getAgentId());
            log.info("钉钉发送消息接收人" + request.getDingTalkUserIds());
            req.setUseridList(StringUtils.join(request.getDingTalkUserIds(), ","));
            OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            obj1.setMsgtype(StringConstant.MSGTYPE);
            OapiMessageCorpconversationAsyncsendV2Request.OA obj2 = new OapiMessageCorpconversationAsyncsendV2Request.OA();
            OapiMessageCorpconversationAsyncsendV2Request.Body obj3 = new OapiMessageCorpconversationAsyncsendV2Request.Body();
            obj3.setTitle(request.getContent());
            obj2.setBody(obj3);
            OapiMessageCorpconversationAsyncsendV2Request.Head obj4 = new OapiMessageCorpconversationAsyncsendV2Request.Head();
            obj2.setMessageUrl(dingTalkConfig.getMessageUrl());
            obj4.setBgcolor(StringConstant.BG_COLOR);
            obj2.setHead(obj4);
            obj1.setOa(obj2);
            req.setMsg(obj1);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, request.getAccessToken());
            System.out.println(rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendMessageByWxMp(MessageToBeSendResponse sendResponse, MessageSendRecord record) {
        ThirdPartyInfo info = thirdPartyInfoService.getThirdPartyInfoByUserId(sendResponse.getReceiveUserId(), PlatformType.WX_ENTERPRISE_APPLICATION.getCode());
        if (ObjectUtil.isNotNull(info)) {
            Map<String, Object> data = new HashMap<>();
            data.put("character_string16", Convert.toStr(sendResponse.getBusinessId()));
            data.put("time14", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_FORMAT));
            String msg = "...";
            if (StrUtil.isNotBlank(sendResponse.getContent()) && StrUtil.length(sendResponse.getContent()) > 17) {
                msg = sendResponse.getContent().substring(0, 16) + msg;
            }
            data.put("thing3", msg);
            String toUser = info.getOpenId();
            String templateId = messagePushConfig.getWx().getWxMpTemplateId();
            String appid = wxMpConfig.getConfigs().get(0).getAppId();
            try {
                String msgId = wxMpCustomService.sendWechatMsg(appid, templateId, toUser, messagePushConfig.getWx().getOpenUrl(), data, "", "");
                if (StrUtil.isNotBlank(msgId) && !StrUtil.contains(msgId, "errmsg")) {
                    record.setSendState(3);
                } else {
                    record.setSendState(4);
                    record.setRemark("微信模板消息发送失败:" + msgId);
                }
            } catch (Exception e) {
                log.info("公众号消息报错" + e);
            }

        } else {
            record.setSendState(4);
            record.setRemark("微信模板消息发送失败,没有绑定过微信公众号");
        }
    }

    private void sendMessageByUniPush(MessageToBeSendResponse sendResponse, MessageSendRecord record) {
        try {
            UniPushMessageRequest UniPushMessage = new UniPushMessageRequest();
            UniPushMessage.setUid(Convert.toStr(sendResponse.getReceiveUserId()));
            UniPushMessage.setTitle(sendResponse.getMessageTemplateName());
            UniPushMessage.setContent(sendResponse.getContent());
            UniPushTransparentMessage uniPushTransparentMessage = new UniPushTransparentMessage();
            uniPushTransparentMessage.setText(sendResponse.getContent());
            UniPushMessage.setData(uniPushTransparentMessage);
            String result = UniPushUtil.sendMessage(messagePushConfig.getUniPushUrl(), UniPushMessage);
            if (StrUtil.isNotBlank(result)) {
                record.setSendState(4);
                record.setRemark("失败原因：" + result);
                log.info("消息" + sendResponse.getMessageId() + ": 发送失败！，失败原因：" + result);
            } else {
                record.setSendState(3);
                log.info("消息" + sendResponse.getMessageId() + ": 发送成功！");
            }
        } catch (Exception e) {
            record.setSendState(4);
            record.setRemark("失败原因：" + e.getMessage());
        }
    }

    /**
     * 超时、超时上报消息添加履历
     *
     * @param sendResponse 消息待发送数据
     */
    private void createTaskRecord(MessageToBeSendResponse sendResponse) {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        log.info("进入超时、超时上报消息添加履历方法");
        ExceptionTaskRecordService taskRecordService = SpringUtil.getBean(ExceptionTaskRecordService.class);
        // 给超时消息添加履历
        if (NoticeLevel.OVERTIME_WARNING.getCode().equals(sendResponse.getMessageLevel())) {
            log.info("进入给超时消息添加履历if分支");
            if (ExceptionType.RESPONSE.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.RESPONSE;
                TaskRecordType recordType = TaskRecordType.RESPONSE_TIMEOUT;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, false);
            }
            if (ExceptionType.HANDING.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.HANDING;
                TaskRecordType recordType = TaskRecordType.HANDING_TIMEOUT;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, false);
            }
            if (ExceptionType.COOPERATION.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.HANDING;
                TaskRecordType recordType = TaskRecordType.COOPERATION_TIMEOUT;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, true);
            }
        }
        // 给超时上报消息添加履历
        if (NoticeLevel.OVERTIME_REPORT.getCode().equals(sendResponse.getMessageLevel())) {
            log.info("进入给超时上报消息添加履历if分支");
            if (ExceptionType.RESPONSE.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.RESPONSE;
                TaskRecordType recordType = TaskRecordType.RESPONSE_TIMEOUT_NOTICE;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, false);
            }
            if (ExceptionType.HANDING.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.HANDING;
                TaskRecordType recordType = TaskRecordType.HANDING_TIMEOUT_NOTICE;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, false);
            }
            if (ExceptionType.COOPERATION.getCode().equals(sendResponse.getBusinessType())) {
                TaskRecordNode recordNode = TaskRecordNode.HANDING;
                TaskRecordType recordType = TaskRecordType.COOPERATION_TIMEOUT_NOTICE;
                taskRecordService.timeOutMessage(sendResponse, recordNode, recordType, true);
            }
        }
    }

    public void asynchronous(MessageToBeSendResponse sendResponse, CountDownLatch latch) {
        if (isRunning) {
            IMessageSendRecordService recordService = SpringUtil.getBean(IMessageSendRecordService.class);
            Runnable runnable = () ->
            {
                try {
                    recordService.process(sendResponse);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                latch.countDown();
            };
            threadPool.execute(runnable);
        }
    }

    private static final int maxCheckTimes = 6;
    private static int checkTimes = 0;

    public synchronized static void clear() {
        checkTimes = 0;
    }

    public synchronized static void plus() {
        checkTimes = checkTimes + 1;
        if (checkTimes >= maxCheckTimes) {
            IMessageSendRecordService messageSendRecordService = SpringUtil.getBean(IMessageSendRecordService.class);
            if (messageSendRecordService.suspend()) {
                clear();
            }
        }
    }

    private void send() {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        if (messageService.moveToSendRecords() && messageService.getMessageQuantityToBeSent() > 0) {
            List<MessageToBeSendResponse> list = this.getBaseMapper().getMessageToBeSend();
            if (ObjectUtil.isNotEmpty(list)) {
                CountDownLatch latch = new CountDownLatch(list.size());
                for (MessageToBeSendResponse messageToBeSendResponse : list) {
                    asynchronous(messageToBeSendResponse, latch);
                }
                try {
                    latch.await();
                } catch (InterruptedException ignored) {
                }
            }
        } else {
            plus();
        }
    }

    @Override
    public void sendByMessageId(Long id) {
        List<MessageToBeSendResponse> list = this.getBaseMapper().getMessageToBeSendById(id);
        if (ObjectUtil.isNotEmpty(list)) {
            IMessageSendRecordService messageSendRecordService = SpringUtil.getBean(IMessageSendRecordService.class);
            for (MessageToBeSendResponse messageToBeSendResponse : list) {
                messageSendRecordService.process(messageToBeSendResponse);
            }
        }
    }

}
