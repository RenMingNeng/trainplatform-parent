package com.bossien.train.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping("/common/script")
    public String common_script() {
        return "common/script";
    }
    @RequestMapping("/common/style")
    public String common_style() {
        return "common/style";
    }
    @RequestMapping("/common/menu")
    public String common_menu() {
        return "common/menu";
    }

}
