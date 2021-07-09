package com.bayi.rerobot.util;

public interface Contants {
    String url="http://115.231.60.194:9090";//与后台接口对接地址
    String IT_IP="IT_IP";
    String IT_PORT="IT_PORT";
    String IOT_IP="IOT_IP";
    String IOT_PORT="IOT_PORT";
    String CEWEN_IP="CEWEN_IP";
    String MIMA="MIMA";
    String CEWEN_PORT="CEWEN_PORT";
    String IOT_osautn="IT_osautn";
    String startName="startname";//起始点或待命点名称
    String mapName="mapname";//起始点或待命点名称
    String lastName="lastname";//点位最后停留时间
    String CHARGE="charge";
    String lowCharge="chargelow";//最低电量
    String highCharge="chargehigh";//最高电量
    String SYSYTEMDATA="systemdata";//获取传感器的环境参数
    String LISTBEAN="LISTBEAN";//存取通步点位
    int PENWU=0;
    int ZHIWAIDENG=1;
    int CHONGDIAN=2;
    int XUNLUO=3;
    int WATER=4;
    int DIANWEI=5;
    /*  int Dialog_Width=360;//dailog 宽高
      int Dialog_Height=560;//1120*/
    int Dialog_Width=600;//dailog 宽高
    int Dialog_Height=980;//1120
    String Config_ip="192.168.11.7";
    int Config_port=26;
    String WATER_ip="115.231.60.194";
    int WATER_port=23;
    int TYPE_REBORT=0;//与机器人通讯
    int TYPE_WATER=1;//加水的通讯
}
