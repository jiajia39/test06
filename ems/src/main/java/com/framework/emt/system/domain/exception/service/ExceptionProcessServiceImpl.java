package com.framework.emt.system.domain.exception.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionProcessErrorCode;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.exception.convert.ExceptionProcessConvert;
import com.framework.emt.system.domain.exception.mapper.ExceptionProcessMapper;
import com.framework.emt.system.domain.exception.request.*;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.response.ExceptionProcessSubmitResponse;
import com.framework.emt.system.domain.formfield.convert.FormFieldConvert;
import com.framework.emt.system.domain.formfield.request.FormFieldListQueryRequest;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfield.service.FormFieldService;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessService;
import com.framework.emt.system.domain.tag.convert.TagConvert;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.domain.tagexception.request.TagExceptionCreateListRequest;
import com.framework.emt.system.domain.tagexception.service.TagExceptionService;
import com.framework.emt.system.domain.user.code.UserErrorCode;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.common.request.ExtendFieldRequest;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import com.framework.emt.system.infrastructure.constant.code.BusinessErrorCode;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 异常流程 实现类
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Service
@RequiredArgsConstructor
public class ExceptionProcessServiceImpl extends BaseServiceImpl<ExceptionProcessMapper, ExceptionProcess> implements ExceptionProcessService {

    private final ExceptionCategoryService exceptionCategoryService;

    private final ReportNoticeProcessService reportNoticeProcessService;

    private final TagExceptionService tagExceptionService;

    private final TagService tagService;

    private final UserService userService;

    private final FormFieldService formFieldService;

    @Override
    @DSTransactional
    public Long create(ExceptionProcessCreateRequest request) {
        // 校验异常原因项和附加字段是否超过最大数量
        checkIsMoreThanMaxNumber(request.getExceptionReasonItemIds(), request.getSubmitExtendFieldList(),
                request.getResponseExtendFieldList(), request.getHandingExtendFieldList(), request.getPendingExtendFieldList(),
                request.getCooperateExtendFieldList(), request.getCheckExtendFieldList());
        // 校验异常分类必须存在、且必须是异常项下的异常分类
        exceptionCategoryService.validateItemCategory(request.getExceptionCategoryId());
        // 校验名称必须唯一
        checkTitleUnique(null, request.getTitle());
        // 校验人员的数量必须和枚举相匹配
        checkUserNumByModeCode(request.exceptionProcessModes(), request.exceptionProcessUserIds());
        // 校验响应、处理、验收人必须都存在
        checkUserExist(request.getResponseUserIds(), request.getHandingUserIds(), request.getCheckUserIds());
        // 校验响应超时上报流程和处理超时上报流程必须都存在
        reportNoticeProcessService.checkExist(request.getResponseReportNoticeProcessId(), request.getHandingReportNoticeProcessId());

        // 得到所有附加字段id对应的附加字段对象列表
        Map<Long, FormFieldResponse> extendFieldMap = findExtendFieldMap(request.extendFieldIdList());

        // 新增异常流程
        Long exceptionProcessId = create(ExceptionProcessConvert.INSTANCE.createRequestToEntity(request, extendFieldMap));
        // 新增异常流程下的异常原因项标签
        Integer sourceTypeCode = SourceTypeEnum.ABNORMAL_PROCESS.getCode();
        tagExceptionService.createList(new TagExceptionCreateListRequest(request.getExceptionReasonItemIds(), exceptionProcessId, sourceTypeCode));

        return exceptionProcessId;
    }

    @Override
    @DSTransactional
    public void delete(Long id) {
        findByIdThrowErr(id);
        // 异常流程下包含未完成的异常提报，则不能删除
        ExceptionTaskSubmitService taskSubmitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
        List<Long> taskIds = taskSubmitService.findById(null, null, null, id);
        if (CollectionUtil.isNotEmpty(taskIds)) {
            ExceptionTaskService taskService = SpringUtil.getBean(ExceptionTaskService.class);
            if (taskService.findFinishCountByIds(taskIds) > 0) {
                throw new ServiceException(ExceptionProcessErrorCode.THIS_PROCESS_CONTAIN_SUBMIT);
            }
        }
        this.deleteById(id);
        // 删除异常流程下的标签
        tagExceptionService.deleteBySourceId(id);
    }

