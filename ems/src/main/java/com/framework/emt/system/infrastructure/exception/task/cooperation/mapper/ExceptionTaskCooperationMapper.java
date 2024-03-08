package com.framework.emt.system.infrastructure.exception.task.cooperation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationExportQueryRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationQueryRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;
import com.framework.emt.system.domain.task.cooperation.response.TaskCooperationExportResponse;
import com.framework.emt.system.domain.task.handing.request.HandingCooperationQueryRequest;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常任务协同 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskCooperationMapper extends BaseMapper<ExceptionTaskCooperation> {

    /**
     * 统计异常协同各个状态的数量
     *
     * @param currentUserId 当前用户id
     * @return
     */
    CooperationStatusNumResponse statisticsStatus(@Param("currentUserId") Long currentUserId);

    /**
     * 异常协同详情
     *
     * @param id      异常协同id
     * @param version 异常协同版本号
     * @return
     */
    TaskResponse detail(@Param("id") Long id, @Param("version") Integer version);

    /**
     * 异常协同分页列表
     *
     * @param page    分页对象
     * @param userId  当前用户id
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("request") TaskCooperationQueryRequest request,
                             @Param("userId") Long userId);

    /**
     * 异常处理协同分页列表
     *
     * @param page    分页对象
     * @param request 查询条件
     * @param userId  当前用户id
     * @return
     */
    IPage<TaskResponse> handingCooperationPage(IPage<TaskResponse> page, @Param("request") HandingCooperationQueryRequest request,
                                               @Param("userId") Long userId);

    /**
     * 查询excel导出的数量
     *
     * @param request
     * @param ids     异常协同id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskCooperationExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request
     * @param ids     异常协同id列表
     * @param userId  当前用户id
     * @return
     */
    List<TaskCooperationExportResponse> findExportData(@Param("request") TaskCooperationExportQueryRequest request,
                                                       @Param("ids") List<Long> ids, @Param("userId") Long userId);

}
