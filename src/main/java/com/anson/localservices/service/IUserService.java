package com.anson.localservices.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anson.localservices.dto.LoginFormDTO;
import com.anson.localservices.dto.Result;
import com.anson.localservices.entity.User;

import javax.servlet.http.HttpSession;

public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

}
