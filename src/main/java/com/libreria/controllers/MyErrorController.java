package com.libreria.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleBadRequest() {
        return "error/400.html";
    }
    @GetMapping("/error")
    public String handleInternalServerError() {
        return "error/500.html";
    }

    @GetMapping("/error")
    public String handleForbbidenString() {
        return "error/403.html";
    }


}
