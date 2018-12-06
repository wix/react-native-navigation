# Changelog
___
## v2.2.0
___
### Added
* Component name can be a number as well to support enum component names [#e32d8d2](https://github.com/wix/react-native-navigation/commit/e32d8d2c1a30e4a50b6b1f6ed8eeb99b81b58cde) by [henrikra](https://github.com/henrikra)
* Update lodash to v4.17.x [#77e2faa](https://github.com/wix/react-native-navigation/commit/77e2faa5988c1e7905bc138030422291413213e0)

#### iOS
* Add sideMenu.openGestureMode option [#0a4bf2a](https://github.com/wix/react-native-navigation/commit/0a4bf2ade3b8b98041c8a6057d26a254e193d84d)

### Fixed
* Add props to TopBarButton type [#4373](https://github.com/wix/react-native-navigation/pull/4373) by [gsdatta](https://github.com/gsdatta)
* Add title alignment to OptionsTopBarTitle  [#bd00422](https://github.com/wix/react-native-navigation/commit/bd004225b64c6e4a0bca45103ca0c1ebdbd80ad3) by [minhloi](https://github.com/minhloi)

#### iOS
* iOS popGesture on stack root freezes the app [#4388](https://github.com/wix/react-native-navigation/issues/4388)
* setRoot on main application window - fix setRoot on iPad [a3922f8](https://github.com/wix/react-native-navigation/commit/a3922f84815a80b094416b4ce2bee342f21890a6)
* Fix "Multiple commands produce..." build error on Xcode 10 [#b5d300f](https://github.com/wix/react-native-navigation/commit/b5d300f0506e3e8098de5be0390b58eea32eb085)

#### Android
* Use proper key for bottom tab height [#3b98553](https://github.com/wix/react-native-navigation/commit/3b9855327363149613f371e6eb47727fc8430aab) by [Krizzu](https://github.com/Krizzu)

## 2.1.3
___
### Added
#### iOS
* Add optional componentId param to bindComponent [#0a6e34f](https://github.com/wix/react-native-navigation/commit/0a6e34f2dd16bbec43edd37c93c0f609b6c589f2) by [luigiinred](https://github.com/luigiinred)

### Fixed
* Avoid calling component generators on registerComponent [#708d594](https://github.com/wix/react-native-navigation/commit/708d594877f223e684df749faff2a3e8abdbe9a8)