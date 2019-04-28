package javaTestProj;

import com.alan.handler.message.Session;
import com.alan.proto.CmdConst;
import com.alan.proto.CmdHello;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.SneakyThrows;

public class App {

    @SneakyThrows
    public static void main(String[] args) {

        NettyClient client = new NettyClient("127.0.0.1", 8080);
        //启动client服务
        client.start();

        Channel channel = client.getChannel();
        //消息体
//        RpcRequest request = new RpcRequest();
//        request.setId(UUID.randomUUID().toString());
//        request.setData("client.message");
        //channel对象可保存在map中，供其它地方发送消息

        CmdHello.Cmd_Hello build = CmdHello.Cmd_Hello.newBuilder().setMsg("aaaabbbbbb").build();
        byte[] bytes = Session.ProtoBuffToMsgData(CmdConst.Cmd_ID.DOBATTLE_VALUE, build.toByteArray());
        CmdHello.Cmd_Hello build2 = CmdHello.Cmd_Hello.newBuilder().setMsg("chat msg ########").build();
        byte[] bytes2 = Session.ProtoBuffToMsgData(CmdConst.Cmd_ID.CHAT_VALUE, build2.toByteArray());
//        session.SendMsg(333,);
        channel.writeAndFlush(Unpooled.copiedBuffer(bytes));
        channel.writeAndFlush(Unpooled.copiedBuffer(bytes2));

    }

}
