package com.framework.center.domain.sync.convert;

import com.framework.center.domain.sync.SyncRawDeptData;
import com.framework.center.domain.sync.request.DeptContentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 部门转换类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@SuppressWarnings("SpellCheckingInspection")
@Mapper
public interface SyncRawDeptDataConvertor {
    SyncRawDeptDataConvertor INSTANCE = Mappers.getMapper(SyncRawDeptDataConvertor.class);


    List<SyncRawDeptData> requestList2List(List<DeptContentRequest> requestList);

    @Mapping(target = "departmentGuid", source = "departmentguid")
    @Mapping(target = "departmentId", source = "departmentid")
    @Mapping(target = "departmentName", source = "departmentname")
    @Mapping(target = "parentUnitGuid", source = "parentunitguid")
    @Mapping(target = "parentUnitId", source = "parentunitid")
    @Mapping(target = "parentUnitName", source = "parentunitname")
    @Mapping(target = "departmentGrade", source = "departmentgrade")
    @Mapping(target = "departmentLevel", source = "departmentlevel")
    @Mapping(target = "companyCode", source = "companycode")
    @Mapping(target = "companyName", source = "companyname")
    @Mapping(target = "leader", source = "leader")
    @Mapping(target = "deptStatus", source = "deptstatus")
    @Mapping(target = "insitution", source = "insitution")
    @Mapping(target = "isCompany", source = "iscompany")
    @Mapping(target = "operation", source = "operation")
    @Mapping(target = "secretKey", source = "key")
    @Mapping(target = "placeholder1", source = "placeholder1")
    @Mapping(target = "placeholder2", source = "placeholder2")
    @Mapping(target = "placeholder3", source = "placeholder3")
    @Mapping(target = "placeholder4", source = "placeholder4")
    @Mapping(target = "placeholder5", source = "placeholder5")
    @Mapping(target = "placeholder6", source = "placeholder6")
    @Mapping(target = "placeholder7", source = "placeholder7")
    @Mapping(target = "placeholder8", source = "placeholder8")
    @Mapping(target = "placeholder9", source = "placeholder9")
    @Mapping(target = "placeholder10", source = "placeholder10")
    SyncRawDeptData request2Entity(DeptContentRequest request);


}

