package com.anson.localservices;

import com.anson.localservices.entity.Shop;
import com.anson.localservices.utils.CacheClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CacheClientTest {
    private StringRedisTemplate redis;
    private ValueOperations<String, String> values;
    private Function<Long, Shop> database;
    private CacheClient cache;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        redis = mock(StringRedisTemplate.class);
        values = mock(ValueOperations.class);
        database = mock(Function.class);
        when(redis.opsForValue()).thenReturn(values);
        cache = new CacheClient(redis);
    }

    @Test
    void cacheHitDoesNotReadDatabase() {
        when(values.get("cache:shop:7")).thenReturn("{\"id\":7,\"name\":\"Example Shop\"}");
        Shop result = cache.queryWithPassThrough("cache:shop:", 7L, Shop.class, database, 30L, TimeUnit.MINUTES);
        assertEquals(7L, result.getId());
        assertEquals("Example Shop", result.getName());
        verifyNoInteractions(database);
    }

    @Test
    void cachedNullDoesNotReadDatabase() {
        when(values.get("cache:shop:7")).thenReturn("");
        assertNull(cache.queryWithPassThrough("cache:shop:", 7L, Shop.class, database, 30L, TimeUnit.MINUTES));
        verifyNoInteractions(database);
    }

    @Test
    void missingRecordReceivesShortNegativeCache() {
        when(database.apply(7L)).thenReturn(null);
        assertNull(cache.queryWithPassThrough("cache:shop:", 7L, Shop.class, database, 30L, TimeUnit.MINUTES));
        verify(database).apply(7L);
        verify(values).set("cache:shop:7", "", 2L, TimeUnit.MINUTES);
    }

    @Test
    void databaseHitPopulatesCacheWithRequestedTtl() {
        Shop shop = new Shop();
        shop.setId(7L);
        when(database.apply(7L)).thenReturn(shop);
        assertSame(shop, cache.queryWithPassThrough("cache:shop:", 7L, Shop.class, database, 30L, TimeUnit.MINUTES));
        verify(values).set(eq("cache:shop:7"), contains("\"id\":7"), eq(30L), eq(TimeUnit.MINUTES));
    }
}
