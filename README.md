# 🧾 Kiosk 통합 프로젝트 (https-db-user0507)

## 📁 프로젝트 구성

```
https-db-user0507/
├── kiosk-backend/            # Spring Boot 백엔드 (공통)
├── kiosk-frontend/           # 관리자 전용 React 프론트엔드
└── kiosk-frontend-user/     # 사용자 전용 React 프론트엔드
```

---

## ✅ 14장까지 구현된 내용 요약

### 1. 유저 프론트 (`kiosk-frontend-user`) 추가

* Vite 기반 React 프로젝트 구성
* 사용자 UI: 매장/포장 선택 → 메뉴 → 장바구니 → 주문

### 2. 백엔드 연결 (Spring Boot)

* 기존 `admin`에서 등록한 메뉴를 사용자에게 제공
* API 경로 `/api/user/menus`로 제공
* CORS 및 시큐리티 설정을 통해 인증 없이 접근 허용

### 3. 시큐리티 설정 수정

```java
.requestMatchers("/api/user/**").permitAll()
.requestMatchers("/api/admin/**").hasRole("ADMIN")
```

### 4. 백엔드 구성 요소 추가

* **Controller**: `UserMenuController`
* **Service**: `UserMenuService`
* **DTO**: `MenuDto` (id, name, price, image)

### 5. 프론트에서 API 요청

```js
GET /api/user/menus
→ https://kiosktest.shop/uploads/${menu.image} 로 이미지 표시
```

### 6. 권한 오류(401) 해결

* `/api/user/menus` 경로를 인증 없이 공개
* 프록시 및 배포 설정을 통해 외부 접근 허용

---

## 🛒 장바구니 기능 개선

* 동일 메뉴 재선택 시 수량 증가
* ➕ / ➖ / ❌ 버튼으로 수량 조절 가능
* 총합계 자동 계산

---

## 📦 주문 흐름

1. 메뉴 선택 후 `localStorage`에 장바구니 저장
2. `/cart`에서 수량 조정 및 주문 버튼 활성화
3. 주문 시 `/api/orders`로 POST 요청
4. 주문 완료 후 `/processing` 페이지로 이동 (주문 확인 표시)

---

## 🧩 DB 연결 (MySQL)

* DB명: `admindb`
* 사용 테이블: `admin_menu`, `admin_user`
* JPA 설정: `spring.datasource.*` + `hibernate.ddl-auto=update`

---

## 🚀 배포 순서 요약

### 📁 1단계: 유저 프론트 빌드

```bash
cd kiosk-frontend-user
npm run build
```

### 📤 2단계: dist 폴더 서버 전송 (scp)

```bash
scp -i <pem파일경로> -r ./dist/* ubuntu@서버주소:/home/ubuntu/kiosk-system/static/user/
```

### 🔑 3단계: 서버에서 권한 변경

```bash
sudo chown -R www-data:www-data /home/ubuntu/kiosk-system/static/user
```

### 🔁 4단계: Nginx 재시작

```bash
sudo systemctl restart nginx
```

### ✅ 5단계: 최종 접속 확인

```
https://kiosktest.shop/
```

---

## ⚠️ 참고 사항

* 어드민은 `/admin`, 유저는 `/` 또는 `/user` 경로
* 이미지 업로드 경로: `/uploads/`
* Nginx 설정 alias로 직접 서빙됨

---


