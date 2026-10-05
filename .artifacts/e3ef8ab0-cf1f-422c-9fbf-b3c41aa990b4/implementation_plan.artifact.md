# Implementation Plan - Add Compose Preview to UserListScreen

The user reported that the design window (Compose Preview) is not visible for `UserListScreen.kt`. This is because there is no `@Preview` annotated function in the file. Additionally, the `UserListScreen` composable takes a `UserListViewmodel` as a parameter, which makes it difficult to preview directly.

## Proposed Changes

### [feature:userlist]

#### [MODIFY] [UserListScreen.kt](file:///Users/varunsingh/Documents/Projects/All_Android_Projects/2026_Android_projects/InterviewTestApplication/feature/userlist/src/main/java/com/example/userlist/UserListScreen.kt)

1.  **Refactor `UserListScreen`**: Separate the stateful logic (fetching state from ViewModel) from the UI rendering logic.
    *   Rename the current UI logic to `UserListContent`.
    *   `UserListContent` will take `UiState` and a `onRetry` callback as parameters.
2.  **Add `@Preview`**:
    *   Create a `UserListPreview` composable function.
    *   Annotate it with `@Preview(showBackground = true)`.
    *   Provide sample data to `UserListContent` within the preview.
    *   Wrap the preview in `InterviewTestApplicationTheme` (if possible, though it's in the `:app` module, so I might need to use a default `MaterialTheme` or move the theme to a core module. Since `:feature:userlist` likely doesn't depend on `:app`, I'll use a local theme or just `MaterialTheme`).
3.  **Clean up imports**: Remove unused or suspicious imports like `android.R` and `android.text.TextUtils.isEmpty`.

## Verification Plan

### Automated Tests
*   Run `./gradlew :feature:userlist:assembleDebug` to ensure the project still builds.

### Manual Verification
*   Check if the Preview is now visible in the Android Studio Design tab for `UserListScreen.kt`.
