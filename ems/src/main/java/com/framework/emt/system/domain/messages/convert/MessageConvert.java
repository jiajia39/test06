package com.framework.emt.system.domain.messages.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.messages.Message;
import com.framework.emt.system.domain.messages.request.MessageCreateRequest;
import com.framework.emt.system.domain.messages.response.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息 转换类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@Mapper
public interface MessageConvert {
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return 实体
     */
    Message createRequest2Entity(MessageCreateRequest request);


    /**
     * 实体转换vo
     *
     * @param message 实体
     * @return vo
     */
    MessageResponse entity2Vo(Message message);

    /**
     * 实体列表转换成vo列表
     *
     * @param itemList 实体列表
     * @return vo列表
     */
    List<MessageResponse> entityList2Vo(List<Message> itemList);


    /**
     * vo分页
     *
     * @param pages 实体分页
     * @return vo分页
     */
    default IPage<MessageResponse> pageVo(IPage<Message> pages) {
        IPage<MessageResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(MessageConvert.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }

    /**
     * 渠道字符串换成list
     *
     * @param sendChannels 渠道字符串
     * @return 渠道列表
     */
    default List<Integer> channelStrToList(String sendChannels) {
        List<String> strList = StrUtil.splitTrim(sendChannels, StrUtil.C_COMMA);
        List<Integer> list=new ArrayList<>();
        if(ObjectUtil.isNotEmpty(strList)){
            list= Convert.toList(Integer.class,strList);
        }
        return list;
    }

}
