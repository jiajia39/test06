package com.framework.center.domain.sync.service.impl;

import com.framework.center.domain.sync.Dept;
import com.framework.center.domain.sync.mapper.DeptMapper;
import com.framework.center.domain.sync.service.IDeptService;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class DeptServiceImpl extends BaseDaoImpl<DeptMapper, Dept> implements IDeptService {


}
