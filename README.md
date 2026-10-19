# High-Concurrency Local Services Platform

A Java and Spring Boot backend for local-business discovery, user sessions, social features, and flash-sale voucher ordering. The main engineering focus is reducing synchronous order-path work and protecting database-backed reads with Redis caching.

## Core components

- **Flash-sale admission:** a Redis Lua script checks inventory and per-user purchase eligibility, decrements cached stock, and appends an order event in one script execution.
- **Asynchronous ordering:** a Redis Streams consumer reads events, checks for existing purchases, conditionally decrements MySQL inventory, persists orders, and acknowledges processed events. Pending messages are retried.
- **Concurrency control:** Redisson locks serialize order creation for an individual user; Redis-backed IDs identify order requests.
- **Caching:** the cache utility implements cache-aside reads, short-lived null entries, mutex-based rebuilds, and logical-expiration background refresh. The shop-read endpoint uses cache-aside by default; other strategies are available in `CacheClient`.
- **Local services:** REST endpoints cover shops, categories, vouchers, users, follows, posts, and image uploads. Redis GEO, sorted sets, bitmaps, and HyperLogLog support location and social features.

## Stack

Java 8-compatible source, Spring Boot 2.3, MyBatis-Plus, MySQL, Spring Data Redis, Lua, and Redisson. Use **JDK 11** and Maven 3.6+ for the existing dependency versions. Redis 6.2+ is recommended.

The message transport is **Redis Streams**, not RocketMQ. No latency or throughput guarantee is claimed; this version does not include a reproducible API benchmark.

## Build and test

```sh
mvn clean verify
```

The default build runs isolated unit tests without MySQL or Redis. Spring-context integration tests are tagged `integration` and excluded by default. With an isolated, populated test database and Redis available:

```sh
mvn -DexcludedGroups= -Dgroups=integration test
```

The integration suite writes test data and must not target production infrastructure.

## Local setup

1. Provision MySQL and Redis locally. Create a UTF-8 database named `local_services` and a dedicated database user.
2. Import `src/main/resources/db/schema.sql` into the new, empty database. It contains schema only, not profiles, phone numbers, third-party images, or copied review content.
3. Set `DB_PASSWORD` and any environment overrides below. The application does not automatically load `.env` files.
4. Initialize the order consumer group before starting the application:

   ```sh
   redis-cli XGROUP CREATE stream.orders g1 0 MKSTREAM
   ```

   An existing group returns `BUSYGROUP`; do not delete the stream. Supply your Redis connection/authentication options when applicable.
5. Start the server:

   ```sh
   mvn spring-boot:run
   ```

The default port is `8081`. Seed your own shops, users, and vouchers before testing business flows. This backend does not include a frontend or an Nginx configuration.

### Environment settings

- `DB_URL`: JDBC URL; defaults to a local `local_services` database.
- `DB_USER`: defaults to `local_services`.
- `DB_PASSWORD`: required; no credential is committed.
- `REDIS_HOST`, `REDIS_PORT`, `REDIS_DATABASE`: default to `localhost`, `6379`, and `0`.
- `REDIS_PASSWORD`: optional; defaults to no password for local development.
- `SERVER_PORT`: defaults to `8081`.
- `UPLOAD_DIR`: defaults to `./uploads`; use a writable directory.
- `APP_LOG_LEVEL`: defaults to `info`.

### Flash-sale flow

Register voucher metadata and stock through `POST /voucher/seckill`. Authenticated users request `POST /voucher-order/seckill/{voucherId}`. A returned order ID means admission to the asynchronous pipeline, not that the database order is already committed.

## Scope and limitations

This is a development backend, not a production-ready service. The current consumer uses a single configured group/consumer pair and requires stream initialization. Transactional boundaries, failed-order compensation, durable idempotency constraints, dead-letter handling, security hardening, and shutdown behavior need further work before production deployment. Authentication uses a development SMS-code placeholder and requires a real provider before deployment. Do not expose this application directly to the public internet.

The existing password utility uses salted MD5 and is unsuitable for production credentials; production authentication should use a modern adaptive password hash. File uploads also require additional validation and access controls before deployment.
