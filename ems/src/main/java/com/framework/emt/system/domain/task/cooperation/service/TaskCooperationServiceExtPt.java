package com.framework.emt.system.domain.task.cooperation.service;

import com.alibaba.cola.extension.ExtensionPointI;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationSubmitRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationTransferRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;

/**
 * 异常协同服务接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
public interface TaskCooperationServiceExtPt extends ExtensionPointI {

    /**
     * 异常协同转派
     *
     * @param id      异常协同id
     * @param request
     * @param user    当前用户
     */
    void transfer(Long id, TaskCooperationTransferRequest request, FtUser user);

    /**
     * 异常协同接收
     *
     * @param id   异常协同id
     * @param user 当前用户
     */
    void accept(Long id, FtUser user);

    /**
     * 异常协同提交
     *
     * @param id      异常协同id
     * @param request
     * @param user    当前用户
     * @return
     */
    void submit(Long id, TaskCooperationSubmitRequest request, FtUser user);

    /**
     * 统计异常协同各个状态的数量
     *
     * @param currentUserId 当前用户id
     * @return
     */
    CooperationStatusNumResponse statistics(Long currentUserId);

    /**
     * 协同撤销
     *
     * @param id   撤销id
     * @param user 当前用户
     */
    void cancel(Long id, FtUser user);

    /**
     * 协同删除
     *
     * @param id
     */
    void delete(Long id);
}
