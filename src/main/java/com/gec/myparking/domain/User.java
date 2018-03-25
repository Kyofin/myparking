package com.gec.myparking.domain;

import java.util.Date;

public class User {
    private Integer id;

    private String userName;

    private String password;

    private String salt;

    private String email;

    private Date createTime;

    private Date updateTime;

    private String headUrl;

    private String nickName ;


    public User() {
        super();
    }

    public User(Integer id, String userName, String password, String salt, String email, Date createTime, Date updateTime, String headUrl, String nickName) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.headUrl = headUrl;
        this.nickName = nickName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}