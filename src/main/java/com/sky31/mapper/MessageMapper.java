package com.sky31.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky31.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 21:22
 */
@Mapper
@Repository
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectConversations(int userId, int offset, int limit);
    int selectConversationCount(int userId);
    List<Message> selectLetters(String conversationId,int offset,int limit);
    int selectLetterCount(String conversationId);
    int selectLetterUnreadCount(int userId);

    int insertMessage(Message message);

    int updateStatus(List<Integer> ids,int status);

    Message selectLatestNotice(int userId,String topic);

    int selectNoticeCount(int userId,String topic);

    int selectNoticeUnreadCount(int userId,String topic);


    List<Message> selectNotices(int userId,String topic,int offset,int limit);




}
