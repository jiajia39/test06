package com.framework.emt.system.domain.goodscategory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.request.*;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryResponse;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryTreeResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionPieResponse;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 异常分类 服务接口
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@Validated
public interface GoodsCategoryService extends BaseService<GoodsCategory> {

    /**
     * 新增异常分类
     *
     * @param request 新增参数
     * @return 异常分类id
     */
    Long create(@Valid GoodsCategoryCreateRequest request);

    /**
     * 更新异常分类
     *
     * @param id      异常分类id
     * @param request 更新参数
     */
    void update(@NotNull(message = "异常分类id不能为空") Long id, @Valid GoodsCategoryUpdateRequest request);

    /**
     * 查看异常分类
     *
     * @param id 异常分类id
     * @return 异常分类
     */
    GoodsCategory findByIdThrowErr(@NotNull(message = "异常标签id不能为空") Long id);

    /**
     * 查看异常分类
     *
     * @param id 异常分类id
     * @return 异常分类
     */
    GoodsCategoryResponse info(@NotNull(message = "异常分类id不能为空") Long id);

    /**
     * 异常分类分页
     *
     * @param pageQuery 分页查询条件
     * @return 查询结果
     */
    IPage<GoodsCategoryResponse> page(@Valid GoodsCategoryQuery pageQuery);

    /**
     * 删除异常分类
     *
     * @param id 异常分类id
     */
    void delete(@NotNull(message = "异常分类id不能为空") Long id);

    /**
     * 启用禁用
     *
     * @param request 参数
     * @return
     */
    Long enableFlag(GoodsCategoryEnableFlagRequest request);

    Map<Long, GoodsCategoryResponse> getByIds(List<Long> categoryIds);

    /**
     * 查询某个时间段内已上架的商品分类数目（增强：支持按不同层级展示）
     *
     * @param request 参数
     */
    List<GoodsCategoryTreeResponse> treeList(GoodsCategoryTreeQuery request);

    List<String> findTitleList(List<String> ids);

    /**
     * 环形统计
     * @param queryRequest 参数
     * @return
     */
    StatisticsProportionPieResponse categoryProportion(GoodsCategoryQueryRequest queryRequest);



}
