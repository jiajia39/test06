package com.framework.emt.system.infrastructure.exception.task.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.task.task.response.SettingCheckResponse;
import com.framework.emt.system.domain.task.task.response.SettingHandingResponse;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.domain.task.task.response.TaskSettingResponse;
import org.apache.ibatis.annotations.Param;

/**
 * 异常任务配置 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskSettingMapper extends BaseMapper<ExceptionTaskSetting> {


    TaskSettingResponse getTaskById(@Param("id") Long id);

    /**
     * 根据id查询验收数据
     *
     * @param id 异常配置id
     * @return
     */
    SettingCheckResponse findCheckDataById(@Param("id") Long id);

    /**
     * 根据id查询处理数据
     *
     * @param id 异常配置id
     * @return
     */
    SettingHandingResponse findCheckHandingById(@Param("id") Long id);

    /**
     * 根据异常任务id查询异常任务配置信息
     *
     * @param taskId 异常任务id
     * @return
     */
    ExceptionTaskSetting findByTaskId(@Param("taskId") Long taskId);

}
