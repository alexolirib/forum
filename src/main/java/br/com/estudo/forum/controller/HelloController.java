package br.com.estudo.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody //para retornar o valor Hello world
    public String hello(){
        return "Hello world!";
    }
}
