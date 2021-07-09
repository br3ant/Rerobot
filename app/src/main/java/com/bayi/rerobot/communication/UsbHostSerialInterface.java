package com.bayi.rerobot.communication;

/**
 * Created by Skygge,  Date on 2019/4/4.
 * dec:USB串行
 */
public interface UsbHostSerialInterface extends ConsumptionOrderInterface{

    /**打开串口*/
    void openDevice();

    /**关闭串口*/
    void CloseDevice();

    /**判断设备是否已经连接到Android 系统*/
    boolean isConnected();

    /**发送网口--命令*/
    void sendCmd(String cmd[]);


    /**响应命令*/
    StringBuilder readDataStr();

    /**清除缓存*/
    void clearCache();

}
