package com.chrisyo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Encapsulate the page result
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
    private Long total;     //Total rows of data
    private List rows;      //Data rows
}

