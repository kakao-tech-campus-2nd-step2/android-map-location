# android-map-location

## 프로젝트 설명
이 프로젝트는 카카오테크캠퍼스 2기 STEP2 4주차 미니과제로, 카카오 로컬 API와 카카오 지도 API를 사용하여 위치 이동 기능을 구현하고 테스트 코드를 작성하여 구현한 기능이 제대로 동작하는지 확인합니다.

## 구현 기능 목록
### 1단계 - 카카오 로컬 API 및 카카오 지도 API
- 저장된 검색어 선택 시, 해당 검색어의 검색 결과 표시
- 검색 결과 목록 중 하나의 항목 선택 시, 해당 항목의 위치를 지도에 표시
- 앱 종료 시, 마지막 위치를 저장 -> 다시 앱 실행 시, 해당 위치로 포커스
- 카카오 지도 onMapError() 호출 시, 에러 화면 출력

### 2단계 - 테스트 코드 작성
- 기능 테스트
  - MapActivity와 관련된 기능 테스트
    - Intent로 알맞은 데이터 값 전달 여부 확인
    - Intent로 받은 데이터가 있을 경우 BottomSheet 출력 확인
  - PlaceActivity와 관련된 기능 테스트
    - 검색어 저장 목록에 장소 추가 확인
    - 검색어 저장 목록에 장소 삭제 확인
    - 장소 목록 출력 확인
    - 검색어 저장 목록 출력 확인
- UI 테스트
  - MapActivity와 관련된 UI 테스트
    - MapView 출력 확인
    - 검색어 입력창 선택 시 화면 전환 확인
  - PlaceActivity와 관련된 UI 테스트
    - 키워드 검색 시 장소 목록 출력 확인
    - 장소 목록 선택 시 화면 전환 확인
