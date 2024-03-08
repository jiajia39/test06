package com.framework.emt.system.infrastructure.exception.task.task.service;

import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.task.response.request.SubmitExtendRequest;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.domain.task.task.response.TaskSettingResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 异常任务配置 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskSettingService extends BaseService<ExceptionTaskSetting> {

    /**
     * 异常任务配置创建
     *
     * @param exceptionProcess
     * @param reasonItems      异常原因项列表
     * @return
     */
    ExceptionTaskSetting create(ExceptionProcessResponse exceptionProcess, List<TagInfo> reasonItems);

    /**
     * 异常任务配置修改
     *
     * @param exceptionProcess
     * @param id               异常任务配置id
     * @param reasonItems      异常原因项列表
     * @return
     */
    Long update(ExceptionProcessResponse exceptionProcess, Long id, List<TagInfo> reasonItems);

    /**
     * 异常任务配置id
     *
     * @param exceptionSettingId
     * @return
     */
    ExceptionTaskSetting info(Long exceptionSettingId);


    List<FormFieldResponse> validateExtendFields(List<FormFieldResponse> dataList, List<ExtendFieldsRequest> extendFieldsRequestList);

    /**
     * 根据id查询异常任务配置表
     *
     * @param id 配置表id
     * @return
     */
    TaskSettingResponse getTaskById(Long id);

    /**
     * 提报任务-校验响应人
     *
     * @param exceptionTaskSetting 异常配置
     * @param responseUserId        响应人id
     * @return
     */
    List<Long> validateResponseUser(ExceptionTaskSetting exceptionTaskSetting, Long responseUserId);

    /**
     * 响应任务提交-校验处理人
     *
     * @param exceptionTaskSetting 异常配置
     * @param handingUserId        处理人id
     * @return
     */
    List<Long> validateHandingUser(ExceptionTaskSetting exceptionTaskSetting, Long userId, Long handingUserId);

    /**
     * 处理任务提交-校验验收人
     *
     * @param setting             配置信息
     * @param checkUserIds        验收人ids
     * @param exceptionTaskSubmit 提报信息
     * @return 验收人列表
     */
    List<Long> validateCheckUser(ExceptionTaskSetting setting, List<Long> checkUserIds, ExceptionTaskSubmit exceptionTaskSubmit);

    /**
     * 根据id查询验收数据
     *
     * @param id 异常配置id
     * @return
     */
    SettingCheckResponse findCheckDataById(Long id);

    /**
     * 根据id查询处理数据
     *
     * @param id 异常配置id
     * @return
     */
    SettingHandingResponse findCheckHandingById(Long id);

    /**
     * 根据异常任务id查询异常任务配置信息
     *
     * @param taskId 异常任务id
     * @return
     */
    ExceptionTaskSetting findByTaskId(Long taskId);

    /**
     * 通过异常任务配置id查询异常任务配置信息
     * 若数据异常则抛出相应的错误信息
     *
     * @param id 异常任务id
     * @return
     */
    ExceptionTaskSetting findByIdThrowErr(Long id);

}
