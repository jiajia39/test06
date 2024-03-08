package com.framework.emt.system.domain.exception.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryCreateRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryImportExcel;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryQueryRequest;
import com.framework.emt.system.domain.exception.request.ExceptionCategoryUpdateRequest;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryResponse;
import com.framework.emt.system.domain.exception.response.ExceptionCategoryTreeResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 异常分类 服务层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface ExceptionCategoryService extends BaseService<ExceptionCategory> {

    /**
     * 异常分类创建
     *
     * @param request
     * @return 主键id
     */
    Long create(ExceptionCategoryCreateRequest request);

    /**
     * 异常分类删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 异常分类更新
     *
     * @param id      主键id
     * @param request
     * @return 主键id
     */
    Long update(Long id, ExceptionCategoryUpdateRequest request);

    /**
     * 异常分类详情
     *
     * @param id 主键id
     * @return
     */
    ExceptionCategoryResponse detail(Long id);

    /**
     * 异常分类分页列表
     *
     * @param request
     * @return
     */
    IPage<ExceptionCategoryResponse> page(ExceptionCategoryQueryRequest request);

    /**
     * 异常分类树状图
     *
     * @return
     */
    List<ExceptionCategoryTreeResponse> tree();

    /**
     * 异常分类导入
     *
     * @param excelImportDataList     excel导入的数据集
     * @param existParentCategoryList excel中在数据库存在的父级异常分类列表
     * @param notExistParentTitleList excel中在数据库不存在的父级异常分类名称列表
     */
    void importExceptionCategory(List<ExceptionCategoryImportExcel> excelImportDataList,
                                 List<ExceptionCategory> existParentCategoryList, List<String> notExistParentTitleList);

    /**
     * 校验异常分类必须存在
     * 且必须是异常项下的异常分类
     *
     * @param id 主键id
     */
    void validateItemCategory(Long id);

    /**
     * 根据id查询此条异常分类
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    ExceptionCategory findByIdThrowErr(Long id);

    /**
     * 通过异常分类名称列表查询异常分类
     *
     * @param titles 异常分类名称列表
     * @return
     */
    ExceptionCategory findByTitles(List<String> titles);

    /**
     * 根据异常分类id列表查询出对应的异常分类名称列表
     *
     * @param ids 主键id列表
     * @return
     */
    List<String> findTitleList(List<String> ids);

    /**
     * 根据异常分类id列表查询出对应的分类名称列表
     *
     * @param ids 主键id列表
     * @return 键为异常分类id，值为分类名称的map列表
     */
    Map<String, String> findIdTitleByIds(List<String> ids);

    /**
     * 通过异常分类id列表获取 异常分类map
     *
     * @param categoryIds 异常分类id列表
     * @return 异常分类map
     */
    Map<Long, ExceptionCategory> getMapByIds(List<Long> categoryIds);

    /**
     * 根据异常分类id查询
     * 此异常分类下的所有的子异常分类id
     *
     * @param id 异常分类id
     * @return
     */
    List<Long> findChildIdsById(Long id);


    /**
     * 根据异常分类id获取所有子id
     *
     * @param id 异常分类id
     * @return
     */
    List<Long> getChildById(Long id);
}
