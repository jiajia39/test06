package com.framework.emt.system.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.knowledge.request.KnowledgeBaseAppQueryRequest;
import com.framework.emt.system.domain.knowledge.response.KnowledgeBaseResponse;
import com.framework.emt.system.domain.knowledge.service.KnowledgeBaseService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 知识库 移动端控制层
 *
 * @author ds_C
 * @since 2023-07-14
 */
@RestController
@RequiredArgsConstructor
@Api(tags = {"移动端-知识库"})
@ApiSupport(order = 26)
public class AppKnowledgeBaseController extends BaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @GetMapping("/app/knowledge/base/list")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "知识库分页列表")
    public R<IPage<KnowledgeBaseResponse>> pageApp(@Validated KnowledgeBaseAppQueryRequest request) {
        return R.data(knowledgeBaseService.pageApp(request));
    }

    @GetMapping("/app/knowledge/base/{id}/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "知识库详情")
    public R<KnowledgeBaseResponse> detailApp(@PathVariable(value = "id") Long id) {
        return R.data(knowledgeBaseService.detailApp(id));
    }

}
