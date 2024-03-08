package com.framework.center.domain.company.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.request.CompanyCreateRequest;
import com.framework.center.domain.company.response.CompanyVo;
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
public interface CompanyConvertor {
    CompanyConvertor INSTANCE = Mappers.getMapper(CompanyConvertor.class);


    /**
     * 创建参数转换成实体
     *
     * @param request 创建参数
     * @return 实体
     */
    Company createRequest2Entity(CompanyCreateRequest request);

    /**
     * 实体列表转换成vo列表
     *
     * @param formConfList 实体列表
     * @return vo列表
     */
    List<CompanyVo> entityList2Vo(List<Company> formConfList);

    /**
     * @param Company 转换的实体类
     * @return CompanyVo
     */
    CompanyVo entity2Vo(Company Company);

    /**
     * 更新参数转换成实体
     *
     * @param id      账户id
     * @param request 更新参数
     * @return 实体
     */
    @Mapping(target = "id", source = "id")
    Company updateRequest2Entity(Long id, CompanyCreateRequest request);


    default IPage<CompanyVo> pageVo(IPage<Company> pages) {
        IPage<CompanyVo> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(CompanyConvertor.INSTANCE.entityList2Vo(pages.getRecords()));
        return pageVo;
    }
}

