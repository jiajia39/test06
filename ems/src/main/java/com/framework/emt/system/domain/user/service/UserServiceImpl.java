package com.framework.emt.system.domain.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.entity.IResultCode;
import com.framework.common.api.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 用户 实现类
 *
 * @author ds_C
 * @since 2023-07-17
 */
@RequiredArgsConstructor
@Service("emt-userServiceImpl")
public class UserServiceImpl implements UserService {

    private final IUserDao userDao;

    @Override
    public void checkUserExist(List<Long> idList, IResultCode error) {
        long count = userDao.count(new LambdaQueryWrapper<User>()
                .in(User::getId, idList));
        if (count != idList.size()) {
            throw new ServiceException(error);
        }
    }

    @Override
    public List<Long> findExistIdList(List<Long> idList) {
        List<User> userList = userDao.list(new LambdaQueryWrapper<User>()
                .select(User::getId).in(User::getId, idList));
        return userList.stream().map(User::getId).collect(Collectors.toList());
    }

    @Override
    public List<User> findUsers(List<Long> idList) {
        return userDao.list(new LambdaQueryWrapper<User>().in(User::getId, idList));
    }

    @Override
    public Map<Long, String> findIdOfNameMap(List<Long> idList) {
        return CollectionUtil.isEmpty(idList) ? null : userDao.list(new LambdaQueryWrapper<User>().select(User::getId, User::getName)
                .in(User::getId, idList)).stream().collect(Collectors.toMap(User::getId, User::getName));
    }

    @Override
    public Map<String, String> findStrIdNameMap(List<String> idList) {
        return userDao.list(new LambdaQueryWrapper<User>().select(User::getId, User::getName).in(User::getId, idList)).stream()
                .collect(Collectors.toMap(user -> String.valueOf(user.getId()), User::getName));
    }

    @Override
    public User findUserByIdThrowErr(Long id, IResultCode error) {
        return Optional.ofNullable(userDao.getById(id)).orElseThrow(() -> new ServiceException(error));
    }

    @Override
    public Map<Long, User> getMapByIds(List<Long> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        List<User> userList = userDao.listByIds(userIds);
        if (CollectionUtil.isEmpty(userList)) {
            return new HashMap<>(NumberUtils.INTEGER_ZERO);
        }
        return userList.stream().collect(toMap(User::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    public User userIdConsistent(Long userId, Long loginUserId, IResultCode error, IResultCode notFindError) {
        if (ObjectUtil.equal(userId, loginUserId)) {
            throw new ServiceException(error);
        }
        return Optional.ofNullable(userDao.getById(userId)).orElseThrow(() -> new ServiceException(notFindError));
    }

}