    @Override
    @DSTransactional
    public void deleteBatch(List<Long> ids) {
        // 异常流程是否存在
        long count = this.count(new LambdaQueryWrapper<ExceptionProcess>().in(ExceptionProcess::getId, ids));
        if (count != ids.size()) {
            throw new ServiceException(BusinessErrorCode.DELETE_FAIL_NOT_EXIST_DATA_CAN_NOT_DELETE);
        }
        this.removeByIds(ids);
        // 删除异常流程下的标签
        tagExceptionService.deleteBySourceIdList(ids);
    }

    @Override
    public Long update(Long id, ExceptionProcessUpdateRequest request) {
        // 校验此条异常流程必须存在
        ExceptionProcess exceptionProcess = findByIdThrowErr(id);
        // 校验异常原因项和附加字段是否超过最大数量
        checkIsMoreThanMaxNumber(null, request.getSubmitExtendFieldList(),
                request.getResponseExtendFieldList(), request.getHandingExtendFieldList(), request.getPendingExtendFieldList(),
                request.getCooperateExtendFieldList(), request.getCheckExtendFieldList());
        // 校验名称必须唯一
        checkTitleUnique(id, request.getTitle());
        // 校验人员的数量必须和枚举相匹配
        checkUserNumByModeCode(request.exceptionProcessModes(), request.exceptionProcessUserIds());
        // 校验响应、处理、验收人必须都存在
        checkUserExist(request.getResponseUserIds(), request.getHandingUserIds(), request.getCheckUserIds());
        // 校验响应超时上报流程和处理超时上报流程必须都存在
        reportNoticeProcessService.checkExist(request.getResponseReportNoticeProcessId(), request.getHandingReportNoticeProcessId());

        // 得到所有附加字段id对应的附加字段对象列表
        Map<Long, FormFieldResponse> extendFieldMap = findExtendFieldMap(request.extendFieldIdList());

        return this.update(ExceptionProcessConvert.INSTANCE.updateRequestToEntity(exceptionProcess, request, extendFieldMap));
    }

    @Override
    public ExceptionProcessResponse detail(Long id) {
        return this.loadUserInfoList(findResByIdThrowErr(id));
    }

    @Override
    public IPage<ExceptionProcessResponse> page(ExceptionProcessQueryRequest request) {
        return DataHandleUtils.loadUserName(this.baseMapper.page(Condition.getPage(request), request));
    }

    @Override
    public Long createTag(ExceptionProcessTagCreateRequest request) {
        Integer sourceTypeCode = SourceTypeEnum.ABNORMAL_PROCESS.getCode();
        return tagService.create(TagConvert.INSTANCE.paramsToCreateRequest(request.getTagContent(), sourceTypeCode));
    }

    @Override
    public Long createTagRelation(ExceptionProcessTagRelationCreateRequest request) {
        if (ObjectUtil.isNull(request.getTagId()) && StringUtils.isBlank(request.getTagContent())) {
            throw new ServiceException(ExceptionProcessErrorCode.EXCEPTION_PROCESS_TAG_ID_OR_TAG_CONTENT_MUST_HAVE_ONE);
        }
        Integer sourceTypeCode = SourceTypeEnum.ABNORMAL_PROCESS.getCode();
        return tagExceptionService.create(request.getId(), request.getTagId(), request.getTagContent(), sourceTypeCode);
    }

    @Override
    public void deleteTag(ExceptionProcessTagDeleteRequest request) {
        Integer sourceTypeCode = SourceTypeEnum.ABNORMAL_PROCESS.getCode();
        tagExceptionService.deleteTagException(request.getTagId(), request.getId(), sourceTypeCode);
    }

    @Override
    public ExceptionProcessSubmitResponse detailSubmit(Long id) {
        return this.loadUserInfoList(this.findSubmitByIdThrowErr(id));
    }

