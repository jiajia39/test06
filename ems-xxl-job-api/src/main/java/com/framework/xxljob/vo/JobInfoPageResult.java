package com.framework.xxljob.vo;

import lombok.Data;

import java.util.List;

public @Data class JobInfoPageResult {
    private int recordsFiltered;
    private List<JobInfoPageItem> data;
    private int recordsTotal;
}