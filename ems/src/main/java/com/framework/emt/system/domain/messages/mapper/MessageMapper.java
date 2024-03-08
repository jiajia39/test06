package com.framework.emt.system.domain.messages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.messages.Message;
import com.framework.emt.system.domain.messages.request.MessageRequest;
import com.framework.emt.system.domain.messages.response.MessageResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息 Mapper层
 *
 * @author yankunw
 * @since 2023-07-19
 */
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询消息列表（管理员查询所有数据，普通用户查看自己的数据）
     * @param request 消息查询参数
     * @return 消息列表
     */
    IPage<MessageResponse> pageMessage(IPage<MessageResponse> page, @Param("request") MessageRequest request);

    /**
     * 根据id查询消息详情
     *
     * @param id 消息id
     * @return 消息详情
     */
    MessageResponse detailMessage(@Param("id") Long id, @Param("userId") Long userId);

    void cancelMessage(@Param("businessType") Integer businessType, @Param("businessId") Long businessId, @Param("level") Integer level, @Param("receiveUserId") Long receiveUserId, @Param("sendTime") String sendTime);

    void cancelSendRecord();

    /**
     * 每天响应超时次数
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
    List<StatisticsTrendValueResponse> statisticsDayTimeout(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

    /**
     * 每月响应超时次数
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
    List<StatisticsTrendValueResponse> statisticsMonthTimeout(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

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

    List<StatisticsTrendValueResponse> statisticsYearTimeout(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);
}
