package com.framework.xxljob.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.common.json.utils.JsonUtil;
import com.framework.xxljob.config.XxlJobAdminProperties;
import com.framework.xxljob.dto.HttpHeader;
import com.framework.xxljob.dto.ReturnT;
import com.framework.xxljob.model.JobGroup;
import com.framework.xxljob.model.XxlJobInfo;
import com.framework.xxljob.service.XxlJobService;
import com.framework.xxljob.vo.JobInfoPageItem;
import com.framework.xxljob.vo.JobInfoPageResult;
import com.xxl.job.core.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XxlJobServiceImpl implements XxlJobService {
    private final HttpHeader loginHeader;
    private final XxlJobAdminProperties xxlJobAdminProperties;
    private final int timeout;

    public XxlJobServiceImpl(HttpHeader loginHeader, XxlJobAdminProperties xxlJobAdminProperties) {
        this.loginHeader = loginHeader;
        this.xxlJobAdminProperties = xxlJobAdminProperties;
        this.timeout = this.xxlJobAdminProperties.getConnectionTimeOut();
    }

    @Override
    public XxlJobInfo loadByHandler(int groupId, String executorHandler) {
        return loadByHandler(groupId,executorHandler,"");
    }

    @Override
    public XxlJobInfo loadByHandler(int groupId, String executorHandler,String executorParam) {
        HttpRequest httpRequest = postHttpRequest(jobGetOne);
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("groupId", groupId);
        jsonObject.put("executorHandler", executorHandler);
        jsonObject.put("executorParam", executorParam);
        ReturnT<XxlJobInfo> returnT = requestXxlJobAdmin(httpRequest, jsonObject, new TypeReference<ReturnT<XxlJobInfo>>() {
        });
        if (returnT != null && returnT.getCode() == 200) {
            return returnT.getContent();
        }
        return null;
    }

    @Override
    public JobInfoPageResult pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc,
                                      String executorHandler, String author) {
        HttpRequest httpRequest = this.getHttpRequest(jobPageListPath);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("jobGroup", jobGroup);
        paramMap.put("triggerStatus", triggerStatus);
        paramMap.put("jobDesc", jobDesc);
        paramMap.put("executorHandler", executorHandler);
        paramMap.put("author", author);
        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();
        int status = response.getStatus();
        String body = response.body();
        Assert.isTrue(status == 200);
        return JsonUtil.parse(body, JobInfoPageResult.class);
    }

    @Override
    public String add(XxlJobInfo jobInfo) {
        HttpRequest httpRequest = postHttpRequest(jobAddPath);
        Map<String, Object> jsonObject = JsonUtil.toMap(JsonUtil.toJson(jobInfo));
        ReturnT<String> returnT = requestXxlJobAdmin(httpRequest, jsonObject, new TypeReference<ReturnT<String>>() {
        });
        return returnT.getContent();
    }

    @Override
    public void update(XxlJobInfo jobInfo) {
        HttpRequest httpRequest = postHttpRequest(jobUpdatePath);
        Map<String, Object> jsonObject = JsonUtil.toMap(JsonUtil.toJson(jobInfo));
        requestXxlJobAdmin(httpRequest, jsonObject, new TypeReference<ReturnT<String>>() {
        });
    }

    @Override
    public void remove(int id) {
        HttpRequest httpRequest = postHttpRequest(jobDeletePath);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        requestXxlJobAdmin(httpRequest, map, new TypeReference<ReturnT<String>>() {
        });
    }

    @Override
    public void remove(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        JobInfoPageResult jobInfoPageResult = this.pageList(
                0, 1, jobGroup, triggerStatus, jobDesc, executorHandler, author);
        List<JobInfoPageItem> data = jobInfoPageResult.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        for (JobInfoPageItem item : data) {
            this.remove(item.getId());
        }
    }

    @Override
    public void removeAll(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        JobInfoPageResult jobInfoPageResult = this.pageList(
                0, 10, jobGroup, triggerStatus, jobDesc, executorHandler, author);
        List<JobInfoPageItem> data = jobInfoPageResult.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        for (JobInfoPageItem item : data) {
            this.remove(item.getId());
        }
        int i = 1;
        while (data.size() > 10) {
            data = jobInfoPageResult.getData();
            if (CollUtil.isEmpty(data)) {
                return;
            }
            for (JobInfoPageItem item : data) {
                this.remove(item.getId());
            }
            jobInfoPageResult = this.pageList(i++, 10, jobGroup, triggerStatus, jobDesc, executorHandler, author);
        }
    }

    @Override
    public void cancel(int jobGroup, String jobDesc, String executorHandler, String author) {
        JobInfoPageResult jobInfoPageResult = this.pageList(0, 1, jobGroup, 1, jobDesc, executorHandler, author);
        List<JobInfoPageItem> data = jobInfoPageResult.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        for (JobInfoPageItem item : data) {
            this.stop(item.getId());
        }
    }

    @Override
    public void cancelAll(int jobGroup, String jobDesc, String executorHandler, String author) {
        JobInfoPageResult jobInfoPageResult = this.pageList(0, 10, jobGroup, 1, jobDesc, executorHandler, author);
        List<JobInfoPageItem> data = jobInfoPageResult.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        for (JobInfoPageItem item : data) {
            this.remove(item.getId());
        }
        int i = 1;
        while (data.size() > 10) {
            data = jobInfoPageResult.getData();
            if (CollUtil.isEmpty(data)) {
                return;
            }
            for (JobInfoPageItem item : data) {
                this.remove(item.getId());
            }
            jobInfoPageResult = this.pageList(i++, 10, jobGroup, 1, jobDesc, executorHandler, author);
        }
    }

    @Override
    public void start(int id) {
        HttpRequest httpRequest = postHttpRequest(jobStartPath);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        requestXxlJobAdmin(httpRequest, map, new TypeReference<ReturnT<String>>() {
        });
    }

    @Override
    public void stop(int id) {
        HttpRequest httpRequest = postHttpRequest(jobStopPath);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        requestXxlJobAdmin(httpRequest, map, new TypeReference<ReturnT<String>>() {
        });
    }

    @Override
    public void triggerJob(int id, String executorParam, String addressList) {
        HttpRequest httpRequest = postHttpRequest(jobTriggerPath);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("executorParam", executorParam);
        map.put("addressList", addressList);
        requestXxlJobAdmin(httpRequest, map, new TypeReference<ReturnT<String>>() {
        });
    }

    @Override
    public List<String> nextTriggerTime(String scheduleType, String scheduleConf) {
        HttpRequest httpRequest = getHttpRequest(jobNextTriggerTimePath);
        Map<String, Object> map = new HashMap<>();
        map.put("scheduleType", scheduleType);
        map.put("scheduleConf", scheduleConf);
        ReturnT<List<String>> returnT = requestXxlJobAdmin(httpRequest, map, new TypeReference<ReturnT<List<String>>>() {
        });
        return returnT.getContent();
    }

    private HttpRequest postHttpRequest(String path) {
        String url = xxlJobAdminProperties.getAdminAddresses() + path;
        HttpRequest httpRequest = HttpRequest.post(url);
        httpRequest.header(loginHeader.getHeaderName(), loginHeader.getHeaderValue());
        return httpRequest;
    }

    private HttpRequest getHttpRequest(String path) {
        String url = xxlJobAdminProperties.getAdminAddresses() + path;
        HttpRequest httpRequest = HttpRequest.get(url);
        httpRequest.header(loginHeader.getHeaderName(), loginHeader.getHeaderValue());
        return httpRequest;
    }

    private <T extends ReturnT> T requestXxlJobAdmin(HttpRequest httpRequest, Map<String, Object> paramMap,
                                                     TypeReference<T> type) {
        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();
        int status = response.getStatus();
        String body = response.body();
        if (StrUtil.isNotBlank(body)) {
            T t = JsonUtil.parse(body, type);
            Assert.isTrue(status == 200, t.getMsg());
            return t;
        }
        return null;
    }

    @Override
    public boolean jobGroupSave(final JobGroup jobGroup) {
        HttpRequest httpRequest = postHttpRequest(jobgroupSave);
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("appname", jobGroup.getAppName());
        jsonObject.put("title", jobGroup.getTitle());
        jsonObject.put("addressType", "0");
        jsonObject.put("addressList", "");
        ReturnT<String> returnT = requestXxlJobAdmin(httpRequest, jsonObject, new TypeReference<ReturnT<String>>() {
        });
        if(returnT!=null){
            return returnT.getCode() == 200;
        }
        return false;
    }

    @Override
    public JobGroup jobGroupGetOne(final String appName) {
        HttpRequest httpRequest = postHttpRequest(jobgroupGetOne);
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("appName", appName);
        ReturnT<Map<String, Object>> returnT = requestXxlJobAdmin(
                httpRequest, jsonObject, new TypeReference<ReturnT<Map<String, Object>>>() {
                });
        if (returnT!=null && returnT.getCode() == 200) {
            Map<String, Object> content = returnT.getContent();
            if (content != null && !content.isEmpty()) {
                JobGroup jobGroup = new JobGroup();
                jobGroup.setAddressList(StrUtil.toString(content.get("addressList")));
                jobGroup.setAppName(StrUtil.toString(content.get("appname")));
                String updateTimeStr = StrUtil.toString(content.get("updateTime"));
                Date updateTime = null;
                if (StrUtil.isNotBlank(updateTimeStr)) {
                    updateTime = DateUtil.parse(updateTimeStr.substring(0, 19), "yyyy-MM-dd'T'HH:mm:ss");
                }
                jobGroup.setUpdateTime(updateTime);
                jobGroup.setId((Integer) content.get("id"));
                jobGroup.setTitle(StrUtil.toString(content.get("title")));
                jobGroup.setAddressType((Integer) content.get("addressType"));
                return jobGroup;
            }
        }
        return null;
    }
}
