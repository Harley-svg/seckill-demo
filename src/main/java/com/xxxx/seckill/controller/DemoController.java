package com.xxxx.seckill.controller;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Harley
 * @create 2022-10-01 17:12
 */
@RequestMapping("/demo")
@Controller
public class DemoController {
    public static void main(String[] args) {

    }

    /**
     * 测试页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","fenglinhai");
        return "hello";

    }
}
