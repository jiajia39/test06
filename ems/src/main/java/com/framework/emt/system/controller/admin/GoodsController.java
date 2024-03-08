package com.framework.emt.system.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.goods.request.ChatQuery;
import com.framework.emt.system.domain.goods.request.GoodsCreateRequest;
import com.framework.emt.system.domain.goods.request.GoodsQuery;
import com.framework.emt.system.domain.goods.request.GoodsUpdateRequest;
import com.framework.emt.system.domain.goods.response.GoodsResponse;
import com.framework.emt.system.domain.goods.service.GoodsService;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryEnableFlagRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品 接口层
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@Api(tags = "商品管理")
@Validated
@RestController
@RequiredArgsConstructor
public class GoodsController extends BaseController {

    private final GoodsService goodsService;

    @ApiOperation(value = "新增商品")
    @PostMapping("/admin/goods")
    public R<Long> create(@RequestBody GoodsCreateRequest request) {
        return R.data(goodsService.create(request));
    }

    @ApiOperation(value = "更新商品")
    @PostMapping("/admin/goods/{id}")
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody GoodsUpdateRequest request) {
        goodsService.update(id, request);
        return R.status(true);
    }

    @ApiOperation(value = "商品分页")
    @GetMapping("/admin/goods/page")
    public R<IPage<GoodsResponse>> page(@Valid GoodsQuery query) {
        return R.data(goodsService.page(query));
    }

    @ApiOperation(value = "查看商品")
    @GetMapping("/admin/goods/{id}")
    public R<GoodsResponse> info(@PathVariable("id") Long id) {
        return R.data(goodsService.info(id));
    }

    @ApiOperation(value = "删除商品")
    @PostMapping("/admin/goods/delete/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        goodsService.delete(id);
        return R.status(true);
    }

    @ApiOperation(value = "上下架")
    @PostMapping("/admin/goods/enable/listing")
    public R<Boolean> updateEnableListing(@RequestBody GoodsCategoryEnableFlagRequest request) {
        goodsService.updateEnableListing(request);
        return R.status(true);
    }

    @ApiOperation(value = "推荐")
    @PostMapping("/admin/goods/is/recommend")
    public R<Boolean> updateIsRecommend(@RequestBody GoodsCategoryEnableFlagRequest request) {
        goodsService.updateIsRecommend(request);
        return R.status(true);
    }

    @ApiOperation(value = "已上架的商品数目")
    @GetMapping("/admin/goods/listing/goods/num")
    public R<List<StatisticsTrendValueResponse>> listingGoodsNum(@Validated ChatQuery request) {
        return R.data(goodsService.listingGoodsNum(request));
    }
}
