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
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) { // ������¼���ʾ���ֳɹ�����Ӹ�ChannelPipeline���Ƴ�HttpRequestHandler����Ϊ��������յ��κ�HTTP��Ϣ��
			ctx.pipeline().remove(HttpRequestHandler.class); // ֪ͨ�����Ѿ����ӵ�WebSocket�ͻ����µĿͻ�����������
			//group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined")); // ���µ�WebSocket
			group.writeAndFlush(new TextWebSocketFrame("���˽�����"));																					// Channel��ӵ�ChannelGroup�У��Ա������Խ��յ����е���Ϣ
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
