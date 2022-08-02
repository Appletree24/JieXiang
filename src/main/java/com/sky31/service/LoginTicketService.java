package com.sky31.service;

import com.sky31.domain.LoginTicket;
import org.springframework.stereotype.Service;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 9:21
 */
@Service
public interface LoginTicketService {
    int insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    int updateStatus(String ticket,int status);
}
