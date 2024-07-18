# android-map-location
## Step 1. 카카오맵 API 심화
### 구현 기능 목록

1. 저장된 검색어 선택 시 -> 해당 검색어의 검색 결과 표시
   - 저장된 검색어 선택 시 해당 검색어의 목록이 조회되는 것
2. 검색 결과 목록 중 하나의 항목 선택 시 -> 해당 항목 위치 지도에 표시
   - BottomSheet 사용
     - 장소명, 주소 표시
3. 앱 종료 시 마지막 위치 저장
   - 앱 재실행 시 해당 위치로 포커스
4. 카카오 지도 onMapError() 호출 시 에러 화면 표시
5. 카카오 API 사용 위한 앱 키 외부 노출 X