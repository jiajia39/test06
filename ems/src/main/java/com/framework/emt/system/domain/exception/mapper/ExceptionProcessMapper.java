package com.framework.emt.system.domain.exception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.request.ExceptionProcessQueryRequest;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessSubmitResponse;
import org.apache.ibatis.annotations.Param;

/**
 * 异常流程 Mapper层
 *
 * @author ds_C
 * @since 2023-07-20
 */
public interface ExceptionProcessMapper extends BaseMapper<ExceptionProcess> {

    /**
     * 异常流程详情
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessResponse detail(@Param("id") Long id);

    /**
     * 异常流程响应数据详情
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessSubmitResponse detailSubmit(@Param("id") Long id);

    /**
     * 异常流程分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<ExceptionProcessResponse> page(IPage<ExceptionProcessResponse> page,
                                         @Param("request") ExceptionProcessQueryRequest request);

}
