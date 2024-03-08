package com.framework.emt.system.domain.user.service;

import com.framework.admin.system.entity.User;
import com.framework.common.api.entity.IResultCode;

import java.util.List;
import java.util.Map;

/**
 * 用户 服务层
 *
 * @author ds_C
 * @since 2023-07-17
 */
public interface UserService {

    /**
     * 校验所有用户必须在数据库中存在
     *
     * @param idList 上报人id列表
     * @param error  错误信息
     * @return
     */
    void checkUserExist(List<Long> idList, IResultCode error);

    /**
     * 根据传递的用户id列表查询数据库中存在的用户id列表
     *
     * @param idList 用户id列表
     * @return 只包含用户id的List列表
     */
    List<Long> findExistIdList(List<Long> idList);

    /**
     * 根据传递的用户id列表查询数用户列表
     *
     * @param idList 用户id列表
     * @return 只包含用户id的List列表
     */
    List<User> findUsers(List<Long> idList);

    /**
     * 获取所有用户id对应的用户姓名列表
     *
     * @param idList 用户id列表
     * @return 键为用户id，值为用户姓名的map列表
     */
    Map<Long, String> findIdOfNameMap(List<Long> idList);

    /**
     * 获取所有字符串类型的用户id对应的用户姓名列表
     *
     * @param idList 用户id列表
     * @return 键为字符串用户id，值为用户姓名的map列表
     */
    Map<String, String> findStrIdNameMap(List<String> idList);

    /**
     * 根据响应人id查询响应人信息
     * 数据异常则抛出错误信息
     *
     * @param id    响应人id
     * @param error 错误信息
     * @return
     */
    User findUserByIdThrowErr(Long id, IResultCode error);

    /**
     * 通过用户id列表获取 用户map
     *
     * @param userIds 用户id列表
     * @return 用户map
     */
    Map<Long, User> getMapByIds(List<Long> userIds);

    /**
     * 判断响应人或处理人或验收人或协同人与当前用户是否一致，一致就抛出异常
     *
     * @param userId       响应人或处理人或验收人或协同人
     * @param loginUserId  登录人
     * @param error        错误信息
     * @param notFindError 找不到对应响应人或处理人或验收人或协同人报错
     * @return 用户
     */
    User userIdConsistent(Long userId, Long loginUserId, IResultCode error, IResultCode notFindError);

}
