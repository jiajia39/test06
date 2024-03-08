package com.framework.emt.system.infrastructure.exception.task.record.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.exception.task.record.ExceptionTaskRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务履历 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskRecordMapper extends BaseMapper<ExceptionTaskRecord> {
    /**
     * 每天挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 分类id
     * @param exceptionProcessId  流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsDayTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

    /**
     * 每周挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 分类id
     * @param exceptionProcessId  流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsWeekTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

    /**
     * 每月挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 分类id
     * @param exceptionProcessId  流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsMonthTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

    /**
     * 每年挂起数量
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 分类id
     * @param exceptionProcessId  流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsYearTrend(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

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
     * 每周响应超时次数
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
    List<StatisticsTrendValueResponse> statisticsWeekTimeout(@Param("type") String type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("deptIds") List<Long> deptIds, @Param("exceptionCategoryId") Long exceptionCategoryId, @Param("exceptionProcessId") Long exceptionProcessId, @Param("workspaceLocationId") Long workspaceLocationId);

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
}
