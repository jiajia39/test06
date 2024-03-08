package com.framework.emt.system.infrastructure.service;

import com.framework.common.api.entity.IResultCode;
import com.framework.core.mybatisplus.dao.BaseDao;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;

/**
 * 基础服务接口层
 *
 * @author jiaXue
 * date 2023/3/14
 */
public interface BaseService<T> extends BaseDao<T> {

    /**
     * 创建数据
     *
     * @param entity 数据实体
     * @return 实体id
     */
    Long create(T entity);

    /**
     * 更新数据
     *
     * @param entity 数据实体
     * @return 实体id
     */
    Long update(T entity);

    /**
     * 删除数据
     *
     * @param id 数据id
     */
    void deleteById(Long id);

    /**
     * 通过实体id查询数据实体
     * 若数据异常则抛出相应的错误信息
     *
     * @param id           实体id
     * @param errorMessage 异常信息
     * @return 数据实体
     */
    T findByIdThrowErr(Long id, IResultCode errorMessage);

}
