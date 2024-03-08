package com.framework.emt.system.domain.task.check.convert;

import cn.hutool.core.util.ObjectUtil;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;

import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import com.framework.emt.system.domain.task.check.request.TaskCheckCreateRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckUpdateRequest;
import com.framework.emt.system.domain.task.check.response.TaskCheckListResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 验收 转换类
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Mapper
public interface TaskCheckConvert {

    TaskCheckConvert INSTANCE = Mappers.getMapper(TaskCheckConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "checkStatus", expression = "java(checkStatusEnumCodeToEnum(request.getCheckStatus()))")
    @Mapping(target = "checkSubstatus", expression = "java(checkSubstatusEnumCodeToEnum(request.getCheckSubstatus()))")
    List<ExceptionTaskCheck> createRequestToEntity(List<TaskCheckCreateRequest> request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "checkStatus", expression = "java(checkStatusEnumCodeToEnum(request.getCheckStatus()))")
    @Mapping(target = "checkSubstatus", expression = "java(checkSubstatusEnumCodeToEnum(request.getCheckSubstatus()))")
    ExceptionTaskCheck updateRequestToEntity(@MappingTarget ExceptionTaskCheck entity, TaskCheckUpdateRequest request);


    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<TaskCheckListResponse> entityList2Vo(List<ExceptionTaskCheck> itemList);

    TaskCheckCreateRequest copy(TaskCheckCreateRequest request);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default CheckStatus checkStatusEnumCodeToEnum(Integer code) {
        if (ObjectUtil.isNotNull(code)) {
            return BaseEnum.parseByCode(CheckStatus.class, code);
        }
        return null;
    }

    default CheckSubStatus checkSubstatusEnumCodeToEnum(Integer code) {
        if (ObjectUtil.isNotNull(code)) {
            return BaseEnum.parseByCode(CheckSubStatus.class, code);
        }
        return null;
    }

}
