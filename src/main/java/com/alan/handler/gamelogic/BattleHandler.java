package com.alan.handler.gamelogic;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.handler.message.Session;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 14:05
 * @Description: TODO
 */
public class BattleHandler {

    @MessageCommandAnnotation(command = 333)
    public void channelRead(Integer sessionID, byte[] msg) throws Exception {

        System.out.println("have a battle........");

    }

}
