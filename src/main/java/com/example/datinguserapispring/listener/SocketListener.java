package com.example.datinguserapispring.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.datinguserapispring.dto.chat.SeenMessage;
import com.example.datinguserapispring.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class SocketListener {

    private final ChatMessageRepository chatMessageRepository;

    public SocketListener(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @OnEvent("seen")
    public void onMessageSeen(SeenMessage seenMessage, AckRequest acknowledge) {
        seenMessage.getMessageIds().forEach(chatMessageRepository::setIsReadTrue);
        acknowledge.sendAckData(seenMessage);
    }
}
