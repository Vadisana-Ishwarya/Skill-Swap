package com.skillswap.dao;

import com.skillswap.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAO {

    public User findByEmail(String email) {

        String sql = "SELECT USER_ID, FULL_NAME, EMAIL, PASSWORD, BIO " +
                     "FROM SKILL_USERS " +
                     "WHERE LOWER(EMAIL) = LOWER(?)";

        return find(sql, email);
    }


    public User findById(int id) {

        String sql = "SELECT USER_ID, FULL_NAME, EMAIL, PASSWORD, BIO " +
                     "FROM SKILL_USERS " +
                     "WHERE USER_ID = ?";

        return find(sql, id);
    }


    private User find(String sql, Object value) {

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            if (value instanceof String) {
                statement.setString(1, (String) value);
            } else {
                statement.setInt(1, (Integer) value);
            }

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                User user = new User();

                user.setId(result.getInt("USER_ID"));
                user.setName(result.getString("FULL_NAME"));
                user.setEmail(result.getString("EMAIL"));
                user.setPassword(result.getString("PASSWORD"));
                user.setBio(result.getString("BIO"));

                return user;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error finding user: " + e.getMessage(),
                    e
            );
        }

        return null;
    }


    public int save(User user) {

        String sql = "INSERT INTO SKILL_USERS " +
                     "(USER_ID, FULL_NAME, EMAIL, PASSWORD, BIO) " +
                     "VALUES (SKILL_USER_SEQ.NEXTVAL, ?, ?, ?, ?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(
                            sql,
                            new String[]{"USER_ID"}
                    )
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail().toLowerCase());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getBio());

            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                return result.getInt(1);
            }

            throw new RuntimeException(
                    "Could not get generated user ID"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving user",
                    e
            );
        }
    }


    // UPDATE USER PROFILE
    public void updateProfile(int userId, String name, String bio) {

        String sql =
                "UPDATE SKILL_USERS " +
                "SET FULL_NAME = ?, BIO = ? " +
                "WHERE USER_ID = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);
            statement.setString(2, bio);
            statement.setInt(3, userId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating profile",
                    e
            );
        }
    }
}