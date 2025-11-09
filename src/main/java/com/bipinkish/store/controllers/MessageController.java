package com.bipinkish.store.controllers;

import com.bipinkish.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayHello(){
        return new Message("Testing 123",3);
    }
}
