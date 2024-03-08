package com.framework.emt.system.domain.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.goods.Goods;
import com.framework.emt.system.domain.goods.request.ChatQuery;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品 mapper
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<StatisticsTrendValueResponse> listingGoodsNum(@Param("request") ChatQuery request);
}
