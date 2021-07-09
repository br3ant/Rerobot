package com.bayi.rerobot.communication;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Skygge,  Date on 2019/4/4.
 * dec:命令缓存
 */
public class OrderStorage {

    private static final String TAG = "OrderStorage";

    private static OrderStorage orderStorage;

    // 仓库最大存储量
    private final int MAX_SIZE = 500;

    // 仓库存储的载体
    private LinkedList<Order> list = new LinkedList<Order>();

    // 锁
    private final Lock lock = new ReentrantLock();

    // 仓库满的条件变量
    private final Condition full = lock.newCondition();

    // 仓库空的条件变量
    private final Condition empty = lock.newCondition();

    public static OrderStorage getInstance() {
        return orderStorage = orderStorage == null ? new OrderStorage() : orderStorage;
    }

    public int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public LinkedList<Order> getList() {
        return list;
    }

    //存货
    public void putOrder(Order order) throws InterruptedException {
        // 获得锁
        lock.lock();

        //如果执行等级为低级
        if (list.size() > MAX_SIZE && order.executiveLevel == Order.ExecutiveLevel.LOW) {
            // 唤醒其他所有线程
            full.signalAll();
            empty.signalAll();
            // 释放锁
            lock.unlock();
            return;
        }

        // 如果仓库剩余容量不足
        while (list.size() > MAX_SIZE) {
            Log.d(TAG, "OrderStorage" + "通讯等待中" + "/t【待执行命令数】:" + list.size() + "/没有待发送的指令!");
            // 由于条件不满足，生产阻塞
            full.await();
        }

        switch (order.executiveLevel) {
            case HIGH:
                list.addFirst(order);
                break;
            case MEDIUM:
                list.add(order);
                break;
            case LOW:
                list.addLast(order);
                break;

        }

        Log.i(TAG, "OrderStorage" + "【指令缓存完毕】" + "/t【现待发送指令数为】:" + list.size());

        // 唤醒其他所有线程
        full.signalAll();
        empty.signalAll();
        // 释放锁
        lock.unlock();
    }

    //出货
    public Order takeOrder() throws InterruptedException {
        // 获得锁
        lock.lock();

        // 如果仓库存储量不足
        while (list.isEmpty()) {
            Log.i(TAG, "OrderStorage" + "--【暂无执行命令】--" + "/t【缓存命令数】:" + list.size() + "/t没有待发送的指令!");
            // 由于条件不满足，消费阻塞
            empty.await();
        }

        Order order = list.getFirst();
        list.removeFirst();
        Log.i(TAG, "OrderStorage" + "--【已经执行命令】--" + "/t【现缓存命令数】:" + list.size());
        // 唤醒其他所有线程
        full.signalAll();
        empty.signalAll();
        // 释放锁
        lock.unlock();

        return order;
    }
}
