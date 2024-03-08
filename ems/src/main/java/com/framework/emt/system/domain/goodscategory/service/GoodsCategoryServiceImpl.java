package com.framework.emt.system.domain.goodscategory.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.goods.Goods;
import com.framework.emt.system.domain.goods.service.GoodsService;
import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.constant.GoodsCategoryErrorCode;
import com.framework.emt.system.domain.goodscategory.convert.GoodsCategoryConvertor;
import com.framework.emt.system.domain.goodscategory.mapper.GoodsCategoryMapper;
import com.framework.emt.system.domain.goodscategory.request.*;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryResponse;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryTreeResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionPieResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsProportionResponse;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品分类 服务实现类
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@RequiredArgsConstructor
@Service
public class GoodsCategoryServiceImpl extends BaseServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {

    private final IUserDao userDao;

    @Override
    public Long create(GoodsCategoryCreateRequest request) {
        // 校验商品分类名称不能重复
        validateName(null, request.getTitle());
        //验证父分类id是否存在
        Integer level = 1;
        String parentIdPath = "";
        if (ObjectUtil.isNotNull(request.getParentId())) {
            GoodsCategory parentGoodsCategory = this.findByIdThrowErr(request.getParentId(), GoodsCategoryErrorCode.PARENT_ID_IS_NOT_EXIST);
            level = parentGoodsCategory.getLevel() + 1;
            // 获取父级ID路径
            parentIdPath = NumberUtils.LONG_ZERO.equals(request.getParentId()) ? StrUtil.EMPTY : joinParentPath(request.getParentId());
            validLevel(level);
        }
        // 添加商品分类
        Long id = create(GoodsCategoryConvertor.INSTANCE.requestToEntity(request, parentIdPath, level));
        return id;
    }

    public void validLevel(Integer level) {
        if (level > 3) {
            throw new ServiceException(GoodsCategoryErrorCode.THE_MAXIMUM_CLASSIFICATION_LEVEL_IS_LEVEL_3);
        }
    }

    /**
     * 根据异常分类父级id和父级id路径
     * 拼接出此条异常分类的父级id路径
     *
     * @param parentId
     * @return
     */
    public String joinParentPath(Long parentId) {
        return findByIdThrowErr(parentId).getParentIdPath() + StrUtil.UNDERLINE + parentId;
    }

