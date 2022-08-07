package com.sky31;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 18:03
 */
@SpringBootTest
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void test(){
        kafkaProducer.sendMessage("test","你好世界");
        kafkaProducer.sendMessage("test","再见");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
class KafkaProducer{
    @Autowired
    KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}

@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handlerMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
