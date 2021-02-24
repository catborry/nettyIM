package com.yyqdemao;

import com.yyqdemao.config.NettyInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class ClientMain {

    private static int port=8080;

    private static String host="127.0.0.1";

    public static void run() throws InterruptedException {
        ChannelFuture sync = null;
        NioEventLoopGroup work=null;
        try {
            work = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(work)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyInit());
            sync = b.connect(host, port).sync();
            Channel channel = sync.channel();
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                channel.writeAndFlush(msg + "\n\n");
            }
        }finally {
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
//        if(args.length>0){
//            for (String s:args) {
//               if("-help".equals(s)){
//
//               }
//            }
//        }
        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
