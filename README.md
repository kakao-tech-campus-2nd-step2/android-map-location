# android-map-location
## Step 2. 테스트 코드
### 구현 기능 목록

1. Unit 테스트 코드(폴더: test [unitTest])
   - MainViewModelTest
     - addSearch: 새로운 검색어 추가&savedSearches LiveData 업데이트 확인
     - removeSearch: 기존 검색어 제거&savedSearches LiveData 업데이트 확인
     - searchPlaces: 검색어 입력 시 searchResults LiveData 업데이트 확인
   - ViewModelTest
     - getSavedSearches: 저장된 검색어 목록 올바르게 불러오는지 확인
     - saveSearches: 검색어 목록 올바르게 저장하는지 확인
   - PlaceRepositoryTest
     - searchPlaces: 주어진 쿼리에 대해 올바른 검색 결과 반환&검색 결과 없는 경우 빈 목록 반환 확인
2. UI 테스트 코드(폴더: androidTest)
   - MainActivityTest
     - 검색어 입력 시 검색 결과 RecyclerView에 표시 되는지 확인
     - 검색 결과 클릭 시 MapActivity 시작되는지 확인
     - 저장된 검색어 RecyclerView에 표시 되는지 확인
     - 저장된 검색어 삭제 시 목록이 올바르게 업데이트 되는지 확인
   - MapActivityTest
     - 지도에 마커가 올바르게 표시 되는지 확인
     - 앱 재실행 시 마지막 위치로 이동하는지 확인