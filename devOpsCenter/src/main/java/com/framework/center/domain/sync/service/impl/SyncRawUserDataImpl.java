package com.framework.center.domain.sync.service.impl;

import com.framework.center.domain.sync.SyncRawUserData;
import com.framework.center.domain.sync.mapper.SyncRawUserDataMapper;
import com.framework.center.domain.sync.service.ISyncRawUserDataService;
import com.framework.center.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SyncRawUserDataImpl extends BaseServiceImpl<SyncRawUserDataMapper, SyncRawUserData>  implements ISyncRawUserDataService {
}
