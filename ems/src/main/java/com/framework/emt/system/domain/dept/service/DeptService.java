package com.framework.emt.system.domain.dept.service;

import com.framework.admin.system.entity.Dept;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 部门 服务层
 *
 * @author ds_C
 * @since 2023-07-17
 */
public interface DeptService extends BaseService<Dept> {

    /**
     * 根据id查询部门信息
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    Dept findByIdThrowErr(Long id);

    /**
     * 通过部门id列表获取 部门map
     *
     * @param deptIds 部门id列表
     * @return 部门map
     */
    Map<Long, Dept> getMapByIds(List<Long> deptIds);

    /**
     * 根据id查询部门信息
     * 数据异常则抛出错误信息
     *
     * @param id 主键id
     * @return
     */
    List<Long> findByParentId(Long id);

}
