package com.alan;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.common.ClassScanner;
import com.alan.handler.message.SessionsManager;
import com.alan.config.GlobalConfig;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/17 17:20
 * @Description: TODO
 */
@Slf4j
public class NetMessageProcessser {

    private final Queue<String> netMessagesQueue = new ConcurrentLinkedDeque<>();

    Map<Integer, Method> handlerMethods = Maps.newHashMap();

    private Map<Integer, Object> handlers = Maps.newHashMap();

    private static NetMessageProcessser instance = null;

    public static NetMessageProcessser getInstance() {
        if (instance == null) {
            instance = new NetMessageProcessser();
        }
        return instance;
    }

    public void init() {
        String handlerpath = GlobalConfig.getInstance().getHandlerpath();
        Map<String, Class<?>> scannerClass = ClassScanner.scannerClass(handlerpath);
        scannerClass.forEach((classname, clazz) -> {

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(MessageCommandAnnotation.class)) {
                    MessageCommandAnnotation messageCommandAnnotation = method.getAnnotation(MessageCommandAnnotation.class);
                    if (messageCommandAnnotation != null) {
                        handlerMethods.put(messageCommandAnnotation.command(), method);
                        try {
                            handlers.put(messageCommandAnnotation.command(), clazz.newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });


        log.info("handlerMethods.size:" + handlerMethods.size());
        log.info("handlers.size:" + handlers.size());
    }

    public void addMessage(String message) {
        netMessagesQueue.add(message);
    }


    public void processNetMessage() {
        String message;
        while (netMessagesQueue.size() > 0) {
            message = netMessagesQueue.poll();
            //netMessageProcessLogic.processMessage(message, nettySession);

            //取到消息的类型
            int cmd = 333;
            //取到玩家id
            String playerId = "";

            ChannelHandlerContext ctxByPlayerId = SessionsManager.getInstance().getCtxByPlayerId(playerId);

            Method method = handlerMethods.get(cmd);
            Object handler = handlers.get(cmd);
            method.setAccessible(true);
            try {
                Object object = method.invoke(handler, ctxByPlayerId, message);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
//            String result = null;
//            if(object != null){
//                result = (String) object;
//            }
//            return result;

        }
    }


    public static void main(String[] args) {
        NetMessageProcessser.getInstance().init();
        NetMessageProcessser netMessageProcessser = NetMessageProcessser.getInstance();
        netMessageProcessser.addMessage("aaaaaaaa");
        netMessageProcessser.processNetMessage();
    }
}
