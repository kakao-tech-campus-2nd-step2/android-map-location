# android-map-location

## Layout requirements

**Kakao map**

Kakao map view

- display the kakao map screen

Search window button

- button to go `Save search keyword`

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

Requirements Rule

- When app is start, display Kakao map layout
- If Search window button is clicked, go to `Save search keyword`
  - Stack `Save search keyword` on top of the this window

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

- Words are not duplicated and recently serached word are added later
- When clicked x button, saved search word is erased

Search result list

- There are at least 15 search results
- Search results have search word as categories
- Selected item is added to the saved search word list
