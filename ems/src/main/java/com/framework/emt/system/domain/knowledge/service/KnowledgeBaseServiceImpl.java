package com.framework.emt.system.domain.knowledge.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.knowledge.KnowledgeBase;
import com.framework.emt.system.domain.knowledge.constant.code.KnowledgeBaseErrorCode;
import com.framework.emt.system.domain.knowledge.convert.KnowledgeBaseConvert;
import com.framework.emt.system.domain.knowledge.mapper.KnowledgeBaseMapper;
import com.framework.emt.system.domain.knowledge.request.*;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseExportResponse;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseResponse;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.domain.tag.convert.TagConvert;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.domain.tagexception.TagException;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.service.TagExceptionService;
import com.framework.emt.system.infrastructure.common.object.TitleMap;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库 实现类
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Service
@RequiredArgsConstructor
public class KnowledgeBaseServiceImpl extends BaseServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService {

    private final KnowledgeBaseCategoryService knowledgeBaseCategoryService;

    private final ExceptionItemService exceptionItemService;

    private final TagService tagService;

    private final TagExceptionService tagExceptionService;

    @Override
    @DSTransactional
    public Long create(KnowledgeBaseCreateRequest request) {
        // 校验名称必须唯一
        checkTitleUnique(null, request.getTitle());
        // 校验文件上传数量
        checkFileUploadNum(request.getFiles().size());
        // 校验知识库分类是否存在
        knowledgeBaseCategoryService.findByIdThrowErr(request.getKnowledgeBaseCategoryId());
        // 校验异常项是否存在
        exceptionItemService.findByIdThrowErr(request.getExceptionItemId());

        // 新增知识库
        Long knowledgeBaseId = create(KnowledgeBaseConvert.INSTANCE.createRequestToEntity(request));
        // 新增知识库对应的关键词
        List<Long> keyWordIds = request.getKeyWordIds();
        if (CollectionUtil.isNotEmpty(keyWordIds)) {
            Integer sourceTypeCode = SourceTypeEnum.KNOWLEDGE_BASE.getCode();
            tagExceptionService.createList(new TagExceptionCreateListRequest(keyWordIds, knowledgeBaseId, sourceTypeCode));
        }

        return knowledgeBaseId;
    }

    @Override
    @DSTransactional
    public void delete(Long id) {
        // 校验知识库是否存在、是否已启用
        KnowledgeBase knowledgeBase = this.getOne(new LambdaQueryWrapper<KnowledgeBase>()
                .eq(KnowledgeBase::getId, id)
                .eq(KnowledgeBase::getEnableFlag, EnableFlagEnum.FORBIDDEN));
        if (ObjectUtil.isNull(knowledgeBase)) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        this.deleteById(id);
        // 删除知识库下的关键词标签
        tagExceptionService.deleteBySourceId(id);
    }

    @Override
    @DSTransactional
    public void deleteBatch(List<Long> ids) {
        // 校验知识库是否存在、是否已启用
        long count = this.count(new LambdaQueryWrapper<KnowledgeBase>()
                .eq(KnowledgeBase::getEnableFlag, EnableFlagEnum.FORBIDDEN)
                .in(KnowledgeBase::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        this.removeByIds(ids);
        // 删除知识库下的关键词标签
        tagExceptionService.deleteBySourceIdList(ids);
    }

    @Override
    public Long update(Long id, KnowledgeBaseUpdateRequest request) {
        KnowledgeBase knowledgeBase = findKnowledgeBaseByIdThrowErr(id);
        // 校验文件上传数量
        checkFileUploadNum(request.getFiles().size());
        // 校验知识库分类是否存在
        knowledgeBaseCategoryService.findByIdThrowErr(request.getKnowledgeBaseCategoryId());
        // 校验异常项是否存在
        exceptionItemService.findByIdThrowErr(request.getExceptionItemId());

        return this.update(KnowledgeBaseConvert.INSTANCE.updateRequestToEntity(knowledgeBase, request));
    }

    @Override
    public KnowledgeBaseResponse detail(Long id) {
        return this.baseMapper.detail(findKnowledgeBaseByIdThrowErr(id).getId());
    }

    @Override
    public IPage<KnowledgeBaseResponse> page(KnowledgeBaseQueryRequest request) {
        return DataHandleUtils.loadUserName(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public IPage<KnowledgeBaseResponse> pageApp(KnowledgeBaseAppQueryRequest request) {
        return this.baseMapper.pageApp(Condition.getPage(request), request);
    }

    @Override
    public KnowledgeBaseResponse detailApp(Long id) {
        return Optional.ofNullable(this.baseMapper.detailApp(id)).orElseThrow(() -> new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_INFO_NOT_FIND));
    }

    @Override
    public void changeEnableFlag(Integer enableFlag, List<Long> idList) {
        long count = this.count(new LambdaQueryWrapper<KnowledgeBase>().in(KnowledgeBase::getId, idList));
        if (count != idList.size()) {
            throw new ServiceException(BusinessErrorCode.STATE_CHANGE_FAIL_DATA_NOT_EXIST_CAN_NOT_CHANGE);
        }
        this.update(new LambdaUpdateWrapper<KnowledgeBase>().set(KnowledgeBase::getEnableFlag, enableFlag).in(KnowledgeBase::getId, idList));
    }

    @Override
    public Long createTag(KnowledgeBaseTagCreateRequest request) {
        Integer sourceTypeCode = SourceTypeEnum.KNOWLEDGE_BASE.getCode();
        return tagService.create(TagConvert.INSTANCE.paramsToCreateRequest(request.getTagContent(), sourceTypeCode));
    }

    @Override
    public Long createTagRelation(KnowledgeBaseTagRelationCreateRequest request) {
        if (ObjectUtil.isNull(request.getTagId()) && StringUtils.isBlank(request.getTagContent())) {
            throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_TAG_ID_OR_TAG_CONTENT_MUST_HAVE_ONE);
        }
        Integer sourceTypeCode = SourceTypeEnum.KNOWLEDGE_BASE.getCode();
        return tagExceptionService.create(request.getId(), request.getTagId(), request.getTagContent(), sourceTypeCode);
    }

    @Override
    public void deleteTag(KnowledgeBaseTagDeleteRequest request) {
        Integer sourceTypeCode = SourceTypeEnum.KNOWLEDGE_BASE.getCode();
        tagExceptionService.deleteTagException(request.getTagId(), request.getId(), sourceTypeCode);
    }

    @Override
    public List<KnowledgeBaseExportResponse> export(KnowledgeBaseExportQueryRequest request) {
        long exportCount = this.baseMapper.findExportDataCount(request, request.getIdList());
        long maxExportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_EXPORT_NUM, AuthUtil.getTenantId()), BusinessConstant.MAX_EXPORT_NUM);
        if (exportCount > maxExportNum) {
            throw new ServiceException(BusinessErrorCode.EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM.getMessage() + Convert.toStr(maxExportNum));
        }
        return DataHandleUtils.loadUserName(this.baseMapper.findExportData(request, request.getIdList()));
    }

