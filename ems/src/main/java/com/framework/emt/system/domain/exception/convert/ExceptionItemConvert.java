package com.framework.emt.system.domain.exception.convert;

import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionItemErrorCode;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.exception.request.ExceptionItemCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionItemImportExcel;
import com.framework.emt.system.domain.exception.request.ExceptionItemUpdateRequest;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 异常项 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface ExceptionItemConvert {

    Logger logger = LogManager.getLogger(ExceptionItemConvert.class);

    ExceptionItemConvert INSTANCE = Mappers.getMapper(ExceptionItemConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    @Mapping(target = "priority", expression = "java(priorityCodeToEnum(request.getPriority()))")
    @Mapping(target = "severity", expression = "java(severityCodeToEnum(request.getSeverity()))")
    ExceptionItem createRequestToEntity(ExceptionItemCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "enableFlag", expression = "java(enableFlagCodeToEnum(request.getEnableFlag()))")
    @Mapping(target = "priority", expression = "java(priorityCodeToEnum(request.getPriority()))")
    @Mapping(target = "severity", expression = "java(severityCodeToEnum(request.getSeverity()))")
    ExceptionItem updateRequestToEntity(@MappingTarget ExceptionItem entity, ExceptionItemUpdateRequest request);

    /**
     * excel导入数据集转实体列表（携带异常分类map列表）
     *
     * @param excelImportDataList  excel导入的数据集
     * @param categoryTitleOfIdMap key为异常分类名称，value为异常分类id的map列表
     * @return
     */
    List<ExceptionItem> importDataListToEntityList(List<ExceptionItemImportExcel> excelImportDataList,
                                                   @Context Map<String, Long> categoryTitleOfIdMap);

    /**
     * 实体列表转响应体列表
     *
     * @param exceptionItems 实体列表
     * @return
     */
    List<ExceptionItemResponse> entitysToResposnes(List<ExceptionItem> exceptionItems);

    /**
     * 状态Code转换成枚举
     *
     * @param code 状态Code
     * @return 状态枚举
     */
    default EnableFlagEnum enableFlagCodeToEnum(Integer code) {
        return BaseEnum.parseByCode(EnableFlagEnum.class, code);
    }

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

    /**
     * 给异常项对应的响应时限、处理时限、紧急程度、严重程度、异常分类id赋值
     *
     * @param exceptionItem        异常项对象
     * @param excelImportData      excel导入的数据
     * @param categoryTitleOfIdMap key为异常分类名称，value为异常分类id的map列表
     */
    @AfterMapping
    default void setDurationTimeAndEnumAndCategoryId(@MappingTarget ExceptionItem exceptionItem,
                                                     ExceptionItemImportExcel excelImportData,
                                                     @Context Map<String, Long> categoryTitleOfIdMap) {
        try {
            exceptionItem.setResponseDurationTime(Integer.parseInt(excelImportData.getResponseDurationTimeStr()));
        } catch (NumberFormatException error) {
            logger.error("[异常项导入] 数据转换异常", error);
            throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_RESPONSE_DURATION_TIME_MUST_INTEGER);
        }
        try {
            exceptionItem.setHandingDurationTime(Integer.parseInt(excelImportData.getHandingDurationTimeStr()));
        } catch (NumberFormatException error) {
            logger.error("[异常项导入] 数据转换异常", error);
            throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_HANDING_DURATION_TIME_MUST_INTEGER);
        }
        exceptionItem.setPriority(BaseEnum.parseByName(PriorityEnum.class, excelImportData.getPriorityName()));
        exceptionItem.setSeverity(BaseEnum.parseByName(SeverityEnum.class, excelImportData.getSeverityName()));
        exceptionItem.setExceptionCategoryId(categoryTitleOfIdMap.get(excelImportData.getExceptionCategoryTitle()));
    }

}
