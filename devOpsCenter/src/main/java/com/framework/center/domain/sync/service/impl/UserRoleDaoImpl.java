package com.framework.center.domain.sync.service.impl;

import com.framework.center.domain.sync.UserRole;
import com.framework.center.domain.sync.mapper.UserRoleMapper;
import com.framework.center.domain.sync.service.IUserRoleDao;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserRoleDaoImpl extends BaseDaoImpl<UserRoleMapper, UserRole> implements IUserRoleDao {
}
