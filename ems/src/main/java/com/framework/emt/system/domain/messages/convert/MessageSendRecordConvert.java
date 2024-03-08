package com.framework.emt.system.domain.messages.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 消息发送记录 转换类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@Mapper
public interface MessageSendRecordConvert {
    MessageSendRecordConvert INSTANCE = Mappers.getMapper(MessageSendRecordConvert.class);
}
