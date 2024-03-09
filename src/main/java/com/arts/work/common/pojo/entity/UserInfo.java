package com.arts.work.common.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    private String id;
    private String userId;
    private String nickName;
    private String password;
    private String userName;
    private String phone;
    private String email;
    private String idCard;
    private String job;
    private String achievement;
    private String level;
    private Date registerTime;
}
