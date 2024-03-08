package com.framework.emt.system.domain.goods.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.goods.Goods;
import com.framework.emt.system.domain.goods.constant.GoodsErrorCode;
import com.framework.emt.system.domain.goods.request.ChatQuery;
import com.framework.emt.system.domain.goods.request.GoodsCreateRequest;
import com.framework.emt.system.domain.goods.request.GoodsUpdateRequest;
import com.framework.emt.system.domain.goods.request.GoodsQuery;
import com.framework.emt.system.domain.goods.response.GoodsResponse;
import com.framework.emt.system.domain.goods.convert.GoodsConvertor;
import com.framework.emt.system.domain.goods.mapper.GoodsMapper;
import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryTreeQuery;
import com.framework.emt.system.domain.goodscategory.request.GoodsCategoryEnableFlagRequest;
import com.framework.emt.system.domain.goodscategory.service.GoodsCategoryService;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品 服务实现类
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@RequiredArgsConstructor
@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final GoodsCategoryService goodsCategoryService;

    private final IUserDao userDao;

    @Override
    public Long create(GoodsCreateRequest request) {
        validateCode(null, request.getCode());
        String categoryPath = getCategoryPath(request.getCategoryId());
        request.setCategoryParentIdPath(categoryPath);
        Goods goods = GoodsConvertor.INSTANCE.requestToEntity(request);
        goods.setListingTime(new Date());
        return create(goods);
    }


    /**
     * 校验商品分类名称不能重复
     *
     * @param code 商品编号
     */
    private void validateCode(Long id, String code) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getCode, code);
        if (ObjectUtil.isNotNull(id)) {
            lambdaQueryWrapper.ne(Goods::getId, id);
        }
        if (Convert.toInt(this.count(lambdaQueryWrapper)) > 0) {
            throw new ServiceException(GoodsErrorCode.GOODS_CODE_EXIST);
        }
    }

    private String getCategoryPath(Long categoryId) {
        GoodsCategory goodsCategory = goodsCategoryService.findByIdThrowErr(categoryId);
        return goodsCategory.getId() + goodsCategory.getParentIdPath();
    }

    @Override
    public void update(Long id, GoodsUpdateRequest request) {
        Goods goods = entity(id);
        validateCode(id, request.getCode());
        String categoryPath = getCategoryPath(request.getCategoryId());
        request.setCategoryParentIdPath(categoryPath);
        if (ObjectUtil.equal(request.getEnableListing(), 1) && !ObjectUtil.equal(goods.getEnableListing(), 1)) {
            request.setListingTime(new Date());
        }
        GoodsConvertor.INSTANCE.requestToUpdate(goods, request);
        update(goods);
    }

    @Override
    public Goods entity(Long id) {
        return this.findByIdThrowErr(id, GoodsErrorCode.NOT_FOUND);
    }

    @Override
    public GoodsResponse info(Long id) {
        GoodsResponse goodsResponse = GoodsConvertor.INSTANCE.entity2Response(entity(id));
        List<String> CategoryParentIds = Arrays.asList(goodsResponse.getCategoryParentIdPath().split("_"));
        List<String> categoryTitleList = goodsCategoryService.findTitleList(CategoryParentIds);
        goodsResponse.setCategoryName(categoryTitleList.get(0));
        goodsResponse.setCategoryParentList(categoryTitleList);
        return goodsResponse;
    }

    @Override
    public IPage<GoodsResponse> page(GoodsQuery query) {
        IPage<Goods> page = Condition.getPage(query);
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getCode()), Goods::getCode, query.getCode());
        queryWrapper.like(StringUtils.isNotBlank(query.getName()), Goods::getName, query.getName());
        queryWrapper.eq(ObjectUtil.isNotNull(query.getCategoryId()), Goods::getCategoryId, query.getCategoryId());
        IPage<GoodsResponse> goodsResponseIPage = GoodsConvertor.INSTANCE.pageVo(this.page(page, queryWrapper));
        if (ObjectUtil.isNotEmpty(goodsResponseIPage.getRecords())) {
            goodsResponseIPage.getRecords().forEach(goodsResponse -> {
                if (StringUtils.isNotBlank(goodsResponse.getCategoryParentIdPath())) {
                    List<String> CategoryParentIds = Arrays.asList(goodsResponse.getCategoryParentIdPath().split("_"));
                    List<String> categoryTitleList = goodsCategoryService.findTitleList(CategoryParentIds);
                    goodsResponse.setCategoryName(categoryTitleList.get(0));
                    goodsResponse.setCategoryParentList(categoryTitleList);
                }

            });
        }
        TypeReference<String> type = new TypeReference<String>() {
        };
        userDao.loadData(goodsResponseIPage.getRecords(), "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(goodsResponseIPage.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);

        return goodsResponseIPage;
    }

    @Override
    public void delete(Long id) {
        Assert.isTrue(this.removeById(id),
                () -> new ServiceException(GoodsErrorCode.DELETE_NOT_EXIST));
    }

    @Override
    public void updateEnableListing(GoodsCategoryEnableFlagRequest request) {
        Goods goods = this.findByIdThrowErr(request.getId(), GoodsErrorCode.NOT_FOUND);
        goods.setEnableListing(request.getEnableFlag());
        goods.setListingTime(new Date());
        this.update(goods);
    }

    @Override
    public void updateIsRecommend(GoodsCategoryEnableFlagRequest request) {
        Goods goods = this.findByIdThrowErr(request.getId(), GoodsErrorCode.NOT_FOUND);
        goods.setIsRecommend(request.getEnableFlag());
        this.update(goods);
    }

    @Override
    public List<StatisticsTrendValueResponse> listingGoodsNum(ChatQuery request) {
        return this.baseMapper.listingGoodsNum(request);
    }
}
