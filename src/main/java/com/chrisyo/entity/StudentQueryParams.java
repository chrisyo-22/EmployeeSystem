package com.chrisyo.entity;

import lombok.Data;

@Data
public class StudentQueryParams {
    private String name;
    private Integer degree;
    private Integer classId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
