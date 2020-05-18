package com.da.shuai.shardingspherecase.entity;

import lombok.Data;

import java.io.Serializable;
/**
 * (Student)实体类
 *
 * @author ${USER}
 * @since 2020-05-08 13:30:50
 */

/**
 * (Student)实体类
 *
 * @author makejava
 * @since 2020-05-08 13:30:50
 */
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 751915763046468531L;

    private Integer id;

    private String name;

    private Integer age;

    private String sex;

    private String stuclass;

}