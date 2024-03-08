package com.framework.emt.system.domain.reportnoticeprocess.convert;

import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcess;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUpdateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessCreateRequest;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 上报流程 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface ReportNoticeProcessConvert {

    ReportNoticeProcessConvert INSTANCE = Mappers.getMapper(ReportNoticeProcessConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    @Mapping(target = "exceptionCategoryId", constant = "0L")
    ReportNoticeProcess createRequestToEntity(ReportNoticeProcessCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    ReportNoticeProcess updateRequestToEntity(@MappingTarget ReportNoticeProcess entity, ReportNoticeProcessUpdateRequest request);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default EnableFlagEnum enableFlagCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(EnableFlagEnum.class, code);
    }

}
