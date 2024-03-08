package com.framework.emt.system.domain.task.submit.convert;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitCreateRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitUpdateRequest;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常提报 转换类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Mapper
public interface TaskSubmitConvert {

    TaskSubmitConvert INSTANCE = Mappers.getMapper(TaskSubmitConvert.class);

    /**
     * 创建参数转实体
     *
     * @param request               创建参数
     * @param exceptionProcessId    异常流程id
     * @param exceptionProcessTitle 异常流程名称
     * @param exceptionCategoryId   异常分类id
     * @param exceptionTaskId       异常任务id
     * @param submitVersion         提报版本号
     * @param extendDataList        附加字段
     * @return
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "submitResponseUserId", source = "request.responseUserId")
    @Mapping(target = "workspaceLocationId", source = "request.workspaceId")
    @Mapping(target = "exceptionTaskId", source = "exceptionTaskId")
    @Mapping(target = "exceptionCategoryId", source = "exceptionCategoryId")
    @Mapping(target = "priority", expression = "java(priorityCodeToEnum(request.getPriority()))")
    @Mapping(target = "severity", expression = "java(severityCodeToEnum(request.getSeverity()))")
    @Mapping(target = "submitVersion", source = "submitVersion")
    @Mapping(target = "submitExtendDatas", source = "extendDataList")
    ExceptionTaskSubmit createRequestToEntity(Long userId, TaskSubmitCreateRequest request, Long exceptionProcessId, String exceptionProcessTitle,
                                              Long exceptionCategoryId, Long exceptionTaskId, Integer submitVersion, List<FormFieldResponse> extendDataList);

    /**
     * 更新参数转实体
     *
     * @param entity           异常提报实体
     * @param request          创建参数
     * @param exceptionProcess 异常流程
     * @param extendDataList   附加字段
     * @return
     */
    @Mapping(target = "exceptionCategoryId", expression = "java(exceptionProcess.getExceptionCategoryId())")
    @Mapping(target = "exceptionProcessTitle", expression = "java(exceptionProcess.getTitle())")
    @Mapping(target = "priority", expression = "java(priorityCodeToEnum(request.getPriority()))")
    @Mapping(target = "severity", expression = "java(severityCodeToEnum(request.getSeverity()))")
    @Mapping(target = "submitExtendDatas", source = "extendDataList")
    ExceptionTaskSubmit updateRequestToEntity(@MappingTarget ExceptionTaskSubmit entity, TaskSubmitUpdateRequest request,
                                              @Context ExceptionProcessResponse exceptionProcess, List<FormFieldResponse> extendDataList);

    /**
     * 创建参数转实体
     *
     * @param exceptionTaskSubmit 异常提报
     * @param taskRejectRequest   驳回参数
     * @param submitVersion       提报版本号
     * @return
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submitFiles", defaultExpression = "java(new ArrayList<>())")
    @Mapping(target = "noticeUserIds", defaultExpression = "java(new ArrayList<>())")
    @Mapping(target = "submitExtendDatas", defaultExpression = "java(new ArrayList<>())")
    @Mapping(target = "rejectNode", source = "taskRejectRequest.taskRejectNode")
    @Mapping(target = "rejectSourceId", source = "taskRejectRequest.sourceId")
    @Mapping(target = "rejectTime", source = "taskRejectRequest.rejectTime")
    @Mapping(target = "rejectReason", source = "taskRejectRequest.rejectReason")
    @Mapping(target = "rejectUserId", source = "taskRejectRequest.rejectUserId")
    @Mapping(target = "submitVersion", source = "submitVersion")
    ExceptionTaskSubmit createRequestToEntity(ExceptionTaskSubmit exceptionTaskSubmit, TaskRejectRequest taskRejectRequest, Integer submitVersion);

    /**
     * 紧急程度Code转换成枚举
     *
     * @param code 紧急程度Code
     * @return 紧急程度枚举
     */
    default PriorityEnum priorityCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(PriorityEnum.class, code);
    }

    /**
     * 严重程度Code转换成枚举
     *
     * @param code 严重程度Code
     * @return 严重程度枚举
     */
    default SeverityEnum severityCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(SeverityEnum.class, code);
    }

}
