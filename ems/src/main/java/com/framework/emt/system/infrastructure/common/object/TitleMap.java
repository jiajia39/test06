package com.framework.emt.system.infrastructure.common.object;

import java.util.Map;

/**
 * 知识库分类和异常项Map列表包装类
 * MapStruct不支持在同一个方法中相同类型的多个参数使用@Context注解
 *
 * @author ds_C
 * @since 2023-09-06
 */
public class TitleMap {

    /**
     * key为知识库分类名称，value为知识库分类id的map列表
     */
    private Map<String, Long> categoryTitleOfIdMap;

    /**
     * key为异常项名称，value为异常项id的map列表
     */
    private Map<String, Long> itemTitleOfIdMap;

    public TitleMap(Map<String, Long> categoryTitleOfIdMap, Map<String, Long> itemTitleOfIdMap) {
        this.categoryTitleOfIdMap = categoryTitleOfIdMap;
        this.itemTitleOfIdMap = itemTitleOfIdMap;
    }

    public Map<String, Long> getCategoryTitleOfIdMap() {
        return categoryTitleOfIdMap;
    }

    public Map<String, Long> getItemTitleOfIdMap() {
        return itemTitleOfIdMap;
    }

}
