package com.alan.bootstarp;

public class Main {
 
    public static void main(String[] args) throws Exception {
        //启动server服务
        new NettyServer().bind(8080);
    }
}