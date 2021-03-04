package com.yyqdemao.config;

import com.yyqdemao.ClientMain;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyHandler extends SimpleChannelInboundHandler<StudentPOJO.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.MyMessage s) throws Exception {
        if(s.getDataType()==StudentPOJO.MyMessage.DataType.studentType){
            StudentPOJO.Student student = s.getStudent();
            System.out.println("["+student.getName()+"]: "+student.getMessage()+"\n当前在线人数"+student.getNumber());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("链接建立成功");
        ctx.writeAndFlush("["+ClientMain.name+"]加入聊天室");
        //发送一个Student对象到服务器
//        StudentPOJO.Student stu = StudentPOJO.Student.newBuilder().setId(4).setName("王五").setMessage("asdhkashdksja").build();
//
//        ctx.writeAndFlush(stu);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().writeAndFlush("["+ClientMain.name+"]退出聊天室");
    }

}
