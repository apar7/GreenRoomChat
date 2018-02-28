package com.chatExample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "index2.html";
    }

    @ResponseBody
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(principal.getName(),
                HttpStatus.OK);
    }

    @MessageMapping("/send")
    @SendTo("/topic/public")
    public String[] sendMessage(@Payload String[] message) {
        logger.info(message[0] + ": " + message[1]);
        Date instant = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        message[2] = sdf.format(instant);
        return message;
    }
/*
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
*/
}