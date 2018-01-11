package com.bossien.train.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/user")
public class AdminUserAction {

    @RequestMapping("/")
    public String admin_user() {
        return "admin/user";
    }
}
