package com.framework.emt.system.domain.messages.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.messages.MessageTemplate;
import com.framework.emt.system.domain.messages.constant.code.MessageErrorCode;
import com.framework.emt.system.domain.messages.convert.MessageTemplateConvert;
import com.framework.emt.system.domain.messages.mapper.MessageTemplateMapper;
import com.framework.emt.system.domain.messages.request.MessageTemplateQueryRequest;
import com.framework.emt.system.domain.messages.request.MessageTemplateRequest;
import com.framework.emt.system.domain.messages.response.MessageTemplateResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/**
 * 消息推送模板 实现类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl extends BaseServiceImpl<MessageTemplateMapper, MessageTemplate> implements IMessageTemplateService {

    private final IUserDao userDao;

    /**
     * 保存模板
     *
     * @param request 保存模板参数
     * @return 是否保存成功
     */
    @Override
    public boolean save(MessageTemplateRequest request) {
        MessageTemplate messageTemplate = MessageTemplateConvert.INSTANCE.createRequest2Entity(request);
        return this.save(messageTemplate);
    }

    /**
     * 启用模板
     *
     * @param id 模板id
     * @return 是否成功
     */
    @Override
    public boolean enable(Long id) {
        Function<MessageTemplate, Boolean> func = messageTemplate -> {
            messageTemplate.setEnableFlag(1);
            return true;
        };
        return changeTemplateStatus(id, func);
    }

    /**
     * 禁用模板
     *
     * @param id 模板id
     * @return 是否成功
     */
    @Override
    public boolean disable(Long id) {
        Function<MessageTemplate, Boolean> func = messageTemplate -> {
            messageTemplate.setEnableFlag(0);
            return true;
        };
        return changeTemplateStatus(id, func);
    }

    public boolean changeTemplateStatus(Long id, Function<MessageTemplate, Boolean> func) {
        MessageTemplate messageTemplate = this.getById(id);
        if (!ObjectUtil.isNotNull(messageTemplate)) {
            throw new ServiceException("未找到对应的模板信息！");
        }
        func.apply(messageTemplate);
        return updateById(messageTemplate);
    }

    /**
     * 查询消息模板列表
     *
     * @param request 查询参数
     * @return 消息列表分页信息
     */
    @Override
    public IPage<MessageTemplateResponse> page(MessageTemplateQueryRequest request) {
        IPage<MessageTemplate> page = Condition.getPage(request);
        LambdaUpdateWrapper<MessageTemplate> query = new LambdaUpdateWrapper<>();
        query.eq(ObjectUtil.isNotNull(request.getId()), MessageTemplate::getId, request.getId());
        query.like(ObjectUtil.isNotNull(request.getName()), MessageTemplate::getName, request.getName());
        query.eq(ObjectUtil.isNotNull(request.getLevel()), MessageTemplate::getLevel, request.getLevel());
        query.like(ObjectUtil.isNotNull(request.getCode()), MessageTemplate::getCode, request.getCode());
        query.like(StrUtil.isNotBlank(request.getContent()), MessageTemplate::getContent, request.getContent());
        query.eq(ObjectUtil.isNotEmpty(request.getEnableFlag()), MessageTemplate::getEnableFlag, request.getEnableFlag());
        query.eq(ObjectUtil.isNotNull(request.getCreateUser()), MessageTemplate::getCreateUser, request.getCreateUser());
        query.ge(ObjectUtil.isNotNull(request.getUpdateUser()), MessageTemplate::getUpdateUser, request.getUpdateUser());
        query.le(ObjectUtil.isNotNull(request.getCreateTimeEnd()), MessageTemplate::getCreateTime, request.getCreateTimeEnd());
        query.ge(ObjectUtil.isNotNull(request.getCreateTimeStart()), MessageTemplate::getCreateTime, request.getCreateTimeStart());
        query.ge(ObjectUtil.isNotNull(request.getUpdateTimeStart()), MessageTemplate::getUpdateTime, request.getUpdateTimeStart());
        query.le(ObjectUtil.isNotNull(request.getUpdateTimeEnd()), MessageTemplate::getUpdateTime, request.getUpdateTimeEnd());
        query.like(StrUtil.isNotBlank(request.getRemark()), MessageTemplate::getRemark, request.getRemark());
        page = this.page(page, query);
        IPage<MessageTemplateResponse> result = MessageTemplateConvert.INSTANCE.pageVo(page);
        if (ObjectUtil.isNotEmpty(result.getRecords())) {
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(result.getRecords(), "createUser", User::getId, "createUserName", "name", type);
            userDao.loadData(result.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
        }
        return result;
    }

    @Override
    public MessageTemplate findByCode(String code) {
        MessageTemplate messageTemplate = this.getOne(new LambdaQueryWrapper<MessageTemplate>()
                .eq(MessageTemplate::getCode, code).last("limit 1"));
        return Optional.ofNullable(messageTemplate).orElseThrow(() -> new ServiceException(MessageErrorCode.TEMPLATE_NOT_FOUND));
    }

}
