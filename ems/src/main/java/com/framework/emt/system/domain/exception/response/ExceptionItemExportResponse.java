package com.framework.emt.system.domain.exception.response;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常项导出 查询条件
 *
 * @author ds_C
 * @since 2023-09-06
 */
@Getter
@Setter
public class ExceptionItemExportResponse implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty("异常项名称")
    private String title;

    @ColumnWidth(20)
    @ExcelProperty("异常分类名称")
    private String exceptionCategoryName;

    @ColumnWidth(20)
    @ExcelProperty("紧急程度")
    private String priorityName;

    @ColumnWidth(20)
    @ExcelProperty("严重程度")
    private String severityName;

    @ColumnWidth(20)
    @ExcelProperty("响应时限")
    private String responseDurationTime;

    @ColumnWidth(20)
    @ExcelProperty("处理时限")
    private String handingDurationTime;

    @ColumnWidth(20)
    @ExcelProperty("创建人")
    private String createUserName;

    @ColumnWidth(20)
    @ExcelProperty("创建时间")
    private Date createTime;

    @ColumnWidth(20)
    @ExcelProperty("最后更新人")
    private String updateUserName;

    @ColumnWidth(20)
    @ExcelProperty("最后更新时间")
    
    private Date updateTime;

    @ExcelIgnore
    @ExcelProperty("紧急程度枚举")
    private PriorityEnum priority;

    @ExcelIgnore
    @ExcelProperty("严重程度枚举")
    private SeverityEnum severity;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人id")
    private Long createUser;

    @ExcelIgnore
    @ApiModelProperty(value = "最后更新人id")
    private Long updateUser;

    @ExcelIgnore
    @ExcelProperty("响应时限 单位：分钟")
    private Integer responseDurationTimeNum;

    @ExcelIgnore
    @ExcelProperty("处理时限 单位：分钟")
    private Integer handingDurationTimeNum;

    public void init() {
        this.priorityName = priority.getName();
        this.severityName = severity.getName();
        this.responseDurationTime = formatTime(responseDurationTimeNum);
        this.handingDurationTime = formatTime(handingDurationTimeNum);
    }

    public static String formatTime(int minute) {
        int days = minute / (NumberConstant.TWENTY_FOUR * NumberConstant.SIXTY);
        int hours = (minute % (NumberConstant.TWENTY_FOUR * NumberConstant.SIXTY)) / NumberConstant.SIXTY;
        int minutes = minute % NumberConstant.SIXTY;
        String formattedTime = StrUtil.EMPTY;
        if (days > NumberUtils.INTEGER_ZERO) {
            formattedTime += days + StringConstant.DAY;
        }
        if (hours > NumberUtils.INTEGER_ZERO) {
            formattedTime += hours + StringConstant.HOURS;
        }
        if (minutes > NumberUtils.INTEGER_ZERO) {
            formattedTime += minutes + StringConstant.MINUTES;
        }

        return formattedTime;
    }

}
