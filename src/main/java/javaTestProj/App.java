package javaTestProj;

import com.alan.entity.RpcRequest;
import io.netty.channel.Channel;
import lombok.SneakyThrows;

import java.util.UUID;

public class App {

    @SneakyThrows
    public static void main(String[] args) {

        NettyClient client = new NettyClient("127.0.0.1", 8080);
        //启动client服务
        client.start();

        Channel channel = client.getChannel();
        //消息体
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setData("client.message");
        //channel对象可保存在map中，供其它地方发送消息
        channel.writeAndFlush(request);

    }

}
