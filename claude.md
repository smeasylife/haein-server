# Haein 쇼핑몰 서버

## 개요

Haein은 Spring Boot 기반의 쇼핑몰 서버로, 현대적인 e-commerce 플랫폼의 핵심 기능들을 제공합니다. 사용자들은 편리하게 쇼핑을 즐기고, 관리자는 효율적으로 상품과 고객을 관리할 수 있습니다.

### 주요 기능
- 🔐 **사용자 인증**: 로컬 회원가입 및 카카오 OAuth 로그인
- 🛍️ **상품 관리**: 상품 목록, 상세 정보, 카테고리별 조회
- 🛒 **쇼핑 카트**: 장바구니 기능
- ❤️ **좋아요 시스템**: 상품 좋아요/취소 기능
- ⭐ **리뷰 시스템**: 상품 리뷰 및 댓글 기능
- ❓ **고객 지원**: Q&A 질문답변 시스템
- 🎫 **프로모션**: 쿠폰 발급 및 관리

### 기술 스택
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Security**: Spring Security + Custom Authentication
- **Database**: MySQL + JPA/Hibernate
- **Cache**: Redis
- **Mail**: Spring Boot Mail
- **Build**: Gradle

---

## API 명세서

### 🔐 인증 (Authentication)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/auth/kakao/login` | `{ "code": "string" }` | `"Login successful"` |

### 👤 회원관리 (Member)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/signup` | `{ "nickname": "string", "email": "string", "password": "string", "phoneNumber": "string" }` | `204 NO CONTENT` |
| POST | `/signup/send-code` | `email=string` (query) | `"인증 번호 전송 성공"` |
| POST | `/signup/verify-code` | `{ "email": "string", "code": "string" }` | `"인증 성공"` |

### 🛍️ 상품 (Items)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| GET | `/items?page={page}` | - | `[{ "id": 1, "name": "string", "price": 10000, "salePrice": 8000, "color": "string", "pictureUrl": "string", "like": false }]` |
| GET | `/items/{itemId}` | - | `{ "itemId": 1, "name": "string", "price": 10000, "salePrice": 8000, "shippingPrice": 2500, "size": "string", "color": "string", "information": "string", "itemPictures": [{"url": "string"}], "reviews": [{"content": "string", "createdAt": "2023-12-19T10:00:00"}], "questions": [{"content": "string", "answer": "string", "createdAt": "2023-12-19T10:00:00"}] }` |

### 🛒 장바구니 (Cart)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/cart` | - (인증 필요) | `204 NO CONTENT` |

### ❤️ 좋아요 (Like)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/like` | - (인증 필요) | `201 CREATED` |

### ⭐ 리뷰 (Review)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/review` | `"string"` (리뷰 내용) | `201 CREATED` |
| POST | `/{reviewId}/comment` | `{ "comment": "string" }` | `200 OK` |

### ❓ Q&A (Customer Support)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/question` | `{ "title": "string", "content": "string" }` | `201 CREATED` |
| POST | `/answer/{questionId}` | `answer=string` (query) | `200 OK` |

### 🎫 쿠폰 (Coupon)
| 메서드 | 엔드포인트 | 요청                                                                                                                                          | 응답 |
|--------|------------|---------------------------------------------------------------------------------------------------------------------------------------------|------|
| POST | `/coupon` | `{ "name": "string", "type": "PERCENT/FIXED_AMOUNT", "value": 1000, "startTime": "2023-12-19T10:00:00", "endTime": "2023-12-25T23:59:59" }` | `"Coupon Saved"` |

---

## 보안 특징

- **Spring Security**: 사용자 인증 및 권한 관리
- **OAuth 2.0**: 카카오 소셜 로그인 연동
- **이메일 인증**: Redis 기반 임시 코드 저장
- **비밀번호 암호화**: 안전한 비밀번호 저장
- **CORS 설정**: 웹 프론트엔드와의 안전한 통신

## 데이터 관리

- **JPA/Hibernate**: 객체 관계형 매핑
- **Redis**: 세션 관리 및 인증 코드 캐싱
- **MySQL**: 기본 데이터베이스
- **파일 업로드**: 상품 이미지 처리