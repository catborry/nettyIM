package com.yyqdemao.config;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NettyHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组,管理所有channel
    //GlobalEventExecutor.INSTANCE 全局事件执行器,单例
    private static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Map<String,String> user=new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        readIdleTimes=0;
        Channel channel = ctx.channel();
        if(user.get(String.valueOf(channel.remoteAddress()))==null){
            // 按指定模式在字符串查找
            String pattern ="^\\[(.*)\\]";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(msg);
            if(m.find()){
                user.put(String.valueOf(channel.remoteAddress()),m.group(1));
            }
        }
//        channel.writeAndFlush(msg);
//        channels.writeAndFlush(msg);
        channels.forEach(channel1 -> {
            if (channel!=channel1){
                //不是当前channel 直接转发
                channel1.writeAndFlush(msg+"\n\n");
            }else{
                channel1.writeAndFlush("消息发送成功\n"+msg+"\n\n");
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
        channels.writeAndFlush("[客户端]"+user.get(String.valueOf(ctx.channel().remoteAddress()))+"离线了\n");
//        channels.remove(ctx.channel());
        System.out.println("size="+channels.size());
    }

    //新连接建立时调用,第一个执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        user.put(String.valueOf(ctx.channel().remoteAddress()),null);
        //新连接进入群发提示消息
        //channels.writeAndFlush("[客户端]"+ctx.channel().remoteAddress()+"加入\n");
    }
    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    //心跳
    int readIdleTimes;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        IdleStateEvent event = (IdleStateEvent)evt;

        String eventType = null;
        switch (event.state()){
            case READER_IDLE:
                eventType = "读空闲";
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType ="读写空闲";
                readIdleTimes ++; // 读空闲的计数加1
                ctx.channel().writeAndFlush("长时间不使用是会被提出链接的哦!!!");
                // 不处理
                break;
        }
        if(readIdleTimes > 3){
            System.out.println(" [server]读空闲超过3次，关闭连接");
            ctx.channel().writeAndFlush("您已被踢出!!!");
            ctx.channel().close();
        }
    }
}
