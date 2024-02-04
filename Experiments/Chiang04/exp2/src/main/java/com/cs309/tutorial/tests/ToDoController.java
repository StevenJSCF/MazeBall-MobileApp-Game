package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.HashMap;

@RestController
@RequestMapping("/toDo") // Base URL for this controller
public class ToDoController {

    HashMap<String, Task> ToDoList = new  HashMap<>();

    @GetMapping("/getList")
    public @ResponseBody HashMap<String,Task> getAllTask() {
        return ToDoList;
    }

    @PostMapping("/addTask")
    public @ResponseBody String addTask(@RequestBody Task task) {
        System.out.println(task);
        ToDoList.put(task.getTitle(), task);
        return "New task created \n tittle: \"" + task.getTitle() + "\"";
    }

}