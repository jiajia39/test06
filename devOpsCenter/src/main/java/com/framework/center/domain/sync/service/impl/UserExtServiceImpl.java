package com.framework.center.domain.sync.service.impl;


import com.framework.center.domain.sync.UserExt;
import com.framework.center.domain.sync.mapper.UserExtMapper;
import com.framework.center.domain.sync.service.IUserExtService;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Service;

@Service
public class UserExtServiceImpl extends BaseDaoImpl<UserExtMapper, UserExt> implements IUserExtService {

}
