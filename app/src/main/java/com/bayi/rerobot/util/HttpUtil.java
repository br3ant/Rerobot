package com.bayi.rerobot.util;

import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.bean.httpBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.bayi.rerobot.App.currentMap;
import static java.lang.Thread.sleep;

/**
 * Created by tongzn
 * on 2020/8/6
 */

public class HttpUtil {
        /**
         * 采用get方法进行对服务器的访问，以字符拼接的形式发送请求
         */
        private String urlAddress = "http://192.168.31.200:8080/gs-robot/cmd/position/navigate";//服务器地址
      //  private String method = "getMemberBySex";//方法名
        private String getUrl;

        private int count=0;
        private Gson gson = new Gson();
       public void get(String s) {

            //getUrl = urlAddress + "?map_name=ceshi&position_name=" + s;//构造getUrl
           getUrl= String.format("%s?map_name=%s&position_name=%s",urlAddress,currentMap,s);
            new Thread(new Runnable() {
                @Override

                public void run() {
                    try {
                        URL url = new URL(getUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();//开启连接
                        connection.connect();//连接服务器

                        if (connection.getResponseCode() == 200) {
                            //使用字符流形式进行回复
                            InputStream is = connection.getInputStream();
                            //读取信息BufferReader
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            StringBuffer buffer = new StringBuffer();
                            String readLine = "";
                            while ((readLine = reader.readLine()) != null) {
                                buffer.append(readLine);
                            }
                            is.close();
                            reader.close();
                            connection.disconnect();

                            Log.d("Msg", buffer.toString());
                            LogUtil.saveLog(buffer.toString()+ s);
                            httpBean bean=gson.fromJson(buffer.toString(), httpBean.class);
                            if(bean.isSuccessed()){
                                count=0;
                            }else {
                               count++;
                               if(count<3){
                                   try {
                                       sleep(500);
                                       get(s);
                                   } catch (InterruptedException e) {
                                       e.printStackTrace();
                                   }
                               }else {
                                   count=0;

                               }
                            }
                        } else {
                            Log.d("TAG", "ERROR CONNECTED");
                            LogUtil.saveLog("http ERROR CONNECTED");
                            //App.toast("网络连接超时");
                            count++;
                            if(count<3){

                                try {
                                    sleep(500);
                                    get(s);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }else {
                                count=0;

                            }
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

