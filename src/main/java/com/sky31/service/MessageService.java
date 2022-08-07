package com.sky31.service;

import com.sky31.domain.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 21:29
 */
@Service
public interface MessageService {
    List<Message> selectConversations(int userId, int offset, int limit);
    int selectConversationCount(int userId);
    List<Message> selectLetters(String conversationId,int offset,int limit);
    int selectLetterCount(String conversationId);
    int selectLetterUnreadCount(int userId);

    int addMessage(Message message);

    int readMessage(List<Integer> ids);

    Message findLatestNotice(int userId,String topic);

    int findNoticeCount(int userId,String topic);

    int findNoticeUnreadCount(int userId,String topic);

    List<Message> selectNotices(int userId,String topic,int offset,int limit);
}
