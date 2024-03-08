package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.goodscategory.request.*;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryResponse;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryTreeResponse;
import com.framework.emt.system.domain.goodscategory.service.GoodsCategoryService;
import com.framework.emt.system.domain.statistics.request.StatisticsTimeQueryRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionPieResponse;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 异常分类 接口层
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@Api(tags = "异常分类管理")
@Validated
@RestController
@RequiredArgsConstructor
public class GoodsCategoryController extends BaseController {

    private final GoodsCategoryService goodsCategoryService;

    @ApiOperation(value = "新增异常分类")
    @PostMapping("/admin/goodsCategory")
    public R<Long> create(@RequestBody GoodsCategoryCreateRequest request) {
        return R.data(goodsCategoryService.create(request));
    }

    @ApiOperation(value = "更新异常分类")
    @PostMapping("/admin/goodsCategory/{id}")
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody GoodsCategoryUpdateRequest request) {
        goodsCategoryService.update(id, request);
        return R.status(true);
    }

    @ApiOperation(value = "异常分类分页")
    @GetMapping("/admin/goodsCategory/page")
    public R<IPage<GoodsCategoryResponse>> page(@Valid GoodsCategoryQuery query) {
        return R.data(goodsCategoryService.page(query));
    }

    @ApiOperation(value = "查看异常分类")
    @GetMapping("/admin/goodsCategory/{id}")
    public R<GoodsCategoryResponse> info(@PathVariable("id") Long id) {
        return R.data(goodsCategoryService.info(id));
    }

    @ApiOperation(value = "删除异常分类")
    @PostMapping("/admin/goodsCategory/delete/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        goodsCategoryService.delete(id);
        return R.status(true);
    }

    @ApiOperation(value = "异常分类启用禁用")
    @PostMapping("/admin/goodsCategory/enable/flag")
    public R<Boolean> enableFlag(@RequestBody GoodsCategoryEnableFlagRequest request) {
        goodsCategoryService.enableFlag(request);
        return R.status(true);
    }

    @ApiOperation(value = "异常分类树形列表")
    @GetMapping("/admin/goodsCategory/tree/list")
    public R<List<GoodsCategoryTreeResponse>> treeList(@Validated GoodsCategoryTreeQuery request) {
        return R.data(goodsCategoryService.treeList(request));
    }

    @ApiOperation(value = "前两级的异常分类树形列表")
    @GetMapping("/admin/goodsCategory/tree/list/level/two")
    public R<List<GoodsCategoryTreeResponse>> excludeLevelTreeList(@Validated GoodsCategoryTreeQuery request) {
        request.setExcludeLevel(3);
        return R.data(goodsCategoryService.treeList(request));
    }

    @GetMapping("admin/goodsCategory/statistics/proportion")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "异常分类占比 ")
    public R<StatisticsProportionPieResponse> categoryProportion(@Validated GoodsCategoryQueryRequest queryRequest) {
        return R.data(goodsCategoryService.categoryProportion(queryRequest));
    }
}
