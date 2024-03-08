package com.framework.emt.system.infrastructure.task.jobs;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.framework.emt.system.domain.messages.service.IMessageSendRecordService;
import com.framework.emt.system.domain.task.handing.service.TaskHandingServiceExtPt;
import com.framework.emt.system.infrastructure.task.constant.TaskConstantSendMessage;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yankunwu
 */
@SuppressWarnings("ALL")
@AllArgsConstructor
@Component
@Slf4j
public class DataXxlJob {
    private final IMessageSendRecordService messageSendRecordService;

    private final TaskHandingServiceExtPt taskHandingService;

    @XxlJob(TaskConstantSendMessage.SEND_MESSAGE_BY_ID)
    public void sendMessageById() {
        String id = XxlJobHelper.getJobParam();
        if (StrUtil.isNotBlank(id)) {
            messageSendRecordService.sendByMessageId(Convert.toLong(id));
        }
    }

    @XxlJob(TaskConstantSendMessage.UPDATE_SUSPEND_INFO_BY_ID)
    public void updateSuspend() {
        String id = XxlJobHelper.getJobParam();
        if (StrUtil.isNotBlank(id)) {
            taskHandingService.updateSuspendByHanding(Convert.toLong(id));
        }
    }
}
