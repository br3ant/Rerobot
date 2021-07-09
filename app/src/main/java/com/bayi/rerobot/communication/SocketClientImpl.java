package com.bayi.rerobot.communication;

import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.Config;
import com.bayi.rerobot.util.CRC16M;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

import static com.bayi.rerobot.util.CRC16M.HexString2Buf;
import static com.bayi.rerobot.util.CRC16M.HexString2Buf2;
import static java.lang.Thread.sleep;

public class SocketClientImpl implements UsbHostSerialInterface {

    private static final String TAG = SocketClientImpl.class.getSimpleName();
    private static final int outTime = 2000;
    private Socket clientSocket,clientSocket1;
    private StringBuilder sd_sb = new StringBuilder();
    private StringBuilder sd_sb1 = new StringBuilder();
    private String address,address1;
    private int port,port1;
    private Thread receiveThread,receiveThread1;

    private int redata = 200;
    private boolean isOpen;
    private SpHelperUtil spHelperUtil;
    public SocketClientImpl() {
        clientSocket=new Socket();
        clientSocket1=new Socket();
        spHelperUtil=new SpHelperUtil(App.context);

    }
    private int type;

    private int i = 0;

    public void runOrder(Order order) {
        boolean isOk = false;
        //判断连接的是哪个socket

        address= Contants.Config_ip;
        port= Contants.Config_port;

        address1= (String)spHelperUtil.getSharedPreference(Contants.IOT_IP,"115.231.60.194");
        port1= Integer.valueOf((String)spHelperUtil.getSharedPreference(Contants.IOT_PORT,"23"));


        type=order.type;
        try {
            String cmd[][] = order.getCmd();
            openDevice();
            for (String a[] : cmd) {
                sendCmd(a);
                sleep(200);
            }

            long startTime = System.currentTimeMillis();

            while (!isOk) {
                if (readDataStr().length() > 0) {
                    StringBuilder sdcp = readDataStr();
                    Log.d(TAG, "ProductCallback.Sur:" + sdcp.toString());
                    order.getProductCallback().onDataReceived(ProductCallback.Sur, sdcp.toString());
                    clearCache();
                    isOk = true;
                } else {
                    if (((System.currentTimeMillis() - startTime) > outTime)) {

                        clearCache();
                        i++;
                        if (i > 3) {
                            CloseDevice();
                            openDevice();
                            i = 0;
                            order.getProductCallback().onDataReceived(ProductCallback.Err, "Socket反馈超时");
                        }else
                            order.getProductCallback().onDataReceived(ProductCallback.TIMEOUT, "Socket反馈超时");

                        isOk = true;
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            order.getProductCallback().onDataReceived(ProductCallback.Err, "网络异常！");
            Log.d(TAG, "ProductCallback.Err：" + "网络异常！");

        }

    }

    @Override
    public void CloseDevice() {
        if(type==0){
            try {
                if (clientSocket== null)
                    return;
                if (clientSocket!= null && clientSocket.isConnected()) {
                    clientSocket.shutdownInput();
                    clientSocket.shutdownOutput();
                    clientSocket.close();
                }

                clientSocket = null;
                isOpen = false;
            } catch (IOException e) {
                e.printStackTrace();
                clientSocket = null;
                isOpen = false;
            }
        }else {
            try {
                if (clientSocket1== null)
                    return;
                if (clientSocket1!= null && clientSocket1.isConnected()) {
                    clientSocket1.shutdownInput();
                    clientSocket1.shutdownOutput();
                    clientSocket1.close();
                }

                clientSocket1 = null;
                isOpen = false;
            } catch (IOException e) {
                e.printStackTrace();
                clientSocket1 = null;
                isOpen = false;
            }

        }
    }

    @Override
    public boolean isConnected() {
        return isOpen;
    }


    public void sendCmd(String[] cmd) {
        StringBuilder sb = new StringBuilder();
        for (String s1 : cmd) {
            sb.append(s1);
        }
        byte[]  s = CRC16M.getSendBuf(sb.toString());

        Log.d(TAG, "ConsumerProduct命令发送成功" + sb.toString());

        if(type==0){
            if (!clientSocket.isConnected()) {
                openDevice();
            }
            // bulkOut传输
            try {

                clientSocket.getOutputStream().write(s);
                Log.d(TAG, "命令发送成功clientSocket : " + "Send communication Succese！");
            } catch (IOException e) {
                e.printStackTrace();
                isOpen = false;
                Log.d(TAG, "命令发送错误clientSocket: " + "bulkOut传输-1");

            }
        }else {
            if (!clientSocket1.isConnected()) {
                openDevice();
            }
            // bulkOut传输
            try {

                clientSocket1.getOutputStream().write(s);
                Log.d(TAG, "命令发送成功clientSocket1 : " + "Send communication Succese！");
            } catch (IOException e) {
                e.printStackTrace();
                isOpen = false;
                Log.d(TAG, "命令发送错误:clientSocket1 " + "bulkOut传输-1");

            }
        }
    }


    @Override
    public StringBuilder readDataStr() {
        if(type==0)
            return sd_sb;
        else
            return  sd_sb1;

    }

    @Override
    public void clearCache() {
        sd_sb.setLength(0);
        sd_sb1.setLength(0);
    }

    @Override
    public void openDevice() {
        if(type==0){
            if (isOpen) {
                receiveMessageFromPoint();

                return;
            }
            if (clientSocket!= null && clientSocket.isConnected()) {
                return;
            }
            if (clientSocket == null || clientSocket.isClosed()) {
                clientSocket = new Socket();
            }
            new Thread(() -> {
                try {
                    if (!clientSocket.isConnected()) {
                        SocketAddress remoteAddr = new InetSocketAddress(address, port);
                        clientSocket.connect(remoteAddr, 3000);
                    }
                } catch (IOException e) {
                    Log.v("tcpserver", e.getMessage());
                    isOpen = false;
                }

                isOpen = true;
            }).start();
            receiveMessageFromPoint();
        }else {
            if (isOpen) {

                receiveMessageFromPoint1();
                return;
            }
            if (clientSocket1!= null && clientSocket1.isConnected()) {
                return;
            }
            if (clientSocket1 == null || clientSocket1.isClosed()) {
                clientSocket1 = new Socket();
            }
            new Thread(() -> {
                try {
                    if (!clientSocket1.isConnected()) {
                        SocketAddress remoteAddr = new InetSocketAddress(address1, port1);
                        clientSocket1.connect(remoteAddr, 3000);
                    }
                } catch (IOException e) {
                    Log.v("tcpserver", e.getMessage());
                    isOpen = false;
                }

                isOpen = true;
            }).start();
            receiveMessageFromPoint1();
        }
    }

    private void receiveMessageFromPoint() {
        if (null != receiveThread && receiveThread.isAlive()) {
            return;
        }
        receiveThread = new Thread(() -> {
            StringBuilder stringBuffer = new StringBuilder();
            boolean isBag = false;   //为真开始缓存接受命令
            try {
                byte[] arrayOfByte = new byte[redata];
                byte[] mByte;
                String str;
                while (isOpen) {
                    int i;

                    if (clientSocket != null) {
                        i = clientSocket.getInputStream().read(arrayOfByte);
                        if (i > 0) {
                            mByte = new byte[i];
                            System.arraycopy(arrayOfByte, 0, mByte, 0, i);
                            String strs = CRC16M.getBufHexStr(mByte);
                            if (stringBuffer.length() == 0) {
                                isBag = true;
                            }
                            stringBuffer.append(strs);
                            Log.d(TAG, "length:" + stringBuffer.length() + " str:" + stringBuffer.toString());
                            // stringBuffer.length()的长度为14，为modeBus中使用
                            if (stringBuffer.length() > 0 /*== 14*/) {
                                isBag = false;
                            }
                            if (!isBag && stringBuffer.length() > 0) {
                                sd_sb.setLength(0);
                                sd_sb.append(stringBuffer.toString());
                            }
                        }
                    }

                    stringBuffer.setLength(0);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isOpen = false;
            }
        });
        receiveThread.start();
    }
    private void receiveMessageFromPoint1() {
        if (null != receiveThread1 && receiveThread1.isAlive()) {
            return;
        }
        receiveThread1 = new Thread(() -> {
            StringBuilder stringBuffer = new StringBuilder();
            boolean isBag = false;   //为真开始缓存接受命令
            try {
                byte[] arrayOfByte = new byte[redata];
                byte[] mByte;
                String str;
                while (isOpen) {
                    int i;

                    if (clientSocket1 != null) {
                        i = clientSocket1.getInputStream().read(arrayOfByte);
                        if (i > 0) {
                            mByte = new byte[i];
                            System.arraycopy(arrayOfByte, 0, mByte, 0, i);
                            String strs = CRC16M.getBufHexStr(mByte);
                            if (stringBuffer.length() == 0) {
                                isBag = true;
                            }
                            stringBuffer.append(strs);
                            Log.d(TAG, "length:" + stringBuffer.length() + " str:" + stringBuffer.toString());
                            // stringBuffer.length()的长度为14，为modeBus中使用
                            if (stringBuffer.length() > 0 /*== 14*/) {
                                isBag = false;
                            }
                            if (!isBag && stringBuffer.length() > 0) {
                                sd_sb1.setLength(0);
                                sd_sb1.append(stringBuffer.toString());
                            }
                        }
                    }

                    stringBuffer.setLength(0);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isOpen = false;
            }
        });
        receiveThread1.start();
    }


}
