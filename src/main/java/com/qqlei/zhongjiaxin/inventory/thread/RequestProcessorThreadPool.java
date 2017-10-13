package com.qqlei.zhongjiaxin.inventory.thread;

import com.qqlei.zhongjiaxin.inventory.request.Request;
import com.qqlei.zhongjiaxin.inventory.request.RequestQueue;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 李雷 on 2017/10/12.
 */
public class RequestProcessorThreadPool {

    private ExecutorService threadPoll = Executors.newFixedThreadPool(10);

    /**
     * 十个线程处理任务队列，每个队列的最大容量处理100个请求
     */
    public RequestProcessorThreadPool(){
        RequestQueue requestQueue = RequestQueue.getInstance();
        for (int i =0;i<10;i++){
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            requestQueue.addQueue(queue);
            threadPoll.submit(new RequestProcessorThread(queue));
        }
    }

    private static class Singleton {

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }

    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }



}
