# Collaboration Rules

## branch
### main
#### 최종 배포를 위한 브랜치
1. develop 브랜치에서 문제가 없이 병합됐고 기능에 문제가 없을 경우, 해당 브랜치에 최종 병합

### feature
#### 각 팀원이 기본적으로 기능 하나당 하나씩 생성하는 브랜치
1. 명명규칙 : feature/#이슈번호-기능(영문)
2. 대문자 금지
3. '-'만 사용
4. 명명규칙 예시 : feature/#1-connect-db-to-mypage
5. 1기능 당 한개의 feature 브랜치 생성

<br/>

## issue
### issue 작성
1. 명명 규칙 예시 : [대문자][첫글자만 대문자]기능설명(한글)
2. [FEATURE][Function]기능 설명(한글)
3. 명명 규칙에서 카멜케이스가 필요할 경우 카멜케이스로 작성
4. 1 기능당 1개의 이슈 생성
5. pull request까지 마치고 merge로 관련된 이슈는 close
6. Assigness와 Label 빼먹지 않고 작성
7. 템플릿에 맞게 작성하고 템플릿에 내용들 빼먹지 않기
8. 템플릿에서 필요없어진 부분은 알아서 제거 후 깔끔하게 정리


<br/>

## pull request
### Pull Request 작성
1. 명명 규칙 예시 : [대문자][첫글자만 대문자]기능설명(한글)
2. [FEATURE][PullRequest]기능
3. 명명 규칙에서 카멜케이스가 피룡할 경우 카멜케이스로 작성
4. pull request의 reviewers, assigness, labels 빼먹지 않고 작성
5. pull request의 템플릿에 맞게 작성 (공백이 생길 경우 잘 정리)
6. 자신이 리뷰어가 아니어도 확인했다면 comment
7. 2인 이상의 comment가 달리면 형상관리자가 merge

<br/>

## commit
### 명명 규칙
- 헤더 : type 작성
- 본문 : 기능 설명 (영어로 - 카멜케이스)
- 푸터 : 날짜 작성

### type의 종류 <br/>
- feat : 새로운 기능 <br/>
- fix : 버그 수정 <br/>
- build : 빌드 관련 수정 <br/>
- chore : 기타 작은 수정 <br/>
- docs : 문서 수정 <br/>
- style : 코드 스타일 혹은 포맷 수정 <br/>
- refactor : 코드 리팩토링 수정 <br/>
- test : 테스트 시 <br/>
- create : 양식 생성

### 예시
feat:PlusFunction/2024-10-21

<hr>

## 팀규칙

1. issue 생성 시 형상관리자에게 말하고 형상관리자가 issue 작성 및 관리
2. 하루에 한번은 병합(merge) -> develop
3. merge가 완료되면 꼭 모두 함께 pull을 받도록 함
4. 쉬는 날 제외 17시에 병합(merge) 및 문제 있을 경우 확인 및 해결
5. 쉬는 날에는 단톡으로 알림 후 병합(merge) -> 따라서 쉬는 날에는 작업을 할 경우 카톡 확인 후 작업할 것
6. 병합할 시간이 됐거나 병합이 필요한 상황에는 해놨던 부분까지하고 완성하지 못한 부분에 대해서는 컴파일에러가 나는 부분은 주석처리를 통해 해결함
7. interface를 통해 공통적으로 사용될 메소드는 통합

## 중요사항
<p>$\it{\large{\color{#DD6565}gitignore}}$</p>
토큰이 담긴 정보나 aws 등의 민감한 정보나 형상관리 필요없는 부분은 gitignore 사용 -> 꼭 숙지해야함
