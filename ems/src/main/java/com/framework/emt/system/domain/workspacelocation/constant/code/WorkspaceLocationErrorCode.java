package com.framework.emt.system.domain.workspacelocation.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 作业单元异常码
 * code为2900-3000
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum WorkspaceLocationErrorCode implements IResultCode {

    /**
     * 异常码
     */
    WORKSPACE_LOCATION_INFO_NOT_FIND(2900, "作业单元信息未查询到数据"),
    THIS_WORKSPACE_LOCATION_BOTTOM_HAVE_OTHER_WORKSPACE(2901, "删除失败！该作业单元下存在其它作业单元"),
    SAME_PARENT_OF_CHILD_TITLE_CAN_NOT_EQUALS(2902, "同一父级下的作业单元名称不能重复，请你检查之后再试！"),
    WORKSPACE_LOCATION_TITLE_IS_NOT_BLANK(2903, "作业单元名称不能为空"),
    NOT_HAVE_PARENT_CAN_NOT_TITLE_EQUALS(2904, "没有父级的作业单元名称不能重复"),
    WORKSPACE_LOCATION_TITLE_LIST_CAN_NOT_CONTAIN_PARENT_TITLE(2905, "导入失败，作业单元名称列表不能包含父级作业单元名称"),
    THIS_WORKSPACE_LOCATION_BOTTOM_HAVE_TASK_SUBMIT(2906, "删除失败！该作业单元下存在未完成的异常提报"),
    PART_WORKSPACE_LOCATION_TITLE_IS_EXIST_IN_PARENT_DOWN(2907, "导入失败，部分作业单元名称已在其对应的父级下存在"),
    PART_NOT_HAVE_PARENT_WORKSPACE_LOCATION_TITLE_IS_EXIST(2908, "导入失败，部分没有父级的作业单元名称已存在"),
    TOP_TITLE_CAN_NOT_EQUALS(2909, "顶层作业单元名称不能重复");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}
