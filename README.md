# 일이관지 📆

## 프로젝트 소개

> **일이관지(一以貫之) : 하나로써 만물을 꿰뚫는다**
> 
- 일이관지는 어떤 일에 있어서 막힘없이 밀고 나가는 것을 의미하는 사자성어입니다.
- 저희 일이관지 애플리케이션을 사용해 어떤 프로젝트든 막힘없이 쭉! 진행하고, 업무의 효율성을 느껴보세요!
- 프로젝트마다 보드를 생성하고, 작업 카드를 만들어 상태별로 관리할 수 있습니다.
- 해야 할 일, 진행 중인 일, 완료된 일을 한 눈에 확인해 프로젝트의 진행 척도를 확인할 수 있습니다.

## 사용 기술

- Java 17
- Spring boot 3.3.1
- JWT
- MySQL
- Redis
- Thymeleaf
- Html, JavaScript, CSS

## 기능 설명

### 유저

- 일이관지 애플리케이션은 로그인 후 이용이 가능합니다.
- 모든 유저는 이메일을 통해 회원가입할 수 있습니다.
- 현재 프로필을 조회할 수 있습니다.
- 비밀번호를 변경할 수 있습니다.
- 회원 탈퇴가 가능합니다.

### 보드

- 새로운 프로젝트를 시작하고 싶을 때 보드를 생성할 수 있습니다.
    - 보드를 생성하면, 자동으로 보드 매니저로 추가됩니다.
- 자신이 관리 중인 보드 목록을 볼 수 있습니다.
- 자신이 참여 중인 보드 목록을 볼 수 있습니다.
- 보드의 참여자, 매니저만 보드에 접근할 수 있습니다.
- 보드에 참여할 사용자를 초대할 수 있습니다.
- 보드에 접근하면 상태(컬럼) 별로 구분된 작업 목록들을 확인할 수 있습니다.

### 컬럼

- 각 보드에는 작업의 상태를 나타내는 컬럼을 추가할 수 있습니다.
    - ex) `To-do`, `In progress`, `Done`
- 컬럼 생성 시 기본적으로 마지막 순서가 지정됩니다.
- 생성 후 컬럼의 순서를 바꿀 수 있습니다.
- 컬럼은 수정, 삭제가 가능합니다.

### 카드

- 각 컬럼에는 작업할 내용을 작성한 카드를 추가할 수 있습니다.
- 카드 제목, 카드 내용을 작성할 수 있습니다.
- 카드에는 작업자와 마감일을 지정할 수 있습니다.
- 카드를 수정, 삭제할 수 있습니다.
- 카드의 상태(컬럼)를 변경할 수 있습니다.

### 댓글

- 각 카드에는 댓글을 작성할 수 있습니다.
- 댓글을 수정, 삭제할 수 있습니다.

### 🧑‍💼 보드 매니저

- 모든 기능을 사용할 수 있습니다.
- 보드를 수정하고 삭제할 수 있습니다.
- 컬럼 생성, 수정, 삭제가 가능합니다.
- 보드에 사용자를 초대할 수 있습니다.
    - 일반 참여자 or 매니저로 권한을 선택해 초대할 수 있습니다.
    - 즉, 보드 하나에 매니저는 여러 명이 될 수 있습니다.
    

### 🧑‍🤝‍🧑 보드 참여자

- 보드, 컬럼, 카드, 댓글 조회가 가능합니다.
- 카드와 댓글은 자유롭게 생성, 수정, 삭제할 수 있습니다.
  

## ERD Diagram

![image](https://github.com/user-attachments/assets/fe042047-370d-42c2-bd6a-fca97cf38c44)
