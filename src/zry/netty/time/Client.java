package zry.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup workGroup=new NioEventLoopGroup();
		try {
			
			Bootstrap bootstrap= new Bootstrap();
			bootstrap.group(workGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast(new ClientHandler());
					
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture channelFuture=bootstrap.connect("127.0.0.1",8765).sync();
			channelFuture.channel().write(Unpooled.copiedBuffer("hello,world".getBytes()));
			channelFuture.channel().flush();
			
			
			channelFuture.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			workGroup.shutdownGracefully();
		}
	}

}
