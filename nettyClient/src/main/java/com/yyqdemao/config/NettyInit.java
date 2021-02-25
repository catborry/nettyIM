package com.yyqdemao.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class NettyInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("decoder",new StringDecoder(Charset.forName("UTF-8"))).addLast("encoder",new StringEncoder(Charset.forName("UTF-8"))).addLast(new NettyHandler());
//        socketChannel.pipeline().addLast(new NettyHandler());
    }
}
