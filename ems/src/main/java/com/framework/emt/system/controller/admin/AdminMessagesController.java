package com.framework.emt.system.controller.admin;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.R;
import com.framework.emt.system.domain.messages.request.*;
import com.framework.emt.system.domain.messages.response.MessageResponse;
import com.framework.emt.system.domain.messages.response.MessageSearchResponse;
import com.framework.emt.system.domain.messages.response.MessageTemplateResponse;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.messages.service.IMessageTemplateService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = {"管理平台-消息管理"})
@ApiSupport(order = 29)
public class AdminMessagesController extends BaseController {

    private final IMessageService messageService;

    private final IMessageTemplateService messageTemplateService;

    @PostMapping("/admin/messages/template/save")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "保存消息模板")
    public R<Boolean> save(@Validated @RequestBody MessageTemplateRequest request) {
        return R.data(messageTemplateService.save(request));
    }

    @PostMapping("/admin/messages/template/delete")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "删除消息模板")
    public R<Boolean> delete(@Validated @RequestBody MessageTemplateDeleteRequest request) {
        List<Long> list = request.getMessageTemplateIdList();
        String ids = list.stream().map(Convert::toStr).collect(Collectors.joining(","));
        return R.data(messageTemplateService.deleteByIds(ids));
    }

    @GetMapping("/admin/messages/template/enable/{id}")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "启用消息模板")
    public R<Boolean> enable(@PathVariable Long id) {
        return R.data(messageTemplateService.enable(id));
    }

    @GetMapping("/admin/messages/template/disable/{id}")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "停用消息模板")
    public R<Boolean> disable(@PathVariable Long id) {
        return R.data(messageTemplateService.disable(id));
    }

    @GetMapping("/admin/messages/template/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "查询消息模板")
    public R<IPage<MessageTemplateResponse>> page(@Valid MessageTemplateQueryRequest request) {
        return R.data(messageTemplateService.page(request));
    }

    @PostMapping("/admin/messages/send")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "发送消息")
    public R<Boolean> send(@Validated @RequestBody MessageCreateRequest request) {
        return R.data(messageService.send(request));
    }

    @PostMapping("/admin/messages/cancel")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "挂起消息")
    public R<Boolean> cancel(@Validated @RequestBody MessageCancelRequest request) {
        messageService.cancel(request);
       return  R.status(true);
    }

    @GetMapping("/admin/messages/list")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询消息发送情况")
    public R<MessageSearchResponse> page(@Valid MessageQueryRequest request) {
        MessageSearchResponse response = new MessageSearchResponse();
        response.setPage(messageService.page(request));
        response.setUnreadQuantity(messageService.getUnreadQuantity());
        return R.data(response);
    }

    @GetMapping("/admin/messages/read")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "设置消息为已读")
    public R<Boolean> changeStatusToRead(@Valid ChangeStatusRequest request) {
        return R.data(messageService.changeStatusToRead(request.getMessageIdList()));
    }

    @GetMapping("/admin/messages/{id}/detail")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "消息详情")
    public R<MessageResponse> detail(@PathVariable(value = "id") Long id) {
        return R.data(messageService.detail(id));
    }

}
