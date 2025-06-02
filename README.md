# README - Updated writeup

Please see the [original writeup/Readme](README_original.md) for more context.

Please note I've disregarded some of the changes in the original and focused on the improvements you suggested.

## Notes:

- I've added UI Tests: BreedPhotosScreenTest, BreedListScreenTest
  - For UI tests, I had a fair bit of trouble trying to get the mockito-inline working, so rather than spending
    too much time on it, I made my own fakes where required, as can be seen in `BreedListScreenTest.kt` & `BreedPhotosScreenTest.kt`
  - I've implemented UI test as pure UI unit tests rather than integration tests,
    however you can see an example of an integration test with [ViewModel and Composables with this older commit where I used the real ViewModel to test.](https://github.com/aljidy/instafetch/commit/0f7a5cdbd3a282aef59bf7e5c821624420d211a4)
    - If this were a production codebase, it would be important to have both integration and UI test coverage.
  - Ideally I would have tests for navigation, however this would likely be covered by integration tests

- I've added more unit tests: BreedPhotosViewModelImplTest & BreedListViewModelTest
  - I've remove DogsRepo as all logic is now in the DogsApiMapper, so a test for DogsRepo would just cover implementation detail.  

- By abstracting the implementation from interface ViewModels and the DogsRepo, it allows for easier testing but also makes the code a bit more maintainable too
- I've also abstract the mapping from API types to Domain Models and also made the ViewModels not depend directly on the API models for the sake of maintainability.
- Navigation routes now have their own sealed class.
- In some instances (like the state models), I've moved these into their own files as opposed to the Viewmodels, for readability and discoverability.  
- By using DogsApiMapper, the viewModel no longer uses any Models from the API layer, making the app easy to maintain as there's no direct dependency anymore.

For the DI, I thought that the boilerplate was the main concern and it might make more sense to use a proper DI solution instead.

- TODO Change DI

TODO reusable Viewmodel loading / error handling