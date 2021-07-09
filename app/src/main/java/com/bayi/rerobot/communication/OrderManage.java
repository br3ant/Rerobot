package com.bayi.rerobot.communication;


import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.bean.IsPw;
import com.bayi.rerobot.util.Command;
import com.bayi.rerobot.util.SpHelperUtil;

import org.greenrobot.eventbus.EventBus;

public class OrderManage {
    private static final String TAG = "OrderManage";

    private OrderStorage orderStorage;
    private static OrderManage orderManage;

    //命令缓存池
    public OrderManage() {
        this.orderStorage = OrderStorage.getInstance();
    }

    public static OrderManage getInstance() {
        return orderManage = orderManage == null ? new OrderManage() : orderManage;
    }

    //开始或停止喷雾(同一个命令)
    public void startPenWu(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.ON_ON};
        Order order = new Order(type,msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
        EventBus.getDefault().post(new IsPw().setPw(true));
    }
    public void stopPenWu(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.ON_OFF};
        Order order = new Order(type,msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
        EventBus.getDefault().post(new IsPw().setPw(false));
    }
    public void startWater(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.WATER_OFF,Command.WATER_OFF,Command.WATER_OFF,Command.WATER_ON};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void stopWater(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.WATER_OFF,Command.WATER_OFF,Command.WATER_OFF};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    //读取环境数据
    public void getData(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.Data};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void setWULIANG(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.WURANK};
        int data=(int) new SpHelperUtil(App.context).getSharedPreference("wuliang",150);
        msg[0][5]=Integer.toHexString(data);
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void setWIND(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.wind};
        int data=(int) new SpHelperUtil(App.context).getSharedPreference("wind",150);
        msg[0][5]=Integer.toHexString(data);
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void getWaterData(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.waterData};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void red_open(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.blue_off,Command.blue_off,Command.green_off,Command.green_off,Command.red_on,Command.red_on};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        //orderStorage.putOrder(order);
    }
    public void blue_open(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.green_off,Command.green_off,Command.red_off,Command.red_off,Command.blue_on,Command.blue_on};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void green_open(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.blue_off,Command.blue_off,Command.red_off,Command.red_off,Command.green_on,Command.green_on};
        Order order = new Order(type, msg,productCallback, executiveLevel);
       // orderStorage.putOrder(order);
    }
    public void look1(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.look1};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void look2(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.look2};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void look3(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.look3};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void look4(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.look4};
        Order order = new Order(type, msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
    }
    public void CanAddWater(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.CanAddWater};
        Order order = new Order(type,msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
        Log.e("加水测试:","是否能加水");
    }
    public void OutWater(int type, ProductCallback productCallback, Order.ExecutiveLevel executiveLevel) throws InterruptedException {
        String[][]msg=new String[][]{Command.D400_0,Command.D401_0,Command.D402_0,Command.D401_1};
        Order order = new Order(type,msg,productCallback, executiveLevel);
        orderStorage.putOrder(order);
        Log.e("加水测试:","出水");
    }
}
