package com.framework.emt.system.infrastructure.constant;

/**
 * 通用常量类
 *
 * @author ds_C
 * @since 2023-07-11
 */
public class BusinessConstant {

    /**
     * excel导出数据最大长度
     */
    public final static Integer MAX_EXPORT_NUM = 1000;

    /**
     * 上传文件最大数量
     */
    public final static Integer FILE_UPLOAD_COUNT = 10;

    /**
     * excel导入数据最大长度
     */
    public final static Integer MAX_IMPORT_NUM = 1000;

    /**
     * excel导入批次数量
     */
    public final static Integer BATCH_IMPORT_NUM = 5001;

    /**
     * 匹配逐级上报消息模板响应人、处理人、协同人位置正则
     */
    public static final String SEND_USER_NAME_POSITION = "\\【.*?\\】";

    /**
     * excel导出数据最大长度（系统）
     */
    public final static String SYSTEM_MAX_EXPORT_NUM = "excel.export.data.count";

    /**
     * 上传文件最大数量（系统）
     */
    public final static String SYSTEM_FILE_UPLOAD_COUNT = "upload.file.count";

    /**
     * excel导入数据最大长度（系统）
     */
    public final static String SYSTEM_MAX_IMPORT_NUM = "excel.import.data.count";

    /**
     * 异常管理字段
     */
    public final static String EXCEPTION_MANAGEMENT = "exceptionManagement";

}
