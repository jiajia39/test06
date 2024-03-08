package com.framework.xxljob.utils;

import com.framework.common.json.utils.JsonUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.xxljob.model.JobGroup;
import com.framework.xxljob.model.XxlJobInfo;
import com.framework.xxljob.service.XxlJobService;
import com.xxl.job.core.glue.GlueTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Fan.junwei
 * @since 2022-07-12 18:09
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
@Slf4j
public class XxlJobUtil {
    /**
     * 添加一个调度任务
     *
     * @param jobGroupId            执行器id
     * @param executorTimeout       任务过期时间
     * @param jobDesc               任务描述
     * @param scheduleType          调度类型 如 ：CRON
     * @param scheduleConf          时间表达式
     * @param misfireStrategy       调度过期策略 DO_NOTHING 啥也不干
     * @param executorRouteStrategy 执行器路由策略 如 FIRST 轮询 随机等
     * @param executorHandler       执行器名称
     * @param executorParam         调用参数
     * @param executorBlockStrategy 阻塞处理策略 SERIAL_EXECUTION 单机串行
     * @param retryCount            重试次数
     * @return 返回数据
     */
    public static String addJob(final int jobGroupId, final int executorTimeout, final String jobDesc,
                                final String scheduleType, final String scheduleConf, final String misfireStrategy,
                                final String executorRouteStrategy, final String executorHandler, final String executorParam,
                                final String executorBlockStrategy, final int retryCount) {
        XxlJobService jobService = SpringUtil.getBean(XxlJobService.class);
        XxlJobInfo jobInfo = new XxlJobInfo();
        //任务执行器主键id 任务描述
        jobInfo.setJobGroup(jobGroupId);
        jobInfo.setJobDesc(jobDesc);
        //调度类型
        jobInfo.setScheduleType(scheduleType);
        //调度配置，值含义取决于调度类型"0/5 * * * * ?"  调度过期策略
        jobInfo.setScheduleConf(scheduleConf);
        jobInfo.setMisfireStrategy(misfireStrategy);
        //执行器路由策略 指一个任务选择使用哪个执行器去执行。这个参数只有当执行器做集群部署的时候才有意义。FIRST表示固定第一个
        jobInfo.setExecutorRouteStrategy(executorRouteStrategy);
        //**********************执行器，任务Handler名称***************************
        jobInfo.setExecutorHandler(executorHandler);
        //执行器任务参数
        jobInfo.setExecutorParam(executorParam);
        //阻塞处理策略 单机串行，默认 调度请求进入单机执行器后，调度请求进入FIFO队列并以串行方式运行
        jobInfo.setExecutorBlockStrategy(executorBlockStrategy);
        //任务执行超时时间，单位秒
        jobInfo.setExecutorTimeout(executorTimeout);
        //任务失败重试次数
        jobInfo.setExecutorFailRetryCount(retryCount);
        //GLUE类型 x.GlueTypeEnum
        jobInfo.setGlueType(GlueTypeEnum.BEAN.getDesc());
        //GLUE源代码 GLUE备注  GLUE更新时间
        jobInfo.setGlueSource("");
        jobInfo.setGlueRemark("");
        //子任务ID，多个逗号分隔
        jobInfo.setChildJobId("");
        //调度状态：0-停止，1-运行  上次调度时间  下次调度时间
        jobInfo.setTriggerStatus(1);
        jobInfo.setAuthor("administroter");
        log.info("xxljob定时任务，创建定时任务，参数：" + JsonUtil.toJson(jobInfo));
        return jobService.add(jobInfo);
    }
}