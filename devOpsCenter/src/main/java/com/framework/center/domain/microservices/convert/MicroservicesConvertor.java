package com.framework.center.domain.microservices.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.center.domain.microservices.Microservices;
import com.framework.center.domain.microservices.request.MicroservicesCreateRequest;
import com.framework.center.domain.microservices.response.MicroservicesVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 公司信息转换类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Mapper
public interface MicroservicesConvertor {
    MicroservicesConvertor INSTANCE = Mappers.getMapper(MicroservicesConvertor.class);


    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return 实体
     */
    Microservices createRequest2Entity(MicroservicesCreateRequest request);

    /**
     * 实体列表转换成vo列表
     *
     * @param formConfList 实体列表
     * @return vo列表
     */
    List<MicroservicesVo> entityList2Vo(List<Microservices> formConfList);

    /**
     * @param Microservices 转换的实体类
     * @return MicroservicesVo
     */
    MicroservicesVo entity2Vo(Microservices Microservices);

    /**
     * 更新参数转换成实体
     *
     * @param id      账户id
     * @param request 更新参数
     * @return 实体
     */
    @Mapping(target = "id", source = "id")
    Microservices updateRequest2Entity(Long id, MicroservicesCreateRequest request);


    default IPage<MicroservicesVo> pageVo(IPage<Microservices> pages) {
        IPage<MicroservicesVo> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(MicroservicesConvertor.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }
}

