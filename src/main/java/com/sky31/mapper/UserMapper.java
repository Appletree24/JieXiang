package com.sky31.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky31.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:50
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
