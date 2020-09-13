package edu.dgut.controller;

import edu.dgut.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @RequestMapping("/showPic")
    public String showPicPage(){
        return "showPic";
    }
    @RequestMapping("/share")
    public String sharePic(){
        return "uploadFile";
    }

}
