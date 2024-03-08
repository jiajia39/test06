package com.framework.emt.system.domain.dept.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.framework.admin.system.dao.IDeptDao;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.mapper.DeptMapper;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.dept.constant.code.DeptErrorCode;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * 部门 实现类
 *
 * @author ds_C
 * @since 2023-07-17
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements DeptService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dept findByIdThrowErr(Long id) {
        return Optional.ofNullable(SpringUtil.getBean(IDeptDao.class).getById(id)).orElseThrow(() -> new ServiceException(DeptErrorCode.DEPT_INFO_NOT_FIND));
    }

    @Override
    public Map<Long, Dept> getMapByIds(List<Long> deptIds) {
        if (CollectionUtil.isEmpty(deptIds)) {
            return new HashMap<>(0);
        }
        List<Dept> deptList = listByIds(deptIds);
        if (CollectionUtil.isEmpty(deptIds)) {
            return new HashMap<>(0);
        }
        return deptList.stream().collect(toMap(Dept::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    public List<Long> findByParentId(Long id) {
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(id);
        List<Map<String, Object>> deptIdList = this.jdbcTemplate.queryForList("WITH RECURSIVE dept_hierarchy AS (SELECT id, parent_id FROM ft_dept WHERE parent_id =" + id + " and is_deleted=0 UNION ALL SELECT d.id, d.parent_id FROM ft_dept d INNER JOIN dept_hierarchy h ON d.parent_id = h.id) select id from dept_hierarchy");
        if (ObjectUtil.isNotEmpty(deptIdList)) {
            for (Map<String, Object> deptIdMap : deptIdList) {
                Long batchNumber = Convert.toLong(deptIdMap.get("id"));
                deptIds.add(batchNumber);
            }
        }
        return deptIds;
    }

}
