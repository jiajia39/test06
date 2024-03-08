package com.framework.emt.system.domain.exception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.request.ExceptionItemExportQueryRequest;
import com.framework.emt.system.domain.exception.request.ExceptionItemQueryRequest;
import com.framework.emt.system.domain.exception.response.ExceptionItemExportResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemShortResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常项 Mapper层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface ExceptionItemMapper extends BaseMapper<ExceptionItem> {

    /**
     * 异常项详情
     *
     * @param itemId 异常项id
     * @return
     */
    ExceptionItemResponse detail(@Param("itemId") Long itemId);

    /**
     * 异常项分页列表
     *
     * @param page
     * @param request 查询条件
     * @return
     */
    IPage<ExceptionItemResponse> page(IPage<ExceptionItemResponse> page, @Param("request") ExceptionItemQueryRequest request);

    /**
     * 查询导出的数量
     *
     * @param request 查询条件
     * @param ids     主键id列表
     * @return
     */
    long findExportDataCount(@Param("request") ExceptionItemExportQueryRequest request, @Param("ids") List<Long> ids);

    /**
     * 查询导出的数据列表
     *
     * @param request 查询条件
     * @param ids     主键id列表
     * @return
     */
    List<ExceptionItemExportResponse> findExportData(@Param("request") ExceptionItemExportQueryRequest request, @Param("ids") List<Long> ids);

    /**
     * 根据异常任务id和提报版本号查询异常项名称
     *
     * @param taskId        异常任务id
     * @param submitVersion 异常提报版本号
     * @return
     */
    String findItemName(@Param("taskId") Long taskId, @Param("submitVersion") Integer submitVersion);

    /**
     * 根据异常项id列表查询异常项列表
     *
     * @param ids 异常项id列表
     * @return
     */
    List<ExceptionItem> listByIds(@Param("ids") List<Long> ids);

    /**
     * 通常异常项简化列表查询异常信息
     *
     * @param list 异常项简化列表
     * @return 异常项
     */
    ExceptionItem getByShortList(@Param("shortList") List<ExceptionItemShortResponse> list);

}
