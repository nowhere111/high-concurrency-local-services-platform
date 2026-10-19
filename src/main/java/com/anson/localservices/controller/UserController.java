package com.anson.localservices.controller;

import cn.hutool.core.bean.BeanUtil;
import com.anson.localservices.dto.LoginFormDTO;
import com.anson.localservices.dto.Result;
import com.anson.localservices.dto.UserDTO;
import com.anson.localservices.entity.User;
import com.anson.localservices.entity.UserInfo;
import com.anson.localservices.service.IUserInfoService;
import com.anson.localservices.service.IUserService;
import com.anson.localservices.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserInfoService userInfoService;

    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {

        return userService.sendCode(phone, session);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){

        return userService.login(loginForm, session);
    }

    @PostMapping("/logout")
    public Result logout(){

        return Result.fail("功能未完成");
    }

    @GetMapping("/me")
    public Result me(){

        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){

        UserInfo info = userInfoService.getById(userId);
        if (info == null) {

            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);

        return Result.ok(info);
    }

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId){

        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        return Result.ok(userDTO);
    }

    @PostMapping("/sign")
    public Result sign(){
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public Result signCount(){
        return userService.signCount();
    }
}
