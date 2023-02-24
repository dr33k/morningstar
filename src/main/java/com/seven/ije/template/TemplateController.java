package com.seven.ije.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController{
    @GetMapping
    public String landing(){return "login";}
    @GetMapping("/login")
    public String login(){return "login";}
    @GetMapping("/dashboard")
    public String dashboard(){return "dashboard";}
    @GetMapping("/locationDetails")
    public String locationDetails(){ return "location_details";}

    @GetMapping("/voyageDetails")
    public String voyageDetails(){ return "voyage_details";}
}
