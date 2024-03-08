package com.framework.emt.system.domain.workspacelocation.request;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 作业单元 导入参数
 *
 * @author ds_C
 * @since 2023-07-21
 */
@Getter
@Setter
public class WorkspaceLocationImportExcel implements Serializable {

    @ExcelProperty(value = "父级作业单元名称")
    private String parentTitle = StrUtil.EMPTY;

    @ExcelProperty(value = "*作业单元名称（同一父级下唯一）")
    private String title;

    @ExcelProperty(value = "空间坐标")
    private String spaceCoordinate;

    @ExcelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态")
    private EnableFlagEnum enableFlag = EnableFlagEnum.ENABLE;

    public String getSpliceStr() {
        return this.title + this.parentTitle;
    }

}
