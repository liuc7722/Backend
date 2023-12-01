package com.example.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.example.dto.UserLoginRequest;
import com.example.dto.UserRegisterRequest;

import jakarta.validation.Valid;

@Component
public class UserService {
    
    private final static Logger log = LoggerFactory.getLogger(UserService.class); 

    @Autowired
    private UserDao userDao;

    // 註冊
    public Integer register(@Valid UserRegisterRequest userRegisterRequest) {
        User user = userDao.getUserByUsername(userRegisterRequest.getUserName());
        
        if(user != null){ // 假設此帳號被註冊過
            log.warn("該 帳號 {} 已經被 註冊", userRegisterRequest.getUserName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 強制停止這次的請求(回傳400)
        }
        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    // 使用ID查詢User
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    // 登入
    public User login(@Valid UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByUsername(userLoginRequest.getUsername());
        
        if(user == null){ // 若帳號不存在
            return null;
        } 
        else if(!user.getPassword().equals(userLoginRequest.getPassword())){ // 密碼錯誤
            return null;
        }else {
            return user;
        }   
    }

    
}