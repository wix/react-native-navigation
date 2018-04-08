[![npm (tag)](https://img.shields.io/npm/v/react-native-navigation/alpha.svg)](https://github.com/wix/react-native-navigation/tree/v2#react-native-navigation-v2-wip)
[![Build Status](https://jenkins-oss.wixpress.com/job/react-native-navigation-master/badge/icon)](https://jenkins-oss.wixpress.com/job/react-native-navigation-master/)
[![Join us on Discord](https://img.shields.io/badge/discord-react--native--navigation-738bd7.svg?style=flat)](https://discord.gg/DhkZjq2)
[![StackExchange](https://img.shields.io/stackexchange/stackoverflow/t/react-native-navigation.svg)](https://stackoverflow.com/questions/tagged/react-native-navigation)


#  React Native Navigation v2 (WIP)
We are rebuilding react-native-navigation.

> As we are in stage alpha, expect breaking API changes or use a specific version (for example "2.0.1234")

- [Why?](#why-rebuild-react-native-navigation)
- [v2 Roadmap](#v2-roadmap)
- [v1 vs v2 feature comparison](#v1-vs-v2-feature-comparison)
- [Documentation](https://wix.github.io/react-native-navigation/v2/)
- [Contributing](/docs/docs/WorkingLocally.md)

## Why Rebuild react-native-navigation?

### A New & Improved Core Architecture
react-native-navigation has a few issues which are unsolvable in its current architecture. These issues stem from the same problem: you cannot specify on which screen you wish to make an action. Whenever you want to push a screen, show a modal or any other action, the action defaults to originate from your current screen. In most cases this is fine, but becomes problematic in specific edge cases. For example: <br>
* What if you want to update your navbar icons and the user pops the screen? Your icons might update on the wrong screen.
* What if you want to push a screen as a result of a redux action?

There are ways to solve some of these problems in v1 but they are not straightforward. We want to change that.

#### New API
To solve this problem in v2, every screen receives its `componentId` as a prop. Whenever you want to perform an action from that screen you need to pass the `componentId` to the function:
```js
Navigator.pop(this.props.componentId)
```
Another big architectural change is that now you can compose arbitrary native layout hierarchies, and assign a custom `id` to each and control them individually.

### Built for Contributors
Currently, it requires a lot of work to accept pull requests. We need to manually make sure that everything works before we approve them because v1 is not thoroughly tested. <br>
v2 is written with contributors in mind from day one.

#### Written In TDD
v2 is written in Test Driven Development. We have a test for every feature including features that are not implemented yet. This makes accepting pull requests extremely easy: If our tests pass, your pull request is accepted.

## v2 Roadmap

### Current Priorities

1) buttons in Android
2) showOverlay in iOS
3) showOverlay in Android
4) async commands
5) currentTab
6) change Options to be nested
7) topTabs in both platforms, with API implications

### Top API

|       Top API         | iOS  | Android |
|--------------------|:-----:|:----:|
| setRoot   |   ✅    |   ✅  |
| registerComponent |   ✅   |  ✅   |
| component     |   ✅   |  ✅   |
| sideMenu             |    ✅  |    ✅ |
| tabs            |    ✅  |    ✅ |
| External Component       |   ✅  |   ✅ |
| splitView           |   [Contribute](/docs/WorkingLocally.md)   |   [Contribute](/docs/WorkingLocally.md) |

### Screen API

|       Screen API         | iOS  | Android |
|--------------------|:-----:|:----:|
| push              |   ✅       |	✅		|
| pop               |  ✅        |	✅	|
| popToRoot              |   ✅         |✅	|
| resetTo             |   ✅        |	✅|
| showModal              |  ✅        |	✅|
| dismissModal           |     ✅       |	✅|
| showOverlay             |  ✅         |	✅ |
| dismissOverlay             |  ✅   |	✅ |
| customTransition            |   ✅        |	[Contribute](/docs/WorkingLocally.md) |
| Screen Visibility        | ✅     |✅|
| async commands (await push)     |  ✅        |✅   |

### Navigation Options

|       topBar         | iOS  | Android | Contributor(s) |
|--------------------|:----:|:-----:|-----|
| title         |        	✅    | 	✅| Wix|
| textColor    | ✅     |     ✅        | Wix|
| textFontSize       |    ✅      |     ✅        | Wix|
| textFontFamily     |      ✅     |     ✅        | Wix |
| backgroundColor    |  ✅       |     ✅         | Wix|
| buttonColor         |    ✅      |✅| Wix|
| hidden             |   ✅      |     ✅        | Wix|
| hideOnScroll         |  ✅    |✅| Wix|
| translucent         |   ✅     |     [Contribute](/docs/WorkingLocally.md)        | Wix|
| transparent         |  ✅        |     [Contribute](/docs/WorkingLocally.md)        | |
| noBorder             |    ✅     |     [Contribute](/docs/WorkingLocally.md)        |  @gtchance|
| drawUnder         |    ✅     |✅||
| blur               |    ✅     |      [Contribute](/docs/WorkingLocally.md)       | @gtchance|
| custom component          |✅     |✅|
| background component          |✅     |[Contribute](/docs/WorkingLocally.md)| |
| subtitleColor            |   ✅      |[Contribute](/docs/WorkingLocally.md)| |
| subtitleFontFamily      |    [Contribute](/docs/WorkingLocally.md)    |     [Contribute](/docs/WorkingLocally.md)| |
| largeTitle (iOS 11)      |    ✅     |     /iOS Specific       | |

|       tabBar         | iOS  | Android | Contributor(s) |
|--------------------|:----:|:-----:|-----|
| drawUnder          |    ✅     |✅| |
| hidden   |   ✅     |    ✅        | @gtchance |
| tabBadge          |       ✅    |✅| Wix|
| currentTab by Index          |       ✅    | ✅ | Wix |
| currentTab by componentId         |✅| ✅ | Wix |

|       buttons        | iOS  | Android | Contributor(s) |
|--------------------|:----:|:-----:|-----|
| id    |   ✅      |✅|@Johan-dutoit|
| testID   |     ✅   |✅| @Johan-dutoit|
| color             |    ✅     |✅| @Johan-dutoit|
| icon          |   ✅     |✅| @Johan-dutoit|
| disableTint        |    ✅       |✅| @Johan-dutoit|
| fontSize        |    ✅       |✅| @Johan-dutoit |
| fontWeight        |    ✅       |    [Contribute](/docs/WorkingLocally.md)   | |

|       statusBar        | iOS  | Android | Contributor(s) |
|--------------------|:----:|:-----:|-----|
| textColorScheme    |✅|[Contribute](/docs/WorkingLocally.md) | |
| textColorSchemeSingleScreen   |✅|     / iOS specific      | |
| blur             |    ✅     |      [Contribute](/docs/WorkingLocally.md)       | @gtchance |
| hideWithTopBar          |   ✅     |     [Contribute](/docs/WorkingLocally.md)       | @gtchance |
| hidden         |    ✅       |     [Contribute](/docs/WorkingLocally.md)      | Wix |

|       other        | iOS  | Android | Contributor(s) |
|--------------------|:----:|:-----:|-----|
| screenBackgroundColor        |   ✅     |     [Contribute](/docs/WorkingLocally.md)       |  Wix |
| orientation       |    ✅     |✅| Wix |
| disabledBackGesture        |    ✅     |    / iOS specific     | |
| screenBackgroundImageName        |   ✅      |    [Contribute](/docs/WorkingLocally.md)        | |
| rootBackgroundImageName              |    ✅     |    [Contribute](/docs/WorkingLocally.md)       | |
| sideMenuVisible          |      ✅   |✅| |


## v1 vs v2 Feature Comparison
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
|---------------------|:--------:|:------------:|:--------------:|
| push                |  ✅     |   ✅       |	✅		|
| pop                 |  ✅     |  ✅        |	✅	|
| showModal           |  ✅     |  ✅        |	✅|
| popToRoot           |   ✅     |   ✅         |✅	|
| resetTo             |   ✅     |    ✅        |	✅|
| dismissModal        |   ✅     |     ✅       |	✅|
| dismissAllModals    |   ✅     |      ✅      |	✅|
| showContextualMenu      |   ✅     |     / Android specific       |[Contribute](/docs/WorkingLocally.md) |
| dismissContextualMenu      |   ✅     |   / Androic specific        |[Contribute](/docs/WorkingLocally.md)  |
| showFab      |   ✅     |    / Android specific     |✅|
| dismissFab      |   ✅     |    / Android specific       |✅|
| showSnackBar     |   ✅     |     / Android specific    |   [Contribute](/docs/WorkingLocally.md) |
| dismissSnackBar     |   ✅     |    / Android specific      |  [Contribute](/docs/WorkingLocally.md) |
| showLightBox        |   ✅     |:x:      |:x:  |
| dismissLightBox     |   ✅     |:x:|:x: |
| showOverlay|:x:|✅|✅|
| dismissOverlay|:x:|✅|✅|
| handleDeepLink      |   ✅     |       [Contribute](/docs/WorkingLocally.md)       | [Contribute](/docs/WorkingLocally.md) |
| Screen Visibility   |   ✅     |       ✅     |✅|

### Styles

Note:  v1 properties with names beginning with 'navBar' are replaced in v2 with properties beginning with 'topBar' to avoid confusion with the Android native bottom nav bar.

|                       | v1  | v2 iOS | v2 Android |
|-----------------------|:--------:|:------------:|:----------|
| topBarTextColor |   ✅    |    ✅     |     ✅        | Wix|
| topBarTextFontSize    |   ✅    |    ✅      |     ✅        | Wix|
| topBarTextFontFamily  |  ✅     |      ✅     |     ✅        | Wix |
| topBarBackgroundColor |  ✅     |  ✅       |     ✅         | Wix|
| topBarButtonColor     |  ✅     |    ✅      |✅| Wix|
| topBarHidden          |   ✅    |   ✅      |     ✅        | Wix|
| topBarHideOnScroll    |  ✅     |  ✅    |✅| Wix|
| topBarTranslucent     |  ✅     |   ✅     |     [Contribute](/docs/WorkingLocally.md)        | Wix|
| topBarTransparent     | ✅      |✅|     [Contribute](/docs/WorkingLocally.md)        |
| topBarNoBorder        |  ✅     |    ✅     |     [Contribute](/docs/WorkingLocally.md)        |  @gtchance|
| drawUnderTabBar       |  ✅     |    ✅     |✅| |
| drawUnderTopBar       |  ✅     |    ✅     |✅||
| statusBarBlur         |  ✅     |    ✅     |      [Contribute](/docs/WorkingLocally.md)       | @gtchance|
| topBarBlur            | ✅      |    ✅     |      [Contribute](/docs/WorkingLocally.md)       | @gtchance|
| tabBarHidden  |   ✅  |   ✅     |✅| @gtchance|
| statusBarTextColorScheme |  ✅   |✅|      / iOS specific    |
| statusBarTextColorSchemeSingleScreen|  ✅   |✅|     / iOS specific      |
| topBarSubtitleColor          |  ✅   |✅|      [Contribute](/docs/WorkingLocally.md)      |
| topBarSubtitleFontFamily    |   ✅  |✅|     [Contribute](/docs/WorkingLocally.md)       |
| screenBackgroundColor     | ✅    |   ✅     |     [Contribute](/docs/WorkingLocally.md)       |  Wix|
| orientation     |  ✅   |    ✅     |✅| Wix|
| statusBarHideWithTopBar        |  ✅   |   ✅     |     [Contribute](/docs/WorkingLocally.md)       | @gtchance|
| statusBarHidden       |  ✅   |    ✅       |     [Contribute](/docs/WorkingLocally.md)      | WIX |
| disabledBackGesture       |   ✅  |   ✅  |    / iOS specific     |
| screenBackgroundImageName         |   ✅  |   ✅      |    [Contribute](/docs/WorkingLocally.md)        |
| rootBackgroundImageName            |  ✅   |    ✅     |    [Contribute](/docs/WorkingLocally.md)       |
| setButtons          |   ✅     |    ✅    |✅| @Johan-dutoit|
| title            |   ✅     |✅|    ✅| Wix|
| toggleDrawer        |   ✅     |       ✅    |✅|
| setTabBadge         |    ✅    |       ✅    |✅| Wix|
| switchToTab         |    ✅    |       ✅   |✅|
| topBar react component        |   ✅     |✅|✅|
|Shared Element Transition|     :x:  |✅| [Contribute](/docs/WorkingLocally.md)|
| splitViewScreen       |     :x:  |    [Contribute](/docs/WorkingLocally.md)      | [Contribute](/docs/WorkingLocally.md)|
