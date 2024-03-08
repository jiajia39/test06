package com.framework.emt.system.domain.goodscategory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryQueryRequest;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryTreeQuery;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryTreeResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常分类 mapper
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

    /**
     * 查询某个时间段内已上架的商品分类数目（增强：支持按不同层级展示）
     *
     * @param request 参数
     */
    List<GoodsCategoryTreeResponse> list(@Param("request") GoodsCategoryTreeQuery request);

    List<StatisticsProportionResponse> categoryProportion(@Param("request") GoodsCategoryQueryRequest queryRequest);
}
