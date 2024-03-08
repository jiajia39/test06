package com.framework.center.domain.microservices.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.service.ICompanyService;
import com.framework.center.domain.microservices.Microservices;
import com.framework.center.domain.microservices.convert.MicroservicesConvertor;
import com.framework.center.domain.microservices.mapper.MicroservicesMapper;
import com.framework.center.domain.microservices.request.MicroservicesCreateRequest;
import com.framework.center.domain.microservices.request.MicroservicesQueryRequest;
import com.framework.center.domain.microservices.response.MicroservicesVo;
import com.framework.center.domain.microservices.service.IMicroservicesService;
import com.framework.center.infrastructure.service.BaseServiceImpl;
import com.framework.center.utils.LinuxScriptUtil;
import com.framework.common.json.utils.JsonUtil;
import com.framework.core.mybatisplus.support.Condition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微服务信息服务实现类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class MicroservicesServiceImpl extends BaseServiceImpl<MicroservicesMapper, Microservices> implements IMicroservicesService {

    private final ICompanyService companyService;
    private final DiscoveryClient discoveryClient;

    @Override
    public Long submit(MicroservicesCreateRequest param) {
        Microservices entity = MicroservicesConvertor.INSTANCE.createRequest2Entity(param);
        return create(entity);
    }

    @Override
    public boolean edit(Long id, MicroservicesCreateRequest param) {
        Microservices entity = MicroservicesConvertor.INSTANCE.updateRequest2Entity(id, param);
        update(entity);
        return true;
    }

    @Override
    public MicroservicesVo detail(Long id) {
        Microservices entity = this.baseMapper.selectById(id);
        MicroservicesVo result = MicroservicesConvertor.INSTANCE.entity2Vo(entity);
        loadDetail(result);
        return result;
    }

    @Override
    public IPage<MicroservicesVo> queryList(MicroservicesQueryRequest param) {
        IPage<Microservices> page = Condition.getPage(param);
        LambdaQueryWrapper<Microservices> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(param.getMicroservices())) {
            query.like(Microservices::getMicroservices, param.getMicroservices());
        }
        if (StrUtil.isNotBlank(param.getName())) {
            query.exists("SELECT id FROM mss_company c where c.id=mss_microservices.company_id and c.is_deleted=0 and c.name like {1} ", "%" + param.getName() + "%");
        }
        query.orderByDesc(Microservices::getUpdateTime);
        page = this.page(page, query);
        IPage<MicroservicesVo> result = MicroservicesConvertor.INSTANCE.pageVo(page);
        loadList(result.getRecords());
        return result;
    }

    public void loadList(List<MicroservicesVo> list) {
        if (ObjectUtil.isNotEmpty(list)) {
            TypeReference<String> type = new TypeReference<String>() {
            };
            companyService.loadData(list, "companyId", Company::getId, "companyName", "name", type);
            companyService.loadData(list, "companyId", Company::getId, "microservices", "microservices", type);
            for (MicroservicesVo microservicesVo : list) {
                if(StrUtil.isNotBlank(microservicesVo.getMicroservices())){
                    setHealth(microservicesVo);
                }
            }
        }
    }

    private void setHealth(MicroservicesVo microservicesVo) {
        List<ServiceInstance> instances = discoveryClient.getInstances(microservicesVo.getMicroservices());
        if (ObjectUtil.isNotEmpty(instances)) {
            List<ServiceInstance> instanceList = instances.stream().filter(i -> StrUtil.equals(i.getHost(), microservicesVo.getMicroservicesHost()) && StrUtil.equals(Convert.toStr(i.getPort()), microservicesVo.getMicroservicesPort())).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(instanceList)) {
                Boolean isHealth = Convert.toBool(instanceList.get(0).getMetadata().get("nacos.healthy"));
                if (isHealth) {
                    microservicesVo.setHealth("正常");
                } else {
                    microservicesVo.setHealth("异常");
                }
            } else {
                microservicesVo.setHealth("未启动");
            }
        }else{
            microservicesVo.setHealth("未启动");
        }
    }

    public void loadDetail(MicroservicesVo res) {
        TypeReference<String> type = new TypeReference<String>() {
        };
        companyService.loadData(ListUtil.toList(res), "companyId", Company::getId, "companyName", "name", type);
        setHealth(res);
    }

    @Override
    public Boolean start(Long id) {
        Microservices entity = this.baseMapper.selectById(id);
        if (ObjectUtil.isNotNull(entity)) {
            if (StrUtil.isNotBlank(entity.getScriptPath()) && StrUtil.isNotBlank(entity.getStartCommand())) {
                List<String> result= exec(entity.getScriptPath(), entity.getScriptFile(), entity.getStartCommand());
                log.info("执行命令结果："+JsonUtil.toJson(result));
                return true;
            }
        }
        return false;
    }

    private static List<String> exec(String scriptPath, String scriptFile, String command) {
        List<String> commandList = new ArrayList<>();
        commandList.add("cd " + scriptPath);
        commandList.add("./" + scriptFile + " " + command);
        return LinuxScriptUtil.executeNewFlow(commandList);
    }

    @Override
    public Boolean stop(Long id) {
        Microservices entity = this.baseMapper.selectById(id);
        if (ObjectUtil.isNotNull(entity)) {
            if (StrUtil.isNotBlank(entity.getScriptPath()) && StrUtil.isNotBlank(entity.getStopCommand())) {
                List<String> result=  exec(entity.getScriptPath(), entity.getScriptFile(), entity.getStopCommand());
                log.info("执行命令结果："+JsonUtil.toJson(result));
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean restart(Long id) {
        Microservices entity = this.baseMapper.selectById(id);
        if (ObjectUtil.isNotNull(entity)) {
            if (StrUtil.isNotBlank(entity.getScriptPath()) && StrUtil.isNotBlank(entity.getRestartCommand())) {
                List<String> result=exec(entity.getScriptPath(), entity.getScriptFile(), entity.getRestartCommand());
                log.info("执行命令结果："+JsonUtil.toJson(result));
                return true;
            }
        }
        return false;
    }

}
