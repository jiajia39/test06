package com.framework.center.domain.sync.service.impl;


import com.framework.center.domain.sync.UserDept;
import com.framework.center.domain.sync.mapper.UserDeptMapper;
import com.framework.center.domain.sync.service.IUserDeptDao;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserDeptDaoImpl extends BaseDaoImpl<UserDeptMapper, UserDept> implements IUserDeptDao {
}
