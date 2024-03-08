package com.framework.emt.system.domain.task.submit.service;

import com.alibaba.cola.extension.ExtensionPointI;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitCreateRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitUpdateRequest;

/**
 * 异常提报服务接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
public interface TaskSubmitServiceExtPt extends ExtensionPointI {

    /**
     * 异常提报创建
     * 异常任务、异常任务配置及异常提报创建
     *
     * @param request
     * @return 异常提报id
     */
    Long create(TaskSubmitCreateRequest request);

    /**
     * 异常提报更新
     *
     * @param id      异常提报id
     * @param request
     */
    void update(Long id, TaskSubmitUpdateRequest request);

    /**
     * 异常提报删除
     *
     * @param id 异常提报id
     */
    void delete(Long id);

    /**
     * 异常提报
     *
     * @param id   异常提报id
     * @param user 当前用户
     */
    void submit(Long id, FtUser user);

    /**
     * 异常提报关闭
     *
     * @param id 异常提报id
     */
    void close(Long id);

    /**
     * 异常提报撤销
     *
     * @param id 异常提报id
     */
    void cancel(Long id);

}
