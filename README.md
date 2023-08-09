# Just a Login

Is just a login...

## Description

This project shows a Login screen with user and password entries.
Is using a repository pattern with clean architecture, each layer has its own module.

#### Presentation layer

On Presentation layer have all ViewModels and Views required for UI; there are implementations for
views using xml as a traditional way and Jetpack compose with the purpose of show both; as the same
for ViewStates and ViewEvents, all those UI states (sealed class) could be handled using only view
states on ViewModel but there are implementations using Flows and LiveData just to show both.

#### Data layer

Data layer contains implementation for repositories with instances of Room and Retrofit to handle
local and remote data sources respectively.

#### Domain layer

Domain layer contains use cases implementation and pure models.

### Communication among layers

1. UI requests for a method of ViewModel.
2. ViewModel executes Use Case
3. Use Case requests data from Repository and processes if necessary.
4. Repository returns data from either local or remote Data Source.
5. Information Flows back to UI where is handled by ViewModel notifying to UI with an state.

### Scenario

After entry user name and password login screen shows a success message and goes to a new screen
with user name deployed.

## How to run the app

1. Clone this [repo](https://github.com/DavEsMtz/Just-a-login....git) import using Android Studio.
2. Once you have opened the project sync project with Gradle, if you are facing problems I let this
   image as reference.
![](../invalidate and restart.jpg)
3. On Build Variants select PoCMockServer to get a mocked login response, otherwise response will always failed, because there is no active server.
4. Run and cheers!

### At a glance:
- Mix of views between xml and Compose is just to show both methods usage, same as Live Data and flows to handle view state and events on ViewModel.
- Both entries should be filled to continue.
- Handles mock response using build types, make sure you are using the correct Build Variant I mentioned above.
- Supports orientation changes
- Room as a local Data Source is implemented and operative but is not called anywhere.
- Has a proto implementation to handle errors.

##### Known Issues
- Background is upside down on preview but when you run the app is shown normally.
