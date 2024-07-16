# android-map-location

## Layout requirements

**Kakao map**

Display kakao Map

- Display selected search result using `bottom sheet`
- When onMapError() is called, print error message
- Save last  location before app closed, and when app is started, focusing that location

Search window button

- If Search window button is clicked, go to *Save search keyword*
  - *Stack Save search keyword* on top of the this window

**Save search keyword**

Input search keyword

- Text input window to search
- It has x button to erase

Saved search keyword list

- It has x button to erase
- It scrolls horizontally

Search result list

- Using `RecyclerView` to implement about search result list
- It scrolls vertically

## Function List

**Kakao map**

**Kakao map**

Display kakao Map

- Display selected search result using `bottom sheet`
- When onMapError() is called, print error message
- Save last  location before app closed, and when app is started, focusing that location

Go to *Save serach keyword*

- If Search window button is clicked, go to *Save search keyword*
  - *Stack Save search keyword* on top of the this window

**Save search keyword**

Requirements Rule

- Using `SQLite` to save search data
  - When application is restart, data is maintained
- Apply the MVVM architectural pattern

Input search keyword

- When application is restart, data is maintained
- Search every time a character is entered
- When clicked x button, string is erased

Saved search keyword list

- Keywords are not duplicated and recently serached keyword are added later
- Can search to select saved search keyword
- When clicked x button, saved search keyword is erased

Search result list

- There are at least 15 search results
- Search results have search word as categories
- Selected item is added to the saved search word list, and display location on the kakao map
