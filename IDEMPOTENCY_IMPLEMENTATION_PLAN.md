# 북마크 생성 멱등성 구현 계획

## 목표
클라이언트가 `Idempotency-Key` 헤더에 고유한 키를 담아 북마크 생성을 요청할 경우, 동일한 키에 대해서는 항상 첫 번째 요청의 결과만 반환하도록 보장합니다.

## 체크리스트

- [x] **1단계: 멱등성 키 저장을 위한 데이터베이스 모델링**
  - 요청된 멱등성 키와 그에 대한 응답을 저장할 새로운 데이터베이스 테이블과 엔티티를 생성합니다.
  - [x] `IdempotencyKey` 엔티티 생성
  - [x] `IdempotencyKeyRepository` 생성

- [x] **2단계: AOP를 활용한 멱등성 처리 로직 구현**
  - 핵심 비즈니스 로직의 수정을 최소화하고 멱등성 처리를 공통 기능으로 분리하기 위해 AOP(관점 지향 프로그래밍)를 사용합니다.
  - [x] `@Idempotent` 애너테이션 생성
  - [x] `IdempotencyAspect` Aspect 생성

- [x] **3단계: 컨트롤러에 멱등성 적용**
  - `BookmarkController`의 `createBookmark` 메소드에 `@Idempotent` 애너테이션을 추가하여, 해당 API에 멱등성 처리 로직이 적용되도록 합니다.
  - [x] `BookmarkController`의 `createBookmark` 메소드에 `@Idempotent` 애너테이션 추가

## 멱등성 테스트 방법 (curl 사용)

### 1. 애플리케이션 실행 확인
가장 먼저, Spring Boot 애플리케이션이 실행 중인지 확인하세요.

### 2. 멱등성 키가 없는 일반 요청 (선택 사항)
멱등성 키 없이 요청했을 때 매번 새로운 북마크가 생성되는지 확인하고 싶다면 아래와 같이 시도할 수 있습니다.

```bash
curl -X POST http://localhost:8080/api/bookmarks \
-H "Content-Type: application/json" \
-d '{"title": "Test Bookmark without Idempotency", "url": "http://test.com/no-key", "description": "This should create a new one every time"}'
```
이 명령어를 여러 번 실행하면 `title`이 같은 여러 개의 북마크가 생성될 것입니다.

### 3. 멱등성 키를 포함한 요청
이제 멱등성 기능을 테스트할 차례입니다. 고유한 `Idempotency-Key`를 사용하여 북마크 생성 요청을 보냅니다.

```bash
curl -X POST http://localhost:8080/api/bookmarks \
-H "Content-Type: application/json" \
-H "Idempotency-Key: my-unique-idempotency-key-123" \
-d '{"title": "Idempotent Bookmark", "url": "http://idempotent.com/first", "description": "This should only be created once"}'
```

-   `-X POST`: HTTP POST 메소드를 사용합니다.
-   `http://localhost:8080/api/bookmarks`: 북마크 생성 API 엔드포인트입니다.
-   `-H "Content-Type: application/json"`: 요청 본문이 JSON 형식임을 알립니다.
-   `-H "Idempotency-Key: my-unique-idempotency-key-123"`: **이것이 핵심입니다.** `my-unique-idempotency-key-123`은 클라이언트가 생성하는 고유한 문자열입니다. 이 키를 통해 서버는 동일한 요청을 식별합니다. 실제 환경에서는 UUID 등을 사용하여 충분히 고유한 키를 생성해야 합니다.
-   `-d '...'`: 요청 본문(JSON 데이터)입니다.

### 4. 멱등성 확인
위 `3번`의 `curl` 명령어를 **동일한 `Idempotency-Key` 값으로** (예: `my-unique-idempotency-key-123`) 여러 번 실행하세요.

**기대 결과:**
-   **첫 번째 요청:** 서버는 북마크를 생성하고, 생성된 북마크 객체와 함께 `200 OK` 또는 `201 Created` 응답을 반환할 것입니다. 동시에 이 응답은 `idempotency_keys` 테이블에 `my-unique-idempotency-key-123`과 함께 저장됩니다.
-   **두 번째 이후 요청 (동일한 `Idempotency-Key` 사용):** 서버는 새 북마크를 생성하지 않고, `idempotency_keys` 테이블에 저장된 첫 번째 요청의 캐시된 응답을 즉시 반환할 것입니다. 응답 자체는 첫 번째 요청과 동일하게 보이지만, 서버 로그를 확인하면 실제 북마크 생성 로직이 다시 실행되지 않았음을 알 수 있습니다.

### 5. 데이터베이스 확인 (선택 사항)
데이터베이스(`idempotency_keys` 테이블과 `bookmark` 테이블)를 직접 조회하여 `my-unique-idempotency-key-123`에 해당하는 `bookmark`는 단 하나만 생성되었는지, 그리고 `idempotency_keys` 테이블에 해당 키와 응답이 잘 저장되어 있는지 확인해 볼 수 있습니다.
