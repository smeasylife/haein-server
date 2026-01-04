# Haein 쇼핑몰 서버

## 📋 프로젝트 개요

Haein은 Spring Boot 3.5.3 기반의 e-commerce 쇼핑몰 백엔드 서버입니다. RESTful API 아키텍처를 따르며, 사용자 인증, 상품 관리, 주문, 리뷰, Q&A 등 쇼핑몰의 핵심 기능을 제공합니다.

**API 명세서**: 별도의 `API.md` 파일에 상세한 API 엔드포인트 문서가 있습니다.

---

## 🛠️ 기술 스택

### Core
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Build**: Gradle (Kotlin DSL)

### Database & Cache
- **RDBMS**: MySQL (JPA/Hibernate)
- **Cache**: Redis (세션 관리, 인증 코드 캐싱)

### Security
- **Spring Security 6**: JWT 기반 인증/인가
- **OAuth 2.0**: 카카오 소셜 로그인
- **Password Encryption**: BCrypt

### Communication
- **Email**: Spring Boot Mail (Gmail SMTP)
- **API**: RESTful API

---

## 📁 패키지 구조

```
src/main/java/ksm/haein/
├── auth/               # 인증 관련
│   └── kakao/         # 카카오 OAuth
├── config/            # 설정
│   ├── security/     # 시큐리티 설정
│   └── redis/        # Redis 설정
├── exception/         # 예외 처리
│   └── handler/      # 글로벌 예외 핸들러
├── coupon/            # 쿠폰
├── item/              # 상품
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   └── enums/
├── like/              # 좋아요
├── qna/               # Q&A
├── review/            # 리뷰
├── mail/              # 메일 발송
├── cart/              # 장바구니
└── user/              # 사용자 (회원)
    └── exception/     # 사용자 관련 커스텀 예외
```

---

## 🎯 코딩 컨벤션 & 패턴

### 1. Entity 패턴
- 모든 Entity는 **Lombok** 사용:
  ```java
  @Entity
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public class Item {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String name;
      // ...
  }
  ```

### 2. Repository 패턴
- `JpaRepository` 상속
- 커스텀 쿼리 메서드는 명확한 네이밍:
  ```java
  public interface ItemRepository extends JpaRepository<Item, Long> {
      Optional<Item> findByName(String name);
      Page<ItemData> findItemByPageWithoutLike(Pageable pageable);
  }
  ```

### 3. Service 패턴
- 클래스 레벨 `@Transactional(readOnly = true)`
- 쓰기 메서드는 `@Transactional` 명시
- 의존성은 `final` + `@RequiredArgsConstructor`:
  ```java
  @Service
  @RequiredArgsConstructor
  @Transactional(readOnly = true)
  public class ItemService {
      private final ItemRepository itemRepository;
      private final CategoryRepository categoryRepository;

      @Transactional
      public Long createItem(ItemCreateRequest request) {
          // ...
      }
  }
  ```

### 4. Controller 패턴
- `@RestController` + `@RequiredArgsConstructor`
- 인증이 필요한 경우: `@AuthenticationPrincipal CustomUser`
- Validation: `@Valid` + DTO:
  ```java
  @RestController
  @RequiredArgsConstructor
  public class ItemController {
      private final ItemService itemService;

      @PostMapping("/items")
      public ResponseEntity<Long> createItem(
          @Valid @RequestBody ItemCreateRequest request
      ) {
          Long itemId = itemService.createItem(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
      }
  }
  ```

### 5. DTO 패턴
- **간단한 데이터**: `record` 사용
- **복잡한 데이터**: `@Data` class 사용
- Validation 어노테이션 활용:
  ```java
  public record ItemCreateRequest(
      @NotBlank(message = "상품명은 필수입니다.")
      String name,

      @NotNull(message = "가격은 필수입니다.")
      @Positive(message = "가격은 양수여야 합니다.")
      Integer price
  ) {}
  ```

### 6. Exception Handling

**글로벌 예외 처리:**
- `@RestControllerAdvice` 사용한 중앙 집중식 예외 처리
- **GlobalExceptionHandler** (`src/main/java/ksm/haein/exception/handler/GlobalExceptionHandler.java`)

**커스텀 예외:**
- `MemberAlreadyExistsException` - 이메일 중복 (406 Not Acceptable)
- `MailSendException` - 이메일 발송 실패
- `CartAlreadyExistsException` - 장바구니 중복
- `KakaoAccessTokenRequestException` - 카카오 OAuth 토큰 요청 실패

**JPA 기본 예외:**
- `EntityNotFoundException` - 엔티티 조회 실패 시

