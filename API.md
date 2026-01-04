# Haein 쇼핑몰 API 명세서

完整的API文档请参考本文件。本文档包含所有REST API端点的详细说明。

---

## 🔐 인증 (Authentication)

### 카카오 로그인
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/auth/kakao/login` | `{ "code": "string" }` | `"Login successful"` |

---

## 👤 회원관리 (Member)

### 회원가입
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/signup` | `{ "nickname": "string", "email": "string", "password": "string", "phoneNumber": "string" }` | `204 NO CONTENT` |

### 이메일 인증
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/signup/send-code` | `email=string` (query) | `"인증 번호 전송 성공"` |
| POST | `/signup/verify-code` | `{ "email": "string", "code": "string" }` | `"인증 성공"` |

---

## 🛍️ 상품 (Items)

### 상품 목록 조회
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| GET | `/items?page={page}` | - | `[{ "id": 1, "name": "string", "price": 10000, "salePrice": 8000, "color": "string", "pictureUrl": "string", "like": false }]` |

### 상품 상세 조회
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| GET | `/items/{itemId}` | - | `{ "itemId": 1, "name": "string", "price": 10000, "salePrice": 8000, "shippingPrice": 2500, "size": "string", "color": "string", "information": "string", "itemPictures": [{"url": "string"}], "reviews": [{"content": "string", "createdAt": "2023-12-19T10:00:00"}], "questions": [{"content": "string", "answer": "string", "createdAt": "2023-12-19T10:00:00"}] }` |

### 상품 등록 (관리자)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/items` | `{ "name": "string", "price": 10000, "salePrice": 8000, "shippingPrice": 2500, "size": "string", "color": "string", "information": "string", "pictureUrls": ["string"], "categories": ["NEW", "SALE"] }` | `201 CREATED (itemId)` |

### 상품 수정 (관리자)
| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| PUT | `/items/{itemId}` | `{ "name": "string", "price": 10000, "salePrice": 8000, "shippingPrice": 2500, "size": "string", "color": "string", "information": "string", "pictureUrls": ["string"], "categories": ["NEW"] }` | `200 OK` |

---

## 🛒 장바구니 (Cart)

| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/cart` | - (인증 필요) | `204 NO CONTENT` |

---

## ❤️ 좋아요 (Like)

| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/like` | - (인증 필요) | `201 CREATED` |

---

## ⭐ 리뷰 (Review)

| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/{itemId}/review` | `"string"` (리뷰 내용) | `201 CREATED` |
| POST | `/{reviewId}/comment` | `{ "comment": "string" }` | `200 OK` |

---

## ❓ Q&A (Customer Support)

| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/question` | `{ "title": "string", "content": "string" }` | `201 CREATED` |
| POST | `/answer/{questionId}` | `answer=string` (query) | `200 OK` |

---

## 🎫 쿠폰 (Coupon)

| 메서드 | 엔드포인트 | 요청 | 응답 |
|--------|------------|------|------|
| POST | `/coupon` | `{ "name": "string", "type": "PERCENT/FIXED_AMOUNT", "value": 1000, "startTime": "2023-12-19T10:00:00", "endTime": "2023-12-25T23:59:59" }` | `"Coupon Saved"` |

---

## 카테고리 enum

### CategoryName
- `NEW` - 신상품
- `BEST` - 베스트
- `SALE` - 세일
- `SPRING` - 봄 시즌
- `FALL` - 가을 시즌
- `SUMMER` - 여름 시즌
- `WINTER` - 겨울 시즌
