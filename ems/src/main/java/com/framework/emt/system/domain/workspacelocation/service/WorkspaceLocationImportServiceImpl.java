package com.framework.emt.system.domain.workspacelocation.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.excel.support.ExcelImporter;
import com.framework.common.property.utils.SpringUtil;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.constant.code.WorkspaceLocationErrorCode;
import com.framework.emt.system.domain.workspacelocation.mapper.WorkspaceLocationMapper;
import com.framework.emt.system.domain.workspacelocation.request.WorkspaceLocationImportExcel;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceExportResponse;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 作业单元导入 实现类
 *
 * @author ds_C
 * @since 2023-07-21
 */
@RequiredArgsConstructor
public class WorkspaceLocationImportServiceImpl implements ExcelImporter<WorkspaceLocationImportExcel> {

    private final WorkspaceLocationService workspaceLocationService;

    @Override
    public void save(List<WorkspaceLocationImportExcel> excelImportDataList) {
        // 校验传入的excel列表不能为空
        if (NumberUtils.INTEGER_ZERO.equals(excelImportDataList.size())) {
            throw new ServiceException(BusinessErrorCode.IMPORT_DATA_IS_NOT_NULL);
        }

        // 校验excel中的作业单元名称列表
        List<String> excelParentTitleList = validateForExcelTitleList(excelImportDataList);

        // 得到excel中在数据库存在的父级作业单元列表
        List<WorkspaceLocation> existParentWorkspaceList = findExistParentWorkspaceList(excelParentTitleList);
        // 得到excel中在数据库不存在的父级作业单元名称列表
        List<String> notExistParentTitleList = getNotExistParentTitleList(existParentWorkspaceList, excelParentTitleList);

        workspaceLocationService.importWorkspaceLocation(excelImportDataList, existParentWorkspaceList, notExistParentTitleList);
    }

    /**
     * 校验excel中的作业单元名称不能为空
     * 校验同一父级下的作业单元名称不能重复
     * 校验作业单元名称列表不能包含父级作业单元名称
     *
     * @param excelImportDataList excel导入结果集
     * @return
     */
    private List<String> validateForExcelTitleList(List<WorkspaceLocationImportExcel> excelImportDataList) {
        // 校验excel中的作业单元名称不能为空
        List<String> excelTitleList = excelImportDataList.stream().map(WorkspaceLocationImportExcel::getTitle).collect(Collectors.toList());
        if (excelTitleList.stream().anyMatch(StringUtils::isBlank)) {
            throw new ServiceException(WorkspaceLocationErrorCode.WORKSPACE_LOCATION_TITLE_IS_NOT_BLANK);
        }

        // 得到excel中父级作业单元为空的作业单元名称列表
        List<String> parentTitleIsBlankTitleList = excelImportDataList.stream().filter(item -> StringUtils.isBlank(item.getParentTitle()))
                .map(WorkspaceLocationImportExcel::getTitle).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(parentTitleIsBlankTitleList)) {
            // 校验excel中父级作业单元为空的作业单元名称列表不能有重复值
            long count = parentTitleIsBlankTitleList.stream().distinct().count();
            if (count != parentTitleIsBlankTitleList.size()) {
                throw new ServiceException(WorkspaceLocationErrorCode.NOT_HAVE_PARENT_CAN_NOT_TITLE_EQUALS);
            }
            // 校验excel中父级作业单元为空的作业单元名称列表不能有在数据库中存在的值
            WorkspaceLocationService workspaceLocationService = SpringUtil.getBean(WorkspaceLocationService.class);
            WorkspaceLocation workspaceLocation = workspaceLocationService.findByTitles(parentTitleIsBlankTitleList);
            if (ObjectUtil.isNotNull(workspaceLocation)) {
                throw new ServiceException(WorkspaceLocationErrorCode.PART_NOT_HAVE_PARENT_WORKSPACE_LOCATION_TITLE_IS_EXIST);
            }
        }
        // 得到excel中父级作业单元不为空的作业单元名称+父级作业单元名称列表
        List<String> parentTitleIsNotBlankTitleList = excelImportDataList.stream().filter(importData -> StringUtils.isNotBlank(importData.getParentTitle()))
                .map(WorkspaceLocationImportExcel::getSpliceStr).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(parentTitleIsNotBlankTitleList)) {
            // 校验有父级的作业单元名称在同一父级下不能重复（针对于excel而言）
            long count = parentTitleIsNotBlankTitleList.stream().distinct().count();
            if (count != parentTitleIsNotBlankTitleList.size()) {
                throw new ServiceException(WorkspaceLocationErrorCode.SAME_PARENT_OF_CHILD_TITLE_CAN_NOT_EQUALS);
            }
            // 校验有父级的作业单元名称在同一父级下不能重复（针对于数据库而言）
            WorkspaceLocationMapper workspaceLocationMapper = SpringUtil.getBean(WorkspaceLocationMapper.class);
            WorkspaceExportResponse workspaceExportResponse = workspaceLocationMapper.findByTitles(parentTitleIsNotBlankTitleList);
            if (ObjectUtil.isNotNull(workspaceExportResponse)) {
                throw new ServiceException(WorkspaceLocationErrorCode.PART_WORKSPACE_LOCATION_TITLE_IS_EXIST_IN_PARENT_DOWN);
            }
        }

        // 得到excel中去空、去重后的父级作业单元名称列表
        List<String> excelParentTitleList = excelImportDataList.stream().map(WorkspaceLocationImportExcel::getParentTitle)
                .filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        // 校验excel中作业单元名称列表不能包含父级作业单元名称
        if (excelParentTitleList.stream().anyMatch(excelTitleList::contains)) {
            throw new ServiceException(WorkspaceLocationErrorCode.WORKSPACE_LOCATION_TITLE_LIST_CAN_NOT_CONTAIN_PARENT_TITLE);
        }
        return excelParentTitleList;
    }

    /**
     * 得到excel中在数据库存在的父级作业单元列表
     *
     * @param excelParentTitleList excel中的父级作业单元名称列表
     * @return
     */
    private List<WorkspaceLocation> findExistParentWorkspaceList(List<String> excelParentTitleList) {
        if (CollectionUtil.isEmpty(excelParentTitleList)) {
            return Collections.emptyList();
        }
        return SpringUtil.getBean(WorkspaceLocationMapper.class).findWorkspaceList(null, excelParentTitleList);
    }

    /**
     * 得到excel中在数据库不存在的父级作业单元名称列表
     *
     * @param existParentWorkspaceList excel中在数据库存在的父级作业单元列表
     * @param excelParentTitleList     excel中的父级作业单元名称列表
     * @return
     */
    private List<String> getNotExistParentTitleList(List<WorkspaceLocation> existParentWorkspaceList, List<String> excelParentTitleList) {
        // existParentWorkspaceList为空、excelParentTitleList为空：父级作业单元名称列表为空
        if (CollectionUtil.isEmpty(existParentWorkspaceList) && CollectionUtil.isEmpty(excelParentTitleList)) {
            return Collections.emptyList();
        }
        // existParentWorkspaceList不为空、excelParentTitleList为空：此种情况不存在
        // existParentWorkspaceList为空、excelParentTitleLis不为空：父级作业单元名称列表的名称都在数据库不存在
        if (CollectionUtil.isEmpty(existParentWorkspaceList) && CollectionUtil.isNotEmpty(excelParentTitleList)) {
            return excelParentTitleList;
        }
        // existParentWorkspaceList不为空、excelParentTitleLis不为空：父级作业单元名称列表的名称部分在数据库存在、部分在数据库不存在
        List<String> existParentTitleList = existParentWorkspaceList.stream().map(WorkspaceLocation::getTitle).collect(Collectors.toList());
        return excelParentTitleList.stream().filter(parentTitle -> !existParentTitleList.contains(parentTitle)).collect(Collectors.toList());
    }

}
