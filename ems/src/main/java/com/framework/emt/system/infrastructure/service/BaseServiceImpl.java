package com.framework.emt.system.infrastructure.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.mybatisplus.dao.impl.BaseDaoImpl;
import com.framework.core.mybatisplus.entity.BaseEntity;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;

import java.util.Optional;

/**
 * 基础服务实现层
 *
 * @author jiaXue
 * date 2023/3/14
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseDaoImpl<M, T> implements BaseService<T> {

    public BaseServiceImpl() {
    }

    @Override
    public Long create(T entity) {
        if (!save(entity)) {
            throw new ServiceException(BusinessErrorCode.CREATE_ERROR);
        }
        return entity.getId();
    }

    @Override
    public Long update(T entity) {
        if (!updateById(entity)) {
            throw new ServiceException(BusinessErrorCode.UPDATE_ERROR);
        }
        return entity.getId();
    }

    @Override
    public void deleteById(Long id) {
        if (!removeById(id)) {
            throw new ServiceException(BusinessErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public T findByIdThrowErr(Long id, IResultCode errorMessage) {
        return Optional.ofNullable(getById(id)).orElseThrow(() -> new ServiceException(errorMessage));
    }

}
