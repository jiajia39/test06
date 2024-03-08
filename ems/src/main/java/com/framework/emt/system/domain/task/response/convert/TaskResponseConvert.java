package com.framework.emt.system.domain.task.response.convert;

import cn.hutool.core.util.ObjectUtil;
import com.framework.emt.system.domain.task.response.request.TaskResponseUpdateRequest;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


/**
 * 响应 转换类
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Mapper
public interface TaskResponseConvert {

    TaskResponseConvert INSTANCE = Mappers.getMapper(TaskResponseConvert.class);


    /**
     * 更新参数转换成实体
     *
     * @param entity  响应实体
     * @param request 更新信息
     * @return 响应实体
     */
    ExceptionTaskResponse updateRequestToEntity(@MappingTarget ExceptionTaskResponse entity, TaskResponseUpdateRequest request);

    /**
     * 新建参数转换成实体
     *
     * @param exceptionTaskResponse 响应
     * @param taskRejectRequest     驳回信息
     * @param responseVersion       响应版本号
     * @param responseRejectNum     响应驳回次数
     * @return
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rejectNode", source = "taskRejectRequest.taskRejectNode")
    @Mapping(target = "rejectSourceId", source = "taskRejectRequest.sourceId")
    @Mapping(target = "rejectTime", source = "taskRejectRequest.rejectTime")
    @Mapping(target = "rejectReason", source = "taskRejectRequest.rejectReason")
    @Mapping(target = "rejectUserId", source = "taskRejectRequest.rejectUserId")
    @Mapping(target = "responseVersion", source = "responseVersion")
    @Mapping(target = "rejectNum", source = "responseRejectNum")
    ExceptionTaskResponse createRequestToEntity(ExceptionTaskResponse exceptionTaskResponse, TaskRejectRequest taskRejectRequest,
                                                Integer responseVersion, Integer responseRejectNum);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default TaskRejectNode rejectNodeEnumCodeToEnum(Integer code) {
        if (ObjectUtil.isNotNull(code)) {
            return BaseEnum.parseByCode(TaskRejectNode.class, code);
        }
        return null;
    }

}
