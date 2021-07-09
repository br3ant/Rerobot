package com.bayi.rerobot.communication;


public interface ProductCallback {

    int Err = -1;
    int Sur = 0;
    int TIMEOUT=2;

    void onDataReceived(int state, String age);

}
