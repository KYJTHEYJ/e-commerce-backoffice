# 고객 및 상품 관리 시스템 프로젝트
- 팀명 : E3-I2
- 팀장 : 김영재
- 팀원 : 김대훈, 나은총, 이준연, 이한비
- 개발 기간 : 2026년 1월 13일 ~ 2026년 1월 20일

## 목차
1. [프로젝트 요구사항](#프로젝트-요구사항)
2. [패키지 구조](#패키지-구조)
3. [삭제 전략](#삭제-전략)
4. [Branch 전략](#branch-전략)
5. [API 명세서](#api-명세서)
    - [관리자](#1-관리자-회원가입)
    - [상품](#1-상품-추가)
    - [고객](#1-고객-리스트-조회)
    - [주문](#1-주문-생성)
    - [리뷰](#1-리뷰-리스트-조회)
    - [대시보드](#1-대시보드-통계)
6. [ERD 및 FlowChart](#erd-flowchart)

## 프로젝트 요구사항
- 고객, 상품, 주문 정보를 **체계적으로 관리**할 수 있는 기능
- 상품 리뷰를 **조회하고 관리**할 수 있으며, **상품별 평점과 통계**를 제공하는 기능
- 관리자가 백오피스에 **회원가입을 요청**하고, 슈퍼 관리자가 **승인/거부**할 수 있는 기능
- 데이터 증가에 따른 **검색, 정렬, 필터, 페이징** 기능
- 커머스 서비스 현황을 한눈에 파악할 수 있는 **대시보드** 기능

## 패키지 구조

<details>
<summary> ecommerce_backoffice 패키지 </summary>
<div markdown="1">

```bash
ecommerce_backoffice
└─ ecommerce_backoffice
   ├─ common
   │  ├─ annotation        # 공통 커스텀 어노테이션
   │  ├─ config            # 전역 설정 (Encoder, Web, JPA)
   │  ├─ dto
   │  │  ├─ response       # 공통 응답 DTO
   │  │  └─ session        # 세션 관련 DTO
   │  ├─ entity            # 공통 엔티티(Base Entity)
   │  ├─ exception
   │  │  └─ dto            # 에러 응답 DTO
   │  ├─ interceptor       # 인터셉터
   │  └─ util
   │     ├─ initializer    # 데이터 시딩(초기화)
   │     └─ pagination     # 페이징 유틸
   │
   └─ domain
      ├─ admin             # 관리자 도메인
      │  ├─ controller
      │  ├─ dto
      │  ├─ entity
      │  ├─ repository
      │  └─ service
      │
      ├─ account           # 고객 도메인
      │  ├─ controller
      │  ├─ dto
      │  ├─ entity
      │  ├─ repository
      │  └─ service
      │
      ├─ product           # 상품 도메인
      │  ├─ controller
      │  ├─ dto
      │  ├─ entity
      │  ├─ repository
      │  │  └─ projection  # 대시보드 조회 전용 Projection
      │  └─ service
      │
      ├─ ordering          # 주문 도메인
      │  ├─ controller
      │  ├─ dto
      │  ├─ entity
      │  ├─ repository
      │  └─ service
      │
      └─ review            # 리뷰 도메인
         ├─ controller
         ├─ dto
         ├─ entity
         ├─ repository
         └─ service
```

</div>
</details>

## 삭제 전략
### Soft Delete 정책
- [x] 실제 삭제 대신 deleted_at 업데이트
- [x] 기본 조회 시 삭제 데이터 제외
- [x] 각 Entity 내부 delete() 메서드로 제어

## Branch 전략
<details>
<summary>Dev, Stage, Prod </summary>
<div markdown="1">

- **main (Prod)**
    - **운영 단계 브랜치**
    - **stage에서만 merge**
    - 3명 이상의 approve 필요
    - PR Conversation 전부 Resolve 필요
    - ***PR만 허용, merge 불가능함***
- **stage**
    - **main 에 배포 전 검증 브랜치**
    - dev 에서 merge
    - 1명 이상의 approve 필요
    - PR Conversation 전부 Resolve 필요
    - ***PR만 허용, merge 불가능함***
- **dev**
    - **feature 가 통합되어 모이는 단계**
    - 1명 이상의 approve 필요
    - ***PR만 허용, merge 불가능함***
- **feature**
    - **각자 기능 개발하는 개인 브랜치**
    - **기능 완성 후엔 삭제**
    - ***feature/업무 ID 형식으로 네이밍***
</div>
</details>

## API 명세서
- 공통 사항
  - API 헤더는 공통적으로 Content-Type: application/json를 사용
  - 인증이 필요한 API는 로그인 필요(세션) - 관리자 회원 가입 외 전 API 적용
    - 로그인 하지않고 해당 API 접근 시 `401 UnAuthorized`
<details>
<summary> 관리자 </summary>
<div markdown="1">

### 1. 관리자 회원가입
- **URL**: `/api/admins/signup`
- **Method**: `POST`
- **Request Body**:
```json
{
  "adminName": "은총",
  "email": "email@email.com",
  "password": "12345678",
  "phone": "010-1111-1111",
  "role": "CS_ADMIN",
  "requestMessage": "지원사유"
}
```
- **Response**:
```json
{
  "code": "CREATED",
  "data": {
    "adminId": 1,
    "adminName": "은총",
    "email": "email@email.com",
    "phone": "010-1111-1111",
    "role": "CS_ADMIN",
    "createdAt": "2026-01-15",
    "status": "WAIT",
    "requestMessage": "지원사유"
  },
  "message": "회원가입 신청이 완료되었습니다. 관리자 승인을 기다려주세요.",
  "success": true
}
```
- **Response Code**
`201 Created`

- **ERROR** <br>
  - 필수 값 누락한 경우: `400 Bad Request` 
  - 이미 존재하는 이메일인 경우: `409 Conflict`


### 2. 관리자 로그인
- **URL**: `/api/admins/login`
- **Method**: `POST`
- **Request Body**:
```json
{
  "email": "super@super.com",
  "password": "12345678"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": null,
    "adminId": 1,
    "adminName": "은총",
    "createdAt": "2026-01-15",
    "email": "super@super.com",
    "phone": "010-1111-1111",
    "role": "SUPER_ADMIN",
    "status": "ACT"
  },
  "message": "로그인 성공",
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 잘못된 입력일 경우: 400 `Bad Reqeuest`
    - 관리자 상태가 ACT 상태가 아닌 경우: `403 Forbidden`
    - 정상적인 관리자 계정이 아닌 경우: `403 Forbidden`

### 3. 관리자 로그아웃
- **URL**: `/api/admins/logout`
- **Method**: `POST`
- **Response**:
- **Response Code**
  `200 OK`

### 4. 관리자 승인
- **URL**: `/api/admins/{adminId}/accept`
- **Method**: `PUT`
- **Parameter** `adminId`
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": "2026-01-15",
    "adminId": 2,
    "adminName": "대훈",
    "createdAt": "2026-01-15",
    "deniedAt": null,
    "email": "op@op.com",
    "phone": "010-1111-1111",
    "requestMessage": "지원사유",
    "role": "OP_ADMIN",
    "status": "ACT"
  },
  "message": "관리자가 승인되었습니다.",
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 잘못된 입력일 경우: 400 `Bad Reqeuest`
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`

### 5. 관리자 거부
- **URL**: `/api/admins/{adminId}/deny`
- **Method**: `PUT`
- **Parameter** `adminId`
- **Request Body**:
```json
{
    "deniedReason": "거부 사유"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": null,
    "adminId": 2,
    "adminName": "대훈",
    "createdAt": "2026-01-15",
    "deniedAt": "2026-01-15T11:41:32.0677364",
    "email": "op@op.com",
    "phone": "010-1111-1111",
    "requestMessage": "지원사유",
    "role": "OP_ADMIN",
    "status": "DENY"
  },
  "message": "관리자 신청이 거부되었습니다.",
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`

### 6. 관리자 리스트 조회
- **URL**: `/api/admins`
- **Method**: `GET`
- **Query Parameters**:
    - `page`: 페이지 번호 (default: 1)
    - `limit`: 페이지당 항목 수 (default: 10)
    - `sortBy` : 정렬 기준 (adminName, email, createdAt 등...) 
    - `sortOrder` : 정렬 순서 (asc, desc)
    - `role`: 역할 필터 (SUPER_ADMIN, CS_ADMIN, OP_ADMIN)
    - `status`: 상태 필터 (ACT, IN_ACT, SUSOEND)
     
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "items": [
      {
        "id": 2,
        "name": "홍길동",
        "email": "hong@test.com",
        "phone": "010-1234-5678",
        "role": "CS_ADMIN",
        "status": "WAIT",
        "createdAt": "2026-01-16",
        "acceptedAt": "",
        "deniedAt": "",
        "requestMessage": "CS관리자에 지원합니다."
      },
      {
        "id": 1,
        "name": "TEST_SUPER_ADMIN",
        "email": "admin@test.com",
        "phone": "010-0000-0000",
        "role": "SUPER_ADMIN",
        "status": "ACT",
        "createdAt": "2026-01-16",
        "acceptedAt": "",
        "deniedAt": ""
      }
    ],
    "pagination": {
      "limit": 10,
      "page": 1,
      "total": 2,
      "totalPages": 1
    }
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`

### 7. 관리자 상세 조회
- **URL**: `/api/admins/{adminId}`
- **Method**: `GET`
- **Parameter** `adminId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "id": 2,
    "name": "홍길동",
    "email": "hong@test.com",
    "phone": "010-1234-5678",
    "role": "CS_ADMIN",
    "status": "WAIT",
    "createdAt": "2026-01-16",
    "acceptedAt": "",
    "deniedAt": "",
    "requestMessage": "CS관리자에 지원합니다."
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`

### 8. 관리자 수정
- **URL**: `/api/admins/{adminId}`
- **Method**: `PUT`
- **Parameter** `adminId`
- **Request Body**:
```json
{
  "adminName": "홍길동",
  "email": "updated@sparta.com",
  "phone": "010-1111-2222"
}
```
- **Response**:
```json
{
  "success": true,
  "code": "200 OK",
  "data": {
    "id": 1,
    "name": "홍길동",
    "email": "updated@sparta.com",
    "phone": "010-1111-2222",
    "role": "OP_ADMIN",
    "status": "ACT",
    "createdAt": "2025-04-26",
    "updatedAt": "2026-01-14",
    "aceeptAt": "2025-04-27",
    "deniedAt": ""

  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`
    - 이미 등록된 이메일이 있을 경우 : `409 Conflict`

### 9. 관리자 삭제
- **URL**: `/api/admins/{adminId}`
- **Method**: `DELETE`
- **Parameter** `adminId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "message": "사용자가 삭제되었습니다"
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우, 삭제 대상 본인인 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`
    - 이미 등록된 이메일이 있을 경우 : `409 Conflict`

### 10. 관리자 역할 변경
- **URL**: `/api/admins/{adminId}/role`
- **Method**: `PUT`
- **Parameter** `adminId`
- **Description**: SUPER_ADMIN 만 역할 변경이 가능
- **Request Body**:
```json
{
  "role": "CS_ADMIN"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": "2026-01-15",
    "adminId": 2,
    "adminName": "대훈",
    "createdAt": "2026-01-15",
    "deniedAt": null,
    "email": "op@op.com",
    "phone": "010-1111-1111",
    "role": "CS_ADMIN",
    "status": "ACT"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`

### 11. 관리자 상태 변경
- **URL**: `/api/admins/{adminId}/status`
- **Method**: `PUT`
- **Parameter** `adminId`
- **Description**: SUPER_ADMIN 만 상태 변경 가능
- **Request Body**:
```json
{
  "status": "SUSPEND"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": "2026-01-15",
    "adminId": 2,
    "adminName": "대훈",
    "createdAt": "2026-01-15",
    "deniedAt": null,
    "email": "op@op.com",
    "phone": "010-1111-1111",
    "role": "OP_ADMIN",
    "status": "ACT"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`


### 12. 내 프로필 조회
- **URL**: `/api/admins/me`
- **Method**: `GET`
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": null,
    "adminId": 1,
    "adminName": "은총",
    "createdAt": "2026-01-15",
    "deniedAt": null,
    "email": "super@super.com",
    "phone": "010-1111-1111",
    "role": "SUPER_ADMIN",
    "status": "ACT"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`

### 13. 내 프로필 수정
- **URL**: `/api/admins/me/profile`
- **Method**: `PUT`
- **Request Body**:
```json
{
  "adminName": "총은",
  "email": "repus@repus.com",
  "phone": "010-9999-9999"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "acceptedAt": null,
    "adminId": 1,
    "adminName": "총은",
    "createdAt": "2026-01-15",
    "deniedAt": null,
    "email": "repus@repus.com",
    "phone": "010-9999-9999",
    "role": "SUPER_ADMIN",
    "status": "ACT"
  },
  "message": "프로필이 성공적으로 업데이트되었습니다.",
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`
    - 이미 등록된 이메일이 있을 경우 : `409 Conflict`

### 14. 내 비밀번호 변경
- **URL**: `/api/admins/me/password`
- **Method**: `PUT`
- **Request Body**:
```json
{
  "currentPassword": "12345678",
  "newPassword": "87654321"
}
```
- **Response**:
```json
{
  "code": "OK",
  "message": "비밀번호가 성공적으로 변경되었습니다.",
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 현재 PW가 일치하지 않는 경우, 새 PW와 기존 PW가 일치할 경우: `400 Bad Request`
    - 슈퍼 관리자가 아닌 경우: `403 Forbidden`
    - 해당 관리자를 찾을 수 없는 경우: `404 Not Found`
    - 이미 등록된 이메일이 있을 경우 : `409 Conflict`

</div>
</details>

<details>
<summary> 상품 </summary>
<div markdown="1">

### 1. 상품 추가
- **URL**: `/api/products`
- **Method**: `POST`
- **Request Body**:
```json
{
  "ProductName":"테스트 상품",
  "category":"ELECTRONICS",
  "price":100000,
  "status":"ON_SALE",
  "quantity":100
}
```
- **Response**:
```json
{
  "code": "CREATED",
  "data": {
    "category": "ELECTRONICS",
    "createdAt": "2026-01-14",
    "id": 1,
    "name": "테스트 상품",
    "price": 100000,
    "quantity": 100,
    "status": "ON_SALE"
  },
  "success": true
}
```
- **Response Code**
  `201 Created`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`

### 2. 상품 조회
- **URL**: `/api/products`
- **Method**: `GET`
- **Query Parameters**:
    - `search`: 상품명    
    - `category`: 카테고리 필터
    - `status`: 상태 필터
    - `page`: 페이지 번호 
    - `limit`: 페이지당 항목 수
    - `sortOrder` : 정렬 순서 (asc, desc)
    - `sortBy` : 정렬 기준 컬럼 (default 는 createdAt)
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "items": [
      {
        "id": 1,
        "productName": "테스트 상품",
        "category": "ELECTRONICS",
        "price": 100000,
        "quantity": 100,
        "status": "ON_SALE",
        "createdAt": "2026-01-16",
        "adminId": 1,
        "adminName": "TEST_SUPER_ADMIN",
        "adminEmail": "admin@test.com"
      }
    ],
    "pagination": {
      "limit": 10,
      "page": 1,
      "total": 1,
      "totalPages": 1
    }
  }
}
```
- **Response Code**
  `200 OK`


### 3. 상품 개별 조회
- **URL**: `/api/products/{productId}`
- **Method**: `GET`
- **Parameter** `productId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "id": 1,
    "productName": "테스트 제품1",
    "category": "ELECTRONICS",
    "price": 1500000,
    "quantity": -4,
    "status": "SOLD_OUT",
    "createdAt": "2026-01-18",
    "adminId": 1,
    "adminName": "TEST_SUPER_ADMIN",
    "adminEmail": "admin@test.com",
    "reviewSummary": {
      "averageRating": 2.3,
      "totalReviews": 4,
      "fiveStarCount": 0,
      "fourStarCount": 1,
      "threeStarCount": 0,
      "twoStarCount": 2,
      "oneStarCount": 1
    },
    "recentReview": [
      {
        "id": 5,
        "productId": 1,
        "customerId": 13,
        "customer": "이서아",
        "customerEmail": "seoa@example.com",
        "product": "테스트 제품1",
        "rating": 4,
        "comment": "테스트 리뷰",
        "date": "2026-01-18",
        "orderNo": "ORDER-005"
      },
      {
        "id": 4,
        "productId": 1,
        "customerId": 16,
        "customer": "백하준",
        "customerEmail": "hajun@example.com",
        "product": "테스트 제품1",
        "rating": 2,
        "comment": "테스트 리뷰",
        "date": "2026-01-18",
        "orderNo": "ORDER-004"
      },
      {
        "id": 3,
        "productId": 1,
        "customerId": 18,
        "customer": "허지호",
        "customerEmail": "jiho2@example.com",
        "product": "테스트 제품1",
        "rating": 2,
        "comment": "테스트 리뷰",
        "date": "2026-01-18",
        "orderNo": "ORDER-003"
      }
    ]
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`


### 4. 상품 정보 수정
- **URL**: `/api/products/{productId}`
- **Method**: `PUT`
- **Parameter** `productId`
- **Request Body**:
```json
{
  "productName":"트스테"
, "category":"CLOTH"
, "price":1000
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "category": "CLOTH",
    "createdAt": "2026-01-15T10:50:46.020801",
    "id": 1,
    "price": 1000,
    "productName": "트스테",
    "quantity": 100,
    "status": "ON_SALE"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`


### 5. 상품 정보 수량 수정
- **URL**: `/api/products/{productId}/quantity`
- **Method**: `PUT`
- **Parameter** `productId`
- **Request Body**:
```json
{
  "quantity": 100
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "category": "CLOTH",
    "createdAt": "2026-01-15T10:50:46.020801",
    "id": 1,
    "price": 1000,
    "productName": "트스테",
    "quantity": 100,
    "status": "ON_SALE"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`

### 6. 상품 정보 상태 수정
- **URL**: `/api/products/{productId}/status`
- **Method**: `PUT`
- **Parameter** `productId`
- **Request Body**:
```json
{
  "status":"SOLD_OUT"
}
```
- **Response**:
```json
{
  "code": "OK",
  "data": {
    "category": "CLOTH",
    "createdAt": "2026-01-15T10:50:46.020801",
    "id": 1,
    "price": 1000,
    "productName": "트스테",
    "quantity": 100,
    "status": "SOLD_OUT"
  },
  "success": true
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`


### 7. 상품 정보 삭제
- **URL**: `/api/products/{productId}`
- **Method**: `DELETE`
- **Parameter** `productId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "message": "상품이 삭제되었습니다"
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 상품을 찾을 수 없는 경우: `404 Not Found`

</div>
</details>

<details>
<summary> 고객 </summary>
<div markdown="1">

### 1. 고객 리스트 조회
- **URL**: `/api/customers`
- **Method**: `GET`
- **Query Parameters**:
    - `search`: 검색 키워드 (고객명 또는 이메일)
    - `page`: 페이지 번호 (default: 1)
    - `limit`: 페이지당 항목 수 (default: 10)
    - `sortBy` : 정렬 기준 (adminName, email, createdAt 등...)
    - `sortOrder` : 정렬 순서 (asc, desc)
    - `status`: 상태 필터 (ACT, IN_ACT, SUSPEND)
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "items": [
      {
        "customerId": 20,
        "customerName": "강태우",
        "email": "taewoo@example.com",
        "phone": "010-0020-0020",
        "status": "SUSPEND",
        "createdAt": "2026-01-16",
        "totalOrders": 2,
        "totalSpent": 15600
      },
      {
        "customerId": 19,
        "customerName": "오수아",
        "email": "sua@example.com",
        "phone": "010-0019-0019",
        "status": "IN_ACT",
        "createdAt": "2026-01-16",
        "totalOrders": 5,
        "totalSpent": 71000
      }
    ],
    "pagination": {
      "limit": 2,
      "page": 1,
      "total": 18,
      "totalPages": 9
    }
  }
}
```
- **Response Code**
  `200 OK`

### 2. 고객 상세 조회
- **URL**: `/api/customers/{customerId}`
- **Method**: `GET`
- **Parameter** `customerId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "customerId": 3,
    "customerName": "정유미",
    "email": "yumi@example.com",
    "phone": "010-0003-0003",
    "status": "ACT",
    "createdAt": "2026-01-16",
    "totalOrders": 5,
    "totalSpent": 76400
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 고객을 찾을 수 없는 경우: `404 Not Found`


### 3. 고객 정보 수정
- **URL**: `/api/customers/{customerId}/info`
- **Method**: `PATCH`
- **Parameter** `customerId`
- **Request Body**:
```json
{
  "customerName": "이연준",
  "email": "123ljy456@gmail.com",
  "phone": "010-1234-1234"
}
```
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "customerId": 1,
    "customerName": "홍길동",
    "email": "123ljy456@gmail.com",
    "phone": "010-1234-5678",
    "status": "IN_ACT",
    "createdAt": "2026-01-16",
    "totalOrders": 1,
    "totalSpent": 30900
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 고객을 찾을 수 없는 경우: `404 Not Found`
    - 이미 등록된 이메일이 있을 경우 : `409 Conflict`

### 4. 고객 상태 변경
- **URL**: `/api/customers/{customerId}/status`
- **Method**: `PATCH`
- **Parameter** `customerId`

- **Request Body**:
```json
{
  "status": "ACT"
}
```
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "customerId": 2,
    "customerName": "송지호",
    "email": "jiho@example.com",
    "phone": "010-0002-0002",
    "status": "ACT",
    "createdAt": "2026-01-16",
    "totalOrders": 4,
    "totalSpent": 35600
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 고객을 찾을 수 없는 경우: `404 Not Found`

### 5. 고객 삭제
- **URL**: `/api/customers/{customerId}`
- **Method**: `DELETE`
- **Parameter** `customerId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "message": "고객이 삭제 되었습니다"
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 고객을 찾을 수 없는 경우: `404 Not Found`

</div>
</details>

<details>
<summary> 주문 </summary>
<div markdown="1">

### 1. 주문 생성
- **URL**: `/api/orders`
- **Method**: `POST`
- **Request Body**:
```json
{
  "customerId":1
, "productId":1
, "orderQuantity":10
}
```
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "orderId": 5,
    "orderNo": "ORDER-005",
    "orderAt": "2026-01-17",
    "customerId": 1,
    "customerName": "강하윤",
    "productId": 1,
    "productName": "테스트 상품",
    "productPrice": 100000,
    "orderQuantity": 10,
    "orderTotalPrice": 1000000,
    "adminId": 1,
    "adminName": "TEST_SUPER_ADMIN",
    "adminEmail": "admin@test.com"
  }
}
```
- **Response Code**
  `201 Created`

- **ERROR** <br>
    - 입력값 검증에 실패했을 경우: `400 Bad Request`
    - 해당 관리자, 제품, 고객을 찾을 수 없는 경우: `404 Not Found`

### 2. 주문 리스트 통합 조회
- **URL**: `/api/orders`
- **Method**: `GET`
- **Query Parameters**:
  - `search`: 검색 키워드 (고객명 또는 주문번호)
  - `orderStatus`: 주문 상태 필터
  - `page`: 페이지 번호
  - `limit`: 페이지당 항목 수
  - `sortOrder` : 정렬 순서 (asc, desc)
  - `sortBy` : 정렬 기준 컬럼 (default 는 createdAt)
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "items": [
      {
        "id": 1,
        "orderNo": "1",
        "customerId": 1,
        "customerName": "강하윤",
        "productId": 1,
        "productName": "hello",
        "quantity": 5,
        "amount": 1241424241,
        "orderAt": "2026-01-16",
        "AdminId": 1,
        "AdminName": "TEST_SUPER_ADMIN",
        "AdminRole": "SUPER_ADMIN",
        "orderStatus": "DELIVERED"
      }
    ],
    "pagination": {
      "limit": 10,
      "page": 1,
      "total": 1,
      "totalPages": 1
    }
  }
}
```
- **Response Code**
  `200 OK`

### 3. 주문 상세 조회
- **URL**: `/api/orders/{orderId}`
- **Method**: `GET`
- **Parameter** `orderId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "id": 1,
    "orderNo": "1",
    "customerId": 1,
    "customerName": "강하윤",
    "productId": 1,
    "productName": "daf",
    "quantity": 1,
    "amount": 52135154,
    "orderAt": "2026-01-16",
    "AdminId": 1,
    "AdminName": "TEST_SUPER_ADMIN",
    "AdminRole": "SUPER_ADMIN",
    "orderStatus": "DELIVERED"
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
  - 해당 주문을 찾을 수 없는 경우: `404 Not Found`

### 4. 주문 상태 수정
- **URL**: `/api/orders/{orderId}/status`
- **Method**: `PUT`
- **Parameter** `orderId`
- **Request Body**:
```json
{
  "status": "SHIPPING"
}
```
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "orderId": "1",
    "orderNo": "O001",
    "customerId": "1",
    "customerName": "권유준",
    "email": "yujun@example.com",
    "productId": "1",
    "productName": "핸드크림 세트",
    "quantity": 1,
    "orderTotalPrice": 18000,
    "createdAt": "2026-01-16",
    "status": "SHIPPING"
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br> 
  - 허용되지 않은 상태 전이의 경우: `400 Bad Request`
  - 배송완료 된 상태일 경우: `403 Forbidden`
  - 해당 주문을 찾을 수 없는 경우: `404 Not Found`

### 5. 주문 취소
- **URL**: `/api/orders/{orderId}/cancel`
- **Method**: `PUT`
- **Parameter** `orderId`
- **Request Body**:
```json
{
  "cancelReason": "취소 사유입니다."
}
```
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "orderId": "ORDER-0010",
    "orderNo": "O001",
    "customerId": "1",
    "customerName": "진이준",
    "email": "ijun@example.com",
    "productId": "P005",
    "productName": "블루투스 스피커",
    "orderQuantity": 3,
    "orderTotalPrice": 375000,
    "deletedAt": "2026-01-15",
    "status": "CANCELLED",
    "cancelReason": "취소 사유입니다."
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 허용되지 않은 상태 전이의 경우: `400 Bad Request`
    - 해당 주문을 찾을 수 없는 경우: `404 Not Found`

</div>
</details>

<details>
<summary> 리뷰 </summary>
<div markdown="1">

### 1. 리뷰 리스트 조회
- **URL**: `/api/reviews`
- **Method**: `GET`
- **Query Parameters**:
    - `search`: 검색 키워드 (고객 또는 상품명)
    - `page`: 페이지 번호 (default: 1)
    - `limit`: 페이지당 항목 수 (default: 10)
    - `sortBy` : 정렬 기준 (adminName, email, createdAt 등...)
    - `sortOrder` : 정렬 순서 (asc, desc)
    - `rating`: 평점 필터 (1~5)
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "items": [
      {
        "id": "R001",
        "orderNo": "ORDER-0073",
        "productId": "1",
        "customerId": "1",
        "customer": "정유미",
        "customerEmail": "yumi@example.com",
        "product": "운동복 세트",
        "rating": 5,
        "comment": "가성비 최고입니다. 강력 추천해요!",
        "date": "2026-01-05"
      },
      {
        "id": "R002",
        "orderNo": "ORDER-0034",
        "productId": "2",
        "customerId": "5",
        "customer": "양지후",
        "customerEmail": "jihoo@example.com",
        "product": "올리브오일",
        "rating": 3,
        "comment": "화면이랑 색상이 조금 다르네요.",
        "date": "2026-01-04"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 10,
      "total": 2,
      "totalPages": 1
    }
  }
}
```
- **Response Code**
  `200 OK`

### 2. 리뷰 상세 조회
- **URL**: `/api/reviews/{reviewId}`
- **Method**: `GET`
- **Parameter** `reviewId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "data": {
    "id": "R001",
    "orderNo": "ORDER-0020",
    "productId": "P100",
    "customerId": "1",
    "customer": "허지호",
    "customerEmail": "jiho2@example.com",
    "product": "인형의 집",
    "rating": 5,
    "comment": "정말 만족스러운 구매였습니다! 품질이 훌륭해요.",
    "date": "2025-12-28"
  }
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 리뷰를 찾을 수 없는 경우: `404 Not Found`

### 3. 리뷰 삭제
- **URL**: `/api/reviews/{reviewId}`
- **Method**: `DELETE`
- **Parameter** `reviewId`
- **Response**:
```json
{
  "success": true,
  "code": "OK",
  "message": "리뷰가 삭제 되었습니다"
}
```
- **Response Code**
  `200 OK`

- **ERROR** <br>
    - 해당 리뷰를 찾을 수 없는 경우: `404 Not Found`

</div>
</details>

<details>
<summary> 대시보드 </summary>
<div markdown="1">

### 1. 대시보드 통계
- **URL**: `/api/dashboard/stat`
- **Method**: `GET`
- **Response**:
```json
{
  "summary": {
    "totalSales": 150000000,
    "totalOrders": 500,
    "totalCustomers": 300,
    "totalProducts": 150
  },
  "salesTrend": [
    {
      "date": "2024-01-01",
      "sales": 5000000,
      "orders": 20
    }
  ],
  "topProducts": [
    {
      "productId": 1,
      "productName": "맥북 프로 16인치",
      "sales": 35000000,
      "quantity": 10
    }
  ],
  "recentOrders": [
    {
      "orderId": 1,
      "orderNumber": "ORD-20240101-0001",
      "customerName": "김고객",
      "amount": 7000000,
      "status": "CONFIRMED",
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "statusBreakdown": {
    "pending": 10,
    "confirmed": 30,
    "shipping": 20,
    "delivered": 400,
    "cancelled": 40
  }
}
```
- **Response Code**
  `200 OK`

</div>
</details>

## ERD, FlowChart

<details>
<summary> ERD </summary>
<div markdown="1">

<img width="1014" height="1125" alt="image" src="https://github.com/user-attachments/assets/3b58a7b4-f834-46b5-ac30-bcd68febf850" />


</div>
</details>

<details>
<summary> FlowChart </summary>
<div markdown="1">

- 플로우 차트
    
    ## 전체 흐름
    
    실선 → 프로세스 처리 흐름
    
    점선 → 데이터 연관 흐름
    
    <img width="1051" height="950" alt="image" src="https://github.com/user-attachments/assets/b20efeea-7d1b-4757-a0c6-e9adff6917da" />

    
    ## 관리자 회원 가입
    
    <img width="944" height="1058" alt="image" src="https://github.com/user-attachments/assets/ea418ad0-5605-4870-b382-60c1a42906ba" />

    
    ## 로그인
    
    <img width="932" height="1072" alt="image" src="https://github.com/user-attachments/assets/1c952564-1c6e-4d00-9636-0c547f1fcf9c" />

    
    ## 로그아웃
    
    <img width="1118" height="893" alt="image" src="https://github.com/user-attachments/assets/d6c84c7b-eb6f-4179-9897-f6149049a753" />

    
    ## 관리자 도메인
    
    <img width="1834" height="767" alt="image" src="https://github.com/user-attachments/assets/d3c7afc4-82f1-4791-862a-b1535d5e5368" />

    
    <img width="1025" height="975" alt="image" src="https://github.com/user-attachments/assets/c854b933-5cdd-4ea0-9f76-c0b5c9fe050c" />

    
    ## 상품 도메인
    
    <img width="965" height="1035" alt="image" src="https://github.com/user-attachments/assets/96f17a86-f516-447c-b6f5-87744fdc9640" />

    
    ## 고객 도메인
    
    <img width="1113" height="897" alt="image" src="https://github.com/user-attachments/assets/3b5b4fa1-143b-4dcf-a3ff-af4950ddb937" />

    
    ## 주문 도메인
    
    <img width="1433" height="697" alt="image" src="https://github.com/user-attachments/assets/c0a94e50-c86f-487c-8efd-e75355e398f6" />

    
    ## 리뷰 도메인
    
    <img width="1050" height="952" alt="image" src="https://github.com/user-attachments/assets/94216618-1acf-4420-b98f-334ebb8dc097" />

    
    ## 대시보드
    
    <img width="694" height="1439" alt="image" src="https://github.com/user-attachments/assets/93e16c2e-3b03-4bab-97c0-fb93e259dbb8" />


</div>
</details>


