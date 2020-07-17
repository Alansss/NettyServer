package com.alan.handler.message;

import com.alan.handler.message.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/17 17:05
 * @Description: TODO
 */
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
//                .addLast(new RpcDecoder(RpcRequest.class)) //解码request
//                .addLast(new RpcEncoder(RpcResponse.class)) //编码response
                .addLast(new ServerHandler()); //使用ServerHandler类来处理接收到的消息
    }


}
