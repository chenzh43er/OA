package com.example.demo;

import com.example.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @RequestMapping(value = "userList")
    public String userList(Model model) {
        List<User> userlist = new ArrayList<User>();
        userlist.add(new User(1, "武松", 10,"g"));
        userlist.add(new User(2, "潘金莲", 50,"z"));
        userlist.add(new User(3, "旺财", 100,"d"));
        model.addAttribute("userlist", userlist);
        return "userList";
    }
}
