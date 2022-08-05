package com.sky31.domain;

import lombok.Data;

import java.util.Date;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 18:23
 */
@Data
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
    private long milsTime;
}
