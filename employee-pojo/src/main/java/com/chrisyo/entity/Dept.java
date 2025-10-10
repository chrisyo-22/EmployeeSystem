package com.chrisyo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Press Ctrl+F12 to see list of getters and setters
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dept {
    private int id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
