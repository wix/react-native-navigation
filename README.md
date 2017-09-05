[![Build Status](https://travis-ci.org/wix/react-native-navigation.svg?branch=v2)](https://travis-ci.org/wix/react-native-navigation)
[![Join us on Discord](https://img.shields.io/badge/discord-react--native--navigation-738bd7.svg?style=flat)](https://discord.gg/DhkZjq2)

#  React Native Navigation v2 (WIP)
We are rebuilding react-native-navigation

- [Why?](#why-rebuild-react-native-navigation)
- [Where is it standing now?](#current-status)
- [Documentation](https://wix.github.io/react-native-navigation/v2/)
- [Contributing](CONTRIBUTING.md)

## Why Rebuild react-native-navigation?

### A New & Improved Core Architecture
react-native-navigation has a few issues which are unsolvable in its current architecture. These issues stem from the same problem: you cannot specify on which screen you wish to make an action. Whenever you want to push a screen, show a modal or any other action, the action defaults to originate from your current screen. In most cases this is fine, but becoms problematic in specific edge cases. For example: <br>
* What if you want to update your navbar icons and the user pops the screen? Your icons might update on the wrong screen.
* What if you want to push a screen as a result of a redux action?

There are ways to solve some of these problems in v1 but they are not straightforward. We want to change that.

#### New API
To solve this problem in v2, every screen receives its `containerId` as a prop. Whenever you want to perform an action from that screen you need to pass the `containerId` to the function:
```js
Navigator.pop(this.props.containerId)
```
### Built for Contributors
Currently, it requires a lot of work to accept pull requests. We need to manually make sure that everything works before we approve them because v1 is not thoroughly tested. <br>
v2 is written with contributors in mind from day one.

#### Written In TDD
v2 is written in Test Driven Development. We have a test for every feature including features that are not implemented yet. This makes accepting pull requests extremely easy: If our tests pass, your pull request is accepted.


## Current Status
v2 currently supports most of react-native-navigation’s basic functionality but it is still behind v1.
Here is the full comparison of features between v1 and v2 (will be updated regularly):
### Top Level API

|    API              | v1  | v2 |
|--------------------|-----|----|
| startTabBasedApp   |   ✅    |   ✅  |
| startSinglePageApp |   ✅   |  ✅   |
| registerScreen     |   ✅   |  ✅   |
| drawer             |    ✅  |    ✅ |
### Screen API

|  API              | v1     | v2  iOS      |	v2 Android |
|---------------------|--------|------------|--------------|
| push                |  ✅     |   ✅       |	✅		|
| pop                 |  ✅     |  ✅        |	✅	|
| showModal           |  ✅     |  ✅        |	✅|
| popToRoot           |   ✅     |   ✅         |✅	|
| resetTo             |   ✅     |    ✅        |	✅|
| dismissModal        |   ✅     |     ✅       |	✅|
| dismissAllModals    |   ✅     |      ✅      |	✅|
| showContextualMenu      |   ✅     |     / Android specific       |[Contribute](CONTRIBUTING.md) |
| dismissContextualMenu      |   ✅     |   / Androic specific        |[Contribute](CONTRIBUTING.md)  |
| showFab      |   ✅     |    / Android specific     |  [Contribute](CONTRIBUTING.md)  |
| dismissFab      |   ✅     |    / Android specific       | [Contribute](CONTRIBUTING.md) |
| showSnackBar     |   ✅     |     / Android specific    |   [Contribute](CONTRIBUTING.md) |
| dismissSnackBar     |   ✅     |    / Android specific      |  [Contribute](CONTRIBUTING.md) |
| showLightBox        |   ✅     |      [Contribute](CONTRIBUTING.md)      | [Contribute](CONTRIBUTING.md)  |
| dismissLightBox     |   ✅     |       [Contribute](CONTRIBUTING.md)       | [Contribute](CONTRIBUTING.md) |
| handleDeepLink      |   ✅     |       [Contribute](CONTRIBUTING.md)       | [Contribute](CONTRIBUTING.md) |
| Screen Visibility   |   ✅     |       ✅     |✅|

### Styles

Note:  v1 properties with names beginning with 'navBar' are replaced in v2 with properties beginning with 'topBar' to avoid confusion with the Android native bottom nav bar.

|                       | v1  | v2 iOS | v2 Android | Contributors |
|-----------------------|-----|--------|------------|------------|
| topBarTextColor |   ✅    |    ✅     |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarTextFontSize    |   ✅    |    ✅      |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarTextFontFamily  |  ✅     |      ✅     |     [Contribute](CONTRIBUTING.md)        | Wix |
| topBarBackgroundColor |  ✅     |  ✅       |     ✅         | Wix|
| topBarButtonColor     |  ✅     |    ✅      |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarHidden          |   ✅    |   ✅      |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarHideOnScroll    |  ✅     |  ✅    |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarTranslucent     |  ✅     |   ✅     |     [Contribute](CONTRIBUTING.md)        | Wix|
| topBarTransparent     | ✅      |   WIP @bogobogo     |     [Contribute](CONTRIBUTING.md)        |
| topBarNoBorder        |  ✅     |    ✅     |     [Contribute](CONTRIBUTING.md)        |  @gtchance|
| drawUnderTabBar       |  ✅     |    WIP @gran33     |      [Contribute](CONTRIBUTING.md)       | |
| drawUnderTopBar       |  ✅     |    WIP @gran33     |      [Contribute](CONTRIBUTING.md)       ||
| statusBarBlur         |  ✅     |    ✅     |      [Contribute](CONTRIBUTING.md)       | @gtchance|
| topBarBlur            | ✅      |    ✅     |      [Contribute](CONTRIBUTING.md)       | @gtchance|
| tabBarHidden  |   ✅  |   ✅     |    [Contribute](CONTRIBUTING.md)        | @gtchance|
| statusBarTextColorScheme |  ✅   |   in development      |      / iOS specific    |
| statusBarTextColorSchemeSingleScreen|  ✅   |     in development    |     / iOS specific      |
| navBarSubtitleColor          |  ✅   |   [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)      |
| navBarSubtitleFontFamily    |   ✅  |    [Contribute](CONTRIBUTING.md)    |     [Contribute](CONTRIBUTING.md)       |
| screenBackgroundColor     | ✅    |   ✅     |     [Contribute](CONTRIBUTING.md)       |  Wix|
| orientation     |  ✅   |    ✅     |   [Contribute](CONTRIBUTING.md)          | Wix|
| statusBarHideWithTopBar        |  ✅   |   ✅     |     [Contribute](CONTRIBUTING.md)       | @gtchance|
| statusBarHidden       |  ✅   |    ✅       |     [Contribute](CONTRIBUTING.md)      | WIX |
| disabledBackGesture       |   ✅  |  WIP @gran33     |    / iOS specific     |
| screenBackgroundImageName         |   ✅  |   [Contribute](CONTRIBUTING.md)      |    [Contribute](CONTRIBUTING.md)        |
| rootBackgroundImageName            |  ✅   |    [Contribute](CONTRIBUTING.md)     |    [Contribute](CONTRIBUTING.md)       |
| setButtons          |   ✅     |    ✅    | [Contribute](CONTRIBUTING.md) | @Johan-dutoit|
| title            |   ✅     |        	✅    | 	✅| Wix|
| toggleDrawer        |   ✅     |        [Contribute](CONTRIBUTING.md)   | [Contribute](CONTRIBUTING.md) |
| toggleTabs          |   ✅     |       in development    | in development|
| setTabBadge         |    ✅    |       ✅    | [Contribute](CONTRIBUTING.md)| Wix|
| switchToTab         |    ✅    |      in development    |[Contribute](CONTRIBUTING.md) |
| toggleNavBar        |   ✅     |      WIP @gran33     | [Contribute](CONTRIBUTING.md)|
| navBarCustomView        |   ✅     |     WIP @gran33     | [Contribute](CONTRIBUTING.md)|
| customTransition(shared element)       |     :x:  |     WIP @bogobogo     | [Contribute](CONTRIBUTING.md)|
| splitViewScreen       |     :x:  |    [Contribute](CONTRIBUTING.md)      | [Contribute](CONTRIBUTING.md)|


Element tranisitions, adding buttons and styles are not yet implemented. [Contribute](CONTRIBUTING.md)