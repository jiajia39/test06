package com.framework.emt.system.infrastructure.exception.task.record.constant.constant;

/**
 * 任务履历内容模版
 *
 * @author ds_C
 * @since 2023-07-11
 */
public class TaskRecordTemplate {


    public final static String EXCEPTION_TASK_CREATE = "[${operator}]新增异常[${code}]提报";

    public final static String EXCEPTION_TASK_UPDATE = "[${operator}]异常提报编辑";

    public final static String REPORT_DELETION = "[${operator}]异常提报删除";

    public final static String REPORTING = "[${operator}]提报异常给[${respondent}]";

    public final static String REPORTING_CLOSURE = "[${operator}]关闭提报信息！";

    public final static String REPORTING_CANCEL = "[${operator}]异常提报撤销";

    public final static String RESPONSE_TRANSFER = "[${operator}]将异常转派给[${respondent}]";

    public final static String EXCEPTION_TASK_RESPONSE_REJECT = "[${operator}]驳回异常提报，驳回原因:[${rejectReason}]";

    public final static String RESPONSE = "[${operator}]接受异常提报";

    public final static String RESPONSE_SUBMISSION_RESPONSE = "[${operator}]提交异常响应给[${processed}]";

    public final static String SUBMIT_ABNORMAL_RESPONSE = "[${operator}]提交异常响应";

    public final static String PROCESS_TRANSFER = "[${operator}]转派处理信息！新处理人为[${newProcessor}]";

    public final static String EXCEPTION_TASK_HANDING_REJECT = "[${operator}]异常处理驳回，驳回节点：[${node}] 驳回原因:[${rejectReason}]";

    public final static String EXCEPTION_TASK_HANDING_TO_OTHER = "[${operator}]转派处理信息！新处理人为[${otherUser}]";

    public final static String EXCEPTION_TASK_HANDING_ACCEPT = "[${operator}]接受异常处理";

    public final static String EXCEPTION_TASK_HANDING_SUSPEND = "[${operator}]异常挂起。挂起原因：[${reason}]，预期恢复时间：[${time}] ";

    public final static String EXCEPTION_TASK_HANDING_SUSPEND_DELAY = "[${operator}]异常挂起延长，延期原因：[${reason}]，预期恢复时间：[${time}] ";

    public final static String EXCEPTION_TASK_HANDING_HAND_RESUME = "[${operator}]手动恢复处理信息";

    public final static String EXCEPTION_TASK_HANDING_AUTO_RESUME = "系统自动恢复处理信息！ ";

    public final static String HANDING_REJECTION = "[${operator}]异常响应驳回，驳回节点：[${node}] 驳回原因:[${rejectionReason}]";

    public final static String PROCESSING_ACCEPTANCE = "[${operator}]接受异常响应";

    public final static String PROCESSING_SUBMISSION_PROCESSING = "[${operator}]异常处理更新";

    public final static String PROCESSING_PENDING = "[${operator}]挂起处理信息。挂起原因：[${reason}]，预期恢复时间：[${time}]";

    public final static String PROCESS_SETTINGS_COLLABORATION = "[${operator}]设置[${cooperationUser}] 为协同人！";

    public final static String PROCESSING_APPLICATION_ACCEPTANCE = "[${operator}]申请验收处理结果！";

    public final static String HANDING_MANUAL_RECOVERY = "[${operator}]手动恢复处理信息！";

    public final static String PROCESS_AUTOMATIC_RECOVERY = "系统自动恢复处理信息！";

    public final static String PROCESSING_EXTENDED_RECOVERY = "[${operator}]延长恢复至[${time}]";

    public final static String EXCEPTION_TASK_HANDING_COLLABORATION_TRANSFER = "[${operator}]转派协同信息！新协同人为[${newCollaborator}]";

    public final static String HANDING_ACCEPTANCE_COLLABORATION = "[${operator}]接受协同信息！";

    public final static String PROCESS_SUBMISSION_COLLABORATION = "[${operator}]提交协同结果！";

    public final static String COLLABORATION_CANCEL = "[${operator}]协同任务:[${title}]撤销";

    public final static String ACCEPTANCE_PASSED = "[${operator}]通过验收信息！";

    public final static String ACCEPTANCE_REJECTION = "[${operator}]驳回验收信息！驳回原因:[${reason}]";

    public final static String PROCESSING_TIMEOUT = "处理人:[${processed}]，处理时限: [${processingTimeLimit}]";

    public final static String PROCESSING_COLLABORATION_TIMEOUT = "协同人:[${collaborator}]，协同时限: [${collaborationTimeLimit}]";

    public final static String PROCESSING_SUBMISSION_PROCESSING_BY_ADMIN = "管理员[${operator}]异常处理更新";

    //管理员【管理员名称】申请验收处理结果！
    public final static String PROCESSING_APPLICATION_ACCEPTANCE_BY_ADMIN = "管理员[${operator}]申请验收处理结果！";

    public final static String ACCEPTANCE_PASSED_BY_ADMIN = "管理员[${operator}]通过验收信息！";

    public final static String ACCEPTANCE_REJECTION_BY_ADMIN = "管理员[${operator}]驳回验收信息！驳回原因:[${reason}]";

}



