package com.qqlei.zhongjiaxin.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 李雷 on 2017/10/12.
 */
public class RequestQueue {

    /**
     * 内存队列
     *
     * ArrayBlockingQueue是数组实现的线程安全的有界的阻塞队列。
        线程安全是指，ArrayBlockingQueue内部通过“互斥锁”保护竞争资源，实现了多线程对竞争资源的互斥访问。
        而有界，则是指ArrayBlockingQueue对应的数组是有界限的。
        阻塞队列，是指多线程访问竞争资源时，当竞争资源已被某线程获取时，其它要获取该资源的线程需要阻塞等待；
        而且，ArrayBlockingQueue是按 FIFO（先进先出）原则对元素进行排序，元素都是从尾部插入到队列，从头部开始返回。
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    /**
     * 标识位map
     */
    private Map<Integer, Boolean> flagMap = new ConcurrentHashMap<>();

    /**
     * 静态内部类
     */
    private static class Singleton {

        private static RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return instance;
        }

    }

    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }


    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize() {
        return queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }

}
