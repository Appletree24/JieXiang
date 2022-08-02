package com.sky31;

import com.sky31.domain.DiscussPost;
import com.sky31.domain.LoginTicket;
import com.sky31.mapper.DiscussPostMapper;
import com.sky31.mapper.LoginTicketMapper;
import com.sky31.service.LoginTicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 9:52
 */
@SpringBootTest
public class MapperTests {
    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void test01() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(114514);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketService.insertLoginTicket(loginTicket);
    }

    @Test
    public void test02(){
        LoginTicket abc = loginTicketService.selectByTicket("abc");
        System.out.println(abc);
    }


    @Test
    public void test03(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        discussPosts.forEach(System.out::println);
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
