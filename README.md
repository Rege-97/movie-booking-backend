# 🎬 대규모 트래픽 영화 예매 시스템 (Movie Booking System)

## 📖 프로젝트 소개
> **"선착순 티켓팅 트래픽을 감당할 수 있는 예매 시스템"**

인기 영화 개봉이나 명절 연휴 등 특정 시점에 트래픽이 폭주하는 환경을 가정하여 설계된 백엔드 서버입니다. 단순한 기능을 넘어 **대용량 트래픽 상황에서의 데이터 정합성 보장**과 **조회 성능 최적화**에 주안점을 두었습니다.

### 🎯 핵심 목표
* **동시성 제어:** 다수의 사용자가 동시에 같은 좌석을 예매할 때 발생하는 데이터 불일치 해결 (Redis 분산락)
* **대용량 데이터 조회 성능:** 10만 건 이상의 데이터 환경에서 조회 속도 개선 (Caching, Indexing, Fetch Join)
* **안정적인 운영:** Docker 컨테이너 기반의 배포 및 스케줄러 부하 분산 (Batch Update)

## 🚀 실행 방법
이 프로젝트는 **AWS ECS(Fargate)** 환경에 배포되어 있습니다.
별도의 설치 없이 **Swagger UI**를 통해 API를 목록을 확인할 수 있습니다.

**[Notice] 현재 AWS 배포 서버는 비용 문제로 인해 운영이 중단되었습니다.**

### 🔗 배포 서버 접속 
기본 관리자 계정
```json 
{
    "email": "admin@admin.com",
    "password":"asd123!!"
}
```
> **Swagger UI:** `http://15.165.237.61:8080/swagger-ui/index.html`

## 🛠 아키텍처 (Architecture)
<img width="700" alt="스크린샷 2025-11-22 181400" src="https://github.com/user-attachments/assets/460ac24f-4383-4f0f-8929-8572706e5a34" />

### 🏗 시스템 구조
* **Application:** Spring Boot 기반의 RESTful API 서버 (ECS Fargate)
* **Database:** Amazon RDS (MySQL)를 통한 데이터 영속성 관리
* **Cache/Lock:** Amazon ElastiCache (Redis)를 활용한 캐싱, 분산락, 인증 토큰 관리
* **Deployment:** GitHub Actions -> AWS ECR -> AWS ECS (Fargate) 기반의 무중단 배포 파이프라인

### 💾 ERD (Entity Relationship Diagram)
<img width="700" alt="스크린샷 2025-11-19 124925" src="https://github.com/user-attachments/assets/bc6b2364-0878-404b-af20-97153b4fd97b" />

## 📂 프로젝트 구조
```text
src
├── main
│   ├── java/com/cinema/moviebooking
│   │   ├── common          # 공통 응답 포맷 (ApiResponse)
│   │   ├── config          # 설정 파일 (Security, Redis, Swagger, QueryDSL)
│   │   ├── controller      # API 컨트롤러 (진입점)
│   │   ├── service         # 비즈니스 로직
│   │   ├── repository      # 데이터 접근 계층 (JPA, QueryDSL)
│   │   ├── entity          # DB 엔티티
│   │   ├── dto             # 데이터 전송 객체 (Request/Response)
│   │   ├── exception       # 전역 예외 처리 (GlobalExceptionHandler)
│   │   ├── security        # 인증/인가 (JWT, CustomUserDetails)
│   │   ├── scheduler       # 스케줄러 (상영 상태 자동 변경)
│   │   └── util            # 유틸리티 (QueryCounter 등)
│   └── resources
│       └── application.yaml
└── test                    # 테스트 코드
```

## ⚙️ 기술 스택 (Tech Stack)

| Category | Technology |
| --- | --- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.7, Spring Security |
| **Database** | MySQL 8.4, Redis (ElastiCache) |
| **ORM** | Spring Data JPA, QueryDSL |
| **Infrastructure** | AWS (ECS, ECR, RDS), Docker |
| **CI/CD** | GitHub Actions |
| **Testing** | JMeter (부하 테스트), JUnit5 |