    @Override
    public ExceptionProcessResponse findResByIdThrowErr(Long id) {
        return Optional.ofNullable(this.baseMapper.detail(id)).orElseThrow(() -> new ServiceException(ExceptionProcessErrorCode.NOT_FOUND));
    }

    @Override
    public ExceptionProcessSubmitResponse findSubmitByIdThrowErr(Long id) {
        return Optional.ofNullable(this.baseMapper.detailSubmit(id)).orElseThrow(() -> new ServiceException(ExceptionProcessErrorCode.NOT_FOUND));
    }

    @Override
    public ExceptionProcess findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, ExceptionProcessErrorCode.NOT_FOUND);
    }

    @Override
    public ExceptionProcess findReportProcessByIdThrowErr(Long id) {
        ExceptionProcess exceptionProcess = this.getOne(new LambdaQueryWrapper<ExceptionProcess>()
                .select(ExceptionProcess::getResponseReportNoticeProcessId, ExceptionProcess::getHandingReportNoticeProcessId)
                .eq(ExceptionProcess::getId, id));
        return Optional.ofNullable(exceptionProcess).orElseThrow(() -> new ServiceException(ExceptionProcessErrorCode.NOT_FOUND));
    }

    @Override
    public ExceptionProcessResponse validateResponseUser(@NonNull Long exceptionProcessId, Long responseUserId) {
        ExceptionProcessResponse exceptionProcessResponse = findResByIdThrowErr(exceptionProcessId);
        if (responseUserId != null) {
            List<Long> responseUserIds = exceptionProcessResponse.getResponseUserIds();
            switch (exceptionProcessResponse.getResponseMode()) {
                case NOT_SPECIFIED:
                    userService.findUserByIdThrowErr(responseUserId, ExceptionProcessErrorCode.RESPONSE_USER_NOT_FOUND);
                    break;
                case FIXED_PERSONNEL:
                    if (!responseUserIds.contains(responseUserId)) {
                        throw new ServiceException(ExceptionProcessErrorCode.RESPONSE_USER_NOT_SAME);
                    }
                    break;
                case MULTIPLE_PERSONNEL:
                    if (!responseUserIds.contains(responseUserId)) {
                        throw new ServiceException(ExceptionProcessErrorCode.RESPONSE_USER_NOT_IN_LIST);
                    }
                    break;
                default:
                    break;
            }
        }
        return exceptionProcessResponse;
    }

    @Override
    public Integer findByFormField(List<Long> formFieldIds) {
        LambdaQueryWrapper<ExceptionProcess> query = new LambdaQueryWrapper<>();
        formFieldIds.forEach(formFieldId -> {
            query.or(qu -> {
                qu.like(ExceptionProcess::getSubmitExtendField, formFieldId);
                qu.or();
                qu.like(ExceptionProcess::getResponseExtendField, formFieldId);
                qu.or();
                qu.like(ExceptionProcess::getHandingExtendField, formFieldId);
                qu.or();
                qu.like(ExceptionProcess::getPendingExtendField, formFieldId);
                qu.or();
                qu.like(ExceptionProcess::getCooperateExtendField, formFieldId);
                qu.or();
                qu.like(ExceptionProcess::getCheckExtendField, formFieldId);
            });
        });
        return Convert.toInt(this.count(query));
    }

    /**
     * 装载异常流程详情对应的用户信息列表
     *
     * @param detailResult
     * @return
     */
    private ExceptionProcessResponse loadUserInfoList(ExceptionProcessResponse detailResult) {
        Map<Long, String> userInfoMap = userService.findIdOfNameMap(detailResult.getUserIds());
        return ExceptionProcessConvert.INSTANCE.mapValueToResponse(detailResult, userInfoMap);
    }

    /**
     * 装载异常流程响应数据对应的响应人信息列表
     *
     * @param submitResult
     * @return
     */
    private ExceptionProcessSubmitResponse loadUserInfoList(ExceptionProcessSubmitResponse submitResult) {
        Map<Long, String> userInfoMap = userService.findIdOfNameMap(submitResult.getResponseUserIds());
        return ExceptionProcessConvert.INSTANCE.mapValueToResponse(submitResult, userInfoMap);
    }

    /**
     * 通过附加字段id列表查询到对应的附加字段对象列表map列表
     *
     * @param extendFieldIds 异常流程附加字段id列表
     * @return key为附加字段id，value为附加字段对象的map列表
     */
    private Map<Long, FormFieldResponse> findExtendFieldMap(List<Long> extendFieldIds) {
        if (CollectionUtil.isEmpty(extendFieldIds)) {
            return Collections.emptyMap();
        }
        FormFieldListQueryRequest queryRequest = FormFieldConvert.INSTANCE.paramsToQueryRequest(extendFieldIds, Boolean.FALSE);
        return formFieldService.list(queryRequest).stream().collect(Collectors.toMap(FormFieldResponse::getId, Function.identity()));
    }

    /**
     * 根据响应、处理、验收模式，校验响应、处理、验收人数量
     *
     * @param exceptionProcessModes   响应、处理、验收模式编码列表
     * @param exceptionProcessUserIds 响应、处理、验收人id列表
     */
    private void checkUserNumByModeCode(List<Integer> exceptionProcessModes, List<List<Long>> exceptionProcessUserIds) {
        ResponseModeEnum.checkUserNumByModeCode(exceptionProcessModes.get(NumberUtils.INTEGER_ZERO), exceptionProcessUserIds.get(NumberUtils.INTEGER_ZERO).size());
        HandingModeEnum.checkUserNumByModeCode(exceptionProcessModes.get(NumberUtils.INTEGER_ONE), exceptionProcessUserIds.get(NumberUtils.INTEGER_ONE).size());
        CheckModeEnum.checkUserNumByModeCode(exceptionProcessModes.get(NumberUtils.INTEGER_TWO), exceptionProcessUserIds.get(NumberUtils.INTEGER_TWO).size());
    }

    /**
     * 校验异常流程名称在数据库中不能存在
     *
     * @param id    主键id
     * @param title 异常流程名称
     */
    private void checkTitleUnique(Long id, String title) {
        LambdaQueryWrapper<ExceptionProcess> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.ne(ExceptionProcess::getId, id);
        }
        queryWrapper.eq(ExceptionProcess::getTitle, title);
        if (this.count(queryWrapper) > NumberUtils.LONG_ZERO) {
            throw new ServiceException(ExceptionProcessErrorCode.EXCEPTION_PROCESS_TITLE_IS_EXIST);
        }
    }

    /**
     * 校验所有响应、处理、验收人不为空的情况下必须存在
     *
     * @param responseUserIds 响应人id列表
     * @param handingUserIds  处理人id列表
     * @param checkUserIds    验收人id列表
     */
    private void checkUserExist(List<Long> responseUserIds, List<Long> handingUserIds, List<Long> checkUserIds) {
        List<Long> distinctUserIds = DataHandleUtils.mergeElements(responseUserIds, handingUserIds, checkUserIds);
        if (CollectionUtil.isEmpty(distinctUserIds)) {
            return;
        }
        List<Long> dataBaseUserIds = userService.findExistIdList(distinctUserIds);
        if (CollectionUtil.isNotEmpty(responseUserIds)) {
            Integer responseUserMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_RESPONSE_USER_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_RESPONSE_USER_DEFAULT_MAX_COUNT);
            if (responseUserIds.size() > responseUserMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.RESPONDENT_HAS_EXCEEDED_THE_MAXIMUM_VALUE);
            }
            if (!responseUserIds.stream().allMatch(dataBaseUserIds::contains)) {
                throw new ServiceException(UserErrorCode.RESPONSE_USER_LIST_CONTAIN_NOT_EXIST_RESPONSE_USER);
            }
        }
        if (CollectionUtil.isNotEmpty(handingUserIds)) {
            Integer handingUserMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_HANDING_USER_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_HANDING_USER_DEFAULT_MAX_COUNT);
            if (handingUserIds.size() > handingUserMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_HANDLER_HAS_EXCEEDED_THE_MAXIMUM_VALUE);
            }
            if (!handingUserIds.stream().allMatch(dataBaseUserIds::contains)) {
                throw new ServiceException(UserErrorCode.HANDING_USER_LIST_CONTAIN_NOT_EXIST_HANDING_USER);
            }
        }
        if (CollectionUtil.isNotEmpty(checkUserIds)) {
            Integer checkUserMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_CHECK_USER_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_CHECK_USER_DEFAULT_MAX_COUNT);
            if (checkUserIds.size() > checkUserMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ACCEPTANCE_PERSON_HAS_EXCEEDED_THE_MAXIMUM_VALUE);
            }
            if (!checkUserIds.stream().allMatch(dataBaseUserIds::contains)) {
                throw new ServiceException(UserErrorCode.CHECK_USER_LIST_CONTAIN_NOT_EXIST_CHECK_USER);
            }
        }
    }

    /**
     * 验证异常原因项和附加字段是否超过最大次数
     *
     * @param exceptionReasonItemIds   异常原因项
     * @param submitExtendFieldList    提报附加字段
     * @param responseExtendFieldList  响应附加字段
     * @param handingExtendFieldList   处理附加字段
     * @param pendingExtendFieldList   挂起附加字段
     * @param cooperateExtendFieldList 协同附加字段
     * @param checkExtendFieldList     验收附加字段
     */
    public void checkIsMoreThanMaxNumber(List<Long> exceptionReasonItemIds, List<ExtendFieldRequest> submitExtendFieldList,
                                         List<ExtendFieldRequest> responseExtendFieldList, List<ExtendFieldRequest> handingExtendFieldList,
                                         List<ExtendFieldRequest> pendingExtendFieldList, List<ExtendFieldRequest> cooperateExtendFieldList,
                                         List<ExtendFieldRequest> checkExtendFieldList) {
        if (CollectionUtil.isNotEmpty(exceptionReasonItemIds)) {
            Integer itemMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_REASON_ITEMS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.SETTING_PROCESS_REASON_ITEMS_DEFAULT_MAX_COUNT);
            if (exceptionReasonItemIds.size() > itemMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.EXCEPTION_REASON_ITEM_HAS_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(submitExtendFieldList)) {
            Integer submitMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_SUBMIT_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.PROCESS_SETTING_SUBMIT_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (submitExtendFieldList.size() > submitMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_FIELDS_REPORTED_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(responseExtendFieldList)) {
            Integer responseMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_RESPONSE_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.PROCESS_SETTING_RESPONSE_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (responseExtendFieldList.size() > responseMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_RESPONSE_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(handingExtendFieldList)) {
            Integer handingMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_HANDING_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.PROCESS_SETTING_HANDING_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (handingExtendFieldList.size() > handingMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_HANDING_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(pendingExtendFieldList)) {
            Integer pendingMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_PENDING_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.PROCESS_SETTING_PENDING_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (pendingExtendFieldList.size() > pendingMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_SUSPEND_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(cooperateExtendFieldList)) {
            Integer cooperateMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_COOPERATION_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.EXCEPTION_PROCESS_SETTING_COOPERATION_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (cooperateExtendFieldList.size() > cooperateMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_COOPERATION_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
        if (CollectionUtil.isNotEmpty(checkExtendFieldList)) {
            Integer checkMaxCount = Convert.toInt(ParamCache.getValue(ExceptionTaskConstant.PROCESS_SETTING_CHECK_EXTEND_FIELDS_MAX_COUNT,
                    AuthUtil.getTenantId()), ExceptionTaskConstant.PROCESS_SETTING_CHECK_EXTEND_FIELDS_DEFAULT_MAX_COUNT);
            if (checkExtendFieldList.size() > checkMaxCount) {
                throw new ServiceException(ExceptionProcessErrorCode.THE_ADDITIONAL_CHECK_HAVE_EXCEEDED_THE_MAXIMUM_VALUE);
            }
        }
    }

}
