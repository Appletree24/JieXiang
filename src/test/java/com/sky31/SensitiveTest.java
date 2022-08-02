package com.sky31;

import com.sky31.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/1
 * @TIME 12:43
 */
@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Test
    public void testSensitiveFilter() {
        String text = "这里可以 赌 博，可以 嫖 娼，可以 吸 毒，可以 开 票，哈哈";
        String text2 = "新生找我办电话卡，联系QQ：114514";
        String filter = sensitiveFilter.filter(text);
        System.out.println(sensitiveFilter.filter(text2));
        System.out.println(filter);
    }
}
