# 고객 및 상품 관리 시스템 프로젝트
- 팀명 : E3-I2
- 팀장 : 김영재
- 팀원 : 김대훈, 나은총, 이준연, 이한비
- 개발 기간 : 2026년 1월 13일 ~ 2026년 1월 20일

## 패키지 구조
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
## 삭제 전략
### Soft Delete 정책
- [x] 실제 삭제 대신 deleted_at 업데이트
- [x] 기본 조회 시 삭제 데이터 제외
- [x] 각 Entity 내부 delete(), restore() 메서드로 제어

## Branch 전략
### Dev, Stage, Prod
- ***main (Prod)***
    - **운영 단계 브랜치**
    - **stage에서만 merge**
    - 3명 이상의 approve 필요
    - PR Conversation 전부 Resolve 필요
    - ***PR만 허용, merge 불가능함***
- ***stage***
    - **main 에 배포 전 검증 브랜치**
    - dev 에서 merge
    - 1명 이상의 approve 필요
    - PR Conversation 전부 Resolve 필요
    - ***PR만 허용, merge 불가능함***
- ***dev***
    - **feature 가 통합되어 모이는 단계**
    - 1명 이상의 approve 필요
    - ***PR만 허용, merge 불가능함***
- ***feature***
    - **각자 기능 개발하는 개인 브랜치**
    - **기능 완성 후엔 삭제**
    - ***feature/업무 ID 형식으로 네이밍***
