package com.framework.emt.system.domain.exception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryQueryRequest;
import com.framework.emt.system.domain.exception.response.ChildCountResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryTreeResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常分类 Mapper层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface ExceptionCategoryMapper extends BaseMapper<ExceptionCategory> {

    /**
     * 异常分类分页列表
     *
     * @param page
     * @param request
     * @return
     */
    IPage<ExceptionCategoryResponse> page(IPage<ExceptionCategoryResponse> page,
                                          @Param("request") ExceptionCategoryQueryRequest request);

    /**
     * 异常分类列表
     *
     * @return
     */
    List<ExceptionCategoryTreeResponse> list();

    /**
     * 根据异常分类id或异常分类名称查询异常分类列表
     *
     * @param idList    异常分类id
     * @param titleList 异常分类名称
     * @return
     */
    List<ExceptionCategory> findCategoryList(@Param("idList") List<String> idList, @Param("titleList") List<String> titleList);

    /**
     * 根据异常分类id查询此条异常分类下的
     * 子异常分类、异常项、异常流程、上报流程的数量
     *
     * @param categoryId
     * @return
     */
    ChildCountResponse findChildCount(@Param("categoryId") Long categoryId);

    /**
     * 根据异常分类名称+父级id拼接的字符串
     * 查询出此条异常分类是否存在
     *
     * @param id               异常分类id
     * @param titleParentIdStr 异常分类名称+父级id拼接的字符串
     * @return
     */
    ExceptionCategory findByTitleParentIdStr(@Param("id") Long id, @Param("titleParentIdStr") String titleParentIdStr);

    /**
     * 根据异常分类id列表查询异常分类列表
     *
     * @param ids 异常分类id列表
     * @return
     */
    List<ExceptionCategory> listByIds(@Param("ids") List<Long> ids);


    /**
     * 根据异常分类id获取所有子id
     *
     * @param id 异常分类id
     * @return
     */
    List<Long> getChildById(@Param("id") Long id);
}
