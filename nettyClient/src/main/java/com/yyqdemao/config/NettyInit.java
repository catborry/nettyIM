package com.yyqdemao.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class NettyInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("encoder",new ProtobufEncoder())
                .addLast("decoder",new ProtobufDecoder(StudentPOJO.MyMessage.getDefaultInstance()))
                .addLast(new NettyHandler());
//        socketChannel.pipeline().addLast(new NettyHandler());
    }
}
