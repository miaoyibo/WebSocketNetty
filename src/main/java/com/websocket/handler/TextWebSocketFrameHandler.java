package com.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	private final ChannelGroup group;

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) { // 如果该事件表示握手成功，则从该ChannelPipeline中移除HttpRequestHandler，因为将不会接收到任何HTTP消息了
			ctx.pipeline().remove(HttpRequestHandler.class); // 通知所有已经连接的WebSocket客户端新的客户端连接上了
			//group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined")); // 将新的WebSocket
			group.writeAndFlush(new TextWebSocketFrame("有人进来了"));																					// Channel添加到ChannelGroup中，以便它可以接收到所有的消息
			group.add(ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		//ByteBuf content = msg.content();
		//String text = msg.text();
		group.writeAndFlush(msg.retain());

	}

}
