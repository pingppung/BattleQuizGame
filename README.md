<p align="middle" >
  <img width = "35%" src="https://github.com/pingppung/BattleQuizGame/assets/57535999/916f5aff-0a9d-4221-b559-904191f37e63"/>
</p>
<h1 align="middle"></h1>
<h3 align="middle">자바 Socket 통신을 이용한 배틀 퀴즈 게임</h3>
<h5 align="middle">프로젝트 기간 : 2020년 10월 11일 ~ 2020년 12월 10일</h5>
<h5 align="middle">유튜브 링크 : https://youtu.be/ra82PbbsF6w</h5>
<h6 align="middle">팀 프로젝트로 처음에 시작하였으나 혼자 리팩토링 진행한 프로젝트</h6>
<h6 align="middle">이전 버전 : https://github.com/pingppung/BattleQuizGame/tree/previous_version</h6>

<br/>

# 📝 프로젝트 소개

## Description
BattleQuizGame은 socket 통신을 이용한 4인용 퀴즈 게임입니다.<br/>
게임방에 4명의 플레이어가 입장하여 준비 완료를 하면 게임이 시작되는 형식입니다.<br/>
게임에서 승리하여 얻은 코인으로 자신의 캐릭터를 구매하여 변경할 수 있습니다.<br/>
<br/>
<br/>

## 주요 기능 & 설명
<img src = "https://github.com/pingppung/BattleQuizGame/assets/57535999/8fc39731-63f9-4aea-89ed-3c53949a327b" />

- 게임방
  - 로비창에서 '방찾기' 버튼 누를 시 랜덤으로 게임방에 입장하게 된다.
  - 4명이 입장하여 모두 Ready 버튼을 누를 경우 게임이 시작된다.
  - 퀴즈는 객관식과 ox 문제로 이루어져 있으며 각 문제마다 10초의 제한시간이 주어진다. 
  - 게임 결과 1등부터 차례대로 +10, +5, +2, +0개의 코인을 획득할 수 있다.
    
- 코인샵
  - 코인 10개로 캐릭터를 구매하여 변경할 수 있다.
 
- 채팅
  - 로비방에서 모든 클라이언트와 채팅이 가능하다.
  - 게임방에서 채팅을 할 경우 같은 방 안에 있는 플레이어들끼리 채팅할 수 있다.
    
<br/>

## 프로젝트 구조
<img src = "https://github.com/pingppung/BattleQuizGame/assets/57535999/a9ffc29c-0bb9-41d4-89f6-aea8b1fdc6e0">
<br/>


## 기술 스택

- Language : `Java`
- Library & Framework : `Socket` `Swing`
- Tool : `Eclipse IDE`
<br/>