    /**
     * 校验商品分类名称不能重复
     *
     * @param title 商品分类名称
     */
    private void validateName(Long id, String title) {
        LambdaQueryWrapper<GoodsCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsCategory::getTitle, title);
        if (ObjectUtil.isNotNull(id)) {
            lambdaQueryWrapper.ne(GoodsCategory::getId, id);
        }
        if (Convert.toInt(this.count(lambdaQueryWrapper)) > 0) {
            throw new ServiceException(GoodsCategoryErrorCode.NAME_IS_EXIST);
        }
    }

    @Override
    public void update(Long id, GoodsCategoryUpdateRequest request) {
        GoodsCategory goodsCategory = findByIdThrowErr(id);
        if (request.getParentId() == 0) {
            request.setParentId(null);
        }
        validateName(id, request.getTitle());
        //验证父分类id是否存在
        Integer level = goodsCategory.getLevel();
        if (ObjectUtil.isNotNull(request.getParentId()) && !ObjectUtil.equal(request.getParentId(), 0L)) {
            GoodsCategory parentGoodsCategory = this.findByIdThrowErr(request.getParentId());
            level = parentGoodsCategory.getLevel() + 1;
            // 获取父级ID路径
            request.setParentIdPath(NumberUtils.LONG_ZERO.equals(request.getParentId()) ? StrUtil.EMPTY : joinParentPath(request.getParentId()));
            validLevel(level);
        }
        // 编辑商品分类
        GoodsCategoryConvertor.INSTANCE.requestToUpdate(goodsCategory, request, level);
        this.update(goodsCategory);
    }

    @Override
    public GoodsCategory findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, GoodsCategoryErrorCode.NOT_FOUND);
    }

    @Override
    public GoodsCategoryResponse info(Long id) {
        return GoodsCategoryConvertor.INSTANCE.entity2Response(findByIdThrowErr(id));
    }

    @Override
    public IPage<GoodsCategoryResponse> page(GoodsCategoryQuery query) {
        IPage<GoodsCategory> page = Condition.getPage(query);
        LambdaQueryWrapper<GoodsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getCode()), GoodsCategory::getCode, query.getCode());
        queryWrapper.like(StringUtils.isNotBlank(query.getTitle()), GoodsCategory::getTitle, query.getTitle());
        queryWrapper.eq(ObjectUtil.isNotNull(query.getEnableFlag()), GoodsCategory::getEnableFlag, query.getEnableFlag());
        queryWrapper.eq(ObjectUtil.isNotNull(query.getParentId()), GoodsCategory::getParentId, query.getParentId());
        queryWrapper.orderByAsc(GoodsCategory::getSort);
        queryWrapper.orderByDesc(GoodsCategory::getCreateTime);
        IPage<GoodsCategoryResponse> goodsCategoryResponseIPage = GoodsCategoryConvertor.INSTANCE.pageVo(this.page(page, queryWrapper));
        if (ObjectUtil.isNotEmpty(goodsCategoryResponseIPage.getRecords())) {
            for (GoodsCategoryResponse goodsCategoryResponse :
                    goodsCategoryResponseIPage.getRecords()) {
                goodsCategoryResponse.setHasChildren(hasChildren(goodsCategoryResponse.getId()));
            }
            TypeReference<String> type = new TypeReference<String>() {
            };
            userDao.loadData(goodsCategoryResponseIPage.getRecords(), "createUser", User::getId, "createUserName", "name", type);
            userDao.loadData(goodsCategoryResponseIPage.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
        }

        return goodsCategoryResponseIPage;
    }

    Boolean hasChildren(Long categoryIds) {
        long count = this.count(new LambdaQueryWrapper<GoodsCategory>().eq(GoodsCategory::getParentId, categoryIds));
        return count > 0L;
    }

    @Override
    public void delete(Long id) {
        this.findByIdThrowErr(id);
        long childCount = this.count(new LambdaQueryWrapper<GoodsCategory>().eq(GoodsCategory::getParentId, id));
        if (Convert.toInt(childCount) > 0) {
            throw new ServiceException(GoodsCategoryErrorCode.THERE_ARE_SUBCATEGORIES_UNDER_THIS_CATEGORY_THAT_CANNOT_BE_DELETED);
        }
        final GoodsService goodsService = SpringUtil.getBean(GoodsService.class);
        long goodsCount = goodsService.count(new LambdaQueryWrapper<Goods>().like(Goods::getCategoryParentIdPath, id));
        if (Convert.toInt(goodsCount) > 0) {
            throw new ServiceException("商品已引用该分类无法删除");
        }
        Assert.isTrue(this.removeById(id),
                () -> new ServiceException(GoodsCategoryErrorCode.DELETE_NOT_EXIST));
    }

    @Override
    public Long enableFlag(GoodsCategoryEnableFlagRequest request) {
        GoodsCategory goodsCategory = findByIdThrowErr(request.getId());
        goodsCategory.setEnableFlag(request.getEnableFlag());
        return this.update(goodsCategory);
    }

    @Override
    public Map<Long, GoodsCategoryResponse> getByIds(List<Long> categoryIds) {
        List<GoodsCategory> list = this.list(new LambdaQueryWrapper<GoodsCategory>().in(GoodsCategory::getId, categoryIds));
        List<GoodsCategoryResponse> goodsCategoryResponses = GoodsCategoryConvertor.INSTANCE.entityList2Response(list);
        return goodsCategoryResponses.stream().collect(Collectors.toMap(GoodsCategoryResponse::getId, Function.identity(), (key1, key2) -> key2));
    }

    @Override
    public List<GoodsCategoryTreeResponse> treeList(GoodsCategoryTreeQuery request) {
        List<GoodsCategoryTreeResponse> list = this.baseMapper.list(request);
        return buildTree(list);
    }

    @Override
    public List<String> findTitleList(List<String> ids) {
        List<GoodsCategory> goodsCategoryList = this.list(new LambdaQueryWrapper<GoodsCategory>().in(GoodsCategory::getId, ids));
        return goodsCategoryList.stream().map(GoodsCategory::getTitle).collect(Collectors.toList());
    }

    @Override
    public StatisticsProportionPieResponse categoryProportion(GoodsCategoryQueryRequest queryRequest) {
        StatisticsProportionPieResponse pieResponse = new StatisticsProportionPieResponse();
        List<StatisticsProportionResponse> statisticsProportion = this.baseMapper.categoryProportion(queryRequest);
        pieResponse.init(statisticsProportion, calculateExceptionTotal(statisticsProportion));
        return pieResponse;
    }

    /**
     * 计算异常共计
     *
     * @param statisticsProportion 分类或者流程的个数
     * @return 计算异常共计
     */
    private Integer calculateExceptionTotal(List<StatisticsProportionResponse> statisticsProportion) {
        Integer exceptionTotal = 0;
        for (StatisticsProportionResponse proportion :
                statisticsProportion) {
            exceptionTotal = exceptionTotal + proportion.getCount();
        }
        return exceptionTotal;
    }

    /**
     * 构建分类树状图
     *
     * @param categoryList 分类列表
     * @return 分类树状图
     */
    private List<GoodsCategoryTreeResponse> buildTree(List<GoodsCategoryTreeResponse> categoryList) {
        Map<Long, List<GoodsCategoryTreeResponse>> categoryMap = categoryList.stream().collect(Collectors.groupingBy(GoodsCategoryTreeResponse::getParentId));
        categoryList.forEach(category -> category.setChildList(categoryMap.getOrDefault(category.getId(), Collections.emptyList())));
        return categoryList.stream().filter(category -> NumberUtils.LONG_ZERO.equals(category.getParentId())).collect(Collectors.toList());
    }
}
