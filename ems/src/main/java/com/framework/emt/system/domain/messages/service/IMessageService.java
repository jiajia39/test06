package com.framework.emt.system.domain.messages.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.messages.Message;
import com.framework.emt.system.domain.messages.request.MessageCancelRequest;
import com.framework.emt.system.domain.messages.request.MessageCreateRequest;
import com.framework.emt.system.domain.messages.request.MessageQueryRequest;
import com.framework.emt.system.domain.messages.response.MessageResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息推送 服务层
 *
 * @author yankunw
 * @since 2023-07-19
 */
public interface IMessageService extends BaseService<Message> {

    /**
     * 获得当前用户是否能够进行异常管理 0 否 1 是
     *
     * @return 异常管理 0 否 1 是
     */
    Integer getExceptionManagement();

    /**
     * 发送消息
     *
     * @param request 发送消息参数
     * @return 是否成功
     */
    boolean send(MessageCreateRequest request);

    /**
     * 修改状态为已发送
     *
     * @param messageIdList 消息id列表
     */
    void changeStatusToSend(List<Long> messageIdList);

    /**
     * 修改状态为已读
     *
     * @param messageIdList 消息id列表
     */
    boolean changeStatusToRead(List<Long> messageIdList);

    /**
     * 查询消息列表
     *
     * @param request 查询参数
     * @return 消息列表分页信息
     */
    IPage<MessageResponse> page(MessageQueryRequest request);

    Long getUnreadQuantity();

    /**
     * 抽取待发送记录
     *
     * @return 是否添加到发送通道
     */
    boolean moveToSendRecords();

    /**
     * 获得消息待发送数量
     *
     * @return 消息待发送数量
     */
    Long getMessageQuantityToBeSent();

    /**
     * 根据id查询消息详情
     *
     * @param id 消息id
     * @return 消息详情
     */
    MessageResponse detail(Long id);

    void cancel(MessageCancelRequest request);

    /**
     * 1、修改消息接收人
     * 2、修改逐级上报消息内容中的响应人、处理人或协同人
     *
     * @param businessType     业务类型
     * @param businessId       业务id
     * @param receiveUser      接收人
     * @param oldReceiveUserId 原接收人
     */
    void updateReceiveUser(Integer businessType, Long businessId, User receiveUser, Long oldReceiveUserId);

    /**
     * 修改消息接收人
     *
     * @param businessType     业务类型
     * @param businessId       业务id
     * @param receiveUserId    接收人id
     * @param oleReceiveUserId 原接收人
     */
    void updateReceiveUser(Integer businessType, Long businessId, Long receiveUserId, Long oleReceiveUserId);

    /**
     * 根据id查询此条消息
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    Message findByIdThrowErr(Long id);

    /**
     * 每天响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsDayTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 每月响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */

    List<StatisticsTrendValueResponse> statisticsMonthTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);


    /**
     * 每年响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */

    List<StatisticsTrendValueResponse> statisticsYearTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);
}
