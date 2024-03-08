package com.framework.center.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.company.request.CompanyCreateRequest;
import com.framework.center.domain.company.request.CompanyQueryRequest;
import com.framework.center.domain.company.response.CompanyVo;
import com.framework.center.domain.company.service.ICompanyService;
import com.framework.center.infrastructure.common.IdsRequest;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "运维中心公司管理", tags = "运维中心公司管理")
@ApiSupport(order = 1)
public class CompanyController extends BaseController {
    private final ICompanyService companyService;


    /**
     * 公司创建
     */
    @PostMapping("/admin/company")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "公司创建", notes = "公司创建")
    public R<Boolean> submit(@Valid @RequestBody CompanyCreateRequest param) {
        boolean isSuccess = companyService.submit(param);
        return R.status(isSuccess);
    }

    /**
     * 公司编辑
     */
    @PostMapping("/admin/company/{id}")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "公司编辑", notes = "公司编辑")
    public R<Boolean> edit(@PathVariable(value = "id") Long id, @Valid @RequestBody CompanyCreateRequest param) {
        boolean isSuccess = companyService.edit(id, param);
        return R.status(isSuccess);
    }

    /**
     * 公司详情
     */
    @GetMapping("/admin/company/{id}/detail")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "公司详情", notes = "公司详情")
    public R<CompanyVo> detail(@PathVariable(value = "id") Long id) {
        CompanyVo res = companyService.detail(id);
        return R.data(res);
    }

    /**
     * 批量删除公司
     *
     * @param idsRequest id列表信息
     */
    @DeleteMapping("/admin/company/delete")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "批量删除公司", notes = "批量删除公司")
    public R<Boolean> delete(@RequestBody @Validated IdsRequest idsRequest) {
        String ids = idsRequest.getIds().stream().map(Convert::toStr).collect(Collectors.joining(StrPool.COMMA));
        Boolean result = companyService.deleteByIds(ids);
        return R.status(result);
    }

    /**
     * 公司列表
     */
    @GetMapping("/admin/company/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "公司列表", notes = "公司列表")
    public R<IPage<CompanyVo>> list(CompanyQueryRequest param) {
        IPage<CompanyVo> result = companyService.queryList(param);
        return R.data(result);
    }
}
