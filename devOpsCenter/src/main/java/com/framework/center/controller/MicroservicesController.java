package com.framework.center.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.center.domain.microservices.request.MicroservicesCreateRequest;
import com.framework.center.domain.microservices.request.MicroservicesQueryRequest;
import com.framework.center.domain.microservices.response.MicroservicesVo;
import com.framework.center.domain.microservices.service.IMicroservicesService;
import com.framework.center.infrastructure.common.IdsRequest;
import com.framework.common.api.entity.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "运维中心微服务管理", tags = "运维中心微服务管理")
@ApiSupport(order = 4)
@Slf4j
public class MicroservicesController {
    private final IMicroservicesService microservicesService;

    /**
     * 微服务创建
     */
    @PostMapping("/admin/microservices")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "微服务创建", notes = "微服务创建")
    public R<Long> submit(@Valid @RequestBody MicroservicesCreateRequest param) {
        return R.data(microservicesService.submit(param));
    }

    /**
     * 微服务编辑
     */
    @PostMapping("/admin/microservices/{id}")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "微服务编辑", notes = "微服务编辑")
    public R<Boolean> edit(@PathVariable(value = "id") Long id, @Valid @RequestBody MicroservicesCreateRequest param) {
        return R.status(microservicesService.edit(id, param));
    }

    /**
     * 微服务详情
     */
    @GetMapping("/admin/microservices/{id}/detail")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "微服务详情", notes = "微服务详情")
    public R<MicroservicesVo> edit(@PathVariable(value = "id") Long id) {
        MicroservicesVo res = microservicesService.detail(id);
        return R.data(res);
    }

    /**
     * 批量删除微服务
     *
     * @param idsRequest id 列表
     */
    @DeleteMapping("/admin/microservices/delete")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "批量删除微服务", notes = "批量删除微服务")
    public R<Boolean> delete(@RequestBody @Validated IdsRequest idsRequest) {
        String ids = idsRequest.getIds().stream().map(Convert::toStr).collect(Collectors.joining(StrPool.COMMA));
        Boolean result = microservicesService.deleteByIds(ids);
        return R.status(result);
    }

    /**
     * 微服务列表
     */
    @GetMapping("/admin/microservices/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "微服务列表", notes = "微服务列表")
    public R<IPage<MicroservicesVo>> list(MicroservicesQueryRequest param) {
        IPage<MicroservicesVo> result = microservicesService.queryList(param);
        return R.data(result);
    }

    /**
     * 微服务启动
     */
    @GetMapping("/admin/microservices/start/{id}")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "微服务启动", notes = "微服务启动")
    public R<Boolean> start(@PathVariable Long id) {
        return R.data(microservicesService.start(id));
    }

    /**
     * 微服务停止
     */
    @GetMapping("/admin/microservices/stop/{id}")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "微服务停止", notes = "微服务停止")
    public R<Boolean> stop(@PathVariable Long id) {
        return R.data(microservicesService.stop(id));
    }

    /**
     * 微服务重启
     */
    @GetMapping("/admin/microservices/restart/{id}")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "微服务重启", notes = "微服务重启")
    public R<Boolean> restart(@PathVariable Long id) {
        return R.data(microservicesService.restart(id));
    }

}
