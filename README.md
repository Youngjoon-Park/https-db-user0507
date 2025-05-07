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

* ---
## 📱 CoolSMS 문자 인증 - 회원가입 및 전송 문제 해결 가이드

이 문서는 **CoolSMS 문자 API를 사용하기 위한 최소한의 가입 절차**와
**문자(SMS)가 안 오는 주요 원인 및 해결 방법**을 간단하고 명확하게 설명합니다.
---

### ✅ 1단계: 회원가입

1. [https://coolsms.co.kr](https://coolsms.co.kr) 접속
2. 우측 상단 `회원가입` 클릭
3. 이메일 입력 → 휴대폰 본인 인증 → 비밀번호 설정 후 가입 완료

---

### ✅ 2단계: API 키 발급

1. 로그인 후 상단 메뉴 `내 정보` 클릭
2. 좌측 메뉴 `API 설정` 클릭
3. 아래 두 값을 복사해서 백엔드에 입력:
   - API Key
   - API Secret

---

### ✅ 3단계: 발신번호 등록 (문자 전송을 위해 필수)

1. `내 정보 > 발신번호 관리` 메뉴 진입
2. 본인 전화번호 입력
3. 인증번호 수신 → 입력하여 등록 완료

※ 등록된 번호만 문자 발신에 사용 가능

---

### ❗ 문자(SMS)가 안 오는 5가지 주요 원인

| 원인                  | 설명                                     | 해결 방법                     |
| --------------------- | ---------------------------------------- | ----------------------------- |
| 1. 발신번호 미등록    | 보낸 번호가 사전에 등록되지 않음         | `발신번호 관리`에서 등록 필수 |
| 2. 인증 API 키 오류   | API Key / Secret 오타                    | 정확히 복사하여 코드에 입력   |
| 3. 전화번호 포맷 오류 | `01012345678` 형식만 허용                | `-` 없이 숫자만 입력          |
| 4. 요금 부족          | 문자 건당 요금 필요 (테스트는 무료 50건) | `포인트 충전` 메뉴 확인       |
| 5. 하루 전송 제한     | 무료 테스트 계정은 하루 20건 제한        | 다음 날 재시도 또는 유료 전환 |

---

### ✅ 전송 예시 (API 호출 주소)

```http
POST https://api.coolsms.co.kr/messages/v4/send
```

인증 헤더 예시:

```java
headers.setBasicAuth("API_KEY", "API_SECRET");
```


### 📌 테스트 팁

- **처음에는 발신자/수신자 모두 본인 번호로 테스트**하세요
- 응답에 에러 코드가 포함되면 콘솔에 출력된 에러 메시지를 꼭 확인하세요
- CoolSMS는 개발자센터 > `발송 내역` 메뉴에서도 성공 여부를 확인할 수 있습니다

---고객센터 답변
상태코드 : '3059' 변작된 발신번호
변작된 발신번호는 발신번호의 명의자가 해당 번호를 사용하여 인터넷 문자 발송을 금지해놓은 경우입니다.
발신번호 명의자가 직접 이용하시는 통신사에 연락하여 해당 서비스를 해지하셔야 합니다.
해지 신청 후 정상적으로 반영되기까지 1주일 정도 소요될 수 있습니다.

| 상황                   | 추천                        |
| --------------        | ------------------------- |
| 단기 테스트만 필요     | CoolSMS (단, 발신번호 문제 없을 때) |
| 실서비스 or 안정성 중요 | ✅ **네이버 클라우드 SENS 강력 추천** |



