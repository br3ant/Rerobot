package com.bayi.rerobot.data.net;

import android.net.Uri;
import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by tzn
 * on 2019/4/9
 */
public class Retrofit {


    private final retrofit2.Retrofit RETROFIT;

    private final Interceptor INTERCEPTOR;

    private Retrofit() {

        INTERCEPTOR = new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {

                final Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type","application/json")
                        .addHeader("charset","utf-8")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64")
                        .build();

                return chain.proceed(request);
            }
        };
         SpHelperUtil spHelperUtil=new SpHelperUtil(App.context);
         String url=String.format("%s:%s",(String)spHelperUtil.getSharedPreference(Contants.IT_IP,"http://115.231.60.194"),
                 (String)spHelperUtil.getSharedPreference(Contants.IT_PORT,"9090"));

        RETROFIT = new retrofit2.Retrofit.Builder()
                .baseUrl(url +"/api/robotdata/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(initClient())
                .build();
    }

    private OkHttpClient initClient() {

        return new  OkHttpClient.Builder().addInterceptor(initLogInterceptor())
                .addInterceptor(INTERCEPTOR)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

    }

    private Interceptor initLogInterceptor() {


        return  new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static class RetrofitHolder{

        private static final Retrofit RETROFIT_FACTORY= new Retrofit();

    }

    public  static Retrofit getInstance(){
        return RetrofitHolder.RETROFIT_FACTORY;
    }

    public <T> T create(Class<T> Service){

        return  RETROFIT.create(Service);
    }

}
