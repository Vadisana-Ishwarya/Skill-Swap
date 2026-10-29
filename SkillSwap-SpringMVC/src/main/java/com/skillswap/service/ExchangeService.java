package com.skillswap.service;

import com.skillswap.dao.ExchangeDAO;
import com.skillswap.model.ExchangeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeService {

    private final ExchangeDAO dao;
    private final UserService users;

    public ExchangeService(
            ExchangeDAO d,
            UserService u) {

        dao = d;
        users = u;
    }


    public void send(
            int senderId,
            int receiverId,
            int skillId) {

        users.get(senderId);
        users.get(receiverId);

        if (senderId == receiverId) {

            throw new IllegalArgumentException(
                    "Cannot send request to yourself"
            );
        }

        if (dao.existsActiveRequest(
                senderId,
                receiverId,
                skillId)) {

            throw new IllegalArgumentException(
                    "You already have an active request for this skill."
            );
        }

        dao.create(
                senderId,
                receiverId,
                skillId
        );
    }


    public boolean hasActiveRequest(
            int senderId,
            int receiverId,
            int skillId) {

        return dao.existsActiveRequest(
                senderId,
                receiverId,
                skillId
        );
    }


    public List<ExchangeRequest> incoming(
            int userId) {

        return dao.list(
                userId,
                true
        );
    }


    public List<ExchangeRequest> outgoing(
            int userId) {

        return dao.list(
                userId,
                false
        );
    }


    // Get accepted exchanges
    public List<ExchangeRequest> activeExchanges(
            int userId) {

        users.get(userId);

        return dao.activeExchanges(
                userId
        );
    }


    public void status(
            int requestId,
            int userId,
            String status) {

        if (!status.equals("ACCEPTED") &&
            !status.equals("REJECTED")) {

            throw new IllegalArgumentException(
                    "Invalid status"
            );
        }

        dao.status(
                requestId,
                userId,
                status
        );
    }


    public void delete(
            int requestId,
            int senderId) {

        dao.delete(
                requestId,
                senderId
        );
    }
}