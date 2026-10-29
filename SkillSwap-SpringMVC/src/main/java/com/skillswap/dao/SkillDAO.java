package com.skillswap.dao;

import com.skillswap.model.Skill;
import com.skillswap.model.SkillMatch;
import com.skillswap.model.UserSkill;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SkillDAO {

    // Get all available skills
    public List<Skill> all() {

        String sql =
                "SELECT SKILL_ID, SKILL_NAME " +
                "FROM SKILLS " +
                "ORDER BY SKILL_NAME";

        List<Skill> skills = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {

                Skill skill = new Skill();

                skill.setId(result.getInt("SKILL_ID"));
                skill.setName(result.getString("SKILL_NAME"));

                skills.add(skill);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error finding skills",
                    e
            );
        }

        return skills;
    }


    // Find a skill by name
    // Comparison is case-insensitive
    public Skill findByName(String skillName) {

        String sql =
                "SELECT SKILL_ID, SKILL_NAME " +
                "FROM SKILLS " +
                "WHERE UPPER(SKILL_NAME) = UPPER(?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, skillName.trim());

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    Skill skill = new Skill();

                    skill.setId(
                            result.getInt("SKILL_ID")
                    );

                    skill.setName(
                            result.getString("SKILL_NAME")
                    );

                    return skill;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error finding skill by name",
                    e
            );
        }

        return null;
    }


    // Create a new skill
    public Skill createSkill(String skillName) {

        String sql =
                "INSERT INTO SKILLS " +
                "(SKILL_ID, SKILL_NAME) " +
                "VALUES (SKILL_SEQ.NEXTVAL, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                new String[]{"SKILL_ID"}
                        )
        ) {

            statement.setString(
                    1,
                    skillName.trim()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error creating skill",
                    e
            );
        }

        // Get the skill again after creating it
        return findByName(skillName);
    }


    // Check whether user already has the same skill and type
    public boolean exists(
            int userId,
            int skillId,
            String type) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM SKILL_LIST " +
                "WHERE USER_ID = ? " +
                "AND SKILL_ID = ? " +
                "AND SKILL_TYPE = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, skillId);
            statement.setString(3, type);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return result.getInt(1) > 0;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error checking existing skill",
                    e
            );
        }

        return false;
    }


    // Add a skill for a user
    public void add(
            int userId,
            int skillId,
            String type) {

        String findSkill =
                "SELECT SKILL_NAME " +
                "FROM SKILLS " +
                "WHERE SKILL_ID = ?";

        String insertSkill =
                "INSERT INTO SKILL_LIST " +
                "(SKILL_ID, SKILL_NAME, SKILL_TYPE, USER_ID) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement findStatement =
                        connection.prepareStatement(findSkill)
        ) {

            findStatement.setInt(1, skillId);

            String skillName = null;

            try (ResultSet result =
                         findStatement.executeQuery()) {

                if (result.next()) {

                    skillName =
                            result.getString("SKILL_NAME");
                }
            }

            if (skillName == null) {

                throw new RuntimeException(
                        "Skill not found: " + skillId
                );
            }

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(insertSkill)
            ) {

                insertStatement.setInt(1, skillId);
                insertStatement.setString(2, skillName);
                insertStatement.setString(3, type);
                insertStatement.setInt(4, userId);

                insertStatement.executeUpdate();
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error adding skill",
                    e
            );
        }
    }


    // Get skills belonging to a user
    public List<UserSkill> mine(int userId) {

        String sql =
                "SELECT SKILL_ID, SKILL_NAME, SKILL_TYPE, USER_ID " +
                "FROM SKILL_LIST " +
                "WHERE USER_ID = ? " +
                "ORDER BY SKILL_NAME";

        List<UserSkill> list = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    UserSkill skill = new UserSkill();

                    skill.setId(
                            result.getInt("SKILL_ID")
                    );

                    skill.setSkillId(
                            result.getInt("SKILL_ID")
                    );

                    skill.setSkillName(
                            result.getString("SKILL_NAME")
                    );

                    skill.setType(
                            result.getString("SKILL_TYPE")
                    );

                    skill.setUserId(
                            result.getInt("USER_ID")
                    );

                    list.add(skill);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error finding user's skills",
                    e
            );
        }

        return list;
    }


    // Find users who TEACH skills
    // that the current user wants to LEARN
    public List<SkillMatch> matches(int userId) {

        String sql =
                "SELECT DISTINCT " +
                "u.USER_ID, " +
                "u.FULL_NAME, " +
                "u.EMAIL, " +
                "u.BIO, " +
                "otherSkill.SKILL_ID, " +
                "otherSkill.SKILL_NAME " +

                "FROM SKILL_USERS u " +

                "JOIN SKILL_LIST otherSkill " +
                "ON u.USER_ID = otherSkill.USER_ID " +

                "WHERE otherSkill.SKILL_TYPE = 'TEACH' " +

                "AND otherSkill.SKILL_NAME IN " +

                "(SELECT mySkill.SKILL_NAME " +
                " FROM SKILL_LIST mySkill " +
                " WHERE mySkill.USER_ID = ? " +
                " AND mySkill.SKILL_TYPE = 'LEARN') " +

                "AND u.USER_ID <> ? " +

                "ORDER BY u.FULL_NAME";

        List<SkillMatch> matches =
                new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    SkillMatch match =
                            new SkillMatch();

                    match.setUserId(
                            result.getInt("USER_ID")
                    );

                    match.setUserName(
                            result.getString("FULL_NAME")
                    );

                    match.setEmail(
                            result.getString("EMAIL")
                    );

                    match.setBio(
                            result.getString("BIO")
                    );

                    match.setSkillId(
                            result.getInt("SKILL_ID")
                    );

                    match.setSkillName(
                            result.getString("SKILL_NAME")
                    );

                    matches.add(match);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error finding matching users",
                    e
            );
        }

        return matches;
    }


    // Search users who TEACH a particular skill
    public List<SkillMatch> searchUsersBySkill(
            int skillId,
            int currentUserId) {

        String sql =
                "SELECT DISTINCT " +
                "u.USER_ID, " +
                "u.FULL_NAME, " +
                "u.EMAIL, " +
                "u.BIO, " +
                "sl.SKILL_ID, " +
                "sl.SKILL_NAME " +

                "FROM SKILL_USERS u " +

                "JOIN SKILL_LIST sl " +
                "ON u.USER_ID = sl.USER_ID " +

                "WHERE sl.SKILL_ID = ? " +
                "AND sl.SKILL_TYPE = 'TEACH' " +
                "AND u.USER_ID <> ? " +

                "ORDER BY u.FULL_NAME";

        List<SkillMatch> results =
                new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, skillId);
            statement.setInt(2, currentUserId);

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    SkillMatch match =
                            new SkillMatch();

                    match.setUserId(
                            result.getInt("USER_ID")
                    );

                    match.setUserName(
                            result.getString("FULL_NAME")
                    );

                    match.setEmail(
                            result.getString("EMAIL")
                    );

                    match.setBio(
                            result.getString("BIO")
                    );

                    match.setSkillId(
                            result.getInt("SKILL_ID")
                    );

                    match.setSkillName(
                            result.getString("SKILL_NAME")
                    );

                    results.add(match);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error searching users by skill",
                    e
            );
        }

        return results;
    }


    // Delete a skill belonging to a user
    public void delete(
            int skillId,
            int userId,
            String type) {

        String sql =
                "DELETE FROM SKILL_LIST " +
                "WHERE SKILL_ID = ? " +
                "AND USER_ID = ? " +
                "AND SKILL_TYPE = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, skillId);
            statement.setInt(2, userId);
            statement.setString(3, type);

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error deleting skill",
                    e
            );
        }
    }
}