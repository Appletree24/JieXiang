package com.sky31.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sky31.domain.LoginTicket;
import com.sky31.mapper.LoginTicketMapper;
import com.sky31.service.LoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 9:22
 */
@Service
public class LoginTicketServiceImpl  implements  LoginTicketService{

    @Autowired
    LoginTicketMapper loginTicketMapper;



    public int insertLoginTicket(LoginTicket loginTicket) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(loginTicket.getUserId());
        ticket.setExpired(loginTicket.getExpired());
        ticket.setTicket(loginTicket.getTicket());
        ticket.setStatus(loginTicket.getStatus());
        return loginTicketMapper.insert(ticket);
    }



    public LoginTicket selectByTicket(String ticket) {
        QueryWrapper<LoginTicket> loginTicketQueryWrapper = new QueryWrapper<>();
        QueryWrapper<LoginTicket> wrapper = loginTicketQueryWrapper.eq("ticket", ticket);
        return loginTicketMapper.selectOne(wrapper);
    }


    public int updateStatus(String ticket, int status) {
        UpdateWrapper<LoginTicket> loginTicketUpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper<LoginTicket> wrapper = loginTicketUpdateWrapper.eq("ticket", ticket)
                .set("status", status);
        return loginTicketMapper.update(null, wrapper);
    }
}
