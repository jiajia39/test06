package com.framework.emt.system.infrastructure.exception.task.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.check.request.TaskCheckExportQueryRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckQueryRequest;
import com.framework.emt.system.domain.task.check.response.CheckStatusNumResponse;
import com.framework.emt.system.domain.task.check.response.TaskCheckExportResponse;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常任务验收 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskCheckMapper extends BaseMapper<ExceptionTaskCheck> {

    /**
     * 异常任务验收信息详情
     *
     * @param id      验收id
     * @param userId  当前用户id
     * @param version 验收版本号
     * @return
     */
    TaskResponse detail(@Param("id") Long id, @Param("userId") Long userId, @Param("version") Integer version);

    /**
     * 获取当前用户验收时各个状态的数量
     *
     * @param userId 用户id
     * @return 各个状态的数量
     */
    CheckStatusNumResponse statistics(@Param("userId") Long userId);

    /**
     * 异常任务验收信息分页列表
     *
     * @param page    分页
     * @param userId  当前用户id
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("userId") Long userId, @Param("request") TaskCheckQueryRequest request);

    /**
     * 验收驳回TOP10
     *
     * @param queryRequest 筛选条件
     * @return
     */
    List<StatisticsProportionResponse> checkReject(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 查询excel导出的数量
     *
     * @param request 导出查询条件
     * @param ids     异常验收id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskCheckExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request 导出查询条件
     * @param ids     异常验收id列表
     * @param userId  当前用户id
     * @return
     */
    List<TaskCheckExportResponse> findExportData(@Param("request") TaskCheckExportQueryRequest request,
                                                 @Param("ids") List<Long> ids, @Param("userId") Long userId);

}
