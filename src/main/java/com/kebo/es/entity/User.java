package com.kebo.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 10:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private Integer age;
    private Float salary;
    private String address;
    private Date createTime;
    private String title;

}