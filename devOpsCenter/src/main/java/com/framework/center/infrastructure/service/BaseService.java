package com.framework.center.infrastructure.service;

import com.framework.core.mybatisplus.dao.BaseDao;

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
     * 通过id查询数据实体
     *
     * @param id 实体id
     * @return 数据实体
     */
    T findById(Long id);

    /**
     * 数据实体是否存在
     *
     * @param id 实体id
     * @return true/false
     */
    boolean isExistById(Long id);


}
