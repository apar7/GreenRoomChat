package com.greenroomchat.config;

import com.greenroomchat.userdata.UserListUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;

@Component
public class ConnectionListener {

    private HashSet<String> userList;

    private SimpMessageSendingOperations messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionListener.class);

    @Autowired
    public void setUserList(HashSet<String> userList) {
        this.userList = userList;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void connectListener(SessionConnectedEvent event) {
        String username = event.getUser().getName();
        if (username != null) {
            userList.add(username);
            logger.info(username + " connected to the server");
            UserListUpdate userListUpdate = new UserListUpdate(true, username);
            messagingTemplate.convertAndSend("/users/public", userListUpdate);
        }
        userList.forEach(user -> logger.info("Userlist: " + user));
    }

    @EventListener
    public void disconnectListener(SessionDisconnectEvent event) {

        String username = event.getUser().getName();
        if (username != null) {
            userList.remove(username);
            logger.info(username + " disconnected from the server");
            UserListUpdate userListUpdate = new UserListUpdate(false, username);
            messagingTemplate.convertAndSend("/users/public", userListUpdate);
        }
    }
}
