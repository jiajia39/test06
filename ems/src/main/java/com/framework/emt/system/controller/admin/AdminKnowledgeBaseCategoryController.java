package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryCreateRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryQueryRequest;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseCategoryUpdateRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseCategoryResponse;
import com.framework.emt.system.domain.knowledge.service.KnowledgeBaseCategoryService;
import com.framework.emt.system.infrastructure.common.request.ChangeEnableFlagRequest;
import com.framework.emt.system.infrastructure.common.request.IdListRequest;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 知识库分类 控制层
 *
 * @author ds_C
 * @since 2023-07-14
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-知识库分类"})
@ApiSupport(order = 25)
public class AdminKnowledgeBaseCategoryController extends BaseController {

    private final KnowledgeBaseCategoryService knowledgeBaseCategoryService;

    @PostMapping("/admin/knowledge/base/category")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "知识库分类创建")
    public R<Long> create(@Validated @RequestBody KnowledgeBaseCategoryCreateRequest request) {
        return R.data(knowledgeBaseCategoryService.create(request));
    }

    @PostMapping("/admin/knowledge/base/category/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "知识库分类删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        knowledgeBaseCategoryService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/category/delete/batch")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "知识库分类批量删除")
    public R<Boolean> deleteBatch(@Validated @RequestBody IdListRequest request) {
        knowledgeBaseCategoryService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/category/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "知识库分类更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody KnowledgeBaseCategoryUpdateRequest request) {
        return R.data(knowledgeBaseCategoryService.update(id, request));
    }

    @GetMapping("/admin/knowledge/base/category/{id}/detail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "知识库分类详情")
    public R<KnowledgeBaseCategoryResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(knowledgeBaseCategoryService.detail(id));
    }

    @GetMapping("/admin/knowledge/base/category/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "知识库分类分页列表")
    public R<IPage<KnowledgeBaseCategoryResponse>> page(@Validated KnowledgeBaseCategoryQueryRequest request) {
        return R.data(knowledgeBaseCategoryService.page(request));
    }

    @PostMapping("/admin/knowledge/base/category/enableFlag")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "知识库分类禁用启用")
    public R<Boolean> changeEnableFlag(@Validated @RequestBody ChangeEnableFlagRequest request) {
        knowledgeBaseCategoryService.changeEnableFlag(request.getEnableFlag(), request.getIdList());
        return R.status(true);
    }

}
