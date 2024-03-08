package com.framework.center.domain.sync.service.impl;

import com.framework.center.domain.sync.User;
import com.framework.center.domain.sync.mapper.UserMapper;
import com.framework.center.domain.sync.service.IUserDao;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@AllArgsConstructor
public class UserDaoImpl extends BaseDaoImpl<UserMapper, User> implements IUserDao {
}
