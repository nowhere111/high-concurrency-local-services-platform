package com.anson.localservices.service;

import com.anson.localservices.dto.Result;
import com.anson.localservices.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);
}
