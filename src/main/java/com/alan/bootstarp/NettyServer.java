package com.alan.bootstarp;

import com.alan.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private ChannelFuture serverChannelFuture;

    public void bind(int port) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); //bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //workerGroup就是childGroup,是负责处理Channel(通道)的I/O事件

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024) //初始化服务端可连接队列,指定了队列的大小128
                .childOption(ChannelOption.SO_KEEPALIVE, true) //保持长连接
                .childOption(ChannelOption.TCP_NODELAY, true)//将小的数据包组装为更大的帧然后进行发送，提升网络效率，（会延时）
                .childOption(ChannelOption.SO_REUSEADDR, true) //重用地址,可重复监听同一端口，不会提示占用
                //定义接收或者传输的系统缓冲区buf的大小
                .childOption(ChannelOption.SO_RCVBUF, 65536)
                .childOption(ChannelOption.SO_SNDBUF, 65536)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//使用对象池
                // 绑定客户端连接时候触发操作
                .childHandler(new ServerChannelInitializer());

        //绑定监听端口，调用sync同步阻塞方法等待绑定操作完   (用这种方式会阻塞main线程)
        //ChannelFuture future = sb.bind(port).sync();

        serverChannelFuture = sb.bind(port).sync();
        serverChannelFuture.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);

        if (serverChannelFuture.isSuccess()) {
            log.info("服务端启动成功");
        } else {
            log.info("服务端启动失败");
            serverChannelFuture.cause().printStackTrace();
            bossGroup.shutdownGracefully(); //关闭线程组
            workerGroup.shutdownGracefully();
        }

        //成功绑定到端口之后,给channel增加一个 管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程。
        //serverChannelFuture.channel().closeFuture().sync();

    }
}