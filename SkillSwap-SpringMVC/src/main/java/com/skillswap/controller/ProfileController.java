package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.service.SkillService;
import com.skillswap.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    private final UserService users;
    private final SkillService skills;

    public ProfileController(UserService u, SkillService s) {
        users = u;
        skills = s;
    }

    // SHOW EDIT PROFILE PAGE
    @GetMapping("/profile/edit")
    public String editProfile(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);

        return "edit-profile";
    }


    // UPDATE PROFILE
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String bio,
            HttpSession session) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        User updatedUser =
                users.updateProfile(
                        user.getId(),
                        name,
                        bio
                );

        // Update session with new user information
        session.setAttribute(
                "user",
                updatedUser
        );

        return "redirect:/dashboard";
    }


    // VIEW ANOTHER USER'S PROFILE
    @GetMapping("/profile/view")
    public String viewProfile(
            @RequestParam int userId,
            HttpSession session,
            Model model) {

        User currentUser =
                (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/";
        }

        // Prevent viewing own profile through this page
        if (currentUser.getId() == userId) {
            return "redirect:/profile/edit";
        }

        // Get selected user
        User profileUser =
                users.get(userId);

        // Get selected user's skills
        model.addAttribute(
                "profileUser",
                profileUser
        );

        model.addAttribute(
                "profileSkills",
                skills.mine(userId)
        );

        return "view-profile";
    }
}