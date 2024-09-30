# SIT-common

Add it in your root build.gradle at the end of repositories:
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}	
```
Add the dependency

```
dependencies {
    implementation 'com.github.vivekpshaligram:sit-common:2.0.0'
}
```

## Features
**Network Connection Interceptor**

SIT-common includes a network connection interceptor that checks for network availability before making API calls. This helps ensure that your application handles network-related issues gracefully.

**Authentication Interceptor**

The library also provides an authentication interceptor that manages token-based authentication seamlessly. It automatically attaches the necessary authentication tokens to your requests.

**Base Adapter**

SIT-common provides a base adapter to simplify the implementation of RecyclerViews. This base adapter offers common functionalities and can be easily extended for custom use cases.

**BaseActivity / BaseFragment**

The `BaseActivity / BaseFragment` class provides essential functionalities for managing fragment lifecycle and UI interactions, including:

- **Loader Management**: Easily show and hide loading indicators during data operations.
- **Error Handling**: Display error messages through Snackbars and dialog pop-ups.
- **Message and Success Dialogs**: Show informative and success messages to users with customizable dialogs.

#### Key Features

- **Swipe-to-Refresh**:
  - Integrates swipe-to-refresh functionality out of the box. Simply implement a listener to refresh your data.
  - Automatically manages the scrolling behavior when the RecyclerView is present within the swipe-to-refresh view.

- **Abstract Methods**:
  - `getLayoutId()`: Define the layout resource ID for the fragment.
  - `createViewModel()`: Create and return the ViewModel instance associated with the fragment.

**BaseViewModel**

The `BaseViewModel` class provides essential functionality for managing application-wide states and interactions. It includes dependency injection for application context, shared preferences, and resource provider, along with various LiveData variables for managing UI states.

#### Key Features

- **Injected Dependencies**:
  - `Application Context`: For accessing application-level resources.
  - `Preferences`: To manage user preferences easily.
  - `Resource Provider`: To facilitate string and resource retrieval.

- **LiveData Variables**:
  - **MutableLiveData**:
    - `loading`: Indicates loading states.
    - `error`: Captures error messages or states.
    - `success`: Indicates success messages or states.
    - `isDataEmpty`: Boolean indicating if the data set is empty.
  - **SingleLiveEvent**:
    - Used for one-time events such as showing errors or success messages.

- **Pagination Management**:
  - `perPage`: Number of items per page.
  - `page`: Current page number.

- **Search Management**:
  - `searchValue`: Current search query.
  - `searchJob`: Job used to debounce search actions.

- **Utility Methods**
  - `checkResponse`: Validates the API response and manages success/error states.
  - `manageErrorAndSessionOut`: Manages errors and session timeouts based on the API response.
  - `loading`: Manages loading states, especially useful for pagination.
  - `clearList`: Clears the current data list when starting pagination anew.
  - `managePagination`: Manages pagination logic by checking if more data needs to be loaded.
  - `manageSearch`: Handles search logic and debounces search input.