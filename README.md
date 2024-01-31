# petree-backend
⭐ 프로젝트 소개
---
<img src="./petree-img/home.png" >

- **Who’s Breeder?**
    
    **우수한 견종보존을 위해 동물보호 의식과 견종표준의 이해를 기반으로,**
    
    **견종의 짝짓기, 출산, 질병, 위생 자견분양 등 번식에 전문적인 지식을 갖추어**
    
    **강아지나 고양이의 혈통을 유지하고 올바르게 번식해 애견문화 발전에 기여**
    
-
    **Breeder와 Adopter(분양희망자)를 연결하여, 강아지를 분양받도록 도와주는 프로젝트**

## ⏰개발 기간

- 2023.04.17 ~ 2023.11.05

## 📎배포 사이트

- [펫트리](https://nextpetree.store/)

👤 팀원 소개
---

**박수현(팀장)**
- 데이터베이스 설계
- nginx, docker, CICD를 통한 ec2배포
- 아키텍처 설계브리더 모아보기 거리순 정렬 및 페이징처리
- jwt자체 로그인 / 로그아웃
- 카카오 Oauth로그인
- 강아지 다중 검색
- 반려인 지식테스트 랜덤 조회, 점수 반환 및 틀린 문항에 대한 해설 반환
- 강아지 예약신청 및 결재
- 제3자가 보는 브리더, 분양희망자 프로필 조회
- 분양후기CRUD
- 분양신청내역 페이징 처리
- 회원 정보 수정

**오창현(프론트 부팀장)**
- 로그인
- 회원가입
- 회원탈퇴
- 분양신청내역

**김민준(프론트)**
- 강아지 모아보기
- 브리더 모아보기
- 프로필 관리
- 회원정보 수정

**김용현(프론트)**
- 강아지 모아보기 상세
- 브리더 모아보기 상세
- 분양후기 관리

**이지수(백엔드)**
- 보유견종 CRUD
- 브리더 관련 자격증 제출 및 결재

**이수언(디자이너)**
- 피그마를 통한 디자인
- 시연영상 제작

💠 아키텍쳐 
---
<img src="https://github.com/next-petree/backend/blob/main/petree-img/%EC%B5%9C%EC%A2%85%20%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90(0115).png">

🖥️프로젝트 미리보기
---

🏷️ Backend 폴더 구조
---
<pre>
    <code> 
        backend
        ├─ .github
        ├─ .gitlab
        ├─ .idea
        ├─ gradle
        │  └─ wrapper
        │  │  ├─ gradle-wrapper.jar
        │  │  └─ gradle-wrapper.properties
        ├─ src
        │  ├─ main
        │  │  ├─ java
        │  │  │  ├─ com
        │  │  │  │  ├─ example
        │  │  │  │  │  ├─ example
        │  │  │  │  │  │  ├─ petree
        │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  ├─ adopter
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ basic_test
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ breeder
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ dog
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ main_breed
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ matching
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ member
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  │  ├─ verification
        │  │  │  │  │  │  │  │  │  ├─ controller
        │  │  │  │  │  │  │  │  │  ├─ domain
        │  │  │  │  │  │  │  │  │  ├─ dto
        │  │  │  │  │  │  │  │  │  ├─ repository
        │  │  │  │  │  │  │  │  │  ├─ schema
        │  │  │  │  │  │  │  │  │  └─ service
        │  │  │  │  │  │  │  ├─ global
        │  │  │  │  │  │  │  │  ├─ config
        │  │  │  │  │  │  │  │  ├─ error
        │  │  │  │  │  │  │  │  ├─ jwt
        │  │  │  │  │  │  │  │  ├─ util
        │  │  │  │  │  │  │  └─ └─ web
        │  │  │  └─ resources
        │  │  │  └─ querydsl
        │  │  │  └─ test
        │  ├─ .gitignore
        │  ├─ build.gradle
        │  ├─ docker-compose.yml
        │  ├─ Dockerfile
        │  ├─ Dockerfile-dev
        │  ├─ gradlew
        │  ├─ gradlew.bat
        │  ├─ README.md
        └─ └─  settings.gradle
    </code>
</pre>

