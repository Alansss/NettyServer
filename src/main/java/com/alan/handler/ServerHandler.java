package com.alan.handler;

import com.alan.NetMessageProcessser;
import com.alan.common.SessionManager;
import com.alan.entity.RpcRequest;
import com.alan.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    //接受client发送的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        RpcRequest request = (RpcRequest) msg;
//        System.out.println("接收到客户端信息:" + request.toString());
//        //返回的数据结构
//        RpcResponse response = new RpcResponse();
//        response.setId(UUID.randomUUID().toString());
//        response.setData("server响应结果");
//        response.setStatus(1);
//        ctx.writeAndFlush(response);
        //解析出用户id TODO

        String playserId = "0000";

        NetMessageProcessser.getInstance().addMessage(msg.toString());
        SessionManager.getInstance().addChannelHandlerContext(playserId, ctx);

    }

    //通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端接收数据完毕..");
        ctx.flush();
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    //客户端去和服务端连接成功时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush("hello client");
    }
}