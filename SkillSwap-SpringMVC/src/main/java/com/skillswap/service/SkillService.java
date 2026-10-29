package com.skillswap.service;

import com.skillswap.dao.SkillDAO;
import com.skillswap.model.Skill;
import com.skillswap.model.SkillMatch;
import com.skillswap.model.UserSkill;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillDAO dao;
    private final UserService users;

    public SkillService(
            SkillDAO d,
            UserService u) {

        dao = d;
        users = u;
    }


    // Get all available skills
    public List<Skill> all() {
        return dao.all();
    }


    // Add an existing skill
    public void add(
            int uid,
            int sid,
            String type) {

        users.get(uid);

        if (!type.equals("TEACH") &&
            !type.equals("LEARN")) {

            throw new IllegalArgumentException(
                    "Invalid skill type"
            );
        }

        if (dao.exists(uid, sid, type)) {

            throw new IllegalArgumentException(
                    "You already added this skill as "
                    + type
            );
        }

        dao.add(
                uid,
                sid,
                type
        );
    }


    // Add a skill using its name
    // If the skill does not exist, create it first
    public void addByName(
            int uid,
            String skillName,
            String type) {

        users.get(uid);

        // Validate skill type
        if (!type.equals("TEACH") &&
            !type.equals("LEARN")) {

            throw new IllegalArgumentException(
                    "Invalid skill type"
            );
        }

        // Validate skill name
        if (skillName == null ||
            skillName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please enter a skill name"
            );
        }

        skillName = skillName.trim();

        // Keep skill names reasonably sized
        if (skillName.length() > 100) {

            throw new IllegalArgumentException(
                    "Skill name cannot exceed 100 characters"
            );
        }

        // Check whether the skill already exists
        Skill skill =
                dao.findByName(skillName);

        // If it doesn't exist, create it
        if (skill == null) {

            skill =
                    dao.createSkill(skillName);
        }

        // Check whether user already has this skill
        if (dao.exists(
                uid,
                skill.getId(),
                type)) {

            throw new IllegalArgumentException(
                    "You already added this skill as "
                    + type
            );
        }

        // Add skill to user's skill list
        dao.add(
                uid,
                skill.getId(),
                type
        );
    }


    // Get skills belonging to a user
    public List<UserSkill> mine(int id) {
        return dao.mine(id);
    }


    // Find suggested matches
    public List<SkillMatch> matches(int id) {
        return dao.matches(id);
    }


    // Search users by skill
    public List<SkillMatch> searchUsersBySkill(
            int skillId,
            int currentUserId) {

        users.get(currentUserId);

        return dao.searchUsersBySkill(
                skillId,
                currentUserId
        );
    }


    // Delete a user's skill
    public void delete(
            int skillId,
            int userId,
            String type) {

        dao.delete(
                skillId,
                userId,
                type
        );
    }
}