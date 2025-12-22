# Docker 실행 가이드

이 프로젝트의 모든 마이크로서비스를 Docker 컨테이너로 실행할 수 있습니다.

## 사전 요구사항

- Docker 설치
- Docker Compose 설치

## 구성

### 서비스 목록

| 서비스 | 포트 | 설명 |
|--------|------|------|
| oracle-db | 1521 | Oracle Database XE 21c |
| product-service | 8081 | 상품 관리 API |
| coupon-service | 8082 | 쿠폰 관리 API |
| fee-service | 8083 | 수수료 관리 API |

## 실행 방법

### 1. 전체 서비스 실행

```bash
# 모든 서비스 빌드 및 실행
docker-compose up --build

# 백그라운드 실행
docker-compose up -d --build
```

### 2. 특정 서비스만 실행

```bash
# Product 서비스만 실행
docker-compose up product-service

# Fee 서비스만 실행
docker-compose up fee-service
```

### 3. 서비스 중지

```bash
# 모든 서비스 중지
docker-compose down

# 볼륨까지 삭제 (DB 데이터 초기화)
docker-compose down -v
```

## API 접근

서비스가 실행되면 다음 URL로 접근할 수 있습니다:

### Product API
- 기본 URL: http://localhost:8081/api/products
- Swagger UI: http://localhost:8081/swagger-ui/index.html

### Coupon API
- 기본 URL: http://localhost:8082/api/coupons
- Swagger UI: http://localhost:8082/swagger-ui/index.html

### Fee API
- 기본 URL: http://localhost:8083/api/fees
- Swagger UI: http://localhost:8083/swagger-ui/index.html

## 로그 확인

```bash
# 전체 로그
docker-compose logs

# 특정 서비스 로그
docker-compose logs product-service

# 실시간 로그
docker-compose logs -f fee-service
```

## 개별 서비스 빌드

각 모듈의 Dockerfile을 사용해서 개별적으로 빌드할 수도 있습니다:

```bash
# Product 서비스
docker build -f product-module/Dockerfile -t product-service .

# Coupon 서비스
docker build -f coupon-module/Dockerfile -t coupon-service .

# Fee 서비스
docker build -f fee-module/Dockerfile -t fee-service .
```

## 주의사항

### 첫 실행 시
- Oracle DB 초기화에 1-2분 소요될 수 있습니다
- 서비스들은 DB가 준비될 때까지 자동으로 대기합니다 (`depends_on` 설정)

### 포트 충돌
만약 로컬에서 이미 같은 포트를 사용 중이라면:
1. 로컬 서비스를 중지하거나
2. `docker-compose.yml`에서 포트 번호를 변경하세요

예:
```yaml
ports:
  - "8081:8081"  # 왼쪽을 변경: "18081:8081"
```

## 트러블슈팅

### Oracle DB 연결 실패
```bash
# DB 컨테이너 재시작
docker-compose restart oracle-db

# DB 헬스체크 확인
docker-compose ps
```

### 빌드 캐시 삭제
```bash
# 캐시 없이 재빌드
docker-compose build --no-cache
```

### 컨테이너 상태 확인
```bash
# 실행 중인 컨테이너
docker-compose ps

# 모든 컨테이너 (중지된 것 포함)
docker ps -a
```

## 멀티 스테이지 빌드

각 Dockerfile은 멀티 스테이지 빌드를 사용합니다:

1. **Stage 1 (Builder)**: Gradle로 애플리케이션 빌드
2. **Stage 2 (Runtime)**: JRE만 포함한 경량 이미지로 실행

이를 통해 최종 이미지 크기를 최소화했습니다.
