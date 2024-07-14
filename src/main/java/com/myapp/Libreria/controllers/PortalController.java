package com.myapp.Libreria.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rom")
public class PortalController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

}
