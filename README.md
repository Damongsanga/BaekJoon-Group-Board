## BBOARD

###### SolvedAC api를 활용한 알고리즘 그룹 DashBoard 제공 및 문제 추천 서비스

<img src=".READMEdata/playGif-2.gif">

<hr>

#### 목차 (with link)

1. [프로젝트 개요](#프로젝트-개요)
2. [프로젝트 세부 소개](#프로젝트-세부-소개)
3. [프로젝트 개발 명세](#프로젝트-개발-명세)
4. [소감](#소감)

<hr>

### 프로젝트 개요

##### BBoard

- 서비스 설명 : 사용자가 그룹을 생성하여 그룹원들이 푼 백준 문제들을 통합하고 시각화하며, 사용자 레벨에 맞는 문제를 자동 추천해주는 서비스
- 기획 배경 : 알고리즘을 공부하는 과정에서 서로가 문제를 추천하거나, 각자 풀이한 문제를 비교하면서 동기부여를 얻는 경우가 다수 있음. 따라서 사용자가 그룹을 생성하여 서로 풀어본 문제들을 한눈에 보고 비교할 수 있고, 유사한 티어의 문제를 자동으로 추천해주는 웹페이지를 만들고자함.
- 개발 기간 : 2023.11.13 ~ 2023.11.24 (2주)
  <br>

##### 🧑‍💻 팀소개

| 이담비                                                                                    | 정덕주                                                                                    |
| ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| <img src="https://avatars.githubusercontent.com/u/47710007?v=4" width="200" height="200"> | <img src=".READMEdata/KakaoTalk_Photo_2023-12-15-00-30-05.jpeg" width="200" height="200"> |
| Front 70% Back 30%                                                                        | Back 70% Front 30%                                                                        |

<br>

##### 🛠️ 기술 스택

###### Front

![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white"> <img src="https://img.shields.io/badge/tailwind-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">

###### Back

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/mybatis-333333.svg?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

###### Environment

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) ![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white) ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) <img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">

![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black) ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

###### 버전 요구 사항

```
SpringBoot 3.1.5 Gradle
Vue 3.3.4
solved.ac api v3
```

<hr>

### 프로젝트 세부 소개

##### 🖥️ 화면 구성

| 화면       | 이미지                               |
| ---------- | ------------------------------------ |
| login page | ![Alt text](.READMEdata/image-5.png) |
| group page | ![Alt text](.READMEdata/image-4.png) |
| admin page | ![Alt text](.READMEdata/image-2.png) |
| main page  | ![Alt text](.READMEdata/image-3.png) |

<br>

##### 🎚️ 주요 기능

###### user/group

1. 백준 id 입력하여 로그인, 해당 아이디가 존재하지 않을 시 재입력하도록 알람
2. 아이디당 3개의 그룹에 소속 가능, 그룹에 속한 유저 정보 표시
3. 속한 그룹이 2개 이하일 때 신규 그룹 생성 기능
4. 그룹별 비밀번호 입력하여 admin page에서 유저 추가/방출 기능

###### mainpage

1. 유저 클릭 시 유저 아이디, 레벨, 레이팅 등의 정보 표시
2. 그룹에 속한 유저들이 푼 상위 100문제에서 클릭한 유저가 푼 문제 동적으로 표시
3. 마우스를 hover한 문제들에 대한 링크, 레벨, 연관 알고리즘 정보 제공
4. 상위 100문제 중에서 로그인한 유저 레벨과 유사한 문제 3개 자동 추천
5. 랭크 상위 3위까지 유저 정보 표시
   <br>

###### ⚠️ 참고사항 ⚠️

- solved.ac api는 비공식 api임으로, 이후 version이 변경되거나 서비스를 중단할 수 있음
- solved.ac api 버전 변경에 따른 호출 횟수 제한 변경 인한 오류가 발생할 수 있음.

<hr>

### 프로젝트 개발 명세

##### 🌲 Project Tree

```
BaekJoon-Group-Board
├─ springboot back
│  └─ bboard
│     └─ src
│        ├─ main
│        │  ├─ java
│        │  │  └─ com
│        │  │     └─ ssafypjt
│        │  │        └─ bboard
│        │  │           ├─ BboardApplication.java
│        │  │           ├─ config
│        │  │           │  ├─ SwaggerConfig.java
│        │  │           │  └─ WebConfig.java
│        │  │           ├─ controller
│        │  │           │  ├─ MainController.java
│        │  │           │  ├─ ProblemController.java
│        │  │           │  └─ UserGroupController.java
│        │  │           ├─ model
│        │  │           │  ├─ domain
│        │  │           │  │  ├─ groupinfo
│        │  │           │  │  │  ├─ GroupDomain.java
│        │  │           │  │  │  └─ UserAndGroupObjectDomain.java
│        │  │           │  │  ├─ ReloadDomain.java
│        │  │           │  │  ├─ solvedacAPI
│        │  │           │  │  │  ├─ FetchDomain.java
│        │  │           │  │  │  ├─ ProblemAndAlgoObjectDomain.java
│        │  │           │  │  │  ├─ ProblemDomain.java
│        │  │           │  │  │  ├─ UserDomain.java
│        │  │           │  │  │  ├─ UserPageNoObjectDomain.java
│        │  │           │  │  │  ├─ UserTierDomain.java
│        │  │           │  │  │  └─ UserTierProblemDomain.java
│        │  │           │  │  └─ UserAddReloadDomain.java
│        │  │           │  ├─ dto
│        │  │           │  │  ├─ Group.java
│        │  │           │  │  ├─ Problem.java
│        │  │           │  │  ├─ ProblemAlgorithm.java
│        │  │           │  │  ├─ RecomProblem.java
│        │  │           │  │  ├─ User.java
│        │  │           │  │  └─ UserTier.java
│        │  │           │  ├─ enums
│        │  │           │  │  └─ SACApiEnum.java
│        │  │           │  ├─ repository
│        │  │           │  │  ├─ GroupRepository.java
│        │  │           │  │  ├─ ProblemAlgorithmRepository.java
│        │  │           │  │  ├─ ProblemRepository.java
│        │  │           │  │  ├─ RecomProblemRepository.java
│        │  │           │  │  ├─ TierProblemRepository.java
│        │  │           │  │  ├─ UserGroupRepository.java
│        │  │           │  │  ├─ UserRepository.java
│        │  │           │  │  └─ UserTierProblemRepository.java
│        │  │           │  └─ service
│        │  │           │     ├─ GroupService.java
│        │  │           │     ├─ GroupServiceImpl.java
│        │  │           │     ├─ ProblemService.java
│        │  │           │     ├─ ProblemServiceImpl.java
│        │  │           │     ├─ UserService.java
│        │  │           │     └─ UserServiceImpl.java
│        │  │           ├─ session
│        │  │           │  ├─ SessionConst.java
│        │  │           │  └─ SessionManager.java
│        │  │           └─ util
│        │  │              └─ UtilConfig.java
│        │  └─ resources
│        └─ test
│
└─ vue front
   ├─ src
   │  ├─ App.vue
   │  ├─ assets
   │  │  ├─ logo.svg
   │  │  ├─ logoImg.png
   │  │  └─ main.css
   │  ├─ components
   │  │  ├─ groupselectpage
   │  │  │  ├─ EmptyGroupBox.vue
   │  │  │  └─ GroupBox.vue
   │  │  ├─ header
   │  │  │  └─ items
   │  │  ├─ items
   │  │  │  └─ problem-card
   │  │  │     ├─ AlgorithmTag.vue
   │  │  │     └─ ProblemCard.vue
   │  │  └─ mainpage
   │  │     ├─ header
   │  │     │  ├─ items
   │  │     │  │  ├─ GroupName.vue
   │  │     │  │  ├─ LoginUserInfo.vue
   │  │     │  │  └─ LogoTitle.vue
   │  │     │  └─ NavigationHeader.vue
   │  │     ├─ leftchange
   │  │     │  ├─ pyramid-and-usergrid
   │  │     │  │  ├─ GroupPyramid.vue
   │  │     │  │  ├─ GroupUserGrid.vue
   │  │     │  │  └─ usergrid
   │  │     │  │     ├─ MiniProfile.vue
   │  │     │  │     └─ UserTile.vue
   │  │     │  ├─ PyramidAndUsers.vue
   │  │     │  ├─ userprofile-and-hoverpro
   │  │     │  │  ├─ HoverPro.vue
   │  │     │  │  ├─ userprofile
   │  │     │  │  │  └─ UserDetailInfo.vue
   │  │     │  │  └─ UserProfile.vue
   │  │     │  └─ UserProfileAndHoverPro.vue
   │  │     ├─ LeftChangeBox.vue
   │  │     ├─ rightfixed
   │  │     │  ├─ GroupRating.vue
   │  │     │  ├─ ratingprofile
   │  │     │  │  └─ RatingProfile.vue
   │  │     │  └─ RecoPro.vue
   │  │     └─ RightFixedBox.vue
   │  ├─ main.js
   │  ├─ router
   │  │  └─ index.js
   │  ├─ stores
   │  │  ├─ adminInfo.js
   │  │  ├─ changebox-db.js
   │  │  ├─ fixedbox-db.js
   │  │  ├─ group.js
   │  │  ├─ loginInfo.js
   │  │  ├─ main-api.js
   │  │  └─ userInfo.js
   │  └─ views
   │     ├─ AdminPageView.vue
   │     ├─ AdminPasswordView.vue
   │     ├─ GroupSelectView.vue
   │     ├─ HomeView.vue
   │     ├─ LoginPageView.vue
   │     ├─ MainPageView.vue
   │     └─ NewGroupFormView.vue
   ├─ tailwind.config.js
   └─ vite.config.js

```

<br>

##### 💿 API 정보

###### solvec.ac 비공식 api v3

https://solvedac.github.io/unofficial-documentation/#/

![Alt text](.READMEdata/image-6.png)

###### 사용한 API

1. 사용자 정보 가져오기 (GET)

```
https://solved.ac/api/v3/user/show?handle=[유저아이디]
```

2. 상위 100문제 가져오기 (GET)

```
https://solved.ac/api/v3/user/top_100?handle=[유저아이디]
```

3. 사용자 티어별 푼 문제 수 가져오기 (GET)

```
https://solved.ac/api/v3/user/problem_stats?handle=[유저아이디]
```

4. 사용자가 푼 문제 페이지별로 가져오기 (GET)

```
https://solved.ac/api/v3/search/problem?query=@[유저아이디]&sort=level&direction=desc&page=[페이지번호]"
```

<hr>

### 회고

- 👋 [이담비 회고]()
- 👋 [정덕주 회고](https://velog.io/@damongsanga/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0-Solved.ac-api%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%B0%B1%EC%A4%80-%EA%B7%B8%EB%A3%B9-%EB%A7%8C%EB%93%A4%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8)
