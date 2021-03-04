package com.yyqdemao.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class NettyInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                //指定解码对象
                .addLast(new ProtobufVarint32FrameDecoder())//添加protobuff解码器
                .addLast("decoder", new ProtobufDecoder(StudentPOJO.MyMessage.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast("encoder", new ProtobufEncoder())
//                .addLast("decoder",new StringDecoder(Charset.forName("UTF-8")))
//                .addLast("encoder",new StringEncoder(Charset.forName("UTF-8")))
                .addLast(new IdleStateHandler(2,2,2, TimeUnit.MINUTES))
                .addLast(new NettyHandler());
//        socketChannel.pipeline().addLast(new NettyHandler());
    }
}
