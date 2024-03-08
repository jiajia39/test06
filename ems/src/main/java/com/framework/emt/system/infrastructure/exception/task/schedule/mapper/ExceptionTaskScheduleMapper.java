package com.framework.emt.system.infrastructure.exception.task.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.ExceptionTaskSchedule;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务定时计划 Mapper层
 *
 * @author ds_C
 * @since 2023-08-24
 */
public interface ExceptionTaskScheduleMapper extends BaseMapper<ExceptionTaskSchedule> {

    /**
     * 超时上报流程分页列表
     *
     * @param page        分页查询条件
     * @param timeOutType 超时类型code
     * @param taskId      超时上报流程id
     * @return
     */
    IPage<TaskScheduleResponse> timeOutReportPage(IPage<TaskScheduleResponse> page, @Param("timeOutType") Integer timeOutType, @Param("taskId") Long taskId);

    /**
     * 更新异常任务定时计划表执行状态为已执行
     *
     * @param sourceId     来源id
     * @param templateId   超时提报id
     * @param planSendTime 计划发送时间
     * @return
     */
    void getBySourceIdAndTemplateId(@Param("sourceId") Long sourceId, @Param("templateId") Long templateId, @Param("planSendTime") LocalDateTime planSendTime);

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
