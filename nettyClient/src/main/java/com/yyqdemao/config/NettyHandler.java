package com.yyqdemao.config;

import com.yyqdemao.ClientMain;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("链接建立成功");
        ctx.writeAndFlush("["+ClientMain.name+"]加入聊天室");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().writeAndFlush("["+ClientMain.name+"]退出聊天室");
    }

}
