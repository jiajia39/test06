package com.framework.emt.system.domain.task.task.convert;

import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常任务配置 转换类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Mapper
public interface TaskSettingConvert {

    TaskSettingConvert INSTANCE = Mappers.getMapper(TaskSettingConvert.class);

    /**
     * 创建参数转实体
     *
     * @param exceptionProcess
     * @param reasonItems      异常原因项列表
     * @return
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createUser", ignore = true)
    @Mapping(target = "createDept", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "exceptionProcessId", source = "exceptionProcess.id")
    @Mapping(target = "submitExtendFields", source = "exceptionProcess.submitExtendFieldList")
    @Mapping(target = "responseExtendFields", source = "exceptionProcess.responseExtendFieldList")
    @Mapping(target = "handingExtendFields", source = "exceptionProcess.handingExtendFieldList")
    @Mapping(target = "pendingExtendFields", source = "exceptionProcess.pendingExtendFieldList")
    @Mapping(target = "cooperateExtendFields", source = "exceptionProcess.cooperateExtendFieldList")
    @Mapping(target = "checkExtendFields", source = "exceptionProcess.checkExtendFieldList")
    @Mapping(target = "responseReportNoticeProcessName", source = "exceptionProcess.responseReportNoticeProcessName")
    @Mapping(target = "handingReportNoticeProcessName", source = "exceptionProcess.handingReportNoticeProcessName")
    @Mapping(target = "reasonItems", source = "reasonItems")
    ExceptionTaskSetting createRequestToEntity(ExceptionProcessResponse exceptionProcess, List<TagInfo> reasonItems);

    /**
     * 更新参数转实体
     *
     * @param exceptionProcess
     * @param id               异常任务配置id
     * @param reasonItems      异常原因项列表
     * @return
     */
    @Mapping(target = "createUser", ignore = true)
    @Mapping(target = "createDept", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "exceptionProcessId", source = "exceptionProcess.id")
    @Mapping(target = "submitExtendFields", source = "exceptionProcess.submitExtendFieldList")
    @Mapping(target = "responseExtendFields", source = "exceptionProcess.responseExtendFieldList")
    @Mapping(target = "handingExtendFields", source = "exceptionProcess.handingExtendFieldList")
    @Mapping(target = "pendingExtendFields", source = "exceptionProcess.pendingExtendFieldList")
    @Mapping(target = "cooperateExtendFields", source = "exceptionProcess.cooperateExtendFieldList")
    @Mapping(target = "checkExtendFields", source = "exceptionProcess.checkExtendFieldList")
    @Mapping(target = "responseReportNoticeProcessName", source = "exceptionProcess.responseReportNoticeProcessName")
    @Mapping(target = "handingReportNoticeProcessName", source = "exceptionProcess.handingReportNoticeProcessName")
    @Mapping(target = "reasonItems", source = "reasonItems")
    ExceptionTaskSetting updateRequestToEntity(ExceptionProcessResponse exceptionProcess, Long id, List<TagInfo> reasonItems);

}
