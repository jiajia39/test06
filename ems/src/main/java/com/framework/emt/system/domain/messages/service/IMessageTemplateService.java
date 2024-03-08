package com.framework.emt.system.domain.messages.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.messages.MessageTemplate;
import com.framework.emt.system.domain.messages.request.MessageTemplateQueryRequest;
import com.framework.emt.system.domain.messages.request.MessageTemplateRequest;
import com.framework.emt.system.domain.messages.response.MessageTemplateResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

/**
 * 消息发送记录 服务层
 *
 * @author yankunw
 * @since 2023-07-19
 */
public interface IMessageTemplateService extends BaseService<MessageTemplate> {

    /**
     * 保存模板
     *
     * @param request 保存模板参数
     * @return 是否保存成功
     */
    boolean save(MessageTemplateRequest request);

    /**
     * 启用模板
     *
     * @param id 模板id
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 禁用模板
     *
     * @param id 模板id
     * @return 是否成功
     */
    boolean disable(Long id);

    /**
     * 查询消息模板列表
     *
     * @param request 查询参数
     * @return 消息列表分页信息
     */
    IPage<MessageTemplateResponse> page(MessageTemplateQueryRequest request);

    /**
     * 根据模板code查询模板详细
     *
     * @param code 模板code
     * @return
     */
    MessageTemplate findByCode(String code);

}
