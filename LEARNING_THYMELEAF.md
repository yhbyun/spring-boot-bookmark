# Thymeleaf란 무엇인가?

이 문서는 Spring Boot에서 가장 많이 사용되는 템플릿 엔진인 **Thymeleaf(타임리프)**에 대해 설명합니다.

## 1. 핵심 요약
**Thymeleaf**는 자바 기반의 **서버 사이드 템플릿 엔진**입니다.
쉽게 말해, **HTML 파일에 자바 데이터를 넣어서 동적인 웹 페이지를 만들어주는 도구**입니다.

- **HTML 친화적**: 순수 HTML 구조를 유지하면서 동작합니다. (브라우저에서 파일을 직접 열어도 깨지지 않음)
- **Spring Boot 권장**: Spring 진영에서 JSP 대신 공식적으로 밀고 있는 기술입니다.

---

## 2. 왜 사용하는가? (vs JSP, React/Vue)

### A. Natural Templates (자연스러운 템플릿)
Thymeleaf의 가장 큰 장점입니다.
JSP는 `<% ... %>` 같은 코드가 섞여 있어서, 서버 없이 브라우저에서 열면 화면이 깨지거나 코드가 그대로 노출됩니다.
하지만 Thymeleaf는 **표준 HTML 태그의 속성(Attribute)**을 사용하기 때문에, 서버 없이 열어도 디자인이 유지됩니다.

**예시:**
```html
<!-- 서버 없이 열었을 때: "홍길동"이 보임 -->
<!-- 서버에서 실행했을 때: user.name 값(예: "김철수")으로 바뀜 -->
<p th:text="${user.name}">홍길동</p>
```

### B. 서버 사이드 렌더링 (SSR)
React나 Vue.js는 브라우저(클라이언트)에서 화면을 그리지만(CSR), Thymeleaf는 **서버에서 완성된 HTML을 만들어서** 브라우저에 던져줍니다.
- **장점**: 검색 엔진 최적화(SEO)에 유리하고, 초기 로딩 속도가 빠릅니다.
- **단점**: 화면이 복잡하게 변하는 앱(예: 페이스북, 인스타그램)을 만들기에는 React보다 불편할 수 있습니다.

## 3. 동작 원리

1.  **Controller**: 데이터를 준비해서 `Model`에 담습니다.
    ```java
    model.addAttribute("message", "반갑습니다!");
    return "home"; // home.html을 찾아라!
    ```
2.  **Thymeleaf Engine**: `home.html`을 읽고, `th:`가 붙은 태그를 찾습니다.
3.  **Rendering**: `th:text="${message}"` 부분을 "반갑습니다!"로 바꿔치기합니다.
4.  **Response**: 완성된 순수 HTML을 브라우저로 보냅니다.

## 4. 주요 문법 맛보기

- **텍스트 출력**: `<span th:text="${data}"></span>`
- **반복문**: `<li th:each="item : ${list}" th:text="${item.name}"></li>`
- **조건문**: `<div th:if="${isLoggedIn}">로그인 되었습니다</div>`
- **링크**: `<a th:href="@{/login}">로그인</a>`

## 5. 결론
Thymeleaf는 **백엔드 개발자가 혼자서 빠르게 웹 서비스를 만들 때** 가장 강력한 도구 중 하나입니다. 복잡한 프론트엔드 프레임워크를 몰라도 HTML과 약간의 문법만 알면 훌륭한 웹 사이트를 만들 수 있습니다.
