package javaTestProj;

import com.alan.handler.message.Session;
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
        byte[] bytes = Session.ProtoBuffToMsgData(333, build.toByteArray());
//        session.SendMsg(333,);
        channel.writeAndFlush(Unpooled.copiedBuffer(bytes));

    }

}
