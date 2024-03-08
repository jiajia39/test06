package com.framework.emt.system.domain.workspacelocation.constant;

import java.util.regex.Pattern;

/**
 * 作业单元常量类
 *
 * @author ds_C
 * @since 2023-07-11
 */
public class WorkspaceConstant {

    /**
     * 作业单元移动端接口：通过二维码查询作业单元详情
     * 参数二维码密文解密后的字符串校验的正则表达式
     */
    public final static Pattern CIPHER_TEXT_PATTERN = Pattern.compile("workspace:(\\d+)");

    /**
     * 作业单元树状图缓存名称
     */
    public final static String WORKSPACE_TREE_CACHE_NAME = "workspace.tree.cache.name";

    /**
     * 作业单元树状图键前缀
     */
    public final static String WORKSPACE_TREE_KEY_PREFIX = "workspace.tree";

    /**
     * 作业单元树状图键前缀
     */
    public final static String WORKSPACE_TREE_CACHE_KEY = "workspace.tree.cache.key";

}
