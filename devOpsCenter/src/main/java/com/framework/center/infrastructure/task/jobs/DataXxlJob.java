package com.framework.center.infrastructure.task.jobs;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.framework.center.domain.account.Account;
import com.framework.center.domain.company.Company;
import com.framework.center.domain.company.service.ICompanyService;
import com.framework.center.domain.sync.Dept;
import com.framework.center.domain.sync.service.ISyncService;
import com.framework.center.infrastructure.task.constant.TaskConstant;
import com.framework.core.datasource.response.DatabaseCreateResponse;
import com.framework.core.datasource.service.IDynamicDatasourceService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author yankunwu
 */
@AllArgsConstructor
@Component
@Slf4j
public class DataXxlJob {
    private final ISyncService syncService;
    private IDynamicDatasourceService dynamicDatasourceService;

    private ICompanyService companyService;

    /**
     * 用户数据同步
     */
//    @XxlJob(TaskConstant.USER_DATA_SYNCHRONIZATION)
//    public void userDataSynchronization() {
//        log.info("用户数据同步中........................");
//        syncService.userDataSynchronization();
//        log.info("用户数据同步中结束");
//        Function<Company, Boolean> function= company -> {
//            List<Account> list=syncService.selectUserByCompany(company.getId());
//            log.info("微服务："+company.getMicroservices()+"用户数据共："+list.size());
//            List<Dept> deptList=syncService.selectDeptByAccount(list);
//            log.info("微服务："+company.getMicroservices()+"用户对应的部门数据共："+deptList.size());
//            syncService.syncUser(list,deptList,company.getMicroservices());
//
//            log.info("微服务："+company.getMicroservices()+"同步完成");
//            return true;
//        };
//        syncToCompany(function);
//    }
    /**
     * 组织架构数据同步
     */
//    @XxlJob(TaskConstant.ORGANIZATIONAL_STRUCTURE_DATA_SYNCHRONIZATION)
//    public void organizationalStructureDataSynchronization() {
//        log.info("组织架构数据处理中........................");
//        syncService.organizationalStructureDataSynchronization();
//        log.info("组织架构数据处理结束");
//        log.info("组织架构数据同步中........................");
//        List<Dept> list=syncService.selectDeptAll();
//        Function<Company,Boolean> function= company -> {
//            syncService.syncDept(list,company.getMicroservices());
//            return true;
//        };
//
//        syncToCompany(function);
//        log.info("组织架构数据同步结束");
//    }

//    private void syncToCompany(Function<Company,Boolean> function) {
//        LambdaQueryWrapper<Company> query=new LambdaQueryWrapper<>();
//        query.eq(Company::getIsDeleted,0);
//        List<Company> companyList = companyService.getBaseMapper().selectList(query);
//        if(ObjectUtil.isNotEmpty(companyList)){
//            for (Company company : companyList) {
//                if(companyService.checkParamToCreateDb(company)){
//                    List<DatabaseCreateResponse> result = dynamicDatasourceService.query(company.getMicroservices());
//                    if(ObjectUtil.isNotEmpty(result)){
//                        if(function!=null){
//                            function.apply(company);
//                        }
//                    }
//                }
//            }
//        }
//    }
}
