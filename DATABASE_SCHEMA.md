# Haein 쇼핑몰 데이터베이스 스키마

## 데이터베이스 개요

Haein 쇼핑몰은 **MySQL** 데이터베이스를 사용하며, **JPA/Hibernate**를 통해 객체 관계형 매핑(ORM)을 수행합니다. 총 **14개의 테이블**로 구성되어 있습니다.

---

## 테이블 목록

### 1. 사용자 (Users)

#### Member (회원)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 회원 ID |
| nickname | VARCHAR | NOT NULL | 닉네임 |
| email | VARCHAR | UNIQUE | 이메일 |
| phone_number | VARCHAR | NULLABLE | 전화번호 |
| role | ENUM | - | 역할 (ROLE_USER, ROLE_ADMIN) |
| created_at | TIMESTAMP | - | 가입일 |
| point | INTEGER | - | 포인트 |

#### Credential (인증 정보)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 인증 ID |
| identity_provider | ENUM | - | 인증 제공자 (LOCAL, KAKAO) |
| password | VARCHAR | - | 비밀번호 (암호화됨) |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID (1:1) |

---

### 2. 상품 (Products)

#### Item (상품)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 상품 ID |
| name | VARCHAR | - | 상품명 |
| price | INTEGER | - | 원가 |
| sale_price | INTEGER | - | 할인가 |
| shipping_price | INTEGER | - | 배송비 |
| size | VARCHAR | - | 사이즈 |
| color | VARCHAR | - | 색상 |
| information | TEXT | - | 상품 정보 |
| created_at | TIMESTAMP | - | 등록일 |

#### Category (카테고리)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 카테고리 ID |
| name | ENUM | - | 카테고리명 (NEW, BEST, SALE, SPRING, FALL, SUMMER, WINTER) |

#### ItemCategory (상품-카테고리 매핑)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 매핑 ID |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |
| category_id | BIGINT | FK (Category) → Category.id | 카테고리 ID |

#### ItemPicture (상품 이미지)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 이미지 ID |
| url | VARCHAR | - | 이미지 URL |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |

---

### 3. 활동 (User Activity)

#### Cart (장바구니)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 장바구니 ID |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID |

#### Like (좋아요)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 좋아요 ID |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID |

#### Review (리뷰)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 리뷰 ID |
| content | TEXT | - | 리뷰 내용 |
| created_at | TIMESTAMP | - | 작성일 |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID |

#### ReviewComment (리뷰 댓글)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 댓글 ID |
| comment | TEXT | - | 댓글 내용 |
| created_at | TIMESTAMP | - | 작성일 |
| review_id | BIGINT | FK (Review) → Review.id | 리뷰 ID (1:1) |

#### ReviewPicture (리뷰 이미지)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 이미지 ID |
| url | VARCHAR | - | 이미지 URL |
| review_id | BIGINT | FK (Review) → Review.id | 리뷰 ID |

#### Question (Q&A 질문)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 질문 ID |
| title | VARCHAR | - | 질문 제목 |
| content | TEXT | - | 질문 내용 |
| answer | TEXT | - | 답변 내용 |
| created_at | TIMESTAMP | - | 작성일 |
| item_id | BIGINT | FK (Item) → Item.id | 상품 ID |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID |

---

### 4. 프로모션 (Promotions)

#### Coupon (쿠폰)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 쿠폰 ID |
| name | VARCHAR | NOT NULL | 쿠폰명 |
| discount_type | ENUM | - | 할인 타입 (PERCENT, FIXED_AMOUNT) |
| discount_value | INTEGER | - | 할인 값 |
| start_time | TIMESTAMP | - | 시작일 |
| end_time | TIMESTAMP | - | 종료일 |

#### MemberCoupon (회원-쿠폰 매핑)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 발급 ID |
| created_at | TIMESTAMP | - | 발급일 |
| member_id | BIGINT | FK (Member) → Member.id | 회원 ID |
| coupon_id | BIGINT | FK (Coupon) → Coupon.id | 쿠폰 ID |

---

## ERD (Entity Relationship Diagram)

### 주요 관계

