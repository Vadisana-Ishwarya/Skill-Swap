package com.skillswap.service;

import com.skillswap.dao.UserDAO;
import com.skillswap.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO dao;

    public UserService(UserDAO d) {
        dao = d;
    }


    public User register(User u) {

        if (dao.findByEmail(u.getEmail()) != null) {

            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        return dao.findById(
                dao.save(u)
        );
    }


    public User login(String e, String p) {

        User u = dao.findByEmail(e);

        if (u == null ||
            !u.getPassword().equals(p)) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return u;
    }


    public User get(int id) {

        User u = dao.findById(id);

        if (u == null) {

            throw new IllegalArgumentException(
                    "User not found"
            );
        }

        return u;
    }


    // UPDATE USER PROFILE
    public User updateProfile(
            int userId,
            String name,
            String bio) {

        if (name == null ||
            name.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name cannot be empty"
            );
        }

        dao.updateProfile(
                userId,
                name.trim(),
                bio
        );

        return dao.findById(userId);
    }
}