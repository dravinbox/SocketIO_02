package zry.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	public static void main(String[] args) throws InterruptedException {
		//1 第一个线程组，是用于接收Client连接的
		EventLoopGroup boosGroup =new NioEventLoopGroup();
		//2 第二个线程组，是用于实际的业务处理操作的
		EventLoopGroup workGroup =new NioEventLoopGroup();
		
		//3 ServerBootstrap 辅助类，进行一系列的配置
		ServerBootstrap b = new ServerBootstrap();
			//把两个工作线程组加入进来
		b.group(boosGroup,workGroup)
			//使用NioServerSocketChannel这种的类型通道
		.channel(NioServerSocketChannel.class)
			//使用ChildHandler去绑定具体的时间处理器
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ServerHandler());
				
			}
		})
		.option(ChannelOption.SO_BACKLOG, 128)
			//保证通道是活的
		.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		//4 绑定端口
		ChannelFuture f = b.bind(8765).sync();
		
		//5.阻塞
		f.channel().closeFuture().sync();
		
		//6 关闭两个线程组
		boosGroup.shutdownGracefully();
		workGroup.shutdownGracefully();

	}

}
