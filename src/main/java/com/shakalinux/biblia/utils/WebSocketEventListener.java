package com.shakalinux.biblia.utils;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
        public Set<String> getOnlineUsers() {
            return onlineUsers;
        }


    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();

        if (username != null) {
            onlineUsers.add(username);


            UserStatusChange status = new UserStatusChange(username, "ONLINE");
            messagingTemplate.convertAndSend("/topic/user.status", status);
        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();

        if (username != null) {
            onlineUsers.remove(username);


            UserStatusChange status = new UserStatusChange(username, "OFFLINE");
            messagingTemplate.convertAndSend("/topic/user.status", status);
        }
    }
}

