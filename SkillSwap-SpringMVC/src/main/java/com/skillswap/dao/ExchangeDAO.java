package com.skillswap.dao;

import com.skillswap.model.ExchangeRequest;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class ExchangeDAO {

    public boolean existsActiveRequest(
            int senderId,
            int receiverId,
            int skillId) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM EXCHANGE_REQUESTS " +
                "WHERE SENDER_ID = ? " +
                "AND RECEIVER_ID = ? " +
                "AND SKILL_ID = ? " +
                "AND STATUS IN ('PENDING', 'ACCEPTED')";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setInt(3, skillId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error checking existing exchange request",
                    e
            );
        }

        return false;
    }


    public void create(
            int senderId,
            int receiverId,
            int skillId) {

        String sql =
                "INSERT INTO EXCHANGE_REQUESTS " +
                "(REQUEST_ID, SENDER_ID, RECEIVER_ID, SKILL_ID, STATUS, CREATED_AT) " +
                "VALUES (EXCHANGE_SEQ.NEXTVAL, ?, ?, ?, 'PENDING', SYSTIMESTAMP)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setInt(3, skillId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error creating exchange request",
                    e
            );
        }
    }


    public List<ExchangeRequest> list(
            int userId,
            boolean incoming) {

        String where = incoming
                ? "er.RECEIVER_ID = ?"
                : "er.SENDER_ID = ?";

        String sql =
                "SELECT er.REQUEST_ID, " +
                "er.SENDER_ID, " +
                "er.RECEIVER_ID, " +
                "s.FULL_NAME, " +
                "r.FULL_NAME, " +
                "er.SKILL_ID, " +
                "sk.SKILL_NAME, " +
                "er.STATUS, " +
                "TO_CHAR(er.CREATED_AT, 'YYYY-MM-DD HH24:MI') " +

                "FROM EXCHANGE_REQUESTS er " +

                "JOIN SKILL_USERS s " +
                "ON er.SENDER_ID = s.USER_ID " +

                "JOIN SKILL_USERS r " +
                "ON er.RECEIVER_ID = r.USER_ID " +

                "JOIN SKILLS sk " +
                "ON er.SKILL_ID = sk.SKILL_ID " +

                "WHERE " + where +

                " ORDER BY er.CREATED_AT DESC";

        List<ExchangeRequest> requests =
                new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    ExchangeRequest request =
                            new ExchangeRequest();

                    request.setId(result.getInt(1));
                    request.setSenderId(result.getInt(2));
                    request.setReceiverId(result.getInt(3));
                    request.setSenderName(result.getString(4));
                    request.setReceiverName(result.getString(5));
                    request.setSkillId(result.getInt(6));
                    request.setSkillName(result.getString(7));
                    request.setStatus(result.getString(8));
                    request.setCreatedAt(result.getString(9));

                    requests.add(request);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding exchange requests",
                    e
            );
        }

        return requests;
    }


    // Get accepted exchanges for the logged-in user
    public List<ExchangeRequest> activeExchanges(
            int userId) {

        String sql =
                "SELECT " +
                "er.REQUEST_ID, " +
                "er.SENDER_ID, " +
                "er.RECEIVER_ID, " +
                "sender.FULL_NAME, " +
                "receiver.FULL_NAME, " +
                "er.SKILL_ID, " +
                "sk.SKILL_NAME, " +
                "er.STATUS, " +
                "TO_CHAR(er.CREATED_AT, 'YYYY-MM-DD HH24:MI'), " +

                "CASE " +
                "WHEN er.SENDER_ID = ? " +
                "THEN receiver.EMAIL " +
                "ELSE sender.EMAIL " +
                "END AS OTHER_EMAIL, " +

                "CASE " +
                "WHEN er.SENDER_ID = ? " +
                "THEN receiver.BIO " +
                "ELSE sender.BIO " +
                "END AS OTHER_BIO, " +

                "CASE " +
                "WHEN er.SENDER_ID = ? " +
                "THEN receiver.FULL_NAME " +
                "ELSE sender.FULL_NAME " +
                "END AS OTHER_NAME " +

                "FROM EXCHANGE_REQUESTS er " +

                "JOIN SKILL_USERS sender " +
                "ON er.SENDER_ID = sender.USER_ID " +

                "JOIN SKILL_USERS receiver " +
                "ON er.RECEIVER_ID = receiver.USER_ID " +

                "JOIN SKILLS sk " +
                "ON er.SKILL_ID = sk.SKILL_ID " +

                "WHERE " +
                "(er.SENDER_ID = ? OR er.RECEIVER_ID = ?) " +

                "AND er.STATUS = 'ACCEPTED' " +

                "ORDER BY er.CREATED_AT DESC";

        List<ExchangeRequest> exchanges =
                new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, userId);
            statement.setInt(4, userId);
            statement.setInt(5, userId);

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    ExchangeRequest request =
                            new ExchangeRequest();

                    request.setId(result.getInt(1));
                    request.setSenderId(result.getInt(2));
                    request.setReceiverId(result.getInt(3));
                    request.setSenderName(result.getString(4));
                    request.setReceiverName(result.getString(5));
                    request.setSkillId(result.getInt(6));
                    request.setSkillName(result.getString(7));
                    request.setStatus(result.getString(8));
                    request.setCreatedAt(result.getString(9));

                    /*
                     * We use the existing ExchangeRequest object
                     * to store the other person's details.
                     */
                    request.setOtherName(
                            result.getString(12)
                    );

                    request.setOtherEmail(
                            result.getString(10)
                    );

                    request.setOtherBio(
                            result.getString(11)
                    );

                    exchanges.add(request);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding active exchanges",
                    e
            );
        }

        return exchanges;
    }


    public void status(
            int requestId,
            int userId,
            String status) {

        String sql =
                "UPDATE EXCHANGE_REQUESTS " +
                "SET STATUS = ? " +
                "WHERE REQUEST_ID = ? " +
                "AND RECEIVER_ID = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, requestId);
            statement.setInt(3, userId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating exchange request",
                    e
            );
        }
    }


    public void delete(
            int requestId,
            int senderId) {

        String sql =
                "DELETE FROM EXCHANGE_REQUESTS " +
                "WHERE REQUEST_ID = ? " +
                "AND SENDER_ID = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, requestId);
            statement.setInt(2, senderId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting exchange request",
                    e
            );
        }
    }
}