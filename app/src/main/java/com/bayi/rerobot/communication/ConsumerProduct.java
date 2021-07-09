package com.bayi.rerobot.communication;

import android.util.Log;


/**
 * Created by Skygge,  Date on 2019/4/4.
 * dec:消费者
 */
public class ConsumerProduct extends Thread {

    private static final String TAG = ConsumerProduct.class.getSimpleName();
    private OrderStorage orderStorage;
    private ConsumptionOrderInterface Serial;
    private static ConsumerProduct consumerProduct;

    public static ConsumerProduct getInstance(ConsumptionOrderInterface usbHostSerialImpl, OrderStorage orderStorage) {
        return consumerProduct = consumerProduct == null ? new ConsumerProduct(usbHostSerialImpl, orderStorage) : consumerProduct;
    }

    public ConsumerProduct(ConsumptionOrderInterface Serial, OrderStorage orderStorage) {
        this.orderStorage = orderStorage;
        this.Serial = Serial;
    }

    /**
     * 线程run函数
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Order order = orderStorage.takeOrder();
                Serial.runOrder(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "ConsumerProduct" + "命令消费环节出错！");
            }
        }
    }
}
