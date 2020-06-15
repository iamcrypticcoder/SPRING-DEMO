package com.iamcrypticcoder.mongodbexample.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    public IndexController() {

    }

    @GetMapping(value = {"/", "index.html"})
    public String indexPage() {
        System.out.println("indexPage");
        return "index";
    }

}