    @Override
    @DSTransactional
    public void importDataList(List<KnowledgeBaseImportExcel> importDataList, List<String> keyWordsList, TitleMap titleMap) {
        // 批量新增关键词
        Map<String, Long> keyWordMap = batchCreateKeyWord(keyWordsList);
        // 批量导入excel数据集
        List<KnowledgeBase> knowledgeBases = KnowledgeBaseConvert.INSTANCE.importDataListToEntityList(importDataList, titleMap);
        this.saveBatch(knowledgeBases);
        // 批量新增知识库和关键词的关联关系
        this.batchCreateRelation(keyWordsList, keyWordMap, knowledgeBases);
    }

    @Override
    public KnowledgeBase findByTitles(List<String> titleList) {
        return this.getOne(new LambdaQueryWrapper<KnowledgeBase>().in(KnowledgeBase::getTitle, titleList).last("limit 1"));
    }

    /**
     * 根据id查询此条知识库
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    private KnowledgeBase findKnowledgeBaseByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, KnowledgeBaseErrorCode.KNOWLEDGE_BASE_INFO_NOT_FIND);
    }

    /**
     * 校验知识库文件上传数量必须小于系统指定的文件上传最大数量
     *
     * @param fileNum 文件数量
     */
    private void checkFileUploadNum(int fileNum) {
        Integer maxFileUploadNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_FILE_UPLOAD_COUNT,
                AuthUtil.getTenantId()), BusinessConstant.FILE_UPLOAD_COUNT);
        if (fileNum > maxFileUploadNum) {
            throw new ServiceException(BusinessErrorCode.FILE_UPLOAD_FAIL_EXCEED_SYSTEM_MAX_NUM.getMessage() + Convert.toStr(maxFileUploadNum));
        }
    }

    /**
     * 校验知识库名称在数据库中不能存在
     *
     * @param id    主键id
     * @param title 知识库名称
     */
    private void checkTitleUnique(Long id, String title) {
        LambdaQueryWrapper<KnowledgeBase> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(KnowledgeBase::getId, id);
        }
        queryWrapper.eq(KnowledgeBase::getTitle, title);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(KnowledgeBaseErrorCode.KNOWLEDGE_BASE_TITLE_IS_EXIST);
        }
    }

    /**
     * 批量新增关键词
     *
     * @param keyWordsList 关键词列表
     * @return key为关键词, value为关键词id的map列表
     */
    private Map<String, Long> batchCreateKeyWord(List<String> keyWordsList) {
        if (keyWordsList == null) {
            return null;
        }
        // 获取逗号分割去空格去重之后的关键词列表
        List<String> keyWordList = keyWordsList.stream().flatMap(keyWords ->
                Arrays.stream(keyWords.split(StringConstant.PAUSE))).map(String::trim).distinct().collect(Collectors.toList());
        // 批量新增关键词
        TagTypeEnum tagTypeEnum = TagTypeEnum.KNOWLEDGE_BASE_LABEL;
        return tagService.batchCreate(keyWordList, tagTypeEnum);
    }

    /**
     * 批量新增知识库和关键词的关联关系
     *
     * @param keyWordsList   关键词列表
     * @param keyWordMap     key为关键词, value为关键词id的map列表
     * @param knowledgeBases 知识库列表
     */
    private void batchCreateRelation(List<String> keyWordsList, Map<String, Long> keyWordMap, List<KnowledgeBase> knowledgeBases) {
        if (keyWordMap == null) {
            return;
        }
        Integer sourceTypeCode = SourceTypeEnum.KNOWLEDGE_BASE.getCode();
        List<TagException> tagExceptions = new ArrayList<>();
        for (int i = NumberUtils.INTEGER_ZERO; i < knowledgeBases.size(); i++) {
            String keyWords = keyWordsList.get(i);
            if (StrUtil.EMPTY.equals(keyWords)) {
                continue;
            }
            Long knowledgeBaseId = knowledgeBases.get(i).getId();
            for (String keyWord : keyWords.split(StringConstant.PAUSE)) {
                Long tagId = keyWordMap.get(keyWord);
                if (tagId == null) {
                    continue;
                }
                TagException tagException = new TagException();
                tagException.setSourceId(knowledgeBaseId);
                tagException.setSourceType(sourceTypeCode);
                tagException.setTagId(tagId);
                tagExceptions.add(tagException);
            }
        }
        tagExceptionService.saveBatch(tagExceptions);
    }

}
