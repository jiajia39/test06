package com.framework.emt.system.infrastructure.exception.task.response.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.task.response.request.TaskResponseExportQueryRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseQueryRequest;
import com.framework.emt.system.domain.task.response.response.TaskResponseExportResponse;
import com.framework.emt.system.domain.task.response.response.ResponseStatusNumResponse;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.task.response.TaskResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常任务响应 Mapper层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskResponseMapper extends BaseMapper<ExceptionTaskResponse> {

    /**
     * 异常响应分页列表
     *
     * @param page   分页
     * @param userId 当前用户id
     * @return 分页列表
     */
    IPage<TaskResponse> page(IPage<TaskResponse> page, @Param("userId") Long userId,
                             @Param("request") TaskResponseQueryRequest request);

    /**
     * 异常响应详情
     *
     * @param id     响应id
     * @param userId 用户id
     * @return 详情
     */
    TaskResponse detail(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 获取响应当前用户各个状态的数量
     *
     * @param userId 当前用户id
     * @return 当前用户各个状态的数量
     */
    ResponseStatusNumResponse statistics(@Param("userId") Long userId);

    /**
     * 查询excel导出的数量
     *
     * @param request 导出查询条件
     * @param ids     异常验收id列表
     * @param userId  当前用户id
     * @return
     */
    Long findExportDataCount(@Param("request") TaskResponseExportQueryRequest request,
                             @Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 查询excel导出的结果集
     *
     * @param request 导出查询条件
     * @param ids     异常验收id列表
     * @param userId  当前用户id
     * @return
     */
    List<TaskResponseExportResponse> findExportData(@Param("request") TaskResponseExportQueryRequest request,
                                                    @Param("ids") List<Long> ids, @Param("userId") Long userId);

}
