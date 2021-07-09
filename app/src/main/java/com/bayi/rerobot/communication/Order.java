package com.bayi.rerobot.communication;


public class Order {

    String[][] cmd; //待发送命令
    ProductCallback productCallback; //命令执行结果
    ExecutiveLevel executiveLevel; //执行级别

    boolean hasOnePut;
    int type;


    public Order(int type,String[][] cmd, final ProductCallback productCallback, ExecutiveLevel executiveLevel) {
        setProductCallback(productCallback);
        setCmd(cmd);
        setExecutiveLevel(executiveLevel);
        this.type=type;
        hasOnePut = false;
    }


    public String[][] getCmd() {
        return cmd;
    }

    private void setCmd(String[][] cmd) {
        this.cmd = cmd;
    }
    public int getType(){
        return type;
    }
    public ProductCallback getProductCallback() {
        return productCallback;
    }

    private void setProductCallback(ProductCallback productCallback) {
        this.productCallback = productCallback;
    }

    public ExecutiveLevel getExecutiveLevel() {
        return executiveLevel;
    }

    private void setExecutiveLevel(ExecutiveLevel executiveLevel) {
        this.executiveLevel = executiveLevel;
    }

    /***HIGH 必须执行，MEDIUM可以等待，LOW 可以拒绝*/
    public enum ExecutiveLevel {
        HIGH, MEDIUM, LOW
    }
}