```
Member (1) ──── (1:1) ────> Credential
Member (1) ──── (1:N) ────> Like
Member (1) ──── (1:N) ────> Review
Member (1) ──── (1:N) ────> Question
Member (1) ──── (1:N) ────> Cart
Member (1) ──── (1:N) ────> MemberCoupon

Item (1) ──── (1:N) ────> ItemPicture
Item (N) ──── (N:M) ────> Category (via ItemCategory)
Item (1) ──── (1:N) ────> Like
Item (1) ──── (1:N) ────> Review
Item (1) ──── (1:N) ────> Question

Review (1) ──── (1:1) ────> ReviewComment
Review (1) ──── (1:N) ────> ReviewPicture

Coupon (1) ──── (1:N) ────> MemberCoupon
```

### 관계 유형 상세

| 관계 | 유형 | 설명 |
|------|------|------|
| Member - Credential | 1:1 | 회원당 하나의 인증 정보 |
| Member - Like | 1:N | 회원은 여러 상품을 좋아요 가능 |
| Member - Review | 1:N | 회원은 여러 리뷰 작성 가능 |
| Member - Cart | 1:N | 회원은 여러 상품을 장바구니에 추가 가능 |
| Member - MemberCoupon | 1:N | 회원은 여러 쿠폰 보유 가능 |
| Item - Category | N:M | 상품은 여러 카테고리, 카테고리는 여러 상품 (ItemCategory로 매핑) |
| Item - ItemPicture | 1:N | 상품은 여러 이미지 보유 |
| Item - Like | 1:N | 상품은 여러 좋아요 받을 수 있음 |
| Item - Review | 1:N | 상품은 여러 리뷰 받을 수 있음 |
| Item - Question | 1:N | 상품은 여러 Q&A 받을 수 있음 |
| Review - ReviewComment | 1:1 | 리뷰당 하나의 댓글 (관리자 답변) |
| Review - ReviewPicture | 1:N | 리뷰는 여러 이미지 보유 가능 |
| Coupon - MemberCoupon | 1:N | 쿠폰은 여러 회원에게 발급 가능 |

---

## ENUM 타입

### Role (역할)
- `ROLE_USER` - 일반 사용자
- `ROLE_ADMIN` - 관리자

### IdentityProvider (인증 제공자)
- `LOCAL` - 로컬 회원가입
- `KAKAO` - 카카오 OAuth

### CategoryName (카테고리명)
- `NEW` - 신상품
- `BEST` - 베스트
- `SALE` - 세일 상품
- `SPRING` - 봄 시즌
- `SUMMER` - 여름 시즌
- `FALL` - 가을 시즌
- `WINTER` - 겨울 시즌

### DiscountType (할인 타입)
- `PERCENT` - 퍼센트 할인
- `FIXED_AMOUNT` - 고정 금액 할인

---

## 제약조건 및 규칙

### Unique Constraints (고유 제약조건)
- `Member.email` - 이메일 중복 불가

### Not Null Constraints (NULL 불가)
- `Member.nickname`
- `Coupon.name`

### Cascade Rules (연삭제 규칙)
- **Item 삭제 시**: ItemPicture, ItemCategory, Like, Review, Question 자동 삭제
- **Member 삭제 시**: Like, Review, MemberCoupon 자동 삭제
- **Category 삭제 시**: ItemCategory 자동 삭제
- **Coupon 삭제 시**: MemberCoupon 자동 삭제

### Fetch Strategy (로딩 전략)
- **@ManyToOne**: LAZY (지연 로딩) - 연관 엔티티를 실제 사용할 때 쿼리 실행
- **@OneToOne**: LAZY (지연 로딩)

---

## 인덱스

JPA가 자동으로 생성하는 기본 인덱스:
- 모든 **Primary Key** (id 컬럼)
- 모든 **Foreign Key** (member_id, item_id, category_id 등)
- **Member.email** (Unique 제약조건으로 인한 인덱스)

---

## 데이터베이스 관리

### 마이그레이션
- JPA의 **auto-ddl** 설정 사용 (개발 환경)
- **validate**: 엔티티와 스키마 검증만 수행
- **update**: 엔티티 변경사항을 DB에 반영
- **create**: 시작 시 스키마 재생성
- **create-drop**: 종료 시 스키마 삭제

### 백업 및 복구
- 정기적 MySQL 덤프 권장
- Redis 세션 데이터는 별도 백업 필요 없음 (TTL 기반 자동 소멸)

---

## 추가 정보

- **ORM Framework**: Spring Data JPA / Hibernate
- **Database Engine**: MySQL 8.0+
- **Cache**: Redis (세션 관리, 인증 코드)
- **Character Set**: UTF-8
- **Timezone**: UTC

데이터베이스 스키마 변경 시 Entity 파일 수정 후 JPA가 자동으로 DDL을 생성합니다.
