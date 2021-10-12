package com.jerry.savior_oauth.pojo.BO;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author 22454
 */
@Data
public class UserInfoBO {
    private Long id;

    private String nickname;

    private String phone;

    private String status;

    private Integer nameVerified;

    private String email;

    private String realName;

    private String studentId;

    private String avatar;

    private String gender;

    private Date birthday;

    private String city;

    private String motto;

    private Integer exp;

    private Date createTime;

    private Set<Long> permissionSet;

}
