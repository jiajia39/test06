package com.framework.emt.system.domain.exception.service;

import cn.hutool.core.util.ObjectUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.excel.support.ExcelImporter;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionCategoryErrorCode;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionItemErrorCode;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.exception.mapper.ExceptionCategoryMapper;
import com.framework.emt.system.domain.exception.request.ExceptionItemImportExcel;
import com.framework.emt.system.domain.exception.response.ExceptionItemShortResponse;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常项导入 实现类
 *
 * @author ds_C
 * @since 2023-07-21
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionItemImportServiceImpl implements ExcelImporter<ExceptionItemImportExcel> {

    private final ExceptionItemService exceptionItemService;

    @Override
    public void save(List<ExceptionItemImportExcel> importDataList) {
        // 校验传入的excel列表不能为空
        if (NumberUtils.INTEGER_ZERO.equals(importDataList.size())) {
            throw new ServiceException(BusinessErrorCode.IMPORT_DATA_IS_NOT_NULL);
        }

        // 校验excel列表中的非空数据
        checkIsNotBlankData(importDataList);

        // 校验excel列表中的枚举紧急程度、严重程度
        checkEnumNameExist(importDataList);

        // 通过异常分类名称列表查询出key为异常分类名称：value为异常分类id的map列表，若数据异常则抛出相应的错误信息
        Map<String, Long> categoryTitleOfIdMap = findCategoryMap(importDataList.stream()
                .map(ExceptionItemImportExcel::getExceptionCategoryTitle).distinct().collect(Collectors.toList()));

        // 获取异常项名称+分类id的列表 用于后续异常项同一分类下名称校验
       List<ExceptionItemShortResponse> exceptionItemShortResponseList = importDataList.stream().map(importData -> {
           ExceptionItemShortResponse response = new ExceptionItemShortResponse();
           response.setTitle(importData.getTitle());
           Long categoryId = categoryTitleOfIdMap.get(importData.getExceptionCategoryTitle());
           response.setCategoryId(categoryId == null ? 0 : categoryId);
           return response;

       } ).collect(Collectors.toList());
        // 校验excel列表中的异常项名称在同一分类下不能重复，且不能在数据库中存在
        checkTitle(exceptionItemShortResponseList);

        exceptionItemService.importExceptionItemExcel(importDataList, categoryTitleOfIdMap);
    }

    /**
     * 校验excel中的紧急程度名称、严重程度名称必须都存在
     *
     * @param importDataList excel导入的数据集
     */
    private void checkEnumNameExist(List<ExceptionItemImportExcel> importDataList) {
        PriorityEnum.checkPriorityNameList(importDataList.stream().map(ExceptionItemImportExcel::getPriorityName)
                .distinct().collect(Collectors.toList()));
        SeverityEnum.checkSeverityNameList(importDataList.stream().map(ExceptionItemImportExcel::getSeverityName)
                .distinct().collect(Collectors.toList()));
    }

    /**
     * 校验excel中的异常项名称不能重复，不能在数据库中存在
     *
     * @param list excel中的异常项名分类id称列表
     */
    private void checkTitle(List<ExceptionItemShortResponse> list) {
        long count = list.stream().distinct().count();
        if (count != list.size()) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_TITLE_MUST_UNIQUE);
        }
        ExceptionItem exceptionItem = exceptionItemService.findByShortList(list);
        if (ObjectUtil.isNotNull(exceptionItem)) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_TITLE_IS_EXIST);
        }
    }

    /**
     * 查询excel中的异常分类名称对应的异常分类id，若数据异常则抛出相应的错误信息
     *
     * @param categoryTitleList excel中的异常分类名称列表
     * @return key为异常分类名称，value为异常分类id的map列表
     */
    private Map<String, Long> findCategoryMap(List<String> categoryTitleList) {
        ExceptionCategoryMapper categoryMapper = SpringUtil.getBean(ExceptionCategoryMapper.class);
        List<ExceptionCategory> exceptionCategoryList = categoryMapper.findCategoryList(null, categoryTitleList);
        if (exceptionCategoryList.size() < categoryTitleList.size()) {
            throw new ServiceException(ExceptionCategoryErrorCode.EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_CATEGORY_TITLE);
        }
        return exceptionCategoryList.stream().collect(Collectors.toMap(ExceptionCategory::getTitle, ExceptionCategory::getId));
    }

    /**
     * 校验excel中的必填数据不能为空
     *
     * @param importDataList excel导入的数据集
     */
    private void checkIsNotBlankData(List<ExceptionItemImportExcel> importDataList) {
        for (ExceptionItemImportExcel importData : importDataList) {
            if (StringUtils.isBlank(importData.getExceptionCategoryTitle())) {
                throw new ServiceException(ExceptionCategoryErrorCode.EXCEPTION_CATEGORY_TITLE_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getTitle())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_TITLE_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getPriorityName())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_PRIORITY_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getSeverityName())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_SEVERITY_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getResponseDurationTimeStr())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_RESPONSE_DURATION_TIME_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getHandingDurationTimeStr())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_HANDING_DURATION_TIME_IS_NOT_BLANK);
            }
        }
    }

}
