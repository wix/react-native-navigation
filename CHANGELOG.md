# Changelog
## 2.7.0
### Added
* Adding hideNavBarOnFocusSearchBar option [#83f69d4](https://github.com/wix/react-native-navigation/commit/83f69d4effecfbaaf17af3cebdf8a03b38bfa589) by [sganti564](https://github.com/sganti564)

### Fixed
* Add missing type interface "waitForRender" [#f1ef49e](https://github.com/wix/react-native-navigation/commit/f1ef49e7aeb63ec17b4165cac9d7e9d0cfe6d48e) by [minhtc](https://github.com/minhtc)

### Android
* Fix title height not being set on Android [#09c8c37](https://github.com/wix/react-native-navigation/commit/09c8c37e644fa0af2f00a7ec0536d814cddc36fd) by [davrosull](https://github.com/davrosull)
* Support calling mergeOptions on ExternalComponents [#b1e1ec8](https://github.com/wix/react-native-navigation/commit/b1e1ec84ae5f41693e69da17f7427b59e336fc6a) by [guyca](https://github.com/guyca)

## 2.6.0
### Added
#### iOS
* Support iOS system item icons for top bar [#7a26ea9](https://github.com/wix/react-native-navigation/commit/7a26ea956cfce65035ec902ef3f403f178b69317) by [BerndSchrooten](https://github.com/BerndSchrooten)

### Fixed
* Road to noImplicitAny part 6 (FINAL part) [#08f8581](https://github.com/wix/react-native-navigation/commit/08f8581b3fbf95967a9cc95de2809316065ee275) by [henrikra](https://github.com/henrikra)

#### Android
* Fix ScrollView not scrollable in Overlay [#d3ab1ac](https://github.com/wix/react-native-navigation/commit/d3ab1ac526f5829fe74989144130a13d83795ad8) by [guyca](https://github.com/guyca)

#### iOS
* Fixed settings backButton color dynamically [#8434938](https://github.com/wix/react-native-navigation/commit/84349384958ee9f0d03d24c6ef087cc5b7661d4b) by [masarusanjp](https://github.com/masarusanjp)

## 2.5.2
### Fixed
#### Android
* Revert "Set elevation 0 when creating TopBar" [#135c6eb](https://github.com/wix/react-native-navigation/commit/135c6eb7b240d81e3781e564f021883191736504) by [guyca](https://github.com/guyca)
* Only set elevation values from Options [#487c1da](https://github.com/wix/react-native-navigation/commit/487c1da9dc5277d1ad0e7ca0e410b1c4b5dbc61e) by [guyca](https://github.com/guyca)


## 2.5.1
### Fixed
#### Android
* Set elevation 0 when creating TopBar [#05dacbd](https://github.com/wix/react-native-navigation/commit/05dacbd0729f4ebf0074bd21c50f3bf813ad7fab) by [guyca](https://github.com/guyca)

## 2.5.0
### Fixed
* Road to implicit any part 5 [#ee6dc78](https://github.com/wix/react-native-navigation/commit/ee6dc788023ca78a51834206f9823ca85abd273e) by [henrikra](https://github.com/henrikra)
* Road to implicitAny part 4 [#02985c5](https://github.com/wix/react-native-navigation/commit/02985c507a61c5534f63f134c3f5fecbf6218908) by [henrikra](https://github.com/henrikra)
* Fixed the type mismatch for modalPresentationStyle [#9ef60e9](https://github.com/wix/react-native-navigation/commit/9ef60e9bd9f940c47b7efd05ca104b5404a66d3b) by [masarusanjp](https://github.com/masarusanjp)

#### Android
* Render first tab first [#e5a2efb](https://github.com/wix/react-native-navigation/commit/e5a2efb0d9237cae82fbadb92c3a86d0f01c3b5f) by [guyca](https://github.com/guyca)
* Retrieve BuildConfig.DEBUG from Application in ImageLoader [#b422dd0](https://github.com/wix/react-native-navigation/commit/b422dd0761183edc5f6e5006ba5d5e9b06b9561b) by [guyca](https://github.com/guyca)

#### iOS
* Fix sideMenu intuitive side width [#07cc9d3](https://github.com/wix/react-native-navigation/commit/07cc9d3f6212c9bad59767e0a12ae9243de126f7) by [yogevbd](https://github.com/yogevbd)

## 2.4.0
### Added
#### Android
* Add fab.iconColor option to tint fab icon [#13de5ca](https://github.com/wix/react-native-navigation/commit/13de5cab70834ca5d38f02c512346753dec6c5ed) by [guyca](https://github.com/guyca)

### Fixed
* Refactor options processor [#ee04610](https://github.com/wix/react-native-navigation/commit/ee04610f6a9c9117f9ae8c17ae6d9ce9ca132883) by [henrikra](https://github.com/henrikra)

#### Android
* Fix closing sideMenu when pushing a screen [#dc739de](https://github.com/wix/react-native-navigation/commit/dc739dee337b91c825992e3a77cdcf0262fee162) by [guyca](https://github.com/guyca)
* Orientation.hasValue returns false for default orientation [#43ae659](https://github.com/wix/react-native-navigation/commit/43ae659097f8b6d2cf8897703034829172573fb7) by [guyca](https://github.com/guyca)
* Measure TopBar buttons using using MeasureSpec.UNSPECIFIED [#dd93c51](https://github.com/wix/react-native-navigation/commit/dd93c5147aaac16c852e4795f39abc455f77c90b) by [guyca](https://github.com/guyca)

## v2.3.0
### Added
#### Android
* Add `layout.componentBackgroundColor` option - This option is used to set background color only for component layouts. [#cb48065](https://github.com/wix/react-native-navigation/commit/cb48065aaffa0449f1cd57b27bd3de6bb5a0eac8) by [guyca](https://github.com/guyca)

### Fixed
* SetStackRoot now accepts an array of children which will replace the current children. [#2365e02](https://github.com/wix/react-native-navigation/commit/2365e0211b51a2353949c22a836340eb32cd8cc0) by [guyca](https://github.com/guyca)

#### Android
* Avoid unnecessary BottomTabs view creation. [#b84a3e5](https://github.com/wix/react-native-navigation/commit/b84a3e5fadcbef93a8ef683550743dc84ac8a2fa) by [guyca](https://github.com/guyca)

## v2.2.5
### Added
* Add typed interface to constants [#a71e731](https://github.com/wix/react-native-navigation/commit/a71e7311e270d2feb793c7c61b8e637afe98591e) by [pie6k](https://github.com/pie6k)
* Remove some implicit anys and refactor tests [#c27fa5c](https://github.com/wix/react-native-navigation/commit/c27fa5c97a163b6578058fb3edad8753934b0ada) by [henrikra](https://github.com/henrikra)
* Improve support for Context API and other Provider based apis [#9d36521](https://github.com/wix/react-native-navigation/commit/9d365216d968cb441d02ede36cc21608e091dfed) by [guyca](https://github.com/guyca)

### Fixed
#### iOS
* Fix setting bottomTabs.currentTabIndex on bottomTabs init [#631e7db](https://github.com/wix/react-native-navigation/commit/631e7dbd555ab171679b021207091ae5d9f2f882) by [yogevbd](https://github.com/yogevbd)

## v2.2.2 - v2.2.4
Skipped versions due to CI maintenance

## v2.2.1
### Fixed
#### iOS
* Fix title.font when subtitle supplied - Font wasn't applied on title, when subtitel was provided. [#14a5b74](https://github.com/wix/react-native-navigation/commit/14a5b748fa461a9c4bd50ca0148a0e13a8ae6fba) by [yogevbd](https://github.com/yogevbd)
* Fix invisible modals edge case. When an Overlay was displayed before setRoot was called, Consecutive Modals and Overlays were attached to the wrong window. [#b40f8ed](https://github.com/wix/react-native-navigation/commit/b40f8eda6eea09c465b9cf0e29269fef6238dae0) by [yogevbd](https://github.com/yogevbd)

## v2.2.0

### Added
* Component name can be a number as well to support enum component names [#e32d8d2](https://github.com/wix/react-native-navigation/commit/e32d8d2c1a30e4a50b6b1f6ed8eeb99b81b58cde) by [henrikra](https://github.com/henrikra)
* Update lodash to v4.17.x [#77e2faa](https://github.com/wix/react-native-navigation/commit/77e2faa5988c1e7905bc138030422291413213e0) by [guyca](https://github.com/guyca)

#### iOS
* Add sideMenu.openGestureMode option [#0a4bf2a](https://github.com/wix/react-native-navigation/commit/0a4bf2ade3b8b98041c8a6057d26a254e193d84d) by [yogevbd](https://github.com/yogevbd)

### Fixed
* Add props to TopBarButton type [#4373](https://github.com/wix/react-native-navigation/pull/4373) by [gsdatta](https://github.com/gsdatta)
* Add title alignment to OptionsTopBarTitle  [#bd00422](https://github.com/wix/react-native-navigation/commit/bd004225b64c6e4a0bca45103ca0c1ebdbd80ad3) by [minhloi](https://github.com/minhloi)

#### iOS
* popGesture on stack root freezes the app [#4388](https://github.com/wix/react-native-navigation/issues/4388) by [yogevbd](https://github.com/yogevbd)
* setRoot on main application window - fix setRoot on iPad [a3922f8](https://github.com/wix/react-native-navigation/commit/a3922f84815a80b094416b4ce2bee342f21890a6) by [yogevbd](https://github.com/yogevbd)
* Fix "Multiple commands produce..." build error on Xcode 10 [#b5d300f](https://github.com/wix/react-native-navigation/commit/b5d300f0506e3e8098de5be0390b58eea32eb085) by [yogevbd](https://github.com/yogevbd)

#### Android
* Use proper key for bottom tab height [#3b98553](https://github.com/wix/react-native-navigation/commit/3b9855327363149613f371e6eb47727fc8430aab) by [Krizzu](https://github.com/Krizzu)

## 2.1.3

### Added
#### iOS
* Add optional componentId param to bindComponent [#0a6e34f](https://github.com/wix/react-native-navigation/commit/0a6e34f2dd16bbec43edd37c93c0f609b6c589f2) by [luigiinred](https://github.com/luigiinred)

### Fixed
* Avoid calling component generators on registerComponent [#708d594](https://github.com/wix/react-native-navigation/commit/708d594877f223e684df749faff2a3e8abdbe9a8) by [yogevbd](https://github.com/yogevbd)
