package com.framework.emt.system.domain.messages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.messages.MessageSendRecord;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消息记录 Mapper层
 *
 * @author yankunw
 * @since 2023-07-19
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public interface MessageSendRecordMapper extends BaseMapper<MessageSendRecord> {

    @Select("SELECT  " +
            "s.id," +
            "s.message_id," +
            "m.send_user_id," +
            "send.account as send_user_account," +
            "m.content," +
            "m.receive_user_id," +
            "receive.account as receive_user_account," +
            "s.send_time," +
            "s.send_state, " +
            "m.level as message_level, " +
            "m.business_id, " +
            "m.business_type, " +
            "template.id as message_template_id," +
            "template.`name` as message_template_name," +
            "s.send_channel " +
            "FROM  " +
            "emt_message m  " +
            "LEFT JOIN emt_message_template template on template.id=message_template_id AND template.is_deleted = 0" +
            "LEFT JOIN emt_message_send_record s ON m.id = s.message_id AND s.is_deleted = 0  " +
            "LEFT JOIN ft_user receive ON receive.id = m.receive_user_id AND receive.is_deleted = 0  " +
            "LEFT JOIN ft_user send ON send.id = m.send_user_id AND send.is_deleted = 0  " +
            "WHERE  " +
            "m.is_deleted = 0  and " +
            "m.send_state = 1  and " +
            "s.send_state = 0  and " +
            "m.send_time IS NULL  " +
            "ORDER BY s.create_time asc limit 500")
    List<MessageToBeSendResponse> getMessageToBeSend();

    List<MessageToBeSendResponse> getMessageToBeSendById(@Param("id") Long id);

}
