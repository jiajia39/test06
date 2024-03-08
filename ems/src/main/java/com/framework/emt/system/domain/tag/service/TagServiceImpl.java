package com.framework.emt.system.domain.tag.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.tag.Tag;
import com.framework.emt.system.domain.tag.constant.code.TagErrorCode;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.domain.tag.convert.TagConvert;
import com.framework.emt.system.domain.tag.mapper.TagMapper;
import com.framework.emt.system.domain.tag.request.TagCreateRequest;
import com.framework.emt.system.domain.tag.request.TagQueryRequest;
import com.framework.emt.system.domain.tag.request.TagUpdateRequest;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.tag.response.TagResponse;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 异常标签 实现类
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {

    private final IUserDao userDao;

    private final DeptService deptService;

    @Override
    public Long create(TagCreateRequest request) {
        checkNameUnique(null, request.getContent(), request.getType());
        return this.create(TagConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    public void delete(Long id) {
        findById(id, TagErrorCode.NOT_EXIST_DATA_CAN_NOT_DELETE);
        this.deleteById(id);
    }

    @Override
    public Long update(Long id, TagUpdateRequest request) {
        Tag tag = findById(id, null);
        checkNameUnique(id, request.getContent(), request.getType());
        return this.update(TagConvert.INSTANCE.updateRequestToEntity(tag, request));
    }

    @Override
    public TagResponse detail(Long id) {
        TagResponse tag = TagConvert.INSTANCE.entityToResponse(findById(id, null));
        List<TagResponse> result = new ArrayList<>();
        result.add(tag);
        TypeReference<String> type = new TypeReference<String>() {
        };
        userDao.loadData(result, "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(result, "updateUser", User::getId, "updateUserName", "name", type);
        for (TagResponse record : result) {
            if (ObjectUtil.isNull(record.getCreateUser())) {
                record.setCreateUserName("系统");
            }
            if (ObjectUtil.isNull(record.getUpdateUser())) {
                record.setUpdateUserName("系统");
            }
        }
        return tag;
    }

    @Override
    public IPage<TagResponse> page(TagQueryRequest request) {
        IPage<Tag> page = Condition.getPage(request);
        LambdaUpdateWrapper<Tag> query = new LambdaUpdateWrapper<>();
        query.like(StrUtil.isNotBlank(request.getContent()), Tag::getContent, request.getContent());
        query.eq(ObjectUtil.isNotNull(request.getType()), Tag::getType, request.getType());
        query.orderByDesc(Tag::getCreateTime);
        page = this.page(page, query);
        IPage<TagResponse> result = TagConvert.INSTANCE.pageVo(page);
        if (ObjectUtil.isNotEmpty(result.getRecords())) {
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(result.getRecords(), "createUser", User::getId, "createUserName", "name", type);
            userDao.loadData(result.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
            for (TagResponse record : result.getRecords()) {
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

    /**
     * 根据id查询此条异常标签
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    @Override
    public Tag findById(Long id, IResultCode errorMessage) {
        return this.findByIdThrowErr(id, ObjectUtil.isNull(errorMessage) ? TagErrorCode.TAG_INFO_NOT_FIND : errorMessage);
    }

    /**
     * 根据内容查询标签信息
     *
     * @param content 内容
     * @param type    类型
     * @return 标签信息
     */
    @Override
    public Tag findByContent(String content, Integer type) {
        return this.getOne(new LambdaQueryWrapper<Tag>().eq(Tag::getContent, content).eq(Tag::getType, type));
    }

    @Override
    public long countByList(List<Long> id, Integer type) {
        return this.count(new LambdaQueryWrapper<Tag>().in(Tag::getId, id).eq(Tag::getType, type));
    }

    @Override
    public List<TagInfo> findTagListBySourceId(Long sourceId, SourceTypeEnum sourceTypeEnum) {
        return this.baseMapper.findTagListBySourceId(sourceId, sourceTypeEnum.getCode());
    }

    @Override
    public void checkNameUnique(Long id, String content, Integer type) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(Tag::getId, id);
        }
        queryWrapper.eq(Tag::getContent, content);
        queryWrapper.eq(Tag::getType, type);
        if (this.count(queryWrapper) > 0L) {
            throw new ServiceException(TagErrorCode.EXCEPTION_TAG_CONTENT_ALREADY_EXISTS);

        }
    }

    @Override
    public List<StatisticsProportionResponse> exceptionReasonTop(StatisticsTimeQueryRequest queryRequest) {
        List<Long> deptIds = null;
        if (ObjectUtil.isNotNull(queryRequest.getDeptId())) {
            deptIds = deptService.findByParentId(queryRequest.getDeptId());
        }
        return this.baseMapper.exceptionReasonTop(queryRequest, deptIds);
    }

    @Override
    public Map<String, Long> batchCreate(List<String> contents, TagTypeEnum tagTypeEnum) {
        // 获取数据库中存在的标签列表
        List<Tag> existTags = this.list(new LambdaQueryWrapper<Tag>().eq(Tag::getType, tagTypeEnum).in(Tag::getContent, contents));
        // 获取数据库中不存在的标签内容列表
        List<String> existContents = existTags.stream().map(Tag::getContent).collect(Collectors.toList());
        List<String> notExistContents = contents.stream().filter(content -> !existContents.contains(content)).collect(Collectors.toList());

        // 批量新增数据库中不存在的标签内容列表
        List<Tag> notExistTags = notExistContents.stream().map(content -> new Tag(content, tagTypeEnum)).collect(Collectors.toList());
        this.saveBatch(notExistTags);

        // 返回一个key为标签内容,value为标签id的map列表
        return Stream.of(existTags, notExistTags).flatMap(List::stream).collect(Collectors.toMap(Tag::getContent, Tag::getId));
    }

}
