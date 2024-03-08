package com.framework.emt.system.domain.exception.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessSubmitResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;

/**
 * 异常流程 服务层
 *
 * @author ds_C
 * @since 2023-07-20
 */
public interface ExceptionProcessService extends BaseService<ExceptionProcess> {

    /**
     * 异常流程创建
     *
     * @param request
     * @return 异常流程id
     */
    Long create(ExceptionProcessCreateRequest request);

    /**
     * 异常流程删除
     *
     * @param id 异常流程id
     */
    void delete(Long id);

    /**
     * 异常流程批量删除
     *
     * @param ids 异常流程id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 异常流程更新
     *
     * @param id      异常流程id
     * @param request
     * @return 异常流程id
     */
    Long update(Long id, ExceptionProcessUpdateRequest request);

    /**
     * 异常流程详情
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessResponse detail(Long id);

    /**
     * 异常流程分页列表
     *
     * @param request
     * @return
     */
    IPage<ExceptionProcessResponse> page(ExceptionProcessQueryRequest request);

    /**
     * 异常流程标签新增
     *
     * @param request
     * @return 异常标签id
     */
    Long createTag(ExceptionProcessTagCreateRequest request);

    /**
     * 异常流程标签关系新增
     *
     * @param request
     * @return
     */
    Long createTagRelation(ExceptionProcessTagRelationCreateRequest request);

    /**
     * 异常流程标签删除
     *
     * @param request
     */
    void deleteTag(ExceptionProcessTagDeleteRequest request);

    /**
     * 异常流程响应数据详情
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessSubmitResponse detailSubmit(Long id);

    /**
     * 根据id查询此条异常流程以及关联信息
     * 数据异常则抛出错误信息
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessResponse findResByIdThrowErr(Long id);

    /**
     * 根据id查询此条异常流程对应的响应数据
     * 数据异常则抛出错误信息
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcessSubmitResponse findSubmitByIdThrowErr(Long id);

    /**
     * 根据id查询此条异常流程
     * 数据异常则抛出错误信息
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcess findByIdThrowErr(Long id);

    /**
     * 根据id查询此条异常流程下的
     * 响应超时上报流程id和处理超时上报流程id
     * 数据异常则抛出错误信息
     *
     * @param id 异常流程id
     * @return
     */
    ExceptionProcess findReportProcessByIdThrowErr(Long id);

    /**
     * 提报任务新增编辑-校验响应人
     *
     * @param exceptionProcessId 异常流程id
     * @param responseUserId     响应人id
     * @return
     */
    ExceptionProcessResponse validateResponseUser(Long exceptionProcessId, Long responseUserId);

    /**
     * 根据表单id查询是否使用
     *
     * @param formFieldIds 表单id
     * @return 使用数量
     */
    Integer findByFormField(List<Long> formFieldIds);
}
