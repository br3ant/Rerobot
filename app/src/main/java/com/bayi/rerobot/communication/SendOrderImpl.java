package com.bayi.rerobot.communication;

public interface SendOrderImpl {

    void getHumTemp(String address, ProductCallback callback);

    void setEquipmentSwitch(String address, boolean open, ProductCallback callback, Order.ExecutiveLevel executiveLevel);

    void setRegister(String address, String msg, ProductCallback callback, Order.ExecutiveLevel executiveLevel);

    void getCoilState(String address, String msg, ProductCallback callback);

    void getPMData(ProductCallback callback);

    void myCustomOrder(int type, String address, String msg, Order.ExecutiveLevel executiveLevel, ProductCallback callback);

}
