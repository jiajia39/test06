package com.framework.emt.system.domain.task.handing.convert;

import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * 处理 转换类
 *
 * @author gaojia
 * @since 2023-08-22
 */
@Mapper
public interface TaskHandingConvert {

    TaskHandingConvert INSTANCE = Mappers.getMapper(TaskHandingConvert.class);

    /**
     * 新建参数转换成实体
     *
     * @param exceptionTaskHanding 处理
     * @param taskRejectRequest    驳回信息
     * @param handingVersion       处理版本号
     * @param handingRejectNum     处理驳回次数
     * @return 响应信息
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rejectNode", source = "taskRejectRequest.taskRejectNode")
    @Mapping(target = "rejectSourceId", source = "taskRejectRequest.sourceId")
    @Mapping(target = "rejectTime", source = "taskRejectRequest.rejectTime")
    @Mapping(target = "rejectReason", source = "taskRejectRequest.rejectReason")
    @Mapping(target = "rejectUserId", source = "taskRejectRequest.rejectUserId")
    @Mapping(target = "handingVersion", source = "handingVersion")
    @Mapping(target = "rejectNum", source = "handingRejectNum")
    ExceptionTaskHanding createRequestToEntity(ExceptionTaskHanding exceptionTaskHanding, TaskRejectRequest taskRejectRequest,
                                               Integer handingVersion, Integer handingRejectNum);


}
