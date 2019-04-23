package com.alan.bootstarp;

import com.alan.NetMessageProcesser;
import com.alan.config.GlobalConfigManager;

public class Main {
 
    public static void main(String[] args) throws Exception {

        //启动net服务
        new NettyServer().bind(8080);

        //各种初始化
        GlobalConfigManager.getInstance().init();
        NetMessageProcesser.getInstance().init();

        //处理消息
        MsgExecuteManager.getInstance().logic();

    }
}