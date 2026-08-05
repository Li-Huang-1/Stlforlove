package org.example.myblog.domain.vo;

import lombok.Data;

@Data
public class UserInfoVO {

    private Integer id;

    private String token;

    private String username;

    private String avatar;
}
