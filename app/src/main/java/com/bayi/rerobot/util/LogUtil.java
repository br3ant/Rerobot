package com.bayi.rerobot.util;


import android.os.Environment;
import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.data.response.ApiRepossitory;
import com.bayi.rerobot.greendao.LogS;
import com.bayi.rerobot.service.aiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tongzn on 16/6/1 16:50
 */
public class LogUtil {
    private static boolean logOn = true;

    public static void e(String log) {
        if (logOn) {
            Log.e(getTag(), log);
        }
    }

    public static void e(String tag, String log) {
        if (logOn) {
            Log.e(tag, log);
        }
    }

    public static void e(Throwable e) {
        if (logOn) {
            Log.e(getTag(), e.getMessage(), e);
        }
    }

    public static void e(String msg, Throwable e) {
        if (logOn) {
            Log.e(getTag(), msg, e);
        }
    }

    public static void w(String log) {
        if (logOn) {
            Log.w(getTag(), log);
        }
    }

    public static void w(String tag, String log) {
        if (logOn) {
            Log.w(tag, log);
        }
    }

    public static void w(Throwable e) {
        if (logOn) {
            Log.w(getTag(), e.getMessage(), e);
        }
    }

    public static void i(String log) {
        if (logOn) {
            Log.i(getTag(), log);
        }
    }

    public static void i(String tag, String log) {
        if (logOn) {
            Log.i(tag, log);
        }
    }

    public static void i(Throwable e) {
        if (logOn) {
            Log.i(getTag(), e.getMessage(), e);
        }
    }

    public static void d(String log) {
        if (logOn) {
            Log.d(getTag(), log);
        }
    }

    public static void d(String tag, String log) {
        if (logOn) {
            Log.d(tag, log);
        }
    }

    public static void d(Throwable e) {
        if (logOn) {
            Log.d(getTag(), e.getMessage(), e);
        }
    }

    public static void v(String log) {
        if (logOn) {
            Log.v(getTag(), log);
        }
    }

    public static void v(String tag, String log) {
        if (logOn) {
            Log.v(tag, log);
        }
    }

    public static void v(Throwable e) {
        if (logOn) {
            Log.v(getTag(), e.getMessage(), e);
        }
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "miFamily" + File.separator + "errorLog.txt";

    public static synchronized void writeException2File(Throwable ex) {// 新建或打开日志文件
        Date nowTime = new Date();
        File file = new File(LOG_FILE_PATH);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            pw.println();
            pw.println(getTag() + "  " + sdf.format(nowTime));
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void writeException2File(String logText) {// 新建或打开日志文件
        Date nowTime = new Date();
        File file = new File(LOG_FILE_PATH);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            pw.println();
            pw.println(getTag() + "-->" + sdf.format(nowTime));
            pw.println(logText);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTag() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return "LogUtil";
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(LogUtil.class.getName())) {
                continue;
            }

            return "[" + st.getFileName() + ":" + st.getLineNumber() + "]";
        }

        return "LogUtil";
    }
   public static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void saveLog(String msg){
        Date date = new Date(System.currentTimeMillis());
        String ymd=ymdFormat.format(date);
        String hm=hmFormat.format(date);
        String ymdhms=ymdhmsFormat.format(date);
        LogS log=new LogS();
        log.setYmd(ymd);
        log.setHm(hm);
        log.setMsg(msg);
        App.getDaoSession().getLogSDao().insertOrReplace(log);
        robotlogsave(msg,ymdhms);
    }
    private static void robotlogsave(String rtype, String date) {
        JSONArray jsonArray = new JSONArray();

                JSONObject mJson = new JSONObject();
                try {
                    mJson.put("deviceno", aiService.sn);
                    mJson.put("rdata", date);
                    mJson.put("rtype", rtype);
                    mJson.put("remark","");
                    jsonArray.put(mJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
                new ApiRepossitory().robotlogsave(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }

}
