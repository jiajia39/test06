package com.framework.emt.system.domain.goodscategory.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryCreateRequest;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryUpdateRequest;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 异常分类 转换器
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@Mapper
public interface GoodsCategoryConvertor {

    GoodsCategoryConvertor INSTANCE = Mappers.getMapper(GoodsCategoryConvertor.class);

    /**
     * 异常分类新增参数转换为实体
     *
     * @param request 新增参数
     * @return 实体
     */
    @Mapping(target = "categoryImg", defaultExpression = "java(new ArrayList<>())")
    GoodsCategory requestToEntity(GoodsCategoryCreateRequest request, String parentIdPath, Integer level);

    /**
     * 异常分类更新参数转换为实体
     *
     * @param goodsCategory 异常分类实体
     * @param request       更新参数
     */
    @Mapping(target = "categoryImg", defaultExpression = "java(new ArrayList<>())")
    void requestToUpdate(@MappingTarget GoodsCategory goodsCategory, GoodsCategoryUpdateRequest request, Integer level);

    /**
     * 异常分类转换为返回体
     *
     * @param goodsCategory 异常分类实体
     * @return 返回体
     */
    @Mapping(target = "categoryImg", defaultExpression = "java(new ArrayList<>())")
    GoodsCategoryResponse entity2Response(GoodsCategory goodsCategory);

    /**
     * 异常分类列表转换为返回体
     *
     * @param list 异常分类实体列表
     * @return 返回体列表
     */
    @Mapping(target = "categoryImg", defaultExpression = "java(new ArrayList<>())")
    List<GoodsCategoryResponse> entityList2Response(List<GoodsCategory> list);

    /**
     * 异常分类分页转换为返回体
     *
     * @param pages 分页实体
     * @return 返回体
     */
    default IPage<GoodsCategoryResponse> pageVo(IPage<GoodsCategory> pages) {
        IPage<GoodsCategoryResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(entityList2Response(pages.getRecords()));
        return pageVo;
    }

}
