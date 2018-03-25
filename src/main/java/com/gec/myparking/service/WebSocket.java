package com.gec.myparking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 */
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

	private static final Logger log = LoggerFactory.getLogger(WebSocket.class);


	private Session session;

	private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		webSocketSet.add(this);
		log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
	}

	@OnMessage
	public void onMessage(String message) {
		log.info("【websocket消息】收到客户端发来的消息:{}", message);
		//心跳检测返回
		sendMessage("HeartBeat");
	}

	public void sendMessage(String message) {
		for (WebSocket webSocket: webSocketSet) {
			log.info("【websocket消息】广播消息, message={}", message);
			try {
				webSocket.session.getBasicRemote().sendText(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("【websocket消息】发生错误:{}"+error.getMessage());
		error.printStackTrace();
	}



}