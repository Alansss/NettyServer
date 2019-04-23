package com.alan.handler.message;

import com.alan.log.NetLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        if (NetLog.useNetMsgLog) {
            NetLog.Log(ctx.channel().id() + "(" + ctx.channel().remoteAddress().toString() + ")" + " Session Added");
        }

        SessionsManager.GetInstance().AddSession(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (NetLog.useNetMsgLog) {
            NetLog.Log(ctx.channel().id() + "(" + ctx.channel().remoteAddress().toString() + ")" + " Session Removed");
        }


        Session removedSession = SessionsManager.GetInstance().RemoveSession(ctx);
        if (removedSession != null) {
            //SendSessoinDisconnectedToGameServer(removedSession.GetSessionID());
        }
    }

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

//        String playserId = "0000";
//
//        NetMessageProcessser.getInstance().addMessage(msg.toString());
//        SessionsManager.getInstance().addChannelHandlerContext(playserId, ctx);

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        Session session = SessionsManager.GetInstance().GetSession(ctx);
        if (session != null) {
            ArrayList<byte[]> arrMsgs = session.GetTrueMsg(req);
            for (byte[] cur : arrMsgs) {
                NetMsgBufferNode tmp = new NetMsgBufferNode(session.GetConnectionID(), cur);
                if (NetLog.useNetMsgLog) {
                    NetLog.Log(" Session: " + tmp.GetSessionID() + "  CmdID: " + tmp.GetID());
                }
                NetMsgBuffer.GetInstance().PutMsg(tmp);
            }
        }
        buf.release();

    }

    //通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("服务端接收数据完毕..");
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


//    private void SendSessoinDisconnectedToGameServer(int nSessionID)
//    {
//        Cmd_Session_Disconnected.Builder builder = Cmd_Session_Disconnected.newBuilder();
//        builder.setSessionid(nSessionID);
//        byte[] data = Session.ProtoBuffToMsgData(CmdID.CMD_SESSIONDISCONNECTED, builder.build().toByteArray());
//        NetMsgBufferNode node = new NetMsgBufferNode(nSessionID, data);
//        NetMsgBuffer.GetInstance().PutMsg(node);
//    }
}