package com.sky31.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String token;
    private Integer type;
    private Date createTime;
}
