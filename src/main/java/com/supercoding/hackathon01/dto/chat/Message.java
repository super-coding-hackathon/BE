package com.supercoding.hackathon01.dto.chat;

import com.supercoding.hackathon01.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private MessageType messageType;
    private String msg;
    private String room;

    public Message(MessageType messageType, String msg) {
        this.messageType = messageType;
        this.msg = msg;
    }
}