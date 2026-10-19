package com.anson.localservices.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anson.localservices.dto.Result;
import com.anson.localservices.entity.SeckillVoucher;
import com.anson.localservices.entity.Voucher;
import com.anson.localservices.mapper.VoucherMapper;
import com.anson.localservices.service.ISeckillVoucherService;
import com.anson.localservices.service.IVoucherService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.anson.localservices.utils.RedisConstants.SECKILL_STOCK_KEY;

@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryVoucherOfShop(Long shopId) {

        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);

        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {

        save(voucher);

        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);

        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
    }
}
