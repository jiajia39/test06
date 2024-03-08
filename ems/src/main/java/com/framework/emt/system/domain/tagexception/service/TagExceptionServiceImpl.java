package com.framework.emt.system.domain.tagexception.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.service.ExceptionProcessService;
import com.framework.emt.system.domain.knowledge.service.KnowledgeBaseService;
import com.framework.emt.system.domain.tag.Tag;
import com.framework.emt.system.domain.tag.constant.code.TagErrorCode;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.domain.tag.request.TagCreateRequest;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.domain.tagexception.TagException;
import com.framework.emt.system.domain.tagexception.constant.code.TagExceptionErrorCode;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.domain.tagexception.convert.TagExceptionConvert;
import com.framework.emt.system.domain.tagexception.mapper.TagExceptionMapper;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateRequest;
import com.framework.emt.system.domain.tagexception.request.TagExceptionQueryRequest;
import com.framework.emt.system.domain.tagexception.response.TagExceptionResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常标签关联 实现类
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Service
@RequiredArgsConstructor
public class TagExceptionServiceImpl extends BaseServiceImpl<TagExceptionMapper, TagException> implements TagExceptionService {

    private final IUserDao userDao;

    private final TagService tagService;

    @Override
    public Long create(TagExceptionCreateRequest request) {
        checkTypeIsConsistent(request.getSourceType(), request.getSourceId());
        Integer type;
        if (ObjectUtil.equal(request.getSourceType(), SourceTypeEnum.KNOWLEDGE_BASE.getCode())) {
            type = TagTypeEnum.KNOWLEDGE_BASE_LABEL.getCode();
        } else {
            type = TagTypeEnum.ABNORMAL_CAUSE_ITEM.getCode();
        }
        //如果 标签已存在 则关联表直接保存  不存在 先保存标签再保存关联表信息
        Tag tag = tagService.findByContent(request.getContent(), type);
        if (ObjectUtil.isNotEmpty(tag)) {
            request.setTagId(tag.getId());
        } else {
            TagCreateRequest tagCreateRequest = new TagCreateRequest();
            tagCreateRequest.setContent(request.getContent());
            tagCreateRequest.setType(type);
            Long id = tagService.create(tagCreateRequest);
            request.setTagId(id);
        }
        TagException tagException = getTagException(request.getTagId(), request.getSourceId(), request.getSourceType());
        if (ObjectUtil.isNotEmpty(tagException)) {
            throw new ServiceException(TagExceptionErrorCode.EXCEPTION_TAG_ASSOCIATION_INFORMATION_ALREADY_EXISTS);
        }
        return this.create(TagExceptionConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    public void createList(TagExceptionCreateListRequest request) {
        checkTypeIsConsistent(request.getSourceType(), request.getSourceId());
        //验证标签类型是否和关联类型一致
        Integer type;
        if (ObjectUtil.equal(request.getSourceType(), SourceTypeEnum.KNOWLEDGE_BASE.getCode())) {
            type = TagTypeEnum.KNOWLEDGE_BASE_LABEL.getCode();
        } else {
            type = TagTypeEnum.ABNORMAL_CAUSE_ITEM.getCode();
        }
        long count = tagService.countByList(request.getTagIdList(), type);
        if (count != request.getTagIdList().size()) {
            throw new ServiceException(TagExceptionErrorCode.THE_TYPE_OF_TAG_ID_LIST_DOES_NOT_MATCH_THE_SOURCE_TYPE);
        }
        ArrayList<TagException> tagExceptionList = new ArrayList<>();
        for (Long tagId : request.getTagIdList()) {
            //如果标签关联信息不存在则添加
            LambdaQueryWrapper<TagException> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TagException::getTagId, tagId);
            queryWrapper.eq(TagException::getSourceId, request.getSourceId());
            queryWrapper.eq(TagException::getSourceType, request.getSourceType());
            if (ObjectUtil.equal(this.count(queryWrapper), 0L)) {
                TagException tagException = new TagException();
                tagException.setSourceId(request.getSourceId());
                tagException.setSourceType(request.getSourceType());
                tagException.setTagId(tagId);
                tagExceptionList.add(tagException);
            }
        }
        this.saveBatch(tagExceptionList);
    }

    @Override
    public Long create(Long sourceId, Long tagId, String content, Integer sourceTypeCode) {
        checkTypeIsConsistent(sourceTypeCode, sourceId);
        if (ObjectUtil.isNotNull(tagId)) {
            Tag tag = tagService.findByIdThrowErr(tagId, TagErrorCode.TAG_INFO_NOT_FIND);
            if (!ObjectUtil.equals(sourceTypeCode, tag.getType().getCode())) {
                throw new ServiceException(TagExceptionErrorCode.THE_TYPE_OF_TAG_ID_NOT_MATCH_THE_SOURCE_TYPE);
            }
            return this.create(TagExceptionConvert.INSTANCE.paramsToEntity(sourceId, tagId, sourceTypeCode));
        }
        tagService.checkNameUnique(null, content, sourceTypeCode);
        return create(TagExceptionConvert.INSTANCE.paramsToCreatRequest(sourceId, content, sourceTypeCode));
    }


    @Override
    public void delete(Long id) {
        TagException tagException = findById(id, TagExceptionErrorCode.NOT_EXIST_DATA_CAN_NOT_DELETE);
        this.deleteById(id);
        if (ObjectUtil.equal(findByTagId(tagException.getTagId()), 0L)) {
            tagService.delete(tagException.getTagId());
        }
    }

    @Override
    public TagExceptionResponse detail(Long id) {
        TagExceptionResponse tagException = TagExceptionConvert.INSTANCE.entityToResponse(findById(id, null));
        List<TagExceptionResponse> result = new ArrayList<>();
        result.add(tagException);
        TypeReference<String> type = new TypeReference<String>() {
        };
        userDao.loadData(result, "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(result, "updateUser", User::getId, "updateUserName", "name", type);
        for (TagExceptionResponse record : result) {
            if (ObjectUtil.isNull(record.getCreateUser())) {
                record.setCreateUserName("系统");
            }
            if (ObjectUtil.isNull(record.getUpdateUser())) {
                record.setUpdateUserName("系统");
            }
        }
        return tagException;
    }

    @Override
    public IPage<TagExceptionResponse> page(TagExceptionQueryRequest request) {
        IPage<TagException> page = Condition.getPage(request);
        LambdaUpdateWrapper<TagException> query = new LambdaUpdateWrapper<>();
        query.eq(ObjectUtil.isNotNull(request.getSourceType()), TagException::getSourceType, request.getSourceType());
        page = this.page(page, query);
        IPage<TagExceptionResponse> result = TagExceptionConvert.INSTANCE.pageVo(page);
        if (ObjectUtil.isNotEmpty(result.getRecords())) {
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(result.getRecords(), "createUser", User::getId, "createUserName", "name", type);
            userDao.loadData(result.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
            for (TagExceptionResponse record : result.getRecords()) {
                if (ObjectUtil.isNull(record.getCreateUser())) {
                    record.setCreateUserName("系统");
                }
                if (ObjectUtil.isNull(record.getUpdateUser())) {
                    record.setUpdateUserName("系统");
                }
            }
        }
        return result;
    }

    @Override
    public void deleteBySourceId(Long sourceId) {
        LambdaQueryWrapper<TagException> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TagException::getSourceId, sourceId);
        List<TagException> tagExceptionList = this.list(queryWrapper);
        if (tagExceptionList.size() > 0) {
            deleteTagIdsNotExist(tagExceptionList);
        }
    }

    @Override
    public void deleteBySourceIdList(List<Long> sourceIdList) {
        LambdaQueryWrapper<TagException> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TagException::getSourceId, sourceIdList);
        List<TagException> tagExceptionList = this.list(queryWrapper);
        deleteTagIdsNotExist(tagExceptionList);
    }

    @Override
    public void deleteTagException(Long tagId, Long sourceId, Integer sourceType) {
        TagException tagException = getTagException(tagId, sourceId, sourceType);
        if (ObjectUtil.isNotEmpty(tagException)) {
            this.deleteById(tagException.getId());
            if (ObjectUtil.equal(findByTagId(tagException.getTagId()), 0L)) {
                tagService.delete(tagException.getTagId());
            }
        } else {
            throw new ServiceException(TagExceptionErrorCode.NOT_EXIST_DATA_CAN_NOT_DELETE);
        }
    }

    /**
     * 删除关联id的所有关联标签信息，并判断关联表中是否有对应标签的数据，没有就删除标签
     *
     * @param tagExceptionList 需要被删除的标签关联信息
     */
    private void deleteTagIdsNotExist(List<TagException> tagExceptionList) {
        List<Long> idList = tagExceptionList.stream().map(TagException::getId).collect(Collectors.toList());
        this.removeBatchByIds(idList);

        //判断关联表中是否有对应标签的数据，没有就删除标签
        List<Long> tagIdList = tagExceptionList.stream().map(TagException::getTagId).distinct().collect(Collectors.toList());
        List<Long> tags = this.baseMapper.getIdByTagIds(tagIdList);
        List<Long> nonExistingTagIds = tagIdList.stream()
                .filter(tagId -> !tags.contains(tagId))
                .collect(Collectors.toList());
        if (nonExistingTagIds.size() > 0) {
            tagService.removeBatchByIds(nonExistingTagIds);
        }
    }

    /**
     * 验证关联表id的类型和关联类型是否一致
     *
     * @param sourceType 关联类型
     * @param sourceId   关联表id
     */
    @Override
    public void checkTypeIsConsistent(Integer sourceType, Long sourceId) {
        SourceTypeEnum sourceTypeEnum = SourceTypeEnum.getEnum(sourceType);
        //验证关联表id与类型是否匹配
        switch (sourceTypeEnum) {
            case KNOWLEDGE_BASE:
                KnowledgeBaseService knowledgeBaseService = SpringUtil.getBean(KnowledgeBaseService.class);
                knowledgeBaseService.findByIdThrowErr(sourceId, TagExceptionErrorCode.SOURCE_TYPE_IS_KNOWLEDGE_BASE_THE_SOURCE_ID_MUST_BE_KNOWLEDGE_BASE_DATA);
                break;
            case ABNORMAL_PROCESS:
                ExceptionProcessService exceptionProcessService = SpringUtil.getBean(ExceptionProcessService.class);
                exceptionProcessService.findByIdThrowErr(sourceId, TagExceptionErrorCode.SOURCE_TYPE_IS_ABNORMAL_PROCESS_THE_SOURCE_ID_MUST_BE_ABNORMAL_PROCESS_DATA);
                break;
            case ABNORMAL_TASK:
                break;
        }
    }

    private Long findByTagId(Long tagId) {
        return this.count(new LambdaQueryWrapper<TagException>().eq(TagException::getTagId, tagId));
    }

    /**
     * 根据id查询此条异常标签
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    private TagException findById(Long id, IResultCode errorMessage) {
        return this.findByIdThrowErr(id, ObjectUtil.isNull(errorMessage) ? TagExceptionErrorCode.TAG_EXCEPTION_INFO_NOT_FIND : errorMessage);
    }

    @Override
    public TagException getTagException(Long tagId, Long sourceId, Integer sourceType) {
        LambdaQueryWrapper<TagException> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TagException::getTagId, tagId);
        queryWrapper.eq(TagException::getSourceId, sourceId);
        queryWrapper.eq(TagException::getSourceType, sourceType);
        return this.getOne(queryWrapper);
    }

}
