package com.framework.emt.system.infrastructure.constant;

/**
 * 异常通用常量
 *
 * @author jiaXue
 * date 2023/8/16
 */
public class ExceptionTaskConstant {
    /**
     * excel导入数据最大数目
     */
    public static final long EXCEL_IMPORT_DEFAULT_MAX_COUNT = 2000;
    /**
     * excel导入数据最大数目key
     */
    public static final String EXCEL_IMPORT_MAX_COUNT = "excel.import.max.count";

    /**
     * excel导出数据最大数目
     */
    public static final long EXCEL_EXPORT_DEFAULT_MAX_COUNT = 2000;
    /**
     * excel导出数据最大数目 key
     */
    public static final String EXCEL_EXPORT_MAX_COUNT = "excel.import.max.count";

    /**
     * 异常响应转派总次数
     */
    public static final Integer TASK_TRANSFER_DEFAULT_MAX_COUNT = 10;
    /**
     * 异常响应转派总次数 key
     */
    public static final String TASK_TRANSFER_MAX_COUNT = "exception.task.transfer.max.count";

    /**
     * 异常处理转派总次数
     */
    public static final Integer TASK_HANDING_DEFAULT_MAX_COUNT = 10;

    /**
     * 异常处理转派总次数 key
     */
    public static final String TASK_HANDING_MAX_COUNT = "exception.task.handing.max.count";
    /**
     * 异常协同转派总次数
     */
    public static final Integer TASK_HAND_COOPERATION_DEFAULT_MAX_COUNT = 10;

    /**
     * 异常协同转派总次数 key
     */
    public static final String TASK_HAND_COOPERATION_MAX_COUNT = "exception.task.handing.cooperation.max.count";
    /**
     * 异常挂起最小时间（秒）
     */
    public static final long SUSPEND_MIN_SECONDS = 600;


    /**
     * 异常挂起最大次数 key
     */
    public static final String SUSPEND_MAX_COUNT = "exception.task.suspend.max.count";

    /**
     * 异常挂起最大次数 默认值
     */
    public final static Integer SUSPEND_DEFAULT_MAX_COUNT = 5;

    /**
     * 异常挂起最大时间(分钟) key
     */
    public static final String SUSPEND_MAX_MINUTES = "exception.task.suspend.max.time";

    /**
     * 异常挂起最大时间(分钟) 默认值 1天
     */
    public final static Integer SUSPEND_DEFAULT_MAX_MINUTES = 1440;

    /**
     * 异常流程设置异常原因项最大数目 key
     */
    public final static String PROCESS_SETTING_REASON_ITEMS_MAX_COUNT = "exception.process.setting.reason.items.max.count";

    /**
     * 异常流程设置异常原因项最大数目  默认值
     */
    public final static Integer SETTING_PROCESS_REASON_ITEMS_DEFAULT_MAX_COUNT = 10;

    /**
     * 异常流程设置提报附加字段最大数目 key
     */
    public final static String PROCESS_SETTING_SUBMIT_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.submit.extend.fields.max.count";

    /**
     * 异常流程设置提报附加字段最大数目  默认值
     */
    public final static Integer PROCESS_SETTING_SUBMIT_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;

    /**
     * 异常流程设置响应附加字段最大数目 key
     */
    public final static String PROCESS_SETTING_RESPONSE_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.response.extend.fields.max.count";

    /**
     * 异常流程设置响应附加字段最大数目  默认值
     */
    public final static Integer PROCESS_SETTING_RESPONSE_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;

    /**
     * 异常流程设置处理附加字段最大数目 key
     */
    public final static String PROCESS_SETTING_HANDING_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.handing.extend.fields.max.count";

    /**
     * 异常流程设置处理附加字段最大数目  默认值
     */
    public final static Integer PROCESS_SETTING_HANDING_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;


    /**
     * 异常流程设置挂起附加字段最大数目 key
     */
    public final static String PROCESS_SETTING_PENDING_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.pending.extend.fields.max.count";

    /**
     * 异常流程设置挂起附加字段最大数目  默认值
     */
    public final static Integer PROCESS_SETTING_PENDING_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;

    /**
     * 异常流程设置协同附加字段最大数目 key
     */
    public final static String EXCEPTION_PROCESS_SETTING_COOPERATION_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.cooperation.extend.fields.max.count";

    /**
     * 异常流程设置协同附加字段最大数目 默认值
     */
    public final static Integer EXCEPTION_PROCESS_SETTING_COOPERATION_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;


    /**
     * 异常流程设置验收附加字段最大数目 key
     */
    public final static String PROCESS_SETTING_CHECK_EXTEND_FIELDS_MAX_COUNT = "exception.process.setting.check.extend.fields.max.count";

    /**
     * 异常流程设置验收附加字段最大数目  默认值
     */
    public final static Integer PROCESS_SETTING_CHECK_EXTEND_FIELDS_DEFAULT_MAX_COUNT = 20;

    /**
     * 上报流程设置上报人最大数目 key
     */
    public final static String REPORT_NOTICE_PROCESS_SETTING_REPORT_USER_MAX_COUNT = "report.notice.process.setting.report.user.max.count";

    /**
     * 上报流程设置上报人最大数目  默认值
     */
    public final static Integer REPORT_NOTICE_PROCESS_SETTING_REPORT_USER_DEFAULT_MAX_COUNT = 10;


    /**
     * 异常流程设置响应人最大数目 key
     */
    public final static String EXCEPTION_PROCESS_SETTING_RESPONSE_USER_MAX_COUNT = "exception.process.setting.response.user.max.count";

    /**
     * 异常流程设置响应人最大数目  默认值
     */
    public final static Integer EXCEPTION_PROCESS_SETTING_RESPONSE_USER_DEFAULT_MAX_COUNT = 10;


    /**
     * 异常流程设置处理人最大数目 key
     */
    public final static String EXCEPTION_PROCESS_SETTING_HANDING_USER_MAX_COUNT = "exception.process.setting.handing.user.max.count";

    /**
     * 异常流程设置处理人最大数目  默认值
     */
    public final static Integer EXCEPTION_PROCESS_SETTING_HANDING_USER_DEFAULT_MAX_COUNT = 10;


    /**
     * 异常流程设置验收人最大数目 key
     */
    public final static String EXCEPTION_PROCESS_SETTING_CHECK_USER_MAX_COUNT = "exception.process.setting.check.user.max.count";

    /**
     * 异常流程设置验收人最大数目  默认值
     */
    public final static Integer EXCEPTION_PROCESS_SETTING_CHECK_USER_DEFAULT_MAX_COUNT = 10;

    /**
     * 处理人提交异常协同任务最大数目 key
     */
    public final static String HANDING_USER_SUBMIT_COOPERATION_TASK_MAX_COUNT = "handing.user.submit.cooperation.task.max.count";

    /**
     * 处理人提交异常协同任务最大数目  默认值
     */
    public final static Integer HANDING_USER_SUBMIT_COOPERATION_TASK_DEFAULT_MAX_COUNT = 10;
}
