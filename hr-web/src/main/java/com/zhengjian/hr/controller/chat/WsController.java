package com.zhengjian.hr.controller.chat;

import com.zhengjian.hr.common.pojo.ChatMsg;
import com.zhengjian.hr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat") // Passed by the front desk /ws/chat Send a message in
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        // Send the received message to the designated person，If he has a subscription, he will receive，Need to be connected，I can't receive the previous message on the link behind
        User user = (User) authentication.getPrincipal();
        chatMsg.setFrom(user.getUsername()); // Currently logged in user
        chatMsg.setFromName(user.getName());
        chatMsg.setDate(new Date());
        // Passed by the front desk /user/queue/chat Subscribe to the news here，Added by default /user Prefix
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}
