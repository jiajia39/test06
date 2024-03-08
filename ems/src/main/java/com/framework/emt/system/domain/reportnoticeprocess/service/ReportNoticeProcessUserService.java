package com.framework.emt.system.domain.reportnoticeprocess.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcessUser;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserCreateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserQueryRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserUpdateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 上报流程推送 服务层
 *
 * @author ds_C
 * @since 2023-07-24
 */
public interface ReportNoticeProcessUserService extends BaseService<ReportNoticeProcessUser> {

    /**
     * 上报人及上报消息创建
     *
     * @param request
     * @return 主键id
     */
    Long create(ReportNoticeProcessUserCreateRequest request);

    /**
     * 上报人及上报消息删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 上报人及上报消息批量删除
     *
     * @param ids 主键id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 上报人及上报消息更新
     *
     * @param id      主键id
     * @param request
     * @return 主键id
     */
    Long update(Long id, ReportNoticeProcessUserUpdateRequest request);

    /**
     * 上报人及上报消息详情
     *
     * @param id 主键id
     * @return
     */
    ReportNoticeProcessResponse detail(Long id);

    /**
     * 上报人及上报消息分页列表
     *
     * @param request
     * @return
     */
    IPage<ReportNoticeProcessResponse> page(ReportNoticeProcessUserQueryRequest request);

    /**
     * 根据上报流程主键id删除上报流程推送
     *
     * @param processId 上报流程主键id
     */
    void deleteByProcessId(Long processId);

    /**
     * 根据上报流程主键id列表删除上报流程推送
     *
     * @param processIds 上报流程主键id列表
     */
    void deleteByProcessIds(List<Long> processIds);

    /**
     * 通过用户id列表获取到用户信息列表
     *
     * @param userIds     用户id列表
     * @param userInfoMap 数据库中的用户信息map列表
     * @return
     */
    List<UserInfoResponse> getUserInfoList(List<String> userIds, Map<String, String> userInfoMap);

    /**
     * 根据上报流程主键id查询上报人及上报消息列表
     *
     * @param processId 上报流程主键id
     * @return
     */
    List<ReportNoticeProcessResponse> findByProcessId(Long processId);

    /**
     * 根据上报流程id查询上报人id列表
     *
     * @param processId 上报流程id
     * @return
     */
    List<Long> findUserIdsByProcessId(Long processId);

}
