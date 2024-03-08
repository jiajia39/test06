package com.framework.center.domain.sync.service.impl;

import com.framework.center.domain.sync.SyncRawDeptData;
import com.framework.center.domain.sync.mapper.SyncRawDeptDataMapper;
import com.framework.center.domain.sync.service.ISyncRawDeptDataService;
import com.framework.center.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SyncRawDeptDataImpl extends BaseServiceImpl<SyncRawDeptDataMapper, SyncRawDeptData>  implements ISyncRawDeptDataService {
}
