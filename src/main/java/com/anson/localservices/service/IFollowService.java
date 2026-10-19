package com.anson.localservices.service;

import com.anson.localservices.dto.Result;
import com.anson.localservices.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result followCommons(Long id);
}
