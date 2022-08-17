package com.seven.RailroadApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class TemplateController{
    @GetMapping
    public String landing(){return "login";}
    @GetMapping("/login")
    public String login(){return "login";}
    @GetMapping("/dashboard")
    public String dashboard(){return "dashboard";}

}
