package com.framework.emt.system.domain.exception.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.excel.support.ExcelImporter;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionCategoryErrorCode;
import com.framework.emt.system.domain.exception.mapper.ExceptionCategoryMapper;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryImportExcel;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常分类导入 实现类
 *
 * @author ds_C
 * @since 2023-07-21
 */
@RequiredArgsConstructor
public class ExceptionCategoryImportServiceImpl implements ExcelImporter<ExceptionCategoryImportExcel> {

    private final ExceptionCategoryService exceptionCategoryService;

    @Override
    public void save(List<ExceptionCategoryImportExcel> excelImportDataList) {
        // 校验传入的excel列表不能为空
        if (NumberUtils.INTEGER_ZERO.equals(excelImportDataList.size())) {
            throw new ServiceException(BusinessErrorCode.IMPORT_DATA_IS_NOT_NULL);
        }

        // 校验excel中的异常分类名称不能为空、不能重复，不能在数据库中存在
        List<String> excelTitleList = excelImportDataList.stream().map(ExceptionCategoryImportExcel::getTitle).collect(Collectors.toList());
        if (excelTitleList.stream().anyMatch(StringUtils::isBlank)) {
            throw new ServiceException(ExceptionCategoryErrorCode.EXCEPTION_CATEGORY_TITLE_IS_NOT_BLANK);
        }
        //进行分组和计数
        Map<String, Long> duplicates = excelImportDataList.stream().collect(Collectors.groupingBy(e -> e.getParentTitle() + "-" + e.getTitle(), Collectors.counting())).entrySet().stream().filter(entry -> entry.getValue() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 输出重复项
        duplicates.forEach((key, value) -> {
            throw new ServiceException(key + " 分类重复值有" + value + "个");
        });

        if (ObjectUtil.isNotNull(exceptionCategoryService.findByTitles(excelTitleList))) {
            throw new ServiceException(ExceptionCategoryErrorCode.EXCEPTION_CATEGORY_TITLE_IS_EXIST);
        }

        // 得到excel中去空、去重后的父级异常分类名称列表
        List<String> excelParentTitleList = excelImportDataList.stream().map(ExceptionCategoryImportExcel::getParentTitle).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());

        // 校验excel中异常分类名称列表不能包含父级异常分类名称
        if (excelParentTitleList.stream().anyMatch(excelTitleList::contains)) {
            throw new ServiceException(ExceptionCategoryErrorCode.EXCEPTION_CATEGORY_TITLE_LIST_CAN_NOT_CONTAIN_PARENT_TITLE);
        }

        // 得到excel中在数据库存在的父级异常分类列表
        List<ExceptionCategory> existParentCategoryList = findExistParentCategoryList(excelParentTitleList);
        // 得到excel中在数据库不存在的父级异常分类名称列表
        List<String> notExistParentTitleList = getNotExistParentTitleList(existParentCategoryList, excelParentTitleList);

        exceptionCategoryService.importExceptionCategory(excelImportDataList, existParentCategoryList, notExistParentTitleList);
    }

    /**
     * 得到excel中在数据库存在的父级异常分类列表
     *
     * @param excelParentTitleList excel中的父级异常分类名称列表
     * @return
     */
    private List<ExceptionCategory> findExistParentCategoryList(List<String> excelParentTitleList) {
        if (CollectionUtil.isEmpty(excelParentTitleList)) {
            return Collections.emptyList();
        }
        return SpringUtil.getBean(ExceptionCategoryMapper.class).findCategoryList(null, excelParentTitleList);
    }

    /**
     * 得到excel中在数据库不存在的父级异常分类名称列表
     *
     * @param existParentCategoryList excel中在数据库存在的父级异常分类列表
     * @param excelParentTitleList    excel中的父级异常分类名称列表
     * @return
     */
    private List<String> getNotExistParentTitleList(List<ExceptionCategory> existParentCategoryList, List<String> excelParentTitleList) {
        // existParentCategoryList为空、excelParentTitleList为空：父级异常分类名称列表为空
        if (CollectionUtil.isEmpty(existParentCategoryList) && CollectionUtil.isEmpty(excelParentTitleList)) {
            return Collections.emptyList();
        }
        // existParentCategoryList不为空、excelParentTitleList为空：此种情况不存在
        // existParentCategoryList为空、excelParentTitleLis不为空：父级异常分类名称列表的名称都在数据库不存在
        if (CollectionUtil.isEmpty(existParentCategoryList) && CollectionUtil.isNotEmpty(excelParentTitleList)) {
            return excelParentTitleList;
        }
        // existParentCategoryList不为空、excelParentTitleLis不为空：父级异常分类名称列表的名称部分在数据库存在、部分在数据库不存在
        List<String> existParentTitleList = existParentCategoryList.stream().map(ExceptionCategory::getTitle).collect(Collectors.toList());
        return excelParentTitleList.stream().filter(parentTitle -> !existParentTitleList.contains(parentTitle)).collect(Collectors.toList());
    }

}
