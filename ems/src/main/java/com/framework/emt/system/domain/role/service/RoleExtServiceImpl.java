package com.framework.emt.system.domain.role.service;

import com.framework.emt.system.domain.role.RoleExt;
import com.framework.emt.system.domain.role.mapper.RoleExtMapper;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleExtServiceImpl extends BaseServiceImpl<RoleExtMapper, RoleExt> implements IRoleExtService {
}
