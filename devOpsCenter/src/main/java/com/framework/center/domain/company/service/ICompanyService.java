package com.framework.center.domain.company.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.request.CompanyCreateRequest;
import com.framework.center.domain.company.request.CompanyQueryRequest;
import com.framework.center.domain.company.response.CompanyVo;
import com.framework.center.infrastructure.service.BaseService;

/**
 * @author yankunw
 * @description 针对表【mss_company】的数据库操作Service
 * @createDate 2023-03-08 16:20:38
 */
public interface ICompanyService extends BaseService<Company> {

    boolean submit(CompanyCreateRequest param);

    boolean edit(Long id, CompanyCreateRequest param);

    CompanyVo detail(Long id);

    IPage<CompanyVo> queryList(CompanyQueryRequest param);

    boolean checkParamToCreateDb(Company entity);
}
