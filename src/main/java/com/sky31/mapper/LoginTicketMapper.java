package com.sky31.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky31.domain.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 9:17
 */
@Mapper
@Repository
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

}
