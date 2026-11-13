# Changelog

## 2.0.0 - 2026-11-13

First release under the `ru.kode` group (`ru.kode:yolk`, `ru.kode:yolk-sqldelight-extensions`). Package names are unchanged (`ru.dimsuz.yolk`).

### Breaking changes
- `Ticker.now`, `KeyTimestamps.accessAt` and `KeyTimestamps.updatedAt` now use `kotlin.time.Instant` instead of the deprecated `kotlinx.datetime.Instant`. Custom `Ticker` implementations must be updated. Clock usage moved to `kotlin.time.Clock`.
- Maven group changed from `ru.dimsuz` to `ru.kode`.
- `yolk-sqldelight-extensions` now targets SqlDelight 2.x (`app.cash.sqldelight`) instead of 1.x (`com.squareup.sqldelight`).
- Requires Kotlin 2.2+ (built with 2.3.20) and kotlinx-datetime 0.7.1+.
- iOS targets are now `iosX64`, `iosArm64` and `iosSimulatorArm64`.

### Other
- Updated to Kotlin 2.3.20, kotlinx-coroutines 1.10.2, kotlinx-datetime 0.7.1, SqlDelight 2.4.1, Gradle 9.4.1.
- Publishing moved to Maven Central Portal.

## 1.1.1 - 2023-02-13

* Add `iosSimulatorArm64` target

## 1.1.0 - 2022-01-09

* Add `yolk-sqldelight-extensions` module which provides `Query<*>.asCachedFlow()`
* Add `events` flow, rework logging to work on top of it instead of being hardcoded into Cache

## 1.0.0 - 2021-12-23

* Migrate to KMM
