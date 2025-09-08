package com.chrisyo.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Employee {
    private Integer id; //ID,Primary Key
    private String username; //Username
    private String password; //password
    private String name; //Name
    private Integer gender; //Gender, 1:Male, 2:Female
    private String phone; //Phone
    private Integer job; //Job type, 1:班主任,2:讲师,3:学工主管,4:教研主管,5:咨询师
    private Integer salary; //Salary
    private String image; //Avatar
    private LocalDate entryDate; //Date of entry
    private Integer deptId; //Department Id
    private LocalDateTime createTime; //Create time
    private LocalDateTime updateTime; //Modified/Updated Time

    //封装部门名称数
    private String deptName; //Name of the department belongs to


    //encapsulate job experiences:
    private List<EmpExpr> exprList;

}
