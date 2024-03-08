package com.framework.center.domain.microservices.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.microservices.Microservices;
import com.framework.center.domain.microservices.request.MicroservicesCreateRequest;
import com.framework.center.domain.microservices.request.MicroservicesQueryRequest;
import com.framework.center.domain.microservices.response.MicroservicesVo;
import com.framework.center.infrastructure.service.BaseService;

/**
 * @author yankunw
 * @description 针对表【mss_company】的数据库操作Service
 * @createDate 2023-03-08 16:20:38
 */
public interface IMicroservicesService extends BaseService<Microservices> {

    Long submit(MicroservicesCreateRequest param);

    boolean edit(Long id, MicroservicesCreateRequest param);

    MicroservicesVo detail(Long id);

    IPage<MicroservicesVo> queryList(MicroservicesQueryRequest param);

    Boolean start(Long id);

    Boolean stop(Long id);

    Boolean restart(Long id);
}