**사용 예시:**
```java
// Service에서 예외 발생
throw new EntityNotFoundException("Item not found: " + itemId);
throw new MemberAlreadyExistsException("Email already exists: " + email);

// GlobalExceptionHandler가 자동으로 처리
// 클라이언트에는 적절한 HTTP Status와 에러 메시지 반환
```

---

## 🗄️ 데이터베이스 관계

### 주요 Entity 관계
```
Item (1) ────< (N) ItemPicture
Item (N) >───< (M) Category (via ItemCategory)
Item (1) ────< (N) Like
Item (1) ────< (N) Review
Item (1) ────< (N) Question

Member (1) ────< (N) Like
Member (1) ────< (N) Review
Member (1) ────< (N) Question
Member (1) ────< (N) Cart

Item (N) >───< (M) Member (via Cart)
```

### CascadeType 설정
- **REMOVE**: 부모 삭제 시 자식도 삭제 (ItemPicture, Like, Review, Question)
- **LAZY fetch**: 연관 관계는 지연 로딩 (ManyToOne 관계)

---

## 🔐 보안 및 인증

### 세션 관리
- **Redis 기반 세션**: Spring Session + Redis 사용
- 세션 TTL 설정으로 자동 만료
- 분산 환경에서도 세션 공유 가능

### 역할 (Roles)
| 역할 | 설명 |
|------|------|
| `ROLE_USER` | 일반 사용자 - 상품 조회, 장바구니, 리뷰 작성 등 |
| `ROLE_ADMIN` | 관리자 - 상품 등록/수정, Q&A 답변, 쿠폰 관리 등 |

### 인증 방식
- **로컬 인증**: 이메일 + 비밀번호 (BCrypt 암호화)
- **카카오 OAuth**: 소셜 로그인 지원

### Controller에서 인증 확인
```java
// 인증된 사용자 정보 접근
@GetMapping("/items")
public List<ItemData> getItems(
    @RequestParam int page,
    @AuthenticationPrincipal CustomUser customUser  // 인증된 사용자
) {
    if (customUser == null) {
        // 비회원 처리
        return itemService.getItemDataWithoutLike(page);
    }
    // 회원 처리
    return itemService.getItemDataWithLike(page, customUser.getId());
}
```

### 접근 제어
- 인증 필요 없는 엔드포인트: `permitAll()` 설정
- 인증 필요한 엔드포인트: `@AuthenticationPrincipal`으로 사용자 확인
- 관리자 전용 엔드포인트: ROLE_ADMIN 권한 확인 (추가 예정)

---

## 📝 개발 가이드

### 새로운 기능 추가 시

1. **Entity 생성** (데이터베이스 스키마)
2. **Repository 생성** (데이터 접근)
3. **DTO 생성** (요청/응답)
4. **Service 구현** (비즈니스 로직)
5. **Controller 구현** (API 엔드포인트)
6. **커스텀 예외 필요 시 생성** (예외 처리)
7. **GlobalExceptionHandler에 예외 핸들러 추가** (필요 시)
8. **테스트 코드 작성** (단위 테스트)

### 주요 의존성 주의사항

- **Entity 관계**: `@ManyToOne`은 LAZY fetch 사용
- **일관성**: 모든 Entity는 Builder 패턴 사용
- **Transaction**: 읽기는 `readOnly = true`, 쓰기는 별도 `@Transactional`
- **Validation**: 모든 Request DTO는 `@Valid` 검증
- **Exception Handler**: 새로운 커스텀 예외는 `GlobalExceptionHandler`에 등록

### 커스텀 예외 만들기
```java
// 1. 예외 클래스 생성
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}

// 2. GlobalExceptionHandler에 핸들러 추가
@ExceptionHandler(CustomException.class)
public ResponseEntity<String> handleCustomException(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
}

// 3. Service에서 사용
throw new CustomException("Something went wrong");
```

### Redis 활용
- 이메일 인증 코드 저장 (TTL 5분)
- 세션 관리
- (추가 예정) 캐싱

---

## 🧪 테스트

### 테스트 패턴
- 단위 테스트: Service layer
- 통합 테스트: Controller layer (MockMvc)
- Repository 테스트: @DataJpaTest

### 테스트 코드 위치
- `src/test/java/ksm/haein/`
- 각 패키지별 테스트 코드 동일한 구조

---

## 🚀 실행 방법

### 개발 환경
```bash
# PostgreSQL/MySQL 실행
# Redis 실행
./gradlew bootRun
```

### 환경 변수
- `application.yml` 또는 환경 변수로 설정
- DB 연결 정보
- Redis 연결 정보
- OAuth 클라이언트 정보

---

## 📚 추가 문서

- **API 명세서**: `API.md` (모든 REST API 엔드포인트)
- **데이터베이스 스키마**: `DATABASE_SCHEMA.md` (전체 테이블 구조, ERD, 관계)
- **배포 가이드**: (추가 예정)
