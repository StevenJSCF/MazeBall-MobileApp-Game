package coms309;

import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
class WelcomeController {

    HashMap<String,Person> peopleList=new HashMap<String,Person>();
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/add/{a}/{b}")
    public String add(@PathVariable String a, @PathVariable String b) {
        return a+"+"+b+"="+Integer.toString(Integer.parseInt(a)+Integer.parseInt(b));
    }
}
