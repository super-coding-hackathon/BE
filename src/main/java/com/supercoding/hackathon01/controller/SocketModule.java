package com.supercoding.hackathon01.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.supercoding.hackathon01.dto.chat.Message;
import com.supercoding.hackathon01.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {

    private final SocketIOServer socketIOServer;
    private final SocketService socketService;


    public SocketModule(SocketIOServer socketIOServer, SocketService socketService) {
        this.socketIOServer = socketIOServer;
        this.socketService = socketService;
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
        socketIOServer.addEventListener("send_message", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return ((client, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessage(data.getRoom(), "read_message", client, data.getMsg());
        });
    }

    private ConnectListener onConnected() {
        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            log.info(client.getSessionId().toString() + " 님이 " + room + " 에 접속");
            client.joinRoom(room);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
    }

}
