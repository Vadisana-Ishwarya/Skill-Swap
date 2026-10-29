package com.skillswap.controller;
import com.skillswap.model.User;import com.skillswap.service.UserService;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.*;import javax.servlet.http.HttpSession;
@Controller public class AuthController{
 private final UserService users;public AuthController(UserService u){users=u;}
 @GetMapping("/")public String home(){return "index";}
 @PostMapping("/register")public String register(@RequestParam String name,@RequestParam String email,@RequestParam String password,@RequestParam(required=false)String bio,Model m){try{users.register(new User(name,email,password,bio));m.addAttribute("message","Registration successful. Please login.");}catch(Exception e){m.addAttribute("error",e.getMessage());}return "index";}
 @PostMapping("/login")public String login(@RequestParam String email,@RequestParam String password,HttpSession s,Model m){try{s.setAttribute("user",users.login(email,password));return "redirect:/dashboard";}catch(Exception e){m.addAttribute("error",e.getMessage());return "index";}}
 @GetMapping("/logout")public String logout(HttpSession s){s.invalidate();return "redirect:/";}
}