package com.jerry.savior_oauth.pojo.VO;

import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Set;

/**
 * @author 22454
 */
@Data
public class UserVO {
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

    private Set<Integer> permissionSet;

    public static UserVO createUserVO(UserInfoBO userInfoBO) {
        UserVO result = new UserVO();
        BeanUtils.copyProperties(userInfoBO, result);
        return result;
    }
}
