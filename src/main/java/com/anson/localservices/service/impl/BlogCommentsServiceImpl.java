package com.anson.localservices.service.impl;

import com.anson.localservices.entity.BlogComments;
import com.anson.localservices.mapper.BlogCommentsMapper;
import com.anson.localservices.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
