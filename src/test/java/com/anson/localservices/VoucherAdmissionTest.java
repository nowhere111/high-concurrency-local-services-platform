package com.anson.localservices;

import com.anson.localservices.dto.Result;
import com.anson.localservices.dto.UserDTO;
import com.anson.localservices.service.impl.VoucherOrderServiceImpl;
import com.anson.localservices.utils.RedisIdWorker;
import com.anson.localservices.utils.UserHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VoucherAdmissionTest {
    private VoucherOrderServiceImpl service;
    private StringRedisTemplate redis;

    @BeforeEach
    void setUp() {
        service = new VoucherOrderServiceImpl();
        redis = mock(StringRedisTemplate.class);
        RedisIdWorker ids = mock(RedisIdWorker.class);
        when(ids.nextId("order")).thenReturn(9001L);
        ReflectionTestUtils.setField(service, "stringRedisTemplate", redis);
        ReflectionTestUtils.setField(service, "redisIdWorker", ids);
        UserDTO user = new UserDTO();
        user.setId(42L);
        UserHolder.saveUser(user);
    }

    @AfterEach
    void tearDown() {
        UserHolder.removeUser();
    }

    @SuppressWarnings("unchecked")
    private void scriptReturns(long code) {
        when(redis.execute(any(RedisScript.class), anyList(), eq("7"), eq("42"), eq("9001"))).thenReturn(code);
    }

    @Test
    void acceptedRequestReturnsGeneratedOrderId() {
        scriptReturns(0L);
        Result result = service.seckillVoucher(7L);
        assertTrue(result.getSuccess());
        assertEquals(9001L, result.getData());
    }

    @Test
    void depletedStockRejectsRequest() {
        scriptReturns(1L);
        Result result = service.seckillVoucher(7L);
        assertFalse(result.getSuccess());
        assertNull(result.getData());
    }

    @Test
    void duplicatePurchaseRejectsRequest() {
        scriptReturns(2L);
        Result result = service.seckillVoucher(7L);
        assertFalse(result.getSuccess());
        assertNull(result.getData());
    }
}
