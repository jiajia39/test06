package com.framework.emt.system.domain.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.goods.Goods;
import com.framework.emt.system.domain.goods.request.ChatQuery;
import com.framework.emt.system.domain.goods.request.GoodsCreateRequest;
import com.framework.emt.system.domain.goods.request.GoodsUpdateRequest;
import com.framework.emt.system.domain.goods.request.GoodsQuery;
import com.framework.emt.system.domain.goods.response.GoodsResponse;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryTreeQuery;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryEnableFlagRequest;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品 服务接口
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@Validated
public interface GoodsService extends BaseService<Goods> {

    /**
     * 新增商品
     *
     * @param request 新增参数
     * @return 商品id
     */
    Long create(@Valid GoodsCreateRequest request);
    
    /**
     * 更新商品
     *
     * @param id      商品id
     * @param request 更新参数
     */
    void update(@NotNull(message = "商品id不能为空") Long id, @Valid GoodsUpdateRequest request);
    
    /**
     * 查看商品
     *
     * @param id 商品id
     * @return 商品
     */
    Goods entity(@NotNull(message = "商品id不能为空") Long id);
    
    /**
     * 查看商品
     *
     * @param id 商品id
     * @return 商品
     */
    GoodsResponse info(@NotNull(message = "商品id不能为空") Long id);
    
    /**
     * 商品分页
     *
     * @param pageQuery 分页查询条件
     * @return 查询结果
     */
    IPage<GoodsResponse> page(@Valid GoodsQuery pageQuery);

    /**
     * 删除商品
     *
     * @param id 商品id
     */
    void delete(@NotNull(message = "商品id不能为空") Long id);

    void updateEnableListing(GoodsCategoryEnableFlagRequest request);

    void updateIsRecommend(GoodsCategoryEnableFlagRequest request);

    List<StatisticsTrendValueResponse> listingGoodsNum(ChatQuery request);
}
