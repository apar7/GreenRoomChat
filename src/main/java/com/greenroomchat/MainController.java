package com.greenroomchat;

import com.greenroomchat.userdata.ChatUserDetailsService;
import com.greenroomchat.userdata.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

@Controller
public class MainController {

    private ChatUserDetailsService chatUserDetailsService;

    private HashSet<String> userList;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public void setChatUserDetailsService(ChatUserDetailsService chatUserDetailsService) {
        this.chatUserDetailsService = chatUserDetailsService;
    }

    @Autowired
    public void setUserList(HashSet<String> userList) {
        this.userList = userList;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "index2.html";
    }


    @GetMapping("/login")
    public String login() {
        return "register.html";
    }

    @ResponseBody
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createAccount(@RequestBody User user) {
        logger.info("Trying to register a new user: " + user.getUsername());
        try {
            chatUserDetailsService.save(user);
            logger.info("User created");
        } catch (DataIntegrityViolationException | IllegalArgumentException exc){
            logger.info(exc.getMessage());
            return new ResponseEntity<>(false,null,HttpStatus.OK);
        }
        return new ResponseEntity<>(true, null,
                HttpStatus.CREATED);
    }

    @ResponseBody
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(principal.getName(),
                HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public ResponseEntity<HashSet<String>> getUserList() {
        return new ResponseEntity<>(userList,
                HttpStatus.OK);
    }

    @MessageMapping("/send")
    @SendTo("/messages/public")
    public String[] sendMessage(@Payload String[] message) {
        logger.info(message[1] + ": " + message[2]);
        message[2] = message[2].replace("<", "&lt;");
        Date instant = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        message[0] = sdf.format(instant);
        return message;
    }
}