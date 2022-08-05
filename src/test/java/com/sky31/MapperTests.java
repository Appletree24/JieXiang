package com.sky31;

import com.sky31.domain.DiscussPost;
import com.sky31.domain.LoginTicket;
import com.sky31.domain.Message;
import com.sky31.mapper.DiscussPostMapper;
import com.sky31.mapper.LoginTicketMapper;
import com.sky31.mapper.MessageMapper;
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
    MessageMapper messageMapper;

    @Test
    public void testSelectLetters(){
        List<Message> messages = messageMapper.selectConversations(111,  0, 20);
        messages.forEach(System.out::println);
    }
}
