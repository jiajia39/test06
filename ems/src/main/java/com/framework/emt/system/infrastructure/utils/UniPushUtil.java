package com.framework.emt.system.infrastructure.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.emt.system.domain.messages.request.UniPushMessageRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniPushUtil {
/*    public static void main(String[] args) {
        String url="https://fc-mp-fc0aba68-c684-424a-bced-f51bccd962e4.next.bspapp.com/send";
        UniPushMessageRequest messageRequest=new UniPushMessageRequest();
        messageRequest.setUid("165649638641141760212");
        messageRequest.setContent("消息推送内容");
        messageRequest.setTitle("消息推送标题");
        messageRequest.setRequestId(UUID.randomUUID().toString());
        UniPushTransparentMessage transparentMessage=new UniPushTransparentMessage();
        transparentMessage.setText("消息推送透传内容");
        messageRequest.setData(transparentMessage);
        String sendResult = sendMessage(url, messageRequest);
        log.info("消息发送结果(为空则成功)："+sendResult);
    }*/

    public static String sendMessage(String url, UniPushMessageRequest messageRequest) {
        String sendResult="";
        String requestJson=JsonUtil.toJson(messageRequest);
        log.info("请求参数："+requestJson);
        String result = HttpUtil.createPost(url).body(requestJson).execute().body();
        log.info("响应参数："+result);
        if(StrUtil.isNotBlank(result)){
            JSONObject resultObject = JSONUtil.parseObj(result);
            if(ObjectUtil.isNotNull(resultObject)){
                Integer resultCode = resultObject.getInt("errCode");
                if(ObjectUtil.isNotNull(resultCode) && resultCode==0){
                    sendResult ="";
                }else{
                    String errorMsg=resultObject.getStr("errMsg");
                    if(StrUtil.isNotBlank(errorMsg)){
                        if(StrUtil.equals(errorMsg, "target user is invalid")){
                            sendResult="接收用户不存在";
                        }
                    }
                }
            }
        }
        return sendResult;
    }
}
