package com.framework.emt.system.domain.knowledge.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.knowledge.KnowledgeBaseCategory;
import com.framework.emt.system.domain.knowledge.constant.code.KnowledgeBaseCategoryErrorCode;
import com.framework.emt.system.domain.knowledge.convert.KnowledgeBaseCategoryConvert;
import com.framework.emt.system.domain.knowledge.mapper.KnowledgeBaseCategoryMapper;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryCreateRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryQueryRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryUpdateRequest;
import com.framework.emt.system.domain.knowledge.response.CategoryAndChildResponse;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseCategoryResponse;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识库分类 实现类
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Service
@RequiredArgsConstructor
public class KnowledgeBaseCategoryServiceImpl extends BaseServiceImpl<KnowledgeBaseCategoryMapper, KnowledgeBaseCategory> implements KnowledgeBaseCategoryService {

    @Override
    public Long create(KnowledgeBaseCategoryCreateRequest request) {
        // 校验名称必须唯一
        checkTitleUnique(null, request.getTitle());
        return this.create(KnowledgeBaseCategoryConvert.INSTANCE.createRequestToEntity(request));
    }

    @Override
    public void delete(Long id) {
        // 校验知识库分类是否被引用
        CategoryAndChildResponse response = this.baseMapper.findCategoryAndExistChild(id);
        if (ObjectUtil.isNull(response)) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        if (response.getHasKnowledgeBase()) {
            throw new ServiceException(KnowledgeBaseCategoryErrorCode.KNOWLEDGE_BASE_CATEGORY_CONTAIN_KNOWLEDGE_BASE);
        }
        this.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 校验知识库分类是否被引用
        CategoryAndChildResponse response = this.baseMapper.findCategoryCountAndExistChild(ids);
        if (response.getCategoryCount() != ids.size()) {
            throw new ServiceException(BusinessErrorCode.NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE);
        }
        if (response.getKnowledgeBaseCount() > NumberUtils.LONG_ZERO) {
            throw new ServiceException(KnowledgeBaseCategoryErrorCode.KNOWLEDGE_BASE_CATEGORY_CONTAIN_KNOWLEDGE_BASE);
        }
        this.removeByIds(ids);
    }

    @Override
    public Long update(Long id, KnowledgeBaseCategoryUpdateRequest request) {
        KnowledgeBaseCategory knowledgeBaseCategory = findByIdThrowErr(id);
        // 校验名称必须唯一
        checkTitleUnique(id, request.getTitle());
        return this.update(KnowledgeBaseCategoryConvert.INSTANCE.updateRequestToEntity(knowledgeBaseCategory, request));
    }

    @Override
    public KnowledgeBaseCategoryResponse detail(Long id) {
        return KnowledgeBaseCategoryConvert.INSTANCE.entityToResponse(findByIdThrowErr(id));
    }

    @Override
    public IPage<KnowledgeBaseCategoryResponse> page(KnowledgeBaseCategoryQueryRequest request) {
        return DataHandleUtils.loadUserName(this.baseMapper.pageKnowledgeBaseCategory(Condition.getPage(request), request));
    }

    @Override
    public void changeEnableFlag(Integer enableFlag, List<Long> ids) {
        long count = this.count(new LambdaQueryWrapper<KnowledgeBaseCategory>().in(KnowledgeBaseCategory::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.STATE_CHANGE_FAIL_DATA_NOT_EXIST_CAN_NOT_CHANGE);
        }
        this.update(new LambdaUpdateWrapper<KnowledgeBaseCategory>().set(KnowledgeBaseCategory::getEnableFlag, enableFlag).in(KnowledgeBaseCategory::getId, ids));
    }

    @Override
    public KnowledgeBaseCategory findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, KnowledgeBaseCategoryErrorCode.KNOWLEDGE_BASE_CATEGORY_INFO_NOT_FIND);
    }

    @Override
    public List<KnowledgeBaseCategory> findListByTitles(List<String> titles) {
        return this.list(new LambdaQueryWrapper<KnowledgeBaseCategory>().in(KnowledgeBaseCategory::getTitle, titles));
    }

    /**
     * 校验知识库分类名称在数据库中不能存在
     *
     * @param id    主键id
     * @param title 知识库分类名称
     */
    private void checkTitleUnique(Long id, String title) {
        LambdaQueryWrapper<KnowledgeBaseCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(KnowledgeBaseCategory::getId, id);
        }
        queryWrapper.eq(KnowledgeBaseCategory::getTitle, title);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(KnowledgeBaseCategoryErrorCode.KNOWLEDGE_BASE_CATEGORY_TITLE_IS_EXIST);
        }
    }

}
