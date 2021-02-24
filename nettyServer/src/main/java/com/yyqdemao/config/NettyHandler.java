package com.yyqdemao.config;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;

public class NettyHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组,管理所有channel
    //GlobalEventExecutor.INSTANCE 全局事件执行器,单例
    private static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //时间处理对象
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
//        channel.writeAndFlush(msg);
//        channels.writeAndFlush(msg);
        channels.forEach(channel1 -> {
            if (channel!=channel1){
                //不是当前channel 直接转发
                channel1.writeAndFlush("[客户]"+channel.remoteAddress()+"message: "+msg+"\n\n");
            }else{
                channel1.writeAndFlush("消息发送成功");
            }
        });
    }

    //channel 处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println(socketAddress+"上线了");
    }

    //channel 处于非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }

    //表示断开连接触发
    //handlerRemoved 执行会自动remove ChannelGroup
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.writeAndFlush("[客户端]"+ctx.channel().remoteAddress()+"离线了\n");
//        channels.remove(ctx.channel());
        System.out.println("size="+channels.size());
    }

    //新连接建立时调用,第一个执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("???");
        channels.add(ctx.channel());
        //新连接进入群发提示消息
        channels.writeAndFlush("[客户端]"+ctx.channel().remoteAddress()+"加入\n");
    }
    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
