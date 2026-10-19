package com.anson.localservices.service.impl;

import com.anson.localservices.entity.UserInfo;
import com.anson.localservices.mapper.UserInfoMapper;
import com.anson.localservices.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
