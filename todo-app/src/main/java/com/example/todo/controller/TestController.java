// TestController.java
package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("/csrf-test")
    public String showTestForm() {
        return "csrf-test";
    }

    @PostMapping("/submit-with-csrf")
    @ResponseBody
    public String handleWithCsrf(@RequestParam String data) {
        return "CSRFあり送信成功: " + data;
    }

    @PostMapping("/submit-no-csrf")
    @ResponseBody
    public String handleNoCsrf(@RequestParam String data) {
        return "CSRFなし送信成功: " + data;
    }
}
