package com.anson.localservices.service;

import com.anson.localservices.dto.Result;
import com.anson.localservices.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
