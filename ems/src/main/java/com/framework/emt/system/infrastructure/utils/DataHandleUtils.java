package com.framework.emt.system.infrastructure.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.dao.IDeptDao;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.common.property.utils.SpringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据处理工具类
 *
 * @author ds_C
 * @date 2023/6/9
 */
public class DataHandleUtils {

    private DataHandleUtils() {
    }

    /**
     * 通过用户id装载用户名
     *
     * @param pageResult 分页结果集
     * @param <T>        泛型
     * @return 包含用户名的分页列表
     */
    public static <T> IPage<T> loadUserName(IPage<T> pageResult) {
        if (CollectionUtil.isEmpty(pageResult.getRecords())) {
            return pageResult;
        }
        TypeReference<String> type = new TypeReference<String>() {
        };
        IUserDao userDao = SpringUtil.getBean(IUserDao.class);
        userDao.loadData(pageResult.getRecords(), "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(pageResult.getRecords(), "updateUser", User::getId, "updateUserName", "name", type);
        return pageResult;
    }

    /**
     * 通过用户id装载用户名
     *
     * @param listResult 列表结果集
     * @param <T>        泛型
     * @return 包含用户名的列表
     */
    public static <T> List<T> loadUserName(List<T> listResult) {
        if (CollectionUtil.isEmpty(listResult)) {
            return listResult;
        }
        TypeReference<String> type = new TypeReference<String>() {
        };
        IUserDao userDao = SpringUtil.getBean(IUserDao.class);
        userDao.loadData(listResult, "createUser", User::getId, "createUserName", "name", type);
        userDao.loadData(listResult, "updateUser", User::getId, "updateUserName", "name", type);
        return listResult;
    }

    /**
     * 装载excel导出结果集中id对应的名称
     *
     * @param exportResult 导出结果集
     * @param <T>          泛型
     * @return 包含用户名的列表
     */
    public static <T> List<T> loadExportResult(List<T> exportResult) {
        if (CollectionUtil.isEmpty(exportResult)) {
            return exportResult;
        }
        TypeReference<String> type = new TypeReference<String>() {};
        // 通过部门id装载部门名称
        IDeptDao deptDao = SpringUtil.getBean(IDeptDao.class);
        deptDao.loadData(exportResult, "deptId", Dept::getId, "deptName", "deptName", type);
        // 通过用户id装载用户名称
        return loadUserName(exportResult);
    }

    /**
     * 以逗号拆分一个字符串
     *
     * @param str
     * @return
     */
    public static List<String> splitStr(String str) {
        return Arrays.stream(str.split(StrUtil.COMMA)).collect(Collectors.toList());
    }

    /**
     * 将多个列表元素合并成一个
     * 过滤掉空的列表、以及重复的元素
     *
     * @param elements
     * @return
     */
    public static <T> List<T> mergeElements(List<T>... elements) {
        return Stream.of(elements).filter(CollectionUtil::isNotEmpty).flatMap(List::stream).distinct().collect(Collectors.toList());
    }

}