package com.alan.handler.message;


import com.alan.log.NetLog;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 14:25
 * @Description: TODO
 */
public class SessionsManager {
    private static SessionsManager instance = null;

    private Integer m_nSessionIncrement = 0;
    private HashMap<Integer, Session> mapIntegerSessions = new HashMap<>();
    private HashMap<ChannelHandlerContext, Session> mapChannelSessions = new HashMap<>();

    private int msgCounter = 0;

    public static SessionsManager GetInstance()
    {
        if(instance == null) {
            instance = new SessionsManager();
        }
        return instance;
    }

    public synchronized Session GetSession(int nConnectionID)
    {
        if(mapIntegerSessions.containsKey(nConnectionID)) {
            return mapIntegerSessions.get(nConnectionID);
        }
        return null;
    }

    synchronized Session GetSession(ChannelHandlerContext ctx)
    {
        if(mapChannelSessions.containsKey(ctx)) {
            return mapChannelSessions.get(ctx);
        }
        return null;
    }

    synchronized int AddSession(ChannelHandlerContext ctx)
    {
        int nSessionID = 0;
        synchronized (m_nSessionIncrement)
        {
            nSessionID = ++m_nSessionIncrement;
            Session session = new Session(nSessionID, ctx);

            mapIntegerSessions.put(nSessionID, session);
            mapChannelSessions.put(ctx, session);
        }
        return nSessionID;
    }

    synchronized Session RemoveSession(ChannelHandlerContext ctx)
    {
        Session session = null;
        if(mapChannelSessions.containsKey(ctx))
        {
            session = mapChannelSessions.remove(ctx);
        }

        if(mapIntegerSessions.containsKey(session.GetSessionID()))
        {
            mapIntegerSessions.remove(session.GetSessionID());
        }
        ctx.close();
        return session;
    }

    public boolean HasSession(int sessionID)
    {
        return mapIntegerSessions.containsKey(sessionID);//todo 检测是否连接断开
    }

    public void SendMsgToClient(int sessionID, int id, com.google.protobuf.GeneratedMessage data)
    {

        Session session = SessionsManager.GetInstance().GetSession(sessionID);
        if(session != null)
        {
            byte[] bytes = data.toByteArray();
            if(NetLog.useNetMsgLog)//todo netlog
                NetLog.LogNetMsg((++msgCounter) + ": SendTo " + sessionID + " [" + id + "]" + bytes.length);
            session.SendMsg(id, bytes);
        }
    }


}