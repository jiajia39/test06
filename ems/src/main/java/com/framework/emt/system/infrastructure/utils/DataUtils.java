package com.framework.emt.system.infrastructure.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.admin.system.entity.User;
import com.framework.common.json.utils.JsonUtil;
import com.framework.emt.system.domain.formfield.response.FormFieldListResponse;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 常用数据处理转换工具类
 *
 * @author jiaXue
 * date 2023/8/18
 */
public class DataUtils {

    private DataUtils() {
    }

    /**
     * id 是否可用于查询的id
     *
     * @param id id
     * @return 是 否
     */
    public static boolean isId(Long id) {
        return id != null && id > 0;
    }

    public static String getUserName(Map<Long, User> userMap, Long userId) {
        if (!isId(userId)) {
            return "";
        }
        User user = userMap.get(userId);
        return user == null ? "" : user.getName();
    }

    // 附加json字段转化
    public static String convertFormField(List<FormFieldResponse> extendResponseList) {
        if (ObjectUtil.isEmpty(extendResponseList)) {
            return "";
        }
        List<FormFieldListResponse> formFieldListList = new ArrayList<>();
        TypeReference<List<FormFieldResponse>> type = new TypeReference<List<FormFieldResponse>>() {};
        List<FormFieldResponse> formFields = JsonUtil.parse(JsonUtil.toJson(extendResponseList), type);
        formFields.forEach(responseExtendField -> {
            FormFieldListResponse formFieldListResponse = new FormFieldListResponse();
            formFieldListList.add(formFieldListResponse.init(responseExtendField.getLabel(), responseExtendField.getValue(), responseExtendField.getKey()));
        });
        return JSONUtil.toJsonPrettyStr(formFieldListList);
    }

}
