package com.framework.emt.system.domain.goods.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.emt.system.domain.goods.Goods;
import com.framework.emt.system.domain.goods.request.GoodsCreateRequest;
import com.framework.emt.system.domain.goods.request.GoodsUpdateRequest;
import com.framework.emt.system.domain.goods.response.GoodsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品 转换器
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@Mapper
public interface GoodsConvertor {

    GoodsConvertor INSTANCE = Mappers.getMapper(GoodsConvertor.class);

   /**
     * 商品新增参数转换为实体
     *
     * @param request 新增参数
     * @return 实体
     */
   @Mapping(target = "goodsImg", defaultExpression = "java(new ArrayList<>())")
    Goods requestToEntity(GoodsCreateRequest request);

    /**
     * 商品更新参数转换为实体
     *
     * @param goods 商品实体
     * @param request 更新参数
     */
    @Mapping(target = "goodsImg", defaultExpression = "java(new ArrayList<>())")
    void requestToUpdate(@MappingTarget Goods goods, GoodsUpdateRequest request);
    
    /**
     * 商品转换为返回体
     *
     * @param goods 商品实体
     * @return 返回体
     */
    @Mapping(target = "goodsImg", defaultExpression = "java(new ArrayList<>())")
    GoodsResponse entity2Response(Goods goods);

    /**
     * 商品列表转换为返回体
     *
     * @param list 商品实体列表
     * @return 返回体列表
     */
    List<GoodsResponse> entityList2Response(List<Goods> list);

    /**
     * 商品分页转换为返回体
     *
     * @param pages 分页实体
     * @return 返回体
     */
    default IPage<GoodsResponse> pageVo(IPage<Goods> pages) {
        IPage<GoodsResponse> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(entityList2Response(pages.getRecords()));
        return pageVo;
    }

}
