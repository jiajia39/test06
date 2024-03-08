package com.framework.emt.system.domain.exception.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionItemExportResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemResponse;
import com.framework.emt.system.domain.exception.response.ExceptionItemShortResponse;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 异常项 服务层
 *
 * @author ds_C
 * @since 2023-07-12
 */
public interface ExceptionItemService extends BaseService<ExceptionItem> {

    /**
     * 异常项创建
     *
     * @param request
     * @return 主键id
     */
    Long create(ExceptionItemCreateRequest request);

    /**
     * 异常项删除
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 异常项批量删除
     *
     * @param ids 主键id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 异常项更新
     *
     * @param id
     * @param request
     * @return 主键id
     */
    Long update(Long id, ExceptionItemUpdateRequest request);

    /**
     * 异常项详情
     *
     * @param id 主键id
     * @return
     */
    ExceptionItemResponse detail(Long id);

    /**
     * 异常项分页列表
     *
     * @param request 查询条件
     * @return
     */
    IPage<ExceptionItemResponse> page(ExceptionItemQueryRequest request);

    /**
     * 异常项导出
     *
     * @param request 查询条件
     * @return
     */
    List<ExceptionItemExportResponse> export(ExceptionItemExportQueryRequest request);

    /**
     * 异常项导入
     *
     * @param importDataList       excel导入的数据集
     * @param categoryTitleOfIdMap key为异常分类名称，value为异常分类id的map列表
     */
    void importExceptionItemExcel(List<ExceptionItemImportExcel> importDataList, Map<String, Long> categoryTitleOfIdMap);

    /**
     * 根据id查询此条异常项
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    ExceptionItem findByIdThrowErr(Long id);

    /**
     * 通过异常项简化列表查询异常项
     *
     * @param titles 异常项简化列表
     * @return
     */
    ExceptionItem findByShortList(List<ExceptionItemShortResponse> titles);

    /**
     * 根据异常分类id查询此异常分类
     * 及此异常分类下级的所有的异常项
     *
     * @param categoryId 异常流程id
     * @return
     */
    List<ExceptionItemResponse> findItemsByCategoryId(Long categoryId);

    /**
     * 异常项启用禁用
     *
     * @param enableFlag 禁用启用状态
     * @param ids        主键id列表
     */
    void changeEnableFlag(Integer enableFlag, List<Long> ids);

    /**
     * 校验异常项下的异常分类必须和异常流程下的异常分类是同一个
     *
     * @param exceptionProcessOfCategoryId 异常流程下的异常分类id
     * @param exceptionItemOfCategoryId    异常项下的异常分类id
     * @return
     */
    Long validateExceptionCategory(Long exceptionProcessOfCategoryId,
                                   Long exceptionItemOfCategoryId);

    /**
     * 通过异常项id列表获取 异常项map
     *
     * @param itemIds 异常项id列表
     * @return 异常项map
     */
    Map<Long, ExceptionItem> getMapByIds(List<Long> itemIds);

    /**
     * 根据异常项标题列表查询异常项列表
     *
     * @param titles 异常项标题列表
     * @return
     */
    List<ExceptionItem> findListByTitles(List<String> titles);

    /**
     * 根据异常任务id和提报版本号查询异常项名称
     *
     * @param taskId        异常任务id
     * @param submitVersion 提报版本号
     * @return
     */
    String findItemName(Long taskId, Integer submitVersion);

    /**
     * 通过id获取异常项名称
     *
     * @param id 异常项id
     * @return
     */
    String getTitleById(Long id);

    /**
     * 校验异常分类是否在异常项列表中存在
     *
     * @param categoryId 异常分类id
     */
    void validateCategoryExist(Long categoryId);

}
