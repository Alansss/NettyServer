package com.alan;

import com.alan.annotation.MessageCommandAnnotation;
import com.alan.common.ClassScanner;
import com.alan.handler.message.NetMsgBuffer;
import com.alan.handler.message.NetMsgBufferNode;
import com.alan.config.GlobalConfigManager;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/17 17:20
 * @Description: TODO
 */
@Slf4j
public class NetMessageProcesser {

    //private final Queue<String> netMessagesQueue = new ConcurrentLinkedDeque<>();

    Map<Integer, Method> handlerMethods = Maps.newHashMap();

    private Map<Integer, Object> handlers = Maps.newHashMap();

    private static NetMessageProcesser instance = null;

    public static NetMessageProcesser getInstance() {
        if (instance == null) {
            instance = new NetMessageProcesser();
        }
        return instance;
    }

    public void init() {
        String handlerpath = GlobalConfigManager.getInstance().getHandlerpath();
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

//    public void addMessage(String message) {
//        netMessagesQueue.add(message);
//    }


    /**
     * 分发消息到对应的logic
     */
    public void dispatchNetMessage() {

        Queue<NetMsgBufferNode> netMsgBufferNodes = NetMsgBuffer.GetInstance().GetMsgs();
        for (NetMsgBufferNode netMsgBufferNode : netMsgBufferNodes) {
            int sessionID = netMsgBufferNode.GetSessionID();
            byte[] bytes = netMsgBufferNode.GetProtoMsg();

            //取到消息的类型
//            int cmd = 333;
            //取到玩家id
//            String playerId = "";

            //Session session = SessionsManager.GetInstance().GetSession(sessionID);
            //System.out.println("id:" + ((cur[ID_POS0]<<24 & 0xFF000000) + (cur[ID_POS1]<<16 & 0x00FF0000) + (cur[ID_POS2]<<8 & 0x0000FF00) + (cur[ID_POS3]<<0)));

            int cmdID = netMsgBufferNode.GetID();

            Method method = handlerMethods.get(cmdID);
            Object handler = handlers.get(cmdID);
            method.setAccessible(true);
            try {
                Object object = method.invoke(handler, sessionID, bytes);
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

    }
}
