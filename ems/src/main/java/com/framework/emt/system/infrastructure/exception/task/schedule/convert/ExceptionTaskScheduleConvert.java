package com.framework.emt.system.infrastructure.exception.task.schedule.convert;

import com.framework.emt.system.infrastructure.exception.task.schedule.ExceptionTaskSchedule;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 异常任务定时计划 转换类
 *
 * @author ds_C
 * @since 2023-08-24
 */
@Mapper
public interface ExceptionTaskScheduleConvert {

    ExceptionTaskScheduleConvert INSTANCE = Mappers.getMapper(ExceptionTaskScheduleConvert.class);

}
