package javaTestProj;
/*
 * client消息处理类
 */

import com.alan.handler.message.NetMsgBufferNode;
import com.alan.handler.message.Session;
import com.alan.log.NetLog;
import com.alan.proto.MsgHello;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    //处理服务端返回的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        Session session = new Session(0, null);
        ArrayList<byte[]> arrMsgs = session.GetTrueMsg(req);
        for (byte[] cur : arrMsgs) {
            NetMsgBufferNode tmp = new NetMsgBufferNode(session.GetConnectionID(), cur);
            if (NetLog.useNetMsgLog) {
                NetLog.Log(" Session: " + tmp.GetSessionID() + "  CmdID: " + tmp.GetID());
            }
            //NetMsgBuffer.GetInstance().PutMsg(tmp);
            MsgHello.Msg_Hello msg_hello = MsgHello.Msg_Hello.parseFrom(tmp.GetProtoMsg());
            System.out.println(msg_hello.toString());
        }
        buf.release();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}