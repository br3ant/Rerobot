package com.socketservice;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 *  响应客户端处理
 * @author ranx
 * @create 2018-11-08 21:22
 **/
public class Responder {

    public void sendWithStatus(ChannelHandlerContext ctx, HttpResponseStatus status, String result) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.wrappedBuffer(result.getBytes()));
        setHeaders(response);
        ctx.writeAndFlush(response);
    }

    /**
     * 设置响应格式
     * @param response
     */
    private void setHeaders(FullHttpResponse response) {
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=utf-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
    }
}
