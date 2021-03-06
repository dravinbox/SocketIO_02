package zry.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf buffer = (ByteBuf) msg;
			byte[] data = new byte[buffer.readableBytes()];
			buffer.readBytes(data);
			String request = new String(data, "utf-8");
			System.out.println("GetFromServer: " + request);
			
			

		} finally {
			// ((ByteBuf) msg).release();
			ReferenceCountUtil.release(msg);

		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
