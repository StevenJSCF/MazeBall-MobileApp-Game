package com.example.Game_Backend.Player;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}
