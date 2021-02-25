package com.yyqdemao;


import com.yyqdemao.config.NettyInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerMain {

    private static int port=8989;

    public static void run() throws InterruptedException {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup work=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();
        b.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyInit())
                .childOption(ChannelOption.SO_KEEPALIVE,true);
        b.bind(port).sync();
        System.out.printf("服务端启动完毕");
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
