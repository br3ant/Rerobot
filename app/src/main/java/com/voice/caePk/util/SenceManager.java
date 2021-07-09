package com.voice.caePk.util;

import android.util.Log;

public class SenceManager {

     enum senceType{
        Music,
        other
    };

    static senceType currentSence = senceType.other;

    public static void setMusicSence(){
        Log.d("SenceManager","setMusicSence");
        currentSence = senceType.Music;
    }
    public  static  void exitMusicSence(){

        currentSence = senceType.other;
        Log.d("SenceManager","exitMusicSence");
    }

    public static  boolean isMusicSence(){
        return (senceType.Music == currentSence)?true:false;
    }
}
