package com.sky31;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 15:27
 */
public class BlockingQueueTests {

    public static void main(String[] args){
        BlockingQueue queue=new ArrayBlockingQueue(10);
        new Thread(new Producer(queue)).start();
        new Thread(new consumer(queue)).start();
        new Thread(new consumer(queue)).start();
        new Thread(new consumer(queue)).start();
    }
}

class Producer implements Runnable{

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        try {
            for (int i=0;i<100;i++){
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName()+"生产："+queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

class consumer implements  Runnable{

    private BlockingQueue<Integer> queue;

    public consumer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(new Random().nextInt(1000));
                queue.take();
                System.out.println(Thread.currentThread().getName()+"消费了"+queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
