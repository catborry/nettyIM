package com.yyqdemao;

import com.yyqdemao.config.NettyInit;
import com.yyqdemao.config.StudentPOJO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class ClientMain {

    private static int port = 8989;

    private static String host = "127.0.0.1";

    public static String name = "";

    public static void run() throws InterruptedException {
        ChannelFuture sync = null;
        NioEventLoopGroup work = null;
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
                StudentPOJO.MyMessage stu = StudentPOJO.MyMessage.newBuilder().setDataType(StudentPOJO.MyMessage.DataType.studentType).setStudent(StudentPOJO.Student.newBuilder().setId(4).setName(name).setMessage(msg).build()).build();
                channel.writeAndFlush(stu);
            }
        } finally {
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            int i=0;
            for (String s : args) {
                if ("-help".equals(s)) {
                    System.out.println("-name   设置聊天昵称");
                } else if ("-name".equals(s)) {
                    if(args.length<=i+1){
                        System.out.println("请正确输入昵称");
                        return;
                    }
                    name = args[i+1];
                    try {
                        run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
        } else {

        }

    }
}
