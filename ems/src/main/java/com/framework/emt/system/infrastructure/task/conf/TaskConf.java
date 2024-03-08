package com.framework.emt.system.infrastructure.task.conf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.framework.emt.system.infrastructure.task.constant.TaskConstantSendMessage;
import com.framework.xxljob.model.JobGroup;
import com.framework.xxljob.model.XxlJobInfo;
import com.framework.xxljob.service.XxlJobService;
import com.framework.xxljob.utils.XxlJobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @author yankunwu
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskConf {
  @Value("${framework.xxl-job.app-name}")
  private String appName;

  @Value("${spring.application.name}")
  private String emt;

  private final XxlJobService jobService;
  @Async
  @Order
  @EventListener({WebServerInitializedEvent.class})
  public void afterStart(final WebServerInitializedEvent event) {
    JobGroup group = jobService.jobGroupGetOne(appName);
    if (ObjectUtil.isNull(group)) {
      group = new JobGroup();
      group.setTitle("异常执行器"+ emt);
      group.setAddressType(0);
      group.setAppName(appName);
      jobService.jobGroupSave(group);
    }
  }

  public void sendMessageTask(final LocalDateTime sendTime, final String id) {
    int executorTimeout = 60 * 5;
    JobGroup group = jobService.jobGroupGetOne(appName);
    String jobDesc = "定时发送id为：" + id + "的消息";
    String executorHandler = TaskConstantSendMessage.SEND_MESSAGE_BY_ID;
    String scheduleType = "CRON";
    String scheduleConf = DateUtil.format(sendTime, "ss mm HH dd MM ? yyyy");
    createTask(jobService, group.getId(), executorTimeout, jobDesc, executorHandler, scheduleType, scheduleConf, id);
  }

  public void updateSuspend(final LocalDateTime resumeTime, final String id) {
    int executorTimeout = 60 * 5;
    JobGroup group = jobService.jobGroupGetOne(appName);
    String jobDesc = "定时发送id为：" + id + "的恢复挂起";
    String executorHandler = TaskConstantSendMessage.UPDATE_SUSPEND_INFO_BY_ID;
    String scheduleType = "CRON";
    String scheduleConf = DateUtil.format(resumeTime, "ss mm HH dd MM ? yyyy");
    createTask(jobService, group.getId(), executorTimeout, jobDesc, executorHandler, scheduleType, scheduleConf, id);
  }

  public void cancelSuspend(final String id) {
    JobGroup group = jobService.jobGroupGetOne(appName);
    String executorHandler = TaskConstantSendMessage.UPDATE_SUSPEND_INFO_BY_ID;
    XxlJobInfo jobInfo = jobService.loadByHandler(group.getId(), executorHandler, id);
    jobService.remove(jobInfo.getId());
  }

  private void createTask(final XxlJobService jobService, final int jobGroupId, final int executorTimeout, final String jobDesc, final String executorHandler, final String scheduleType, final String scheduleConf, String executorParam) {
    String misfireStrategy = "DO_NOTHING";
    //RANDOM 随机，FIRST 第一个 ROUND 轮询
    String executorRouteStrategy = "FIRST";
    String executorBlockStrategy = "SERIAL_EXECUTION";
    int retryCount = 3;
    XxlJobInfo jobInfo = jobService.loadByHandler(jobGroupId, executorHandler, executorParam);
    if (jobInfo != null) {
      if (jobInfo.getTriggerStatus() != 1) {
        jobService.start(jobInfo.getId());
      }
    } else {
      String result = XxlJobUtil.addJob(jobGroupId, executorTimeout, jobDesc, scheduleType, scheduleConf, misfireStrategy,
                             executorRouteStrategy, executorHandler, executorParam, executorBlockStrategy, retryCount
                            );
      System.out.println(result);
    }
  }
}
