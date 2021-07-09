package com.socketservice;

import android.util.Log;

import com.bayi.rerobot.bean.Cwjson;
import com.bayi.rerobot.data.response.ApiRepossitory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.InvalidParameterException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 自定义处理器
 * @author ranx
 * @create 2018-11-08 20:42
 **/
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    private ApiRepossitory repossitory = new ApiRepossitory();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println("请求URL:"+request.getUri()+" IP地址："+getRemoteIpAddr(ctx));
        //只接受post请求
        if (request.getMethod() != HttpMethod.POST) {
            System.out.println("非POST方法不支持");
            new Responder().sendWithStatus(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "方法不可用");
            ctx.close();
            return;
        }

        JSONObject param = null;
        String picture="";
        try {
            String paramStr = request.content().toString(Charset.forName("UTF-8"));
            //System.out.println(paramStr);
            Cwjson cwjson=new Cwjson();
            cwjson.setJSON(paramStr);
            EventBus.getDefault().post(cwjson);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
            repossitory.upCwen(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("upcewen",response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("upcewen",t.toString());
                }
            });
            if (paramStr!=null&&paramStr.length()>0) {
                param = new JSONObject(paramStr);
                 picture=param.getString("picture");
            } else {
                param = new JSONObject();
            }
        } catch (Exception e) {
        	System.out.println("[p] json解析出错{}");
        	e.printStackTrace();
        	Log.v("ssss",e.toString());
           // throw new InvalidParameterException();
        }

        //请求映射
//        ctx.writeAndFlush(response)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof  InvalidParameterException) {
            new Responder().sendWithStatus(ctx, HttpResponseStatus.NOT_ACCEPTABLE, "数据解析出错");
        } else {
            new Responder().sendWithStatus(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, "内部错误");
        }
        System.out.println("[p] InvalidParamException:{}"+cause);
        ctx.close();
    }

    private String getRemoteIpAddr(ChannelHandlerContext ctx) {
        String ip = AppConst.DEFAULT_IP_ADDRESS;
        try {
            String ipAndPort = ctx.channel().remoteAddress().toString();
            ipAndPort = ipAndPort.substring(1);
            ipAndPort = ipAndPort.split("\\:")[0];
            ip = ipAndPort;
        } catch (Exception e) {
            ip = AppConst.DEFAULT_IP_ADDRESS;
        }
        return ip;
    }
}
