package com.anson.localservices.service.impl;

import com.anson.localservices.entity.ShopType;
import com.anson.localservices.mapper.ShopTypeMapper;
import com.anson.localservices.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

}
