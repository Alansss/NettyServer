package com.alan.common;


import com.alan.entity.Player;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 14:25
 * @Description: TODO
 */
public class SessionManager {

    private Map<String, Player> playerSessionMap = Maps.newHashMap();
    private Map<String, ChannelHandlerContext> channelMap = Maps.newHashMap();

    private static SessionManager instance = null;

    public static SessionManager getInstance(){
        if (null == instance){
            instance = new SessionManager();
        }
        return instance;
    }

    public void addChannelHandlerContext(String playerid,ChannelHandlerContext channelHandlerContext){
        channelMap.put(playerid,channelHandlerContext);
    }

    public ChannelHandlerContext getCtxByPlayerId(String playerid){
        return channelMap.get(playerid);
    }


}
