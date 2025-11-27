# JPA와 데이터베이스 테이블 자동 생성 원리

이 문서는 Spring Boot JPA를 사용할 때 데이터베이스 테이블이 어떻게 자동으로 생성되는지 설명합니다. 팀원들과 공유하여 JPA의 동작 원리를 이해하는 데 도움이 되길 바랍니다.

## 1. 핵심 요약
우리가 DB에 `CREATE TABLE` 쿼리를 날리지 않았음에도 `bookmark` 테이블이 생성된 이유는 **JPA의 두 가지 기능** 덕분입니다.

1.  **`@Entity` 어노테이션**: 클래스를 테이블과 매핑
2.  **`ddl-auto` 설정**: 애플리케이션 실행 시 테이블 자동 생성/수정

---

## 2. 상세 설명

### A. `@Entity` 어노테이션
자바 클래스 위에 `@Entity`를 붙이면, JPA(Hibernate)는 이 클래스가 데이터베이스 테이블과 1:1로 연결된다고 인식합니다.

```java
// src/main/java/com/example/bookmark/entity/Bookmark.java

@Entity // <-- "이 클래스는 DB 테이블입니다"라고 선언
public class Bookmark {
    @Id // Primary Key (기본키)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    // ... 나머지 필드들
}
```
위 코드를 보고 JPA는 `Bookmark` 클래스 이름과 동일한 `bookmark`라는 테이블이 필요하다는 것을 알게 됩니다.

### B. `application.properties`의 `ddl-auto` 설정
`src/main/resources/application.properties` 파일에 있는 설정이 실제로 테이블을 만드는 행동을 결정합니다.

```properties
spring.jpa.hibernate.ddl-auto=update
```

이 설정값(`update`)은 애플리케이션이 시작될 때 다음과 같이 동작합니다:
1.  DB에 연결해서 `bookmark` 테이블이 있는지 확인합니다.
2.  **없으면**: `CREATE TABLE bookmark (...)` 쿼리를 실행하여 테이블을 만듭니다.
3.  **있으면**: 자바 클래스(`Bookmark`)에 새로운 필드가 추가되었는지 확인하고, 추가되었다면 `ALTER TABLE`로 컬럼을 추가합니다. (기존 데이터는 유지됨)

### 다른 옵션들 (참고)
- **`create`**: 시작할 때마다 기존 테이블을 **삭제(DROP)**하고 새로 만듭니다. (데이터 유실 주의!)
- **`create-drop`**: 시작할 때 만들고, 애플리케이션이 종료될 때 테이블을 삭제합니다. (테스트용)
- **`validate`**: 테이블과 자바 클래스가 일치하는지 확인만 하고, 다르면 에러를 발생시킵니다.
- **`none`**: 아무것도 하지 않습니다. (운영 환경 권장)

## 3. 결론
개발 초기 단계에서는 `update` 모드를 사용하면 DB 스키마 변경을 신경 쓰지 않고 자바 코딩에만 집중할 수 있어 생산성이 매우 높습니다. 하지만 **운영 환경(Production)에서는 절대 `create`나 `update`를 사용하면 안 됩니다.** (실수로 데이터를 날리거나, 의도치 않은 스키마 변경이 일어날 수 있음)
