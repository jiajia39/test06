package com.framework.xxljob.service;

import com.framework.xxljob.model.JobGroup;
import com.framework.xxljob.model.XxlJobInfo;
import com.framework.xxljob.vo.JobInfoPageResult;

import java.util.List;

public interface XxlJobService {
    String jobGetOne = "/jobinfo/loadByHandler";
    String jobAddPath = "/jobinfo/add";
    String jobDeletePath = "/jobinfo/remove";
    String jobUpdatePath = "/jobinfo/update";
    String jobStartPath = "/jobinfo/start";
    String jobStopPath = "/jobinfo/stop";
    String jobTriggerPath = "/jobinfo/trigger";
    String jobPageListPath = "/jobinfo/pageList";
    String jobNextTriggerTimePath = "/jobinfo/nextTriggerTime";
    String jobgroupSave = "/jobgroup/save";
    String jobgroupGetOne = "/jobgroup/findByAppName";

    /**
     * 根据参数获得任务
     *
     * @param groupId         执行器id
     * @param executorHandler 执行器名称
     * @param executorParam 参数
     * @return 任务详情
     */
    XxlJobInfo loadByHandler(int groupId, String executorHandler,String executorParam);

    /**
     * 根据参数获得任务
     *
     * @param groupId         执行器id
     * @param executorHandler 执行器名称
     * @return 任务详情
     */
    XxlJobInfo loadByHandler(int groupId, String executorHandler);


    JobInfoPageResult pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc,
                               String executorHandler, String author);

    /**
     * add job
     *
     * @param jobInfo
     * @return
     */
    String add(XxlJobInfo jobInfo);

    /**
     * update job
     *
     * @param jobInfo
     * @return
     */
    void update(XxlJobInfo jobInfo);

    /**
     * remove job
     * *
     *
     * @param id
     * @return
     */
    void remove(int id);

    void remove(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    void removeAll(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 停止运行中的任务并删除
     */
    void cancel(int jobGroup, String jobDesc, String executorHandler, String author);

    void cancelAll(int jobGroup, String jobDesc, String executorHandler, String author);

    /**
     * start job
     *
     * @param id
     * @return
     */
    void start(int id);

    /**
     * stop job
     *
     * @param id
     * @return
     */
    void stop(int id);

    void triggerJob(int id, String executorParam, String addressList);

    List<String> nextTriggerTime(String scheduleType, String scheduleConf);

    /**
     * 添加新的执行器
     *
     * @param jobGroup 执行器信息
     * @return 是否创建成功
     */
    boolean jobGroupSave(JobGroup jobGroup);

    /**
     * 添加新的执行器
     *
     * @param appName 执行器名称
     * @return 执行器详情
     */
    JobGroup jobGroupGetOne(String appName);
}
