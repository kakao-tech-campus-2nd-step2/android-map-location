# android-map-location

- 카카오 테크 캠퍼스 과제(위치 검색 - 네트워크 통신) 수행을 위한 저장소입니다.

## content

- 실행 영상
  
![week4_1](https://github.com/user-attachments/assets/d05c8ac7-06cf-48c3-ac21-809567de23c3)

- 인증 에러시 화면
  
![week4_1](https://github.com/user-attachments/assets/7c9bc6f0-c163-4058-83cc-1299120c7230)

## flow chart

![map_location](https://github.com/user-attachments/assets/05e64aed-bcc6-4a3f-9a08-9bb5d2377702)

## feature

### 이전 단계

1. 검색어를 입력하면 검색 결과(15개 이상) 목록이 표시된다.
    - 리사이클러뷰 사용(세로 스크롤)

2. 입력한 검색어는 X를 눌러서 삭제할 수 있다.

3. 검색 결과 목록에서 하나의 항목을 선택할 수 있다.
    - 선택된 항목은 검색어 저장 목록에 추가된다.
    - 리사이클러뷰 사용(가로 스크롤)

4. 저장된 검색어는 X를 눌러서 삭제할 수 있다.

5. 이미 검색어 저장 목록에 있는 검색어를 검색 결과 목록에서 선택한 경우 기존 검색어는 삭제하고 다시 추가한다.

### 1단계 - 카카오맵 API 심화

1. 저장된 검색어를 선택하면 해당 검색어의 검색 결과가 표시된다.

2. 검색 결과 목록 중 하나의 항목을 선택하면 해당 항목의 위치를 지도에 표시한다.

3. 앱 종료 시 마지막 위치를 저장하여 다시 앱 실행 시 해당 위치로 포커스 한다.

4. 카카오지도 onMapError() 호출 시 에러 화면을 보여준다.

### 2단계 - 테스트

- 테스트 코드를 작성한다.
