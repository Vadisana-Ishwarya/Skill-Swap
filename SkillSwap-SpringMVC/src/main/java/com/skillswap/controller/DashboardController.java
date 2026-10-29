package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.model.SkillMatch;
import com.skillswap.service.ExchangeService;
import com.skillswap.service.SkillService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DashboardController {

    private final SkillService skills;
    private final ExchangeService exchanges;

    public DashboardController(
            SkillService s,
            ExchangeService e) {

        skills = s;
        exchanges = e;
    }


    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) Integer searchSkillId,
            HttpSession s,
            Model m) {

        User u =
                (User) s.getAttribute("user");

        if (u == null) {
            return "redirect:/";
        }

        List<SkillMatch> matches =
                skills.matches(u.getId());

        Set<String> activeRequests =
                new HashSet<>();

        for (SkillMatch match : matches) {

            boolean active =
                    exchanges.hasActiveRequest(
                            u.getId(),
                            match.getUserId(),
                            match.getSkillId()
                    );

            if (active) {

                String key =
                        match.getUserId()
                        + "-"
                        + match.getSkillId();

                activeRequests.add(key);
            }
        }

        List<SkillMatch> searchResults = null;

        if (searchSkillId != null) {

            searchResults =
                    skills.searchUsersBySkill(
                            searchSkillId,
                            u.getId()
                    );

            for (SkillMatch result : searchResults) {

                boolean active =
                        exchanges.hasActiveRequest(
                                u.getId(),
                                result.getUserId(),
                                result.getSkillId()
                        );

                if (active) {

                    String key =
                            result.getUserId()
                            + "-"
                            + result.getSkillId();

                    activeRequests.add(key);
                }
            }
        }


        /*
         * Get accepted exchanges
         * for the logged-in user.
         */
        var activeExchanges =
                exchanges.activeExchanges(
                        u.getId()
                );


        m.addAttribute(
                "user",
                u
        );

        m.addAttribute(
                "skills",
                skills.all()
        );

        m.addAttribute(
                "mySkills",
                skills.mine(u.getId())
        );

        m.addAttribute(
                "matches",
                matches
        );

        m.addAttribute(
                "activeRequests",
                activeRequests
        );

        m.addAttribute(
                "searchResults",
                searchResults
        );

        m.addAttribute(
                "searchSkillId",
                searchSkillId
        );

        m.addAttribute(
                "incoming",
                exchanges.incoming(u.getId())
        );

        m.addAttribute(
                "outgoing",
                exchanges.outgoing(u.getId())
        );

        m.addAttribute(
                "activeExchanges",
                activeExchanges
        );


        String message =
                (String) s.getAttribute("message");

        if (message != null) {

            m.addAttribute(
                    "message",
                    message
            );

            s.removeAttribute("message");
        }

        return "dashboard";
    }


    // Add a skill by name
    @PostMapping("/skills/add")
    public String add(
            @RequestParam String skillName,
            @RequestParam String type,
            HttpSession s) {

        User u =
                (User) s.getAttribute("user");

        if (u != null) {

            try {

                skills.addByName(
                        u.getId(),
                        skillName,
                        type
                );

                s.setAttribute(
                        "message",
                        "Skill added successfully."
                );

            } catch (IllegalArgumentException e) {

                s.setAttribute(
                        "message",
                        e.getMessage()
                );
            }
        }

        return "redirect:/dashboard";
    }


    @PostMapping("/skills/delete")
    public String delete(
            @RequestParam int skillId,
            @RequestParam String type,
            HttpSession s) {

        User u =
                (User) s.getAttribute("user");

        if (u != null) {

            skills.delete(
                    skillId,
                    u.getId(),
                    type
            );
        }

        return "redirect:/dashboard";
    }


    @PostMapping("/exchange/send")
    public String send(
            @RequestParam int receiverId,
            @RequestParam int skillId,
            HttpSession s) {

        User u =
                (User) s.getAttribute("user");

        if (u != null) {

            try {

                exchanges.send(
                        u.getId(),
                        receiverId,
                        skillId
                );

                s.setAttribute(
                        "message",
                        "Exchange request sent successfully."
                );

            } catch (IllegalArgumentException e) {

                s.setAttribute(
                        "message",
                        e.getMessage()
                );
            }
        }

        return "redirect:/dashboard";
    }


    @PostMapping("/exchange/status")
    public String status(
            @RequestParam int requestId,
            @RequestParam String status,
            HttpSession s) {

        User u =
                (User) s.getAttribute("user");

        if (u != null) {

            try {

                exchanges.status(
                        requestId,
                        u.getId(),
                        status
                );

                s.setAttribute(
                        "message",
                        "Request status updated."
                );

            } catch (IllegalArgumentException e) {

                s.setAttribute(
                        "message",
                        e.getMessage()
                );
            }
        }

        return "redirect:/dashboard";
    }
}