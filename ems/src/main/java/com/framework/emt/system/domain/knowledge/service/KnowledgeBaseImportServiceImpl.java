package com.framework.emt.system.domain.knowledge.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.excel.support.ExcelImporter;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionItemErrorCode;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.knowledge.KnowledgeBase;
import com.framework.emt.system.domain.knowledge.KnowledgeBaseCategory;
import com.framework.emt.system.domain.knowledge.constant.code.KnowledgeBaseCategoryErrorCode;
import com.framework.emt.system.domain.knowledge.constant.code.KnowledgeBaseErrorCode;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseImportExcel;
import com.framework.emt.system.infrastructure.common.object.TitleMap;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识库导入 实现类
 *
 * @author ds_C
 * @since 2023-09-06
 */
@RequiredArgsConstructor
public class KnowledgeBaseImportServiceImpl implements ExcelImporter<KnowledgeBaseImportExcel> {

    private final KnowledgeBaseService knowledgeBaseService;

    @Override
    public void save(List<KnowledgeBaseImportExcel> importDataList) {
        // 校验传入的excel列表不能为空
        if (NumberUtils.INTEGER_ZERO.equals(importDataList.size())) {
            throw new ServiceException(BusinessErrorCode.IMPORT_DATA_IS_NOT_NULL);
        }
        // 校验excel列表中的非空数据
        validateIsNotBlankData(importDataList);
        // 校验excel列表中的知识库标题不能重复，且不能在数据库中存在
        validateTitle(importDataList.stream().map(KnowledgeBaseImportExcel::getTitle).collect(Collectors.toList()));
        // 校验关键词是否都为空，且每条知识库对应的关键词不能重复
        List<String> keyWordsList = validateKeyWords(importDataList.stream()
                .map(KnowledgeBaseImportExcel::getKeyWords).collect(Collectors.toList()));

        // 通过知识库分类名称查询出key为知识库分类名称：value为知识库分类id的map列表，若数据异常则抛出相应的错误信息
        Map<String, Long> categoryTitleOfIdMap = findCategoryMap(importDataList.stream()
                .map(KnowledgeBaseImportExcel::getKnowledgeBaseCategoryTitle).distinct().collect(Collectors.toList()));
        // 通过异常项名称查询出key为异常项名称：value为异常项id的map列表，若数据异常则抛出相应的错误信息
        Map<String, Long> itemTitleOfIdMap = findItemMap(importDataList.stream()
                .map(KnowledgeBaseImportExcel::getExceptionItemTitle).distinct().collect(Collectors.toList()));

        // 封装知识库分类和异常项Map列表包装类
        TitleMap titleMap = new TitleMap(categoryTitleOfIdMap, itemTitleOfIdMap);
        knowledgeBaseService.importDataList(importDataList, keyWordsList, titleMap);
    }

    /**
     * 查询excel中的异常项名称对应的异常项id，若数据异常则抛出相应的错误信息
     *
     * @param itemTitleList excel中的异常项名称列表
     * @return key为异常项名称，value为异常项id的map列表
     */
    private Map<String, Long> findItemMap(List<String> itemTitleList) {
        ExceptionItemService itemService = SpringUtil.getBean(ExceptionItemService.class);
        List<ExceptionItem> itemList = itemService.findListByTitles(itemTitleList);
        if (itemList.size() != itemTitleList.size()) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEl_CONTAIN_NOT_EXIST_ITEM_TITLE);
        }
        return itemList.stream().collect(Collectors.toMap(ExceptionItem::getTitle, ExceptionItem::getId));
    }

    /**
     * 查询excel中的知识库分类名称对应的知识库分类id，若数据异常则抛出相应的错误信息
     *
     * @param categoryTitleList excel中的知识库分类名称列表
     * @return key为知识库分类名称，value为知识库分类id的map列表
     */
    private Map<String, Long> findCategoryMap(List<String> categoryTitleList) {
        KnowledgeBaseCategoryService categoryService = SpringUtil.getBean(KnowledgeBaseCategoryService.class);
        List<KnowledgeBaseCategory> categoryList = categoryService.findListByTitles(categoryTitleList);
        if (categoryList.size() != categoryTitleList.size()) {
            throw new ServiceException(KnowledgeBaseCategoryErrorCode.EXCEl_CONTAIN_NOT_EXIST_KNOWLEDGE_BASE_CATEGORY_TITLE);
        }
        return categoryList.stream().collect(Collectors.toMap(KnowledgeBaseCategory::getTitle, KnowledgeBaseCategory::getId));
    }


    /**
     * 校验excel列表中的知识库标题不能重复，且不能在数据库中存在
     *
     * @param titleList excel中的知识库标题列表
     */
    private void validateTitle(List<String> titleList) {
        long count = titleList.stream().distinct().count();
        if (count != titleList.size()) {
            throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_TITLE_MUST_UNIQUE);
        }
        KnowledgeBase knowledgeBase = knowledgeBaseService.findByTitles(titleList);
        if (ObjectUtil.isNotNull(knowledgeBase)) {
            throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_TITLE_IS_EXIST);
        }
    }

    /**
     * 校验关键词是否都为空，且每条知识库对应的关键词不能重复
     *
     * @param keyWordsList 关键词列表
     * @return
     */
    private List<String> validateKeyWords(List<String> keyWordsList) {
        List<String> keyWordsIsNotBlankList = keyWordsList.stream()
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(keyWordsList)) {
            return null;
        }
        keyWordsIsNotBlankList.stream().distinct().forEach(keyWords -> {
            // 将每条知识库对应的关键词字符串进行拆分，然后校验不能重复
            List<String> keyWordList = Arrays.asList(keyWords.split(StringConstant.PAUSE));
            long count = keyWordList.stream().map(String::trim).distinct().count();
            if (count != keyWordList.size()) {
                throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_KEYWORD_MUST_UNIQUE);
            }
        });
        return keyWordsList;
    }

    /**
     * 校验excel中的必填数据不能为空
     *
     * @param importDataList excel导入的数据集
     */
    private void validateIsNotBlankData(List<KnowledgeBaseImportExcel> importDataList) {
        for (KnowledgeBaseImportExcel importData : importDataList) {
            if (StringUtils.isBlank(importData.getTitle())) {
                throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_TITLE_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getKnowledgeBaseCategoryTitle())) {
                throw new ServiceException(KnowledgeBaseCategoryErrorCode.KNOWLEDGE_BASE_CATEGORY_TITLE_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getExceptionItemTitle())) {
                throw new ServiceException(ExceptionItemErrorCode.EXCEPTION_ITEM_TITLE_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getProblemDescription())) {
                throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_PROBLEM_DESCRIPTION_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getProblemAnalysis())) {
                throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_PROBLEM_ANALYSIS_IS_NOT_BLANK);
            }
            if (StringUtils.isBlank(importData.getSuggestion())) {
                throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_SUGGESTION_IS_NOT_BLANK);
            }
        }
    }

}
