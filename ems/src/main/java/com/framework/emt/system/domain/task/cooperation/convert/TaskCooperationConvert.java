package com.framework.emt.system.domain.task.cooperation.convert;

import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.response.TaskCooperationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 异常任务协同 转换类
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Mapper
public interface TaskCooperationConvert {

    TaskCooperationConvert INSTANCE = Mappers.getMapper(TaskCooperationConvert.class);

    /**
     * 实体转响应体
     *
     * @param entity 实体
     * @return
     */
    TaskCooperationResponse entityToRes(ExceptionTaskCooperation entity);

}
