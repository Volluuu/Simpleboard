# Simpleboard

1. 학습 예정 내용
- JWT, Spring security를 이용하여 간단한 게시판 만들기 진행.
- 로그인 시, JWT token을 통해 access, refresh token 생성 예정.
- client에 따라 구분하여 게시판 접근권한 설정 구현 예정.
[추가_230216]
- Spring boot layer에 대한 이해와 architecture 구성  목표


2. 구현 진행 상황
- filter를 통해 client 요청 확인 후, config를 통해 접근 권한 확인 완료.


3. 수정 필요 사항
- 로그아웃 시, 에러 발생. 해당 부분 수정 필요.
- token을 어디다 저장할 지?
