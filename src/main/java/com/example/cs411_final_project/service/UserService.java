package com.example.cs411_final_project.service;

import com.example.cs411_final_project.dao.UserDAO;
import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User findUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }

    // 其他用户相关服务方法可以在这里添加
}
