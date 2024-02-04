package coms309.demo2.test.playerStore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}
