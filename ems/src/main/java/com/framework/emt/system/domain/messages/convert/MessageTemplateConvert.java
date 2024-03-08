package com.framework.emt.system.domain.messages.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.messages.MessageTemplate;
import com.framework.emt.system.domain.messages.request.MessageTemplateRequest;
import com.framework.emt.system.domain.messages.response.MessageTemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 消息模板 转换类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@Mapper
public interface MessageTemplateConvert {
    MessageTemplateConvert INSTANCE = Mappers.getMapper(MessageTemplateConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return 实体
     */
    MessageTemplate createRequest2Entity(MessageTemplateRequest request);



    /**
     * 实体转换vo
     *
     * @param messageTemplate 实体
     * @return vo
     */
    MessageTemplateResponse entity2Vo(MessageTemplate messageTemplate);

    /**
     * 实体列表转换成vo列表
     *
     * @param messageTemplateList 实体列表
     * @return vo列表
     */
    List<MessageTemplateResponse> entityList2Vo(List<MessageTemplate> messageTemplateList);


    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<MessageTemplateResponse> pageVo(IPage<MessageTemplate> pages) {
        IPage<MessageTemplateResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(MessageTemplateConvert.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }
}
