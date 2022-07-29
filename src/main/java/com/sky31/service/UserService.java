package com.sky31.service;

import com.sky31.domain.User;
import org.springframework.stereotype.Service;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:41
 */
@Service
public interface UserService { User login(User user);
}
