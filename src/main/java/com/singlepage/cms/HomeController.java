package com.singlepage.cms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    // inject via application.properties
    @Value("${home.message}")
    private String message = "Hi";

    @RequestMapping(value="/")
    public String home(Map<String, Object> model) {
        model.put("message", this.message);
        model.put("timestamp",  new java.util.Date());
        return "home/home";
    }

}