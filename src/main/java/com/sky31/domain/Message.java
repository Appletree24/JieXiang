package com.sky31.domain;

import lombok.Data;

import java.util.Date;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 21:16
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
    private long timeMils;
}
