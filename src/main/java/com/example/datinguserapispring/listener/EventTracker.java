package com.example.datinguserapispring.listener;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.datinguserapispring.dto.chat.request.MessageRequest;
import com.example.datinguserapispring.service.chat.impl.ChatMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventTracker implements SocketEventTracker{

    private final Map<String, Boolean> eventMap = new ConcurrentHashMap<>();
    private final SocketIOServer socketServer;
    private final ChatMessageListener chatMessageListener;


    public void   createSocketEvent(String eventKey) {
        if (!isEventRegistered(eventKey)) {
            this.socketServer.addEventListener(eventKey, MessageRequest.class, chatMessageListener);
            markEventAsRegistered(eventKey);
            log.info("Event created with name {}", eventKey);
        } else {
            log.warn("Event already exists with name {}", eventKey);
        }
    }

    private boolean isEventRegistered(String eventKey) {
        return eventMap.containsKey(eventKey);
    }

    private void markEventAsRegistered(String eventKey) {
        eventMap.put(eventKey, true);
    }

}
