package com.lanyuan.vo;

import java.io.Serializable;

/**
 * Created by liwanzhong on 2016/3/2.
 */
public class CustomVO implements Serializable {



    private Long id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String birthday;
    private Integer body_height;
    private Integer weight;
    private String mobile;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getBody_height() {
        return body_height;
    }

    public void setBody_height(Integer body_height) {
        this.body_height = body_height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
