package com.framework.emt.system.domain.messages.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.login.service.IThirdPartyInfoService;
import com.framework.emt.system.domain.messages.Message;
import com.framework.emt.system.domain.messages.MessageSendRecord;
import com.framework.emt.system.domain.messages.constant.code.MessageErrorCode;
import com.framework.emt.system.domain.messages.constant.enums.NoticeLevel;
import com.framework.emt.system.domain.messages.convert.MessageConvert;
import com.framework.emt.system.domain.messages.mapper.MessageMapper;
import com.framework.emt.system.domain.messages.request.*;
import com.framework.emt.system.domain.messages.response.MessageResponse;
import com.framework.emt.system.domain.role.RoleExt;
import com.framework.emt.system.domain.role.mapper.RoleExtMapper;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.enums.SendChannelEnum;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.task.conf.TaskConf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息推送 实现类
 *
 * @author yankunw
 * @since 2023-07-19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements IMessageService {

    private final IUserDao userDao;

    private final IMessageSendRecordService messageSendRecordService;

    private final TaskConf taskConf;

    private final RoleExtMapper roleExtMapper;

    @Override
    public Integer getExceptionManagement() {
        if (ObjectUtil.isNotNull(AuthUtil.getUser())) {
            LambdaQueryWrapper<RoleExt> query = new LambdaQueryWrapper<>();
            query.eq(RoleExt::getRoleId, AuthUtil.getUser().getRoleId());
            query.eq(RoleExt::getIsDeleted, 0);
            query.last(" limit 1");
            RoleExt roleExt = roleExtMapper.selectOne(query);
            if (ObjectUtil.isNotNull(roleExt)) {
                return roleExt.getExceptionManagement();
            }
        }
        return 0;
    }

    private final IThirdPartyInfoService thirdPartyInfoService;


    /**
     * 发送消息
     *
     * @param request 发送消息参数
     * @return 是否成功
     */
    @Override
    public boolean send(MessageCreateRequest request) {
        if (ObjectUtil.isNull(request)) {
            return false;
        }
        List<MessageCreate> messageCreateList = request.getMessageCreateList();
        if (ObjectUtil.isEmpty(messageCreateList)) {
            return false;
        }
        List<Long> userIdList = messageCreateList.stream().map(MessageCreate::getReceiveUserId).distinct().collect(Collectors.toList());
        Map<Long, String> useChannelMap = thirdPartyInfoService.getChannelsByUserIdList(userIdList);
        List<Message> messageList = new ArrayList<>();
        for (MessageCreate messageCreate : messageCreateList) {
            String uuid = UUID.randomUUID().toString();
            Message message = new Message();
            String channel = Convert.toStr(SendChannelEnum.UNI_PUSH.getCode());
            if(useChannelMap.containsKey(messageCreate.getReceiveUserId())){
                channel=channel+","+useChannelMap.get(messageCreate.getReceiveUserId());
            }
            message.setSendChannels(channel);
            message.setLevel(messageCreate.getLevel());
            message.setMessageTemplateId(messageCreate.getMessageTemplateId());
            message.setContent(messageCreate.getContent());
            message.setLevel(messageCreate.getLevel());
            message.setSendTime(messageCreate.getSendTime());
            message.setRemark(messageCreate.getRemark());
            message.setBusinessId(messageCreate.getBusinessId());
            message.setBusinessType(messageCreate.getBusinessType());
            message.setSendState(0);
            message.setSendUserId(messageCreate.getSendUserId());
            message.setReadState(0);
            message.setBatchNumber(uuid);
            message.setReceiveUserId(messageCreate.getReceiveUserId());
            messageList.add(message);
        }
        if (this.saveBatch(messageList, 500)) {
            messageSendRecordService.start();
            return true;
        }
        return false;
    }

    /**
     * 修改状态为已发送
     *
     * @param messageIdList 消息id列表
     */
    @Override
    public void changeStatusToSend(List<Long> messageIdList) {
        Function<LambdaUpdateWrapper<Message>, Boolean> func = query -> {
            query.eq(Message::getSendState, 1);
            query.notExists( "select id from emt_message_send_record where send_state =0 and message_id=emt_message.id");
            query.set(Message::getSendState, 2);
            query.set(Message::getRealSendTime, LocalDateTime.now());
            return true;
        };
        changeStatus(messageIdList, func);
    }

    /**
     * 修改状态为已读
     *
     * @param messageIdList 消息id列表
     */
    @Override
    public boolean changeStatusToRead(List<Long> messageIdList) {
        Function<LambdaUpdateWrapper<Message>, Boolean> func = query -> {
            query.eq(Message::getReadState, 0);
            query.set(Message::getReadState, 1);
            query.set(Message::getReadTime, LocalDateTime.now());
            return true;
        };
        return changeStatus(messageIdList, func);
    }

    /**
     * 修改状态
     *
     * @param messageIdList 消息id列表
     * @param func          函数
     * @return 是否成功
     */
    public boolean changeStatus(List<Long> messageIdList, Function<LambdaUpdateWrapper<Message>, Boolean> func) {
        LambdaUpdateWrapper<Message> query = new LambdaUpdateWrapper<>();
        query.in(Message::getId, messageIdList);
        query.eq(Message::getIsDeleted, 0);
        func.apply(query);
        query.set(Message::getUpdateTime, LocalDateTime.now());
        query.set(Message::getUpdateUser, AuthUtil.getUserId());
        return this.update(query);
    }


    /**
     * 查询消息发送情况
     *
     * @param request 查询参数
     * @return 详细列表
     */
    @Override
    public IPage<MessageResponse> page(MessageQueryRequest request) {
        Long userId = Convert.toLong(AuthUtil.getUserId(), null);
        IPage<Message> page = Condition.getPage(request);
        LambdaQueryWrapper<Message> query = new LambdaQueryWrapper<>();
        query.eq(ObjectUtil.isNotNull(request.getBusinessType()), Message::getBusinessType, request.getBusinessType());
        query.eq(ObjectUtil.isNotNull(userId), Message::getReceiveUserId, userId);
        query.eq(ObjectUtil.isNotNull(request.getReceiveUserId()), Message::getReceiveUserId, request.getReceiveUserId());
        query.ge(ObjectUtil.isNotNull(request.getSendTimeStart()), Message::getSendTime, request.getSendTimeStart());
        query.le(ObjectUtil.isNotNull(request.getSendTimeEnd()), Message::getSendTime, request.getSendTimeEnd());
        query.eq(ObjectUtil.isNotNull(request.getReadState()), Message::getReadState, request.getReadState());
        query.ge(ObjectUtil.isNotNull(request.getReadTimeStart()), Message::getReadTime, request.getReadTimeStart());
        query.le(ObjectUtil.isNotNull(request.getReadTimeEnd()), Message::getReadTime, request.getReadTimeEnd());
        query.ne(Message::getReceiveUserId, NumberUtils.INTEGER_ZERO);
        query.eq(Message::getSendState, NumberUtils.INTEGER_TWO);
        if (ObjectUtil.isNotNull(request.getOrderBy()) && ObjectUtil.equal(request.getOrderBy(), 1)) {
            query.last("ORDER BY read_state asc,CASE WHEN send_time IS NOT NULL THEN send_time ELSE create_time END DESC");
        } else {
            query.last("ORDER BY CASE WHEN send_time IS NOT NULL THEN send_time ELSE create_time END DESC");
        }
        page = this.page(page, query);
        IPage<MessageResponse> result = MessageConvert.INSTANCE.pageVo(page);
        if (ObjectUtil.isNotEmpty(result.getRecords())) {
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(result.getRecords(), "createUser", User::getId, "createUserName", "name", type);
            userDao.loadData(result.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
            userDao.loadData(result.getRecords(), "sendUserId", User::getId, "sendUserName", "name", type);
            userDao.loadData(result.getRecords(), "receiveUserId", User::getId, "receiveUserName", "name", type);
            for (MessageResponse record : result.getRecords()) {
                if (ObjectUtil.isNotNull(record.getCreateUser())) {
                    record.setCreateUserName("系统");
                }
                if (ObjectUtil.isNotNull(record.getUpdateUser())) {
                    record.setUpdateUserName("系统");
                }
            }
        }
        return result;
    }

    /**
     * 获得未读数量
     *
     * @return 未读数量
     */
    @Override
    public Long getUnreadQuantity() {
        Long userId = Convert.toLong(AuthUtil.getUserId(), null);
        LambdaUpdateWrapper<Message> query = new LambdaUpdateWrapper<>();
        query.eq(ObjectUtil.isNotNull(userId), Message::getReceiveUserId, userId);
        query.eq(Message::getReadState, 0);
        query.eq(Message::getIsDeleted, 0);
        query.eq(Message::getSendState, NumberUtils.INTEGER_TWO);
        return this.count(query);
    }

    /**
     * 抽取待发送记录
     *
     * @return 是否添加到发送通道
     */
    @Override
    @Transactional
    public boolean moveToSendRecords() {
        List<Message> list;
        while (ObjectUtil.isNotEmpty((list = getMessages()))) {
            List<MessageSendRecord> recordList = new ArrayList<>();
            for (Message message : list) {
                String channels = message.getSendChannels();
                if (StrUtil.isNotBlank(channels)) {
                    List<String> array = StrUtil.splitTrim(channels, StrPool.C_COMMA);
                    for (String channel : array) {
                        MessageSendRecord record = new MessageSendRecord();
                        record.setMessageId(message.getId());
                        record.setSendState(0);
                        record.setSendChannel(Convert.toInt(channel, -1));
                        record.setSendTime(message.getSendTime());
                        if (!ObjectUtil.equals(record.getSendChannel(), -1)) {
                            recordList.add(record);
                        }
                    }
                }
                if (ObjectUtil.isNotEmpty(recordList)) {
                    message.setSendState(1);
                }
                if (ObjectUtil.isNotEmpty(message.getSendTime())) {
                    taskConf.sendMessageTask(message.getSendTime(), Convert.toStr(message.getId()));
                }
            }
            updateBatchById(list, 500);
            if (ObjectUtil.isNotEmpty(recordList)) {
                messageSendRecordService.saveOrUpdateBatch(recordList, 500);
            }
        }
        return true;
    }

    /**
     * 抽取500条未发送的消息
     *
     * @return 500条未发送的消息
     */
    private List<Message> getMessages() {
        LambdaUpdateWrapper<Message> query = new LambdaUpdateWrapper<>();
        query.eq(Message::getIsDeleted, 0);
        query.eq(Message::getSendState, 0);
        query.orderByAsc(Message::getCreateTime);
        query.last(" limit 500 ");
        return this.getBaseMapper().selectList(query);
    }

    /**
     * 获得消息发送中数量
     *
     * @return 消息发送中数量
     */
    @Override
    public Long getMessageQuantityToBeSent() {
        LambdaUpdateWrapper<Message> query = new LambdaUpdateWrapper<>();
        query.eq(Message::getSendState, 1);
        query.eq(Message::getIsDeleted, 0);
        query.isNull(Message::getSendTime);
        return this.count(query);
    }

    /**
     * 根据id查询消息详情
     *
     * @param id 消息id
     * @return 消息详情
     */
    @Override
    public MessageResponse detail(Long id) {
        MessageResponse messageResponse = this.baseMapper.detailMessage(findByIdThrowErr(id).getId(), null);
        if (messageResponse != null) {
            List<MessageResponse> result = new ArrayList<>();
            result.add(messageResponse);
            if (ObjectUtil.isNotEmpty(messageResponse)) {
                TypeReference<String> type = new TypeReference<String>() {
                };
                userDao.loadData(result, "createUser", User::getId, "createUserName", "name", type);
                userDao.loadData(result, "updateUser", User::getId, "updateUserName", "name", type);
                userDao.loadData(result, "sendUserId", User::getId, "sendUserName", "name", type);
                userDao.loadData(result, "receiveUserId", User::getId, "receiveUserName", "name", type);
            }
            return result.get(0);
        }
        return null;
    }

    @Override
    public void cancel(MessageCancelRequest request) {
        for (MessageCancel messageCancel : request.getMessageCancelList()) {
            String sendTime = "";
            if (ObjectUtil.isNotNull(messageCancel.getSendTime())) {
                sendTime = DateUtil.format(messageCancel.getSendTime(), DatePattern.NORM_DATETIME_PATTERN);
            }
            //删除消息
            this.getBaseMapper().cancelMessage(
                    messageCancel.getBusinessType(),
                    messageCancel.getBusinessId(),
                    messageCancel.getLevel(),
                    messageCancel.getReceiveUserId(),
                    sendTime
            );
        }
        //删除发送记录
        this.getBaseMapper().cancelSendRecord();
    }

    @Override
    public void updateReceiveUser(Integer businessType, Long businessId, User receiveUser, Long receiveUserId) {
        // 修改消息接收人
        Integer overtimeWarningCode = NoticeLevel.OVERTIME_WARNING.getCode();
        Message message = this.getOne(new LambdaQueryWrapper<Message>()
                .eq(Message::getBusinessType, businessType)
                .eq(Message::getLevel, overtimeWarningCode)
                .eq(Message::getBusinessId, businessId)
                .eq(ObjectUtil.isNotNull(receiveUserId), Message::getReceiveUserId, receiveUserId)
                .eq(Message::getSendState, NumberUtils.INTEGER_ONE));


        // 修改逐级上报消息内容中的响应人、处理人或协同人
        Integer overtimeReportCode = NoticeLevel.OVERTIME_REPORT.getCode();
        List<Message> messages = this.list(new LambdaQueryWrapper<Message>().eq(Message::getBusinessType, businessType).eq(Message::getLevel, overtimeReportCode).eq(Message::getBusinessId, businessId).eq(ObjectUtil.isNotNull(receiveUserId), Message::getReceiveUserId, receiveUserId).eq(Message::getSendState, NumberUtils.INTEGER_ONE));
        messages.forEach(msg -> {
            String userNamePosition = BusinessConstant.SEND_USER_NAME_POSITION;
            String replacedContent = msg.getContent().replaceFirst(userNamePosition, "【" + receiveUser.getName() + "】");
            msg.setContent(replacedContent);
            msg.setReceiveUserId(receiveUser.getId());
        });
        if (ObjectUtil.isNotNull(message)) {
            message.setReceiveUserId(receiveUser.getId());
            // 批量修改
            messages.add(message);
        }

        this.updateBatchById(messages);
    }

    @Override
    public void updateReceiveUser(Integer businessType, Long businessId, Long receiveUserId, Long oleReceiveUserId) {
        Integer overtimeWarningCode = NoticeLevel.OVERTIME_WARNING.getCode();
        Integer overtimeReportCode = NoticeLevel.OVERTIME_REPORT.getCode();
        List<Message> messages = this.list(new LambdaQueryWrapper<Message>().eq(Message::getBusinessType, businessType).eq(Message::getBusinessId, businessId).eq(ObjectUtil.isNotNull(receiveUserId), Message::getReceiveUserId, oleReceiveUserId).eq(Message::getSendState, NumberUtils.INTEGER_ONE).and(q -> {
            q.eq(Message::getLevel, overtimeWarningCode);
            q.or();
            q.eq(Message::getLevel, overtimeReportCode);
        }));

        if (ObjectUtil.isNull(messages)) {
            return;
        }
        messages.forEach(message -> message.setReceiveUserId(receiveUserId));

        this.updateBatchById(messages);
    }

    @Override
    public Message findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, MessageErrorCode.NO_DATA_FOUND_IN_THE_MESSAGE);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsDayTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsDayTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsMonthTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsMonthTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsYearTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsYearTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);

    }


}
