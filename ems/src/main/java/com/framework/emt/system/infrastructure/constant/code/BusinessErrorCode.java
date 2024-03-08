package com.framework.emt.system.infrastructure.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用异常码
 * code为1000-2000
 * 异常分类：code为2000-2100
 * 异常项：code为2100-2200
 * 异常流程：code为2200-2300
 * 用户：code为2300-2400
 * 知识库分类：code为2400-2500
 * 知识库：code为2500-2600
 * 上报流程：code为2600-2700
 * 消息：code为2700-2800
 * 上报流程推送：code为2800-2900
 * 作业单元：code为2900-3000
 * 异常提报：code为3000-3100
 * 异常表单：code为3100-3200
 * 异常标签：code为3200-3300
 * 标签关联异常：code为3300-3400
 * 部门：code为3400-3500
 * 异常任务：code为3500-3600
 * 异常处理：code为3600-3700
 * 异常协同：code为3700-3800
 * 异常任务定时计划：code为3800-3900
 *
 * @author ds_C
 * @since 2023-07-11
 */
@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements IResultCode {

    /**
     * 异常码
     */
    CREATE_ERROR(1000, "新增数据失败"),
    UPDATE_ERROR(1001, "更新数据失败"),
    DELETE_ERROR(1002, "删除数据失败"),
    STATE_CHANGE_FAIL_DATA_NOT_EXIST_CAN_NOT_CHANGE(1003, "状态更新失败，不存在的数据无法更新"),
    DELETE_FAIL_NOT_EXIST_DATA_CAN_NOT_DELETE(1004, "删除失败，不存在的数据无法删除"),
    NOT_EXIST_OR_ENABLE_DATA_CAN_NOT_DELETE(1005, "删除失败，不存在或已启用的数据无法删除"),
    FILE_UPLOAD_FAIL_EXCEED_SYSTEM_MAX_NUM(1006, "文件上传失败，最大文件上传数量"),
    IMPORT_FAIL_EXCEED_SYSTEM_MAX_IMPORT_NUM(1007, "导入失败，最大导入条数"),
    EXPORT_FAIL_EXCEED_SYSTEM_MAX_EXPORT_NUM(1008, "导出失败，最大导出条数"),
    IMPORT_DATA_IS_NOT_NULL(1009, "导入失败，导入数据不能为空");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
