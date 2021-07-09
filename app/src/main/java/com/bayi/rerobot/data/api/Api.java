package com.bayi.rerobot.data.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tzn
 * on 2019/4/9
 */
public interface Api {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("anslogsave")
    Call<ResponseBody> anslogsave(@Body RequestBody anslog);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("robotlogsave")
    Call<ResponseBody> robotlogsave(@Body RequestBody robotlogsave);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("robotstatsave")
    Call<ResponseBody> robotstatsave(@Body RequestBody robotstatsave);

    @FormUrlEncoded
    @POST("borr")
    Call<String> borr(@Field("title")String title,
                            @Field("inflag")int inflag);
    @FormUrlEncoded
    @POST("guide")
    Call<String> guide(@Field("title")String title);
    @FormUrlEncoded
    @POST("")
    Call<String> baidu();

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("data")
    Call<ResponseBody> upCewen(@Body RequestBody upCewen);

    @GET("doc_list?")
    Call<String> search(@Query("search_txt")String search);
}
