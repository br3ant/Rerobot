package com.bayi.rerobot.data.response;



import com.bayi.rerobot.data.api.Api;
import com.bayi.rerobot.data.net.Retrofit;
import com.bayi.rerobot.data.net.Retrofit1;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

/**
 * Created by tzn
 * on 2019/4/9
 */
public class ApiRepossitory {
    public Call<ResponseBody> anslogsave(@Body RequestBody anslog){
        return    Retrofit.getInstance().create(Api.class).anslogsave(anslog);
    }


    public Call<ResponseBody> robotlogsave(@Body RequestBody robotlogsave){
        return    Retrofit.getInstance().create(Api.class).robotlogsave(robotlogsave);
    }

    public Call<ResponseBody> robotstatsave(@Body RequestBody robotstatsave){
        return    Retrofit.getInstance().create(Api.class).robotstatsave(robotstatsave);
    }
    public Call<String> borr(String title,int inflag){
        return    Retrofit.getInstance().create(Api.class).borr(title,inflag);
    }
    public Call<String> guide(String title){
        return    Retrofit.getInstance().create(Api.class).guide(title);
    }
    public Call<String> baidu(){
        return    Retrofit.getInstance().create(Api.class).baidu();
    }

    public Call<String> search(String search){
        return    Retrofit.getInstance().create(Api.class).search(search);
    }
    public Call<ResponseBody> upCwen(@Body RequestBody upCewen){
        return    Retrofit1.getInstance().create(Api.class).upCewen(upCewen);
    }
}
