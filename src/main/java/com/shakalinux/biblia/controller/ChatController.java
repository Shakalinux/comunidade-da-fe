package com.shakalinux.biblia.controller;

import com.shakalinux.biblia.model.Message;
import com.shakalinux.biblia.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void processMessage(Message msg) {
        messageService.save(msg);
        if (msg.getReceiver() != null && !msg.getReceiver().isBlank()) {

            String receiver = msg.getReceiver();
            String sender = msg.getSender();

            messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", msg);
            messagingTemplate.convertAndSendToUser(sender, "/queue/messages", msg);

            long unreadCount = messageService.countUnreadMessagesFromSender(receiver, sender);
            Map<String, Object> notificationPayload = new HashMap<>();
            notificationPayload.put("sender", sender);
            notificationPayload.put("count", unreadCount);

            messagingTemplate.convertAndSendToUser(receiver, "/queue/notifications", notificationPayload);

            return;
        }
        messagingTemplate.convertAndSend("/topic/chat", msg);
    }


    @MessageMapping("/chat.typing")
    public void handleTypingStatus(Map<String, Object> typingStatus) {
        String receiver = (String) typingStatus.get("receiver");

        messagingTemplate.convertAndSendToUser(
                receiver,
                "/queue/typing",
                typingStatus
        );
    }

    @MessageMapping("/chat.read")
    public void handleReadStatus(Map<String, String> readReceipt) {
        String reader = readReceipt.get("receiver");
        String originalSender = readReceipt.get("sender");


        messageService.markMessagesAsRead(reader, originalSender);


        List<Integer> readMessageIds = messageService.findSentMessageIds(reader, originalSender);


        Map<String, Object> readPayload = new HashMap<>();
        readPayload.put("sender", reader);
        readPayload.put("readIds", readMessageIds);

        messagingTemplate.convertAndSendToUser(
                originalSender,
                "/queue/read-status",
                readPayload
        );


        Map<String, Object> notificationZero = new HashMap<>();
        notificationZero.put("sender", originalSender);
        notificationZero.put("count", 0L);
        messagingTemplate.convertAndSendToUser(reader, "/queue/notifications", notificationZero);
    }
}