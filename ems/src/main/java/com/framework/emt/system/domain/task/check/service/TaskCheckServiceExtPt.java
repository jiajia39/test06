package com.framework.emt.system.domain.task.check.service;

import com.alibaba.cola.extension.ExtensionPointI;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.check.request.TaskCheckRejectRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 异常验收服务接口
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Validated
public interface TaskCheckServiceExtPt extends ExtensionPointI {

    /**
     * 创建验收信息
     *
     * @param exceptionTaskId 任务id
     * @param checkVersion    版本
     * @param userIdList      验收人列表
     * @return 验收id
     */
    void create(@NotNull Long exceptionTaskId, @NotNull Integer checkVersion, @NotEmpty List<Long> userIdList, List<FormFieldResponse> submitExtendDatas);

    /**
     * 任务验收 验收通过
     *
     * @param user    当前登录人
     * @param id      验收id
     * @param request
     * @return
     */
    Long exceptionCheckPass(@NotNull FtUser user, @NotNull Long id, TaskCheckRequest request);

    /**
     * 任务验收 验收驳回
     *
     * @param user    当前登录人
     * @param id      验收id
     * @param request 验收驳回信息
     * @return
     */
    Long exceptionCheckReject(@NotNull FtUser user, @NotNull Long id, TaskCheckRejectRequest request);

    /**
     * 管理员操作 任务验收 验收通过
     *
     * @param user    当前登录人
     * @param id      验收id
     * @param request
     * @return
     */
    Long administratorCheckPass(@NotNull FtUser user, @NotNull Long id, TaskCheckRequest request);

    /**
     * 管理员操作 任务验收 验收驳回
     *
     * @param user    当前登录人
     * @param id      验收id
     * @param request 验收驳回信息
     * @return
     */
    Long administratorCheckReject(@NotNull FtUser user, @NotNull Long id, TaskCheckRejectRequest request);
}
