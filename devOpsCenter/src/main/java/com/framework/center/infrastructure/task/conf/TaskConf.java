package com.framework.center.infrastructure.task.conf;

import cn.hutool.core.util.ObjectUtil;
import com.framework.center.infrastructure.task.constant.TaskConstant;
import com.framework.xxljob.model.JobGroup;
import com.framework.xxljob.model.XxlJobInfo;
import com.framework.xxljob.service.XxlJobService;
import com.framework.xxljob.utils.XxlJobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author yankunwu
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(prefix = "framework.xxl-job", value = "enable", havingValue = "true")
public class TaskConf {
  @Value("${framework.xxl-job.app-name}")
  private String appName;
  private final XxlJobService jobService;
  @Async
  @Order
  @EventListener({WebServerInitializedEvent.class})
  public void afterStart(final WebServerInitializedEvent event) {
    JobGroup group = jobService.jobGroupGetOne(appName);
    boolean groupIsOk = true;
    if (ObjectUtil.isNull(group)) {
      group = new JobGroup();
      group.setTitle("异常系统运维中心执行器");
      group.setAddressType(0);
      group.setAppName(appName);
      groupIsOk = jobService.jobGroupSave(group);
      group = jobService.jobGroupGetOne(appName);
    }
    if (groupIsOk) {
      int jobGroupId = group.getId();
      //任务超时时间（秒）
      int executorTimeout = 60 * 5;
      userDataSynchronization(jobGroupId, executorTimeout);
      organizationalStructureDataSynchronization(jobGroupId, executorTimeout);
    }
  }

  private void userDataSynchronization(final int jobGroupId, final int executorTimeout) {
    String jobDesc = "用户数据同步-定时任务";
    String executorHandler = TaskConstant.USER_DATA_SYNCHRONIZATION;
    String scheduleType = "CRON";
    String scheduleConf = "0 0/5 * * * ?";
    createTask(jobService, jobGroupId, executorTimeout, jobDesc, executorHandler, scheduleType, scheduleConf);
  }
  private void organizationalStructureDataSynchronization(final int jobGroupId, final int executorTimeout) {
    String jobDesc = "组织架构数据同步-定时任务";
    String executorHandler = TaskConstant.ORGANIZATIONAL_STRUCTURE_DATA_SYNCHRONIZATION;
    String scheduleType = "CRON";
    String scheduleConf = "0 0/5 * * * ?";
    createTask(jobService, jobGroupId, executorTimeout, jobDesc, executorHandler, scheduleType, scheduleConf);
  }

  private void createTask(final XxlJobService jobService, final int jobGroupId, final int executorTimeout,
                          final String jobDesc, final String executorHandler, final String scheduleType,
                          final String scheduleConf) {
    String misfireStrategy = "DO_NOTHING";
    //RANDOM 随机，FIRST 第一个 ROUND 轮询
    String executorRouteStrategy = "FIRST";
    String executorParam = "";
    String executorBlockStrategy = "SERIAL_EXECUTION";
    int retryCount = 3;
    XxlJobInfo jobInfo = jobService.loadByHandler(jobGroupId, executorHandler);
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
