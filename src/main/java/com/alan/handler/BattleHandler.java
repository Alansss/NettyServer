package com.alan.handler;

import com.alan.annotation.MessageCommandAnnotation;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 14:05
 * @Description: TODO
 */
public class BattleHandler {

    @MessageCommandAnnotation(command = 333)
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("have a battle........");

    }

}
