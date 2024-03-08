package com.framework.emt.system.domain.reportnoticeprocess.convert;

import cn.hutool.core.util.StrUtil;
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcessUser;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserCreateRequest;
import com.framework.emt.system.domain.reportnoticeprocess.request.ReportNoticeProcessUserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 上报流程推送 转换类
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Mapper
public interface ReportNoticeProcessUserConvert {

    ReportNoticeProcessUserConvert INSTANCE = Mappers.getMapper(ReportNoticeProcessUserConvert.class);

    /**
     * 创建参数转换成实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "receiveUserIds", expression = "java(splitWithCommasUserIdList(request.getReceiveUserIds()))")
    ReportNoticeProcessUser createRequestToEntity(ReportNoticeProcessUserCreateRequest request);

    /**
     * 更新参数转换成实体
     *
     * @param entity
     * @param request
     * @return
     */
    @Mapping(target = "id", expression = "java(entity.getId())")
    @Mapping(target = "receiveUserIds", expression = "java(splitWithCommasUserIdList(request.getReceiveUserIds()))")
    ReportNoticeProcessUser updateRequestToEntity(@MappingTarget ReportNoticeProcessUser entity, ReportNoticeProcessUserUpdateRequest request);

    /**
     * 拆分上报人id列表以逗号分隔拼接成一个字符串返回
     *
     * @param receiveUserIdList 上报人id列表
     * @return
     */
    default String splitWithCommasUserIdList(List<Long> receiveUserIdList) {
        return receiveUserIdList.stream().map(String::valueOf).collect(Collectors.joining(StrUtil.COMMA));
    }

}
