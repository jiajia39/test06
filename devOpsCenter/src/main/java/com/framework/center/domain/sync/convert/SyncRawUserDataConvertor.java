package com.framework.center.domain.sync.convert;

import com.framework.center.domain.sync.SyncRawUserData;
import com.framework.center.domain.sync.request.UserContentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户转换类
 *
 * @author yankunw
 * @since 2023-04-12
 */
@Mapper
public interface SyncRawUserDataConvertor {
    SyncRawUserDataConvertor INSTANCE = Mappers.getMapper(SyncRawUserDataConvertor.class);

    /**
     * 请求列表转为数据库对象列表
     * @param requestList 请求对象列表
     * @return
     */
    List<SyncRawUserData> requestList2List(List<UserContentRequest> requestList);


    @Mapping(target = "accountStatus", source = "accountstatus")
    @Mapping(target = "birthday", source = "birthday")
    @Mapping(target = "cardNumber", source = "cardnumber")
    @Mapping(target = "cn", source = "cn")
    @Mapping(target = "companyGuid", source = "companyguid")
    @Mapping(target = "companyId", source = "companyid")
    @Mapping(target = "companyName", source = "companyname")
    @Mapping(target = "departmentGuid", source = "departmentguid")
    @Mapping(target = "departmentId", source = "departmentid")
    @Mapping(target = "departmentName", source = "departmentname")
    @Mapping(target = "domicile", source = "domicile")
    @Mapping(target = "education", source = "education")
    @Mapping(target = "employeeStatus", source = "employeestatus")
    @Mapping(target = "enterhtDate", source = "enterhtdate")
    @Mapping(target = "graduatedFrom", source = "graduatedfrom")
    @Mapping(target = "guid", source = "guid")
    @Mapping(target = "highLevel", source = "highlevel")
    @Mapping(target = "htgdMail", source = "htgdmail")
    @Mapping(target = "htGroupMail", source = "htgroupmail")
    @Mapping(target = "identityCard", source = "identitycard")
    @Mapping(target = "jobLevel", source = "joblevel")
    @Mapping(target = "mail", source = "mail")
    @Mapping(target = "major", source = "major")
    @Mapping(target = "maritalStatus", source = "maritalstatus")
    @Mapping(target = "mobile", source = "mobile")
    @Mapping(target = "nativeplace", source = "nativeplace")
    @Mapping(target = "operation", source = "operation")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "permanentAddress", source = "permanentaddress")
    @Mapping(target = "politicalStatus", source = "politicalstatus")
    @Mapping(target = "postGuid", source = "postguid")
    @Mapping(target = "postId", source = "postid")
    @Mapping(target = "postName", source = "postname")
    @Mapping(target = "postSeriesGuid", source = "postseriesguid")
    @Mapping(target = "postSeriesId", source = "postseriesid")
    @Mapping(target = "postSeriesName", source = "postseriesname")
    @Mapping(target = "postTypeGuid", source = "posttypeguid")
    @Mapping(target = "postTypeId", source = "posttypeid")
    @Mapping(target = "postTypeName", source = "posttypename")
    @Mapping(target = "sendOutStaff", source = "sendoutstaff")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "sscn", source = "sscn")
    @Mapping(target = "startPostDate", source = "startpostdate")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "userStatus", source = "userstatus")
    @Mapping(target = "wordtel", source = "wordtel")
    SyncRawUserData request2Entity(UserContentRequest request);
}

