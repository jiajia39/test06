package com.framework.emt.system.infrastructure.constant;

/**
 * 模板code常量类
 * 源于表emt_message_template
 *
 * @author ds_C
 * @since 2023-08-22
 */
public class TemplateCodeConstant {

    /**
     * 异常提报-提报
     */
    public static final String SUBMIT = "001";

    /**
     * 异常响应-驳回
     */
    public static final String RESPONSE_REJECT = "002";

    /**
     * 异常响应-转派
     */
    public static final String RESPONSE_TO_OTHER = "003";

    /**
     * 异常响应-设置处理人
     */
    public static final String RESPONSE_SUBMIT = "004";

    /**
     * 异常处理-接受
     */
    public static final String HANDING_ACCEPT = "005";

    /**
     * 异常处理-驳回/异常提报节点
     */
    public static final String HANDING_REJECT_TO_SUBMIT_NODE = "006";

    /**
     * 异常处理-驳回/异常响应节点
     */
    public static final String HANDING_REJECT_TO_RESPONSE_NODE = "007";

    /**
     * 异常处理-转派
     */
    public static final String HANDING_TO_OTHER = "008";

    /**
     * 异常处理-设置协同
     */
    public static final String HANDING_CREATE_COOPERATION = "009";

    /**
     * 异常处理-提交验收
     */
    public static final String HANDING_SUBMIT = "010";

    /**
     * 异常验收-驳回
     */
    public static final String CHECK_REJECT = "011";

    /**
     * 异常验收-验收
     */
    public static final String CHECK_PASS = "012";

    /**
     * 异常协同-转派
     */
    public static final String COOPERATION_TRANSFER = "013";

    /**
     * 响应超时
     */
    public static final String RESPONSE_TIME_OUT = "014";

    /**
     * 处理超时
     */
    public static final String HANDING_TIME_OUT = "015";

    /**
     * 协同超时
     */
    public static final String COOPERATION_TIME_OUT = "016";

    /**
     * 响应逐级上报
     */
    public static final String RESPONSE_TIME_OUT_REPORT = "017";

    /**
     * 处理逐级上报
     */
    public static final String HANDING_TIME_OUT_REPORT = "018";

    /**
     * 协同逐级上报
     */
    public static final String COOPERATION_TIME_OUT_REPORT = "019";

    /**
     * 异常处理-申请挂起
     */
    public static final String HANDING_SUSPEND = "020";

    /**
     * 挂起自动恢复
     */
    public static final String HANDING_SUSPEND_RESUME = "021";

}
