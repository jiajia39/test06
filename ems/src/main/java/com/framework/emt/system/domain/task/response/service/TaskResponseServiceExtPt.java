package com.framework.emt.system.domain.task.response.service;

import com.alibaba.cola.extension.ExtensionPointI;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.task.response.request.TaskResponseTransferRequest;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 异常响应服务接口
 *
 * @author gaojia
 * date 2023/8/8
 */
@Validated
public interface TaskResponseServiceExtPt extends ExtensionPointI {

    /**
     * 驳回
     *
     * @param id           异常任务响应id
     * @param rejectReason 驳回原因
     * @param user         驳回人
     * @return
     */
    Long reject(Long id, String rejectReason, FtUser user);

    /**
     * 转派
     *
     * @param id          异常任务响应id
     * @param otherRemark 转派备注
     * @param userId      转派人id
     * @param user        当前登录用户
     * @return
     */
    Long toOther(Long id, FtUser user, String otherRemark, Long userId);

    /**
     * 接受
     *
     * @param id     响应id
     * @param userId 接受人id
     * @return
     */
    Long accept(Long id, Long userId);

    /**
     * 异常响应提交
     *
     * @param id                  响应id
     * @param submitHandingUserId 提交处理人id
     * @param submitExtendDatas   提交附加数据
     * @return
     */
    Long submit(Long id, Long submitHandingUserId, @Validated List<ExtendFieldsRequest> submitExtendDatas);

    /**
     * 转派
     *
     * @param taskId      异常任务id
     * @param request 转派参数
     * @return
     */
    Long adminResponseTransfer(Long taskId, TaskResponseTransferRequest request);
}
