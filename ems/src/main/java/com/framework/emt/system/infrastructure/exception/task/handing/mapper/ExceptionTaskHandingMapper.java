package com.framework.emt.system.infrastructure.exception.task.handing.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.task.handing.request.TaskHandingExportQueryRequest;
import com.framework.emt.system.domain.task.handing.request.TaskHandingQueryRequest;
import com.framework.emt.system.domain.task.handing.response.TaskHandingExportResponse;
import com.framework.emt.system.domain.task.handing.response.HandingStatusNumResponse;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常任务处理 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskHandingMapper extends BaseMapper<ExceptionTaskHanding> {

    /**
     * 异常处理分页列表
     *
     * @param page    分页对象
     * @param userId  当前用户id
     * @param request 查询条件
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("userId") Long userId,
                             @Param("request") TaskHandingQueryRequest request);

    /**
     * 异常处理分页列表
     *
     * @param id 异常处理id
     * @return
     */
    TaskResponse detail(@Param("id") Long id);

    /**
     * 获取处理当前用户各个状态的数量
     *
     * @param userId 当前用户id
     * @return 当前用户各个状态的数量
     */
    HandingStatusNumResponse statistics(@Param("userId") Long userId);

    /**
     * 查询excel导出的数量
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskHandingExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<TaskHandingExportResponse> findExportData(@Param("request") TaskHandingExportQueryRequest request,
                                                   @Param("ids") List<Long> ids, @Param("userId") Long userId);

}
