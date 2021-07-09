package com.bayi.rerobot.data.callback;

import android.util.Log;
import android.widget.Toast;


import com.bayi.rerobot.App;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class baseback implements Callback<String> {
    public abstract  void onResponse(Response<String> response);

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    public abstract void onFailure();

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        try {
            onResponse(response);
        }catch (Exception e){
            Log.e("base",e.toString());
            Toast.makeText(App.getContext(), "服务器异常", Toast.LENGTH_SHORT);
            onFailure();
        }

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        onFailure();
    }
}
