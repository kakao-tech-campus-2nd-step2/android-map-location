# android-map-location

## 1단계 기능 목록
---
1. bottomSheet 레이아웃 제작하기
- feat(ui): Create BottomSheet layout for search results
2. 목록 중 하나의 항목을 선택하면 해당 항목의 위치를 지도에 마커 표시
- feat: implement search result item selection to show location on map
3. BottomSheetDialog 클래스 제작 및 맵에 통합하기
- feat: Create and integrate BottomSheetDialog with the map 
4. 현재 지도의 중심 위치를 저장 후 재실행 시 저장된 위치로 지도의 포커스를 설정. (SharedPreference 활용)
- feat: save last map position on app exit and restore on app start
5. 에러 화면 표시 기능
- feat: display error screen on Kakao map API error