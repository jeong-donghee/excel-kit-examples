# excel-kit-examples

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

[`excel-kit-grid`](https://github.com/jeong-donghee/excel-kit)의 **권장 사용법(베스트 프랙티스)** 레퍼런스 앱과 **메모리 벤치마크**.
발행된 아티팩트(`io.github.jeong-donghee:excel-kit-grid:0.1.0`)를 실제 소비자처럼 의존한다.

## 실행

```bash
./mvnw spring-boot:run
```

| 엔드포인트 | 설명 |
|------------|------|
| `GET /sessions/excel` | ① 가장 단순 — `List<T>` 반환 + 행번호 컬럼 |
| `GET /sessions/wrapped/excel` | ② 공통 응답 래퍼(`ApiResponse<List<T>>`) 그대로 반환 |
| `GET /sessions/large/excel` | ③ 25만 행 → 시트 3개로 자동 분할 |

## 베스트 프랙티스 포인트

- **DTO** (`SessionRow`): 단순 값은 필드에 `@ExcelColumn`, 합치거나 계산하는 컬럼은 **메서드**에 `@ExcelColumn`.
- **컨트롤러** (`SessionController`): `@ExcelDownload`만 붙이고 평소처럼 목록/래퍼 반환.
- **공통 래퍼** (`ExcelKitConfig`): 래퍼 타입당 `ExcelDataExtractor` 빈 하나 등록.
- **컴파일 타임 검증**: `excel-kit-processor`를 애노테이션 프로세서로 붙여, 잘못된 `@ExcelDownload`를 빌드에서 잡는다(이 프로젝트 pom 참고).

## 메모리 벤치마크

행 수를 늘려도 피크 힙이 거의 일정함을 보여준다(SXSSF 스트리밍).

```bash
java -Xmx256m -cp <classpath> io.github.jeongdonghee.excelkit.examples.benchmark.MemoryBenchmark
```

### 측정 결과 (JVM `-Xmx256m`)

| 행 수 | 피크 힙 | 소요 시간 |
|------:|--------:|----------:|
| 100,000 | 33.7 MB | 0.3s |
| 500,000 | 33.5 MB | 1.0s |
| 1,000,000 | 33.7 MB | 2.0s |

**피크 힙이 행 수와 무관하게 ~34MB로 일정.** 100만 행도 256MB 힙에서 완료된다.

## 라이선스

[MIT](LICENSE)
