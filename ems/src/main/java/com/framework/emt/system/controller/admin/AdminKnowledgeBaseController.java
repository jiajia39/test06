package com.framework.emt.system.controller.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.cache.ParamCache;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.common.excel.util.ExcelUtil;
import com.framework.emt.system.domain.knowledge.request.*;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseExportResponse;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseResponse;
import com.framework.emt.system.domain.knowledge.service.KnowledgeBaseImportServiceImpl;
import com.framework.emt.system.domain.knowledge.service.KnowledgeBaseService;
import com.framework.emt.system.infrastructure.common.request.ChangeEnableFlagRequest;
import com.framework.emt.system.infrastructure.common.request.IdListRequest;
import com.framework.emt.system.infrastructure.common.request.IdRequest;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 知识库 管理平台控制层
 *
 * @author ds_C
 * @since 2023-07-14
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-知识库"})
@ApiSupport(order = 26)
public class AdminKnowledgeBaseController extends BaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @PostMapping("/admin/knowledge/base")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "知识库创建")
    public R<Long> create(@Validated @RequestBody KnowledgeBaseCreateRequest request) {
        return R.data(knowledgeBaseService.create(request.init()));
    }

    @PostMapping("/admin/knowledge/base/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "知识库删除")
    public R<Boolean> delete(@Validated @RequestBody IdRequest request) {
        knowledgeBaseService.delete(request.getId());
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/delete/batch")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "知识库批量删除")
    public R<Boolean> deleteBatch(@Validated @RequestBody IdListRequest request) {
        knowledgeBaseService.deleteBatch(request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "知识库更新")
    public R<Long> update(@PathVariable(value = "id") Long id, @Validated @RequestBody KnowledgeBaseUpdateRequest request) {
        return R.data(knowledgeBaseService.update(id, request.init()));
    }

    @GetMapping("/admin/knowledge/base/{id}/detail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "知识库详情")
    public R<KnowledgeBaseResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(knowledgeBaseService.detail(id));
    }

    @GetMapping("/admin/knowledge/base/list")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "知识库分页列表")
    public R<IPage<KnowledgeBaseResponse>> page(@Validated KnowledgeBaseQueryRequest request) {
        return R.data(knowledgeBaseService.page(request));
    }

    @PostMapping("/admin/knowledge/base/enableFlag")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "知识库禁用启用")
    public R<Boolean> changeEnableFlag(@Validated @RequestBody ChangeEnableFlagRequest request) {
        knowledgeBaseService.changeEnableFlag(request.getEnableFlag(), request.getIdList());
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/tag")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "知识库标签新增")
    public R<Long> createTag(@Validated @RequestBody KnowledgeBaseTagCreateRequest request) {
        return R.data(knowledgeBaseService.createTag(request));
    }

    @PostMapping("/admin/knowledge/base/tag/relation")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "知识库标签关系新增")
    public R<Long> createTagRelation(@Validated @RequestBody KnowledgeBaseTagRelationCreateRequest request) {
        return R.data(knowledgeBaseService.createTagRelation(request.init()));
    }

    @PostMapping("/admin/knowledge/base/tag/delete")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "知识库标签删除")
    public R<Boolean> deleteTag(@Validated @RequestBody KnowledgeBaseTagDeleteRequest request) {
        knowledgeBaseService.deleteTag(request);
        return R.status(true);
    }

    @PostMapping("/admin/knowledge/base/import")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "知识库导入")
    public R<Boolean> importExceptionItem(@ApiParam("上传文件") @RequestPart @RequestParam MultipartFile file) {
        KnowledgeBaseImportServiceImpl importKnowledgeBase = new KnowledgeBaseImportServiceImpl(knowledgeBaseService);
        Integer maxImportNum = Convert.toInt(ParamCache.getValue(BusinessConstant.SYSTEM_MAX_IMPORT_NUM, AuthUtil.getTenantId()), BusinessConstant.MAX_IMPORT_NUM);
        ExcelUtil.save(file, importKnowledgeBase, KnowledgeBaseImportExcel.class, BusinessConstant.BATCH_IMPORT_NUM, maxImportNum, true);
        return R.success("操作成功");
    }

    @PostMapping("/admin/knowledge/base/export")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "知识库导出")
    public void export(@Validated @RequestBody KnowledgeBaseExportQueryRequest request, HttpServletResponse response) {
        ExcelUtil.export(response, "知识库" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN),
                "知识库数据表", knowledgeBaseService.export(request), KnowledgeBaseExportResponse.class);
    }

}
