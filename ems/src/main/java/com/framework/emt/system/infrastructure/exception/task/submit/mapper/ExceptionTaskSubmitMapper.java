package com.framework.emt.system.infrastructure.exception.task.submit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitExportQueryRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitQueryRequest;
import com.framework.emt.system.domain.task.submit.response.TaskSubmitExportResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitDetailResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常任务提报 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskSubmitMapper extends BaseMapper<ExceptionTaskSubmit> {

    /**
     * 异常提报详情
     *
     * @param id     异常提报id
     * @param userId 当前用户id
     * @return
     */
    TaskResponse detail(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 异常提报分页列表
     *
     * @param page     分页对象
     * @param userId   当前用户id
     * @param request  查询条件
     * @param isSubmit 是否提报
     * @return
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("request") TaskSubmitQueryRequest request,
                             @Param("userId") Long userId, @Param("isSubmit") Boolean isSubmit);

    /**
     * 查询excel导出的数量
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskSubmitExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request
     * @param ids     异常提报id列表
     * @param userId  当前用户id
     * @return
     */
    List<TaskSubmitExportResponse> findExportData(@Param("request") TaskSubmitExportQueryRequest request,
                                                  @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 提报驳回次数
     *
     * @param queryRequest 筛选条件
     * @param deptIds 部门id
     * @return
     */
    List<StatisticsProportionResponse> submitReject(@Param("request") StatisticsTimeQueryRequest queryRequest, @Param("deptIds") List<Long> deptIds);

    /**
     * 根据id查询异常提报详情
     *
     * @param id 异常提报id
     * @return
     */
    TaskSubmitDetailResponse findDetailById(@Param("id") Long id);

}
