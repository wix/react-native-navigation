# Changelog
# 3.3.0
## Added
* **[experimental]** Support updating component props with Navigation.mergeOptions [#291f161](https://github.com/wix/react-native-navigation/commit/291f16177d2f67a474d3a980a503a85d0acf2b2a) by [justtal](https://github.com/justtal)
### Android
* Add support for getLaunchArgs [#16646e7](https://github.com/wix/react-native-navigation/commit/16646e7c88d78f1ddd7fb6ae434ef968ac051f06) by [swabbass](https://github.com/swabbass)
* Support bottomTab.selectedIcon [#45e8389](https://github.com/wix/react-native-navigation/commit/45e8389b2b7d282878a80c49b146ddeb4ec2cd89) by [guyca](https://github.com/guyca)
### iOS
* Added fontWeight option for iOS 13 [#6ab2345](https://github.com/wix/react-native-navigation/commit/6ab2345ad6e9ddd26ac1275537ec2791fa50c7c2) by [yogevbd](https://github.com/yogevbd)

## Fixed
### Android
* Merge options with ParentViewControllers [#0dd3315](https://github.com/wix/react-native-navigation/commit/0dd331590770c33d067bfeae596aae7d4ff992ea) by [guyca](https://github.com/guyca)
### iOS
* Prefer new imageWithTintColor API when tinting an UIImage [#5d751f6](https://github.com/wix/react-native-navigation/commit/5d751f643ad80a67abccc51d75f1127e2b65824a) by [danilobuerger](https://github.com/danilobuerger)
* Fixed disappearing StatusBar when displaying native ViewControllers [#58c76e1](https://github.com/wix/react-native-navigation/commit/58c76e1ec218741b461bca30e8126e952b87a180) by [yogevbd](https://github.com/yogevbd)
* Fixed title layout issues on iOS 13 [#898e187](https://github.com/wix/react-native-navigation/commit/898e187d4cbea76b93709c95ad89e984e660b904), [#a3f176d](https://github.com/wix/react-native-navigation/commit/a3f176d56e94e5a5de0be079de9f63b180dc6f5a), [#094b9a7](https://github.com/wix/react-native-navigation/commit/094b9a7ef153d62ea9c195e342cafca4892c1428) by [yogevbd](https://github.com/yogevbd)
* Fixed leaking pageSheet modals on iOS 13 [#2b4d897](https://github.com/wix/react-native-navigation/commit/2b4d897f19684e2c04c3050c3882f1558cb1efed) by [yogevbd](https://github.com/yogevbd)

# 3.2.0
## Added
### Android
* Add animation support for setStackRoot [#d0a17fa](https://github.com/wix/react-native-navigation/commit/d0a17fabf4f5360b0f54797867a7a49960a1937d) by [Jazqa](https://github.com/Jazqa)

## Fixed
* Don't cache values for constants() call [#a99e138](https://github.com/wix/react-native-navigation/commit/a99e138839abc8ab84b67489ef9a83a6874778da) by [ItsNoHax](https://github.com/ItsNoHax)

### iOS
* Fix applying merged backButton options [#aef5b2e](https://github.com/wix/react-native-navigation/commit/aef5b2e3bd60acb33847c0ab3b66b8fc51fef703) by [guyca](https://github.com/guyca)
* Remove duplicate setDefaultOptions in UIViewController categories [#0d31e30](https://github.com/wix/react-native-navigation/commit/0d31e30800dc3e7314cfbf1dcf6b645614b781f4) by [danilobuerger](https://github.com/danilobuerger)
* Don't consume SideMenu enabled option after applying it in mergeOptions [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* supportedInterfaceOrientations didn't take default orientation value into account [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* SideMenu always returned the centre ViewController as the current child and didn't take open SideMenu into account  [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* Stop recursive double setting of default options [#d5c92b1](https://github.com/wix/react-native-navigation/commit/d5c92b1609d5fc1a75c51065791e5aad2a80b654) by [danilobuerger](https://github.com/danilobuerger)
* Immediately unmount buttons removed by mergeOptions, instead of unmounting them when screen is unmounted [#65dde34](https://github.com/wix/react-native-navigation/commit/65dde342fb087bd122bc19de308cbf283485aac7) by [yogevbd](https://github.com/yogevbd)

## Android
* Catch rare RuntimeException in setRoot when waitForRender = true [#b048581](https://github.com/wix/react-native-navigation/commit/b04858190e1fc2a1fe22f67879a6c7ca3769c297) by [rawrmaan](https://github.com/rawrmaan)
* Fix SideMenu enabled property issues [#92fcf70](https://github.com/wix/react-native-navigation/commit/92fcf70773a3614c48e3ac844e81cb16d1ff8d2a) by [Royce](https://github.com/Royce)


# 3.1.2
## Fixed
* Add @babel/core to devDependencies [#fdec91d](https://github.com/wix/react-native-navigation/commit/fdec91dc39364aaa23a48dfc4d1d44e73a6f2e21) by [ItsNoHax](https://github.com/ItsNoHax)

### iOS
* Fix replacing react title with text title not working [#b434b4f](https://github.com/wix/react-native-navigation/commit/b434b4f82a6f0642af9d11ad8dda6ec8e9c5603a) by [FRizzonelli](https://github.com/FRizzonelli)
* Fix merging TopBar title, buttons and status bar options, broke in 3.1.1 [#5409a62](https://github.com/wix/react-native-navigation/commit/5409a625d9d272c5c0a7d9b10ed0df5ff51c6155) by [guyca](https://github.com/guyca)

# 3.1.1
## Fixed
### Android
* Fix defaultOptions not being applied if called after setRoot [#338b096](https://github.com/wix/react-native-navigation/commit/338b0961f9bee9fa20583efe0f165e3cefa14c92) by [guyca](https://github.com/guyca)
# 3.1.0
## Added
* Support passing null color to StatusBar backgroundColor and bottom tab icon color [#3519837](https://github.com/wix/react-native-navigation/commit/3519837cc2a82cb14ec1849bfc358865e407f556) by [guyca](https://github.com/guyca)

## Fixed
### Android
* Removed the legacy support lib [#8663669](https://github.com/wix/react-native-navigation/commit/8663669ed92f34bedf7bdbdb8a9b1a64be5b8cdf) by [SudoPlz](https://github.com/SudoPlz) 
* Apply layout direction directly on views [#fffd2d2](https://github.com/wix/react-native-navigation/commit/fffd2d23f169d11ddb7c9348e2070c3385844e34) by [guyca](https://github.com/guyca)

# 3.0.0
## Android
* Support RN 0.60
* Migrate to AndroidX
* Improve draw behind StatusBar<br>
  Added `statusBar.translucent` boolean property
* BottomTabs are not pushed upwards when keyboard opens
* Removed SyncUiImplementation
  [SyncUiImplementation](https://github.com/wix/react-native-navigation/blob/master/lib/android/app/src/reactNative57WixFork/java/com/reactnativenavigation/react/SyncUiImplementation.java) was used to overcome a bug in RN's UiImplementation. This workaround was added to RN's `UiImplementation` in RN 0.60 and can be removed from RNN.

  If you're using `SyncUiImplementation` your app will fail to compile after upgrading to v3. Simply remove the following code from your `MainApplication.java`
  ```diff
  - import com.facebook.react.uimanager.UIImplementationProvider;
  - import com.reactnativenavigation.react.SyncUiImplementation;


  - @override
  - protected UIImplementationProvider getUIImplementationProvider() {
  -     return new SyncUiImplementation.Provider();
  - }
  ```
* BottomTab badge and dot indicator are not animated by default.
  * The following option will show a badge with animation
    ```js
    bottomTab: {
      badge: 'new,
      animateBadge: true
    }
    ```

  * The following option will show a dot indicator with animation
    ```js
    bottomTab: {
      dotIndicator: {
        visible: true,
        animate: true
      }
    }
    ```
* Stack, BottomTabs and SideMenu are drawn behind StatusBar.<br>
While parent controllers are drawn behind the StatusBar, their background isn't.
This means that when transitioning from a destinations drawn under the StatusBar to a destination drawn behind it, the application's default background color will be visible behind the StatusBar.
If you application's theme is dark, you might want to change the `windowBackground` property to mitigate this:
Add the following to your application's `style.xml`
  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
          <item name="android:windowBackground">@color/backgroundColor</item>
      </style>

      <!--This is your application's default background color.
      It will be visible when the app is first opened (while the splash layout is visible)
      and when transitioning between a destination a screen drawn under the StatusBar to
      a destination drawn behind it-->
      <item name="backgroundColor" type="color">#f00</item>
  </resources>
  ```
# 2.29.0
## Added
* Introduce `Navigation.updateProps` command [#0eb0570](https://github.com/wix/react-native-navigation/commit/0eb0570840a26d4b848d7c763060a2b8faf1dc80) by [guyca](https://github.com/guyca)
## Fixed
### iOS
* Fix compilation error on Xcode 10.x [#99ddcd8](https://github.com/wix/react-native-navigation/commit/99ddcd864005ce768ca7c0b34d2ecfa8246dc568) and [#83f03cd](https://github.com/wix/react-native-navigation/commit/83f03cd8f945152cda93c664d69bf36047989571) by [yogevbd](https://github.com/yogevbd)
### Android
* Don't merge null bottomTab.selectedIconColor and bottomTab.iconColor [#c48ed74](https://github.com/wix/react-native-navigation/commit/c48ed747a163a2917820e62ab40d5047389f5fcb) by [guyca](https://github.com/guyca)
# 2.28.0
## Added
* Support updating component props with Navigation.mergeOptions [#291f161](https://github.com/wix/react-native-navigation/commit/291f16177d2f67a474d3a980a503a85d0acf2b2a) by [justtal](https://github.com/justtal)
### Android
* Support bottomTab.selectedIcon [#45e8389](https://github.com/wix/react-native-navigation/commit/45e8389b2b7d282878a80c49b146ddeb4ec2cd89) by [guyca](https://github.com/guyca)
# 2.27.7
## Added
### iOS
* Font weight option support on iOS [#f283e15](https://github.com/wix/react-native-navigation/commit/f283e155948c0ae190c1dde0fb3d78d5ac129af0) by [yogevbd](https://github.com/yogevbd)

# 2.27.6
## Fixed
### iOS
* Fix status bar disappear when presenting native camera screen on iOS [#6cfde5e](https://github.com/wix/react-native-navigation/commit/6cfde5e24c95506b6d31b2f40164fa3f196b72a6) by [yogevbd](https://github.com/yogevbd)

# 2.27.5
## Fixed
### iOS
* Stop recursive double setting of default options  [#3da2ca8](https://github.com/wix/react-native-navigation/commit/3da2ca8afc7597b46cad500828dffc0102c034a6) by [danilobuerger](https://github.com/danilobuerger)
* Fix infinite loop while trying to remove react buttons [#88fd1f1](https://github.com/wix/react-native-navigation/commit/88fd1f15d0bc22d8c53c7e518eb0bb178e15ea6c) by [guyca](https://github.com/guyca)

# 2.27.4
## Fixed
### iOS
* Immediately unmount buttons removed by mergeOptions, instead of unmounting them when screen is unmounted [#65dde34](https://github.com/wix/react-native-navigation/commit/65dde342fb087bd122bc19de308cbf283485aac7) by [yogevbd](https://github.com/yogevbd)
* Don't consume SideMenu enabled option after applying it in mergeOptions [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* supportedInterfaceOrientations didn't take default orientation value into account [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* SideMenu always returned the centre ViewController as the current child and didn't take open SideMenu into account  [#9faf458](https://github.com/wix/react-native-navigation/commit/9faf458cb451829e86809d9162728eed17a7f56c) by [guyca](https://github.com/guyca)
* Remove duplicate setDefaultOptions in UIViewController categories [#452c4e6](https://github.com/wix/react-native-navigation/commit/452c4e692fe700600447f19282bd42b07dcc9bb4) by [danilobuerger](https://github.com/danilobuerger)

# 2.27.3
## Fixed
### iOS
* Fix merging back button [#1800e70](https://github.com/wix/react-native-navigation/commit/1800e708a8483bc4e56873e826f2d72f02d659b9) by [guyca](https://github.com/guyca)

# 2.27.2
## Fixed
### iOS
* Fix TopBar, title, buttons and StatusBar which broke in the previous release [#8044b2d](https://github.com/wix/react-native-navigation/commit/8044b2d12cfd2b937bfbe846d98f9034a88aa254) by [guyca](https://github.com/guyca)

# 2.27.1
## Fixed
### iOS
* Fix defaultOptions not being applied if called after setRoot [#338b096](https://github.com/wix/react-native-navigation/commit/338b0961f9bee9fa20583efe0f165e3cefa14c92) by [guyca](https://github.com/guyca)

# 2.27.0
## Added
* Support passing null color to StatusBar backgroundColor and bottom tab icon color [#3519837](https://github.com/wix/react-native-navigation/commit/3519837cc2a82cb14ec1849bfc358865e407f556) by [guyca](https://github.com/guyca)

## Fixed
### Android
* Apply layout direction directly on views [#fffd2d2](https://github.com/wix/react-native-navigation/commit/fffd2d23f169d11ddb7c9348e2070c3385844e34) by [guyca](https://github.com/guyca)

# 2.26.1

## Fixed
### iOS
* Fix transparent topBar background color transition [#147cf4c](https://github.com/wix/react-native-navigation/commit/147cf4cafca83f0903b68a47d3812009a11d3018) by [yogevbd](https://github.com/yogevbd)

# 2.26.0

## Fixed
### Android
* Apply TopBar buttons only if they are different than current buttons [#f15e9b3](https://github.com/wix/react-native-navigation/commit/f15e9b3a2a8c661d10c93d9c2f9a7155de979240) by [guyca](https://github.com/guyca)

# 2.25.0

## Fixed
### Android
* Ensure appLaunched event is emitted only when app is resumed [#21584fd](https://github.com/wix/react-native-navigation/commit/21584fd4a525d7a8085caf18d624668cc5865d93) by [guyca](https://github.com/guyca)

# 2.24.0
## Added
* Add return type to Navigation.constants() [#66ab3cd](https://github.com/wix/react-native-navigation/commit/66ab3cd695be68e7d7536fde4c61bad4e7067281) by [danilobuerger](https://github.com/danilobuerger)

## Fixed
### Android
* Destroy modals on setRoot [#f06787d](https://github.com/wix/react-native-navigation/commit/f06787dec9997cd51c52bef4d319ef32bca64f48) by [guyca](https://github.com/guyca)

### iOS
* Fixed react component button flicker [#77ee4df](https://github.com/wix/react-native-navigation/commit/77ee4df6c9e3824c418fbba6ed9fe82711b5d520) by [yogevbd](https://github.com/yogevbd)
* Fix bug that reverts navbar title size to 17 [#e677f97](https://github.com/wix/react-native-navigation/commit/e677f97cb775e43af1352caf38c03dd352851b47) by [dcvz](https://github.com/dcvz)

# 2.23.0
## Added
* BottomTab dot indicator [#f425155](https://github.com/wix/react-native-navigation/commit/f42515524d88a5f4f12a35684ab288dad6af1b1d) by [guyca](https://github.com/guyca)

## Fixed
### Android
* Fix margin top topbar when statusbar drawBehind [#234c59c](https://github.com/wix/react-native-navigation/commit/234c59ce7b35acc6ba53bc7a43f4c688bfb6e11e) by [isuhar](https://github.com/isuhar)

### iOS
* Fix SideMenu size after screen rotation [#a591fe4](https://github.com/wix/react-native-navigation/commit/a591fe4476ca152a336022e2570431b677f60225) by [lionerez1](https://github.com/lionerez1)
* Set nil as default UITabBarItem.title value [#76d832b](https://github.com/wix/react-native-navigation/commit/76d832b683b3daca530340acdcfbc30acf36b568) by [danilobuerger](https://github.com/danilobuerger)

# 2.22.3
## Fixed
### iOS
* Fix Constants.topBarHeight being zero if root ViewController isn't a NavigationViewController [#f19e523](https://github.com/wix/react-native-navigation/commit/f19e523afcc013681a601d2c9d4b0340f5459b59) by [guyca](https://github.com/guyca)

# 2.22.2
## Added
### iOS
* Support changing javascript bundle location in runtime [#8959d68](https://github.com/wix/react-native-navigation/commit/8959d680d8efc8ca9f11d5ae7d78134c2f9a7959) by [yogevbd](https://github.com/yogevbd)

## Fixed
### iOS
* Fix back button initialization on setStackRoot [#c0ad194](https://github.com/wix/react-native-navigation/commit/c0ad1945a3dbbc29192abdcc1c598516b391a10f) by [yogevbd](https://github.com/yogevbd)

# 2.22.1
## Fixed
* Add width and height attributes to SideMenuSide type [#73d621d](https://github.com/wix/react-native-navigation/commit/73d621d48d24ba270dec42f82789bfbc911262cd) by [ball-hayden](https://github.com/ball-hayden)

### iOS
* Fixed crash when calling Navigation.constants() when root view isn't BottomTabs [#663b1c3](https://github.com/wix/react-native-navigation/commit/663b1c3f60cb474ebd9cdbda824121add1c34801) by [daveyjones](https://github.com/daveyjones)
* Fixed crash when setting react component as left button [#29829ae](https://github.com/wix/react-native-navigation/commit/29829ae2aa972fa7df20cd50e0d43efa8991fbf2) by [MarianPalkus](https://github.com/MarianPalkus)

### Android
* Remove android * imports for support [#35a19b5](https://github.com/wix/react-native-navigation/commit/35a19b5f687b89dc414e65184995ebc69f847704) by [heroic](https://github.com/heroic)
* Add back button to last child in setStackRoot [#898cf7a](https://github.com/wix/react-native-navigation/commit/898cf7ae2703656459f28e21123f3b0f8a40b22e) by [guyca](https://github.com/guyca)

# 2.21.1
## Fixed
### iOS
* Fix `setStackRoot` options resolving [#8ced964](https://github.com/wix/react-native-navigation/commit/8ced96443f8c279821719e842f7580a988aeb47c) by [yogevbd](https://github.com/yogevbd)

# 2.21.0
## Added
* Title topMargin option [#069cb85](https://github.com/wix/react-native-navigation/commit/069cb85132dcc441c27c56e0f25e475c1d44eef2) by [guyca](https://github.com/guyca)

## Fixed
* Safer check around component listener trigger [#51d1b66](https://github.com/wix/react-native-navigation/commit/51d1b6676027c38d439dff03d23660ac8d617a5a) by [dozoisch](https://github.com/dozoisch)
### Android
* Emit SideMenu visibility events [#7ee9c12](https://github.com/wix/react-native-navigation/commit/7ee9c12d53dffe3461a3c4f6721619f9ceb5eb91) by [guyca](https://github.com/guyca)
* Fix setStackRoot crash when called with the same id  [#3c08b1c](https://github.com/wix/react-native-navigation/commit/3c08b1c99559a3485fb8661ca98ce256db59adb8) by [guyca](https://github.com/guyca)
* Fix crashes related to race conditions around ViewController.destroy  [#f2e46ea](https://github.com/wix/react-native-navigation/commit/f2e46ea4e7f6a32164ce0a0b1e1b697544177f33) by [guyca](https://github.com/guyca)

# 2.20.2
## Fixed
### iOS
* Fixed missing TopBar React component background [#d2d5d0f](https://github.com/wix/react-native-navigation/commit/d2d5d0fe7951e2c0c1e8d9fba247de392793a73b) by [yogevbd](https://github.com/yogevbd)

# 2.20.1
## Fixed
* Include PassProps in ComponentDidAppearListener [#c226a7d](https://github.com/wix/react-native-navigation/commit/c226a7d55193c9c630e102dce35bc02243222921) by [yogevbd](https://github.com/yogevbd)

# 2.20.0
## Fixed
* Include PassProps in layout parameter of CommandListener [#d3d01c2](https://github.com/wix/react-native-navigation/commit/d3d01c221f6c63ac36d8ef13a66e03fab980cf9f) by [yogevbd](https://github.com/yogevbd)
### Android
* Fix TopBar background React component flicker when pushing screens [#99032e0](https://github.com/wix/react-native-navigation/commit/99032e060d2e0a429d3da2775884f624e8cd5fd5) by [FRizzonelli](https://github.com/FRizzonelli)

# 2.19.1
## Fixed
### Android
* Fix missing absolute positioned views [#ecadcb0](https://github.com/wix/react-native-navigation/commit/ecadcb0f352d5c96944966deb09a7c2d570ccb2d) by [guyca](https://github.com/guyca)

# 2.19.0
## Added
* Add passProps to component typings file [#42fd86d](https://github.com/wix/react-native-navigation/commit/42fd86d654feac83177c272b19276e71c08ef75a) by [Andarius](https://github.com/Andarius)
* Add missing topBar options [#5566ffd](https://github.com/wix/react-native-navigation/commit/5566ffd47c65f7bfc608f3a0f0b19814039b541e) by [retyui](https://github.com/retyui)

## Fixed
### iOS
* Fixes broken modals animations [#42e26d7](https://github.com/wix/react-native-navigation/commit/42e26d77b8d231debe0489dbe874fc06d9a97589) by [yogevbd](https://github.com/yogevbd)

### Android
* Fixed buggy currentTabIndex when calling setRoot multiple times [#cd182f4](https://github.com/wix/react-native-navigation/commit/cd182f4693a6a4bd943eddf9a15706d943c88d4e) by [guyca](https://github.com/guyca)

# 2.18.5
## Fixed
### iOS
* Handle simultaneous recognizers, Fixes a crash when tapping on the screen with other gesture recognizers active [#a5b9f58](https://github.com/wix/react-native-navigation/commit/a5b9f58affd132bba03f961a255d05e41272bae9) by [jordoh](https://github.com/jordoh)

# 2.18.4
## Fixed
### iOS
* Fix topBar.title.component fill alignment [#9f439da](https://github.com/wix/react-native-navigation/commit/9f439dabd8fabc151bb96fbb04fa34bfe2b469d8) by [yogevbd](https://github.com/yogevbd)

# 2.18.3
## Fixed
### iOS
* Fix topBar.titleView calculation on props change [#f3b1d34](https://github.com/wix/react-native-navigation/commit/f3b1d34ea61341f08ab864b2134933ec9764b127) by [yogevbd](https://github.com/yogevbd)

# 2.18.2
## Fixed
### iOS
* Fix TopBar react view measurement issue [#1993b93](https://github.com/wix/react-native-navigation/commit/1993b93c2ec388bce8923b6d70edf11fc5499976) by [yogevbd](https://github.com/yogevbd)

# 2.18.1
## Fixed
### iOS
* Fix bottomTabs’s initial currentTabIndex [#0e888fb](https://github.com/wix/react-native-navigation/commit/0e888fb65a70343949386f0d6f9f59b03e7b93b7) by [yogevbd](https://github.com/yogevbd)

# 2.18.0
## Fixed
* Add missing topMargin type to OptionsTopBar [#9d7d7f4](https://github.com/wix/react-native-navigation/commit/9d7d7f4600ce4994ed680c123f59eb198130a32c) by [ceyhuno](https://github.com/ceyhuno)
### iOS
* Fix bottomTabs’s animate option [#9836730](https://github.com/wix/react-native-navigation/commit/9836730570f8a84c389ddf59728176fa6c828222) by [wsliaw](https://github.com/wsliaw)
* Stop keeping hard reference to ViewControllers, remove RNNStore [#275304c](https://github.com/wix/react-native-navigation/commit/275304c88e8f35bc053aec2328a94a38a6fce088) by [yogevbd](https://github.com/yogevbd)
* Return componentId on showModal [#81dc07d](https://github.com/wix/react-native-navigation/commit/81dc07d5b899ed2df1751562afc5a9703fbe0ab9) by [yogevbd](https://github.com/yogevbd)
* Apply bottomTabs options after children added [#2bddff3](https://github.com/wix/react-native-navigation/commit/2bddff390d939f21d1387645077f57cb81399970) by [yogevbd](https://github.com/yogevbd)
* Fix sideMenu orientation options [#0e1a35d](https://github.com/wix/react-native-navigation/commit/0e1a35d467fc22eab3742fd92cbf6062a645b535) by [yogevbd](https://github.com/yogevbd)
* Fix broken TextInput focus in Overlay [#e9ca247](https://github.com/wix/react-native-navigation/commit/e9ca247a524e474daf3ccf56989289ce679fc063) by [yogevbd](https://github.com/yogevbd)
* Fix TopBar react view measurement issue [#be00c4c](https://github.com/wix/react-native-navigation/commit/be00c4c36d9eee1da39f18a37240c1980cd22951) by [yogevbd](https://github.com/yogevbd)

## 2.17.0
* Migrate to Detox 12 [#9428233](https://github.com/wix/react-native-navigation/commit/942823390a8d628b0e94a8d1c35301ece0bb0971) by [guyca](https://github.com/guyca)

### Fixed
#### iOS
* Fix Height of SideMenu when device orientation changes [#68c62f3](https://github.com/wix/react-native-navigation/commit/68c62f33b586d1d9dfd7839ea66342861dacf534) by [mohammadalijf](https://github.com/mohammadalijf)
* adding and removing components from registry manually by presenter [#ac60d2f](https://github.com/wix/react-native-navigation/commit/ac60d2fe6ad036528c31954a2997109b06f0c947) by [yogevbd](https://github.com/yogevbd)
* Attach and detach viewControllers explicitly in store [#2830059](https://github.com/wix/react-native-navigation/commit/28300597ede5de1f08d7b32ba4a9313ffdf4aac1) by [yogevbd](https://github.com/yogevbd)
* Fix StatusBarOptions duplication in xcodeproj [#ab9fd65](https://github.com/wix/react-native-navigation/commit/ab9fd658c2abde508a42374baad983ba2a3c143d) by [tyronet-sportsbet](https://github.com/tyronet-sportsbet)

#### Android
* Match android dependencies to app configuration [#e954a41](https://github.com/wix/react-native-navigation/commit/e954a41e64f203b17c70a54224bcac2190c689be) by [alpha0010](https://github.com/alpha0010)
* Do not setTag to bottomTabs if testId is null [#9126ced](https://github.com/wix/react-native-navigation/commit/9126ced3bd1e0d82d966f6b45f529ac876ecc9d8) by [EliSadaka](https://github.com/EliSadaka)
* Clear sideMenu's visible options after applying [#283f226](https://github.com/wix/react-native-navigation/commit/283f226f55be633da5022692c76d90a391ec3fd8) by [ItsNoHax](https://github.com/ItsNoHax)

## 2.16.0
### Fixed
* Update  app lifecycle docs [#1c740b7](https://github.com/wix/react-native-navigation/commit/1c740b74f25157bcd0b58f88c7da7716deea763b) by [ericketts](https://github.com/ericketts)

#### iOS
* Fix command completion event commandId [#0e29a03](https://github.com/wix/react-native-navigation/commit/0e29a03a40df26755d71c3578ca5ca554096b14c) by [yogevbd](https://github.com/yogevbd) 
* Fix topBar buttons iconsInsets [#e2dcef9](https://github.com/wix/react-native-navigation/commit/e2dcef9d4a4a5efb6021e00a80f3898cc0254343) by [yogevbd](https://github.com/yogevbd) 

## 2.15.0
### Added
* Add `externalComponent` prop to Layout TS declaration [#5ba7ccb](https://github.com/wix/react-native-navigation/commit/5ba7ccb75fd9e3e9ecf0b954d78704930f50d8f6) by [yedidyak](https://github.com/yedidyak)

### Fixed
#### iOS
* Fix prevent retaining button component in componentRegistry [#0186b1a](https://github.com/wix/react-native-navigation/commit/0186b1ac36e919fb6b2a796677db1905b48aec7e) by [yogevbd](https://github.com/yogevbd)
* Fix and refactor animations options [#a98f187](https://github.com/wix/react-native-navigation/commit/a98f18704cc49094cd91859e75089328b4fd7cbc) by [yogevbd](https://github.com/yogevbd)
* Fix display empty custom topBar background over valid custom background [#6cb1e18](https://github.com/wix/react-native-navigation/commit/6cb1e18a883db803a5b193ca86f077d4e281a8e4)
by [RoTTex](https://github.com/RoTTex)

## 2.14.0
### Fixed
* Add direction property to Layout TS declaration [#025c5e8](https://github.com/wix/react-native-navigation/commit/025c5e8dd6a0eec75f3a27a49e52af1d252b5351) by [mohammadalijf](https://github.com/mohammadalijf)
* Add enabled property to StackAnimation TS declaration [#996f2b1](https://github.com/wix/react-native-navigation/commit/996f2b11ff4dc98d579a7c7a0ff7ab6fa8577916) by [larryranches](https://github.com/larryranches)
* [BREAKING] Rename animation options class name [#4517d22](https://github.com/wix/react-native-navigation/commit/4517d22b38a1450b8e426f8de03c4b9fdc1213e8) by [guyca](https://github.com/guyca)

#### Android
* Fixed title centering issues [#1899601](https://github.com/wix/react-native-navigation/commit/1899601fb99093f804f8b97773a6470a7587017c) by [hadimostafapour](https://github.com/hadimostafapour)
* Cancel in-flight push animation on pop [#47b7d2c](https://github.com/wix/react-native-navigation/commit/47b7d2c7c54af861e99b922aa258489d86c9c0b2) by [guyca](https://github.com/guyca)
* Fix crash when calling setStackRoot multiple times in quick succession  [#fdee254](https://github.com/wix/react-native-navigation/commit/fdee25422f6568be4ba5507b26f470b511decc95) by [guyca](https://github.com/guyca)
* External component lifecycle events [#602c669](https://github.com/wix/react-native-navigation/commit/602c669b02b31c45b040965e27c327c2bd0fde61) by [guyca](https://github.com/guyca)

#### iOS
* Fix iOS pop gesture when topBar is hidden [#81d8b69](https://github.com/wix/react-native-navigation/commit/81d8b69d61934e4702d59d531ce84294c9b92c55) by [rawrmaan](https://github.com/rawrmaan)

## 2.13.1
### Fixed
* Moved `react-native-uilib` to dev dependencies [#2c514d9](https://github.com/wix/react-native-navigation/commit/2c514d931f74cf97807cb565672ddce50644349f) by [guyca](https://github.com/guyca)

## 2.13.0
### Added
* Add `enabled?` property to interface OptionsAnimationProperties [#6065bd1](https://github.com/wix/react-native-navigation/commit/6065bd1345ef5087d9dea92c9c332ba42619411f) by [taichi-jp](https://github.com/taichi-jp)

### Fixed
#### iOS
* Fixes initial screen size [#e036743](https://github.com/wix/react-native-navigation/commit/e03674381315f92292add444055aeaba791076d3) by [yogevbd](https://github.com/yogevbd)
* Fix top bar buttons size on iOS 10 [#8282d93](https://github.com/wix/react-native-navigation/commit/8282d934f70d512548d6d4ceae25e9798d591141) by [yogevbd](https://github.com/yogevbd)
* Prevent keyboard from hiding when overlay is shown [#aba58b6](https://github.com/wix/react-native-navigation/commit/aba58b6c5aa4b39a0fb76fa2f8ebbd28dc80952e) by [tomhicks](https://github.com/tomhicks)

#### Android
* Fix loading local images [#c82bc57](https://github.com/wix/react-native-navigation/commit/c82bc57d58227f8ecb54e7cf351da46b38b4f8f9) by [guyca](https://github.com/guyca)
* Update side menu options on open / close callback  [#43f05ee](https://github.com/wix/react-native-navigation/commit/43f05ee01574c18d216acfb510be4b5e38165e4d) by [gosuperninja](https://github.com/gosuperninja)
* Fix overflow visible for react button components in TopBar [#54ff1cd](https://github.com/wix/react-native-navigation/commit/54ff1cd049b7a418f7fd2658f569d06853bcea6c) by [guyca](https://github.com/guyca)

## 2.12.0
### Added
* Add waitForRender to root animation options [#298ec43](https://github.com/wix/react-native-navigation/commit/298ec43f27eb9a031c7168675c40ab5be47396ec) by [guyca](https://github.com/guyca)

#### Android
* RTL support [#d09d010](https://github.com/wix/react-native-navigation/commit/d09d0108d1530cf10e24c46efb6c9d9962807ead) by [hadimostafapour](https://github.com/hadimostafapour)

### Fixed
* Stop using lodash in store.js [#8ba9796](https://github.com/wix/react-native-navigation/commit/8ba9796d2d94c5dd58266841c2563bbcd563f635) by [guyca](https://github.com/guyca)

#### iOS
* Use autolayout constraints to set size of custom bar button item [#362606b](https://github.com/wix/react-native-navigation/commit/362606b82ed4de37e05ebf8603739b16adf0e0d2) by [eliperkins](https://github.com/eliperkins)
* Ignore pan gesture when no drawer is enabled [#664ef34](https://github.com/wix/react-native-navigation/commit/664ef343090051049213eb5d56285e0432b4e2d7) by [StasDoskalenko](https://github.com/StasDoskalenko)
* Fix peek and pop preview on iOS [#e7c0d16](https://github.com/wix/react-native-navigation/commit/e7c0d166cb70fa27edf68eae8a00f23257eddf9a) by [yogevbd](https://github.com/yogevbd)
* Fix launch image matching for iPhone XR/XS Max portrait [#dfd894a](https://github.com/wix/react-native-navigation/commit/dfd894ab2f4ab434548a4ef57f0eb176ee17627f) by [oblador](https://github.com/oblador)

## 2.11.0
### Fixed
#### iOS
* Support updating bottomTab options [#2362655](https://github.com/wix/react-native-navigation/commit/23626556cf9ae4c161993b39776098855117d928) by [rsispal](https://github.com/rsispal)

#### Android
* Fix android build for RN 0.58.x [#600a1d1](https://github.com/wix/react-native-navigation/commit/600a1d188b634b7834c1720620336dd5d02bfd80) by [alpha0010](https://github.com/alpha0010)

## 2.10.0
### Added
#### iOS
* Add Icon insets support for topBar buttons [#545e5fe](https://github.com/wix/react-native-navigation/commit/545e5fef5fa570aaa20d95bbb40ed9aed72fc480) by [yogevbd](https://github.com/yogevbd)
* SetRoot wait for render [#5abea28](https://github.com/wix/react-native-navigation/commit/5abea28c53ed34dc822641f30abe2190c08f8185) by [yogevbd](https://github.com/yogevbd)

### Fixed
#### iOS
* Improved RNNSplashScreen status bar styling [#b3b88d1](https://github.com/wix/react-native-navigation/commit/b3b88d15bbb730b96de1fa2c0378d0f3c59b53ab) by [danielgindi](https://github.com/danielgindi)

#### Android
* Null check when parsing strings [#eda4b9c](https://github.com/wix/react-native-navigation/commit/eda4b9ce0a6a9b732241f662012a1e7e5750e193) by [guyca](https://github.com/guyca)
* Fixed modalDismissed event being emitted with wrong id [#aef7745](https://github.com/wix/react-native-navigation/commit/aef7745cd3a8d9cfce69f9553c8c01b9bdf1cc06) by [guyca](https://github.com/guyca)

## 2.9.0
### Fixed
#### iOS
* Improve SplitView and BottomTabs interactions [#954e734](https://github.com/wix/react-native-navigation/commit/954e7348d78d97477927beb0f1ad3f1e37fedf65) by [zzorba](https://github.com/zzorba)
* SplitView options are handled by presenter [#00d5e31](https://github.com/wix/react-native-navigation/commit/00d5e313f8992cac5b7fb2301515d8e35e20c2f8) by [zzorba](https://github.com/zzorba)
* Fixes Large title and noBorder issue which caused color to change to default [#9c48a78](https://github.com/wix/react-native-navigation/commit/9c48a78bc2c01e68e3d9d184f5df637f6b331c53) by [mohammadalijf](https://github.com/mohammadalijf)
* Pull BottomTabs height from correct controller [#8cee745](https://github.com/wix/react-native-navigation/commit/8cee74533489e1877ffea95c1622bd4ab6fb33ff) by [paubins](https://github.com/paubins)

#### Android
* Fix custom back button missing id [#578f6a8](https://github.com/wix/react-native-navigation/commit/578f6a8eeac543b64dce5637d6e6e856c5fdda1b) by [guyca](https://github.com/guyca)
* Fix Android title centering [#4aa5cd1](https://github.com/wix/react-native-navigation/commit/4aa5cd17bfb8a2acad884e2614eef137698b0f5a) by [StasDoskalenko](https://github.com/StasDoskalenko)


## 2.8.0
### Added
* passProps passed to setStackRoot and showOverlay can specify type with generics [#bc23fba](https://github.com/wix/react-native-navigation/commit/bc23fbad608dc9e38a7f09ff76868867310a4d62) by [henrikra](https://github.com/henrikra)
* passProps passed to showModal can specify type with generics [#34f37aa](https://github.com/wix/react-native-navigation/commit/34f37aa7c5790e10b3f7db8c5a2af23c6848c6c8) by [ruscoder](https://github.com/ruscoder)

#### Android
* Bottom tabs attach mode [#740ad3c](https://github.com/wix/react-native-navigation/commit/740ad3c326f29f51205b8f0fb046ff0658076925) by [guyca](https://github.com/guyca)

### Fixed
#### iOS
* Fix system & back button color [#57d8ff7](https://github.com/wix/react-native-navigation/commit/57d8ff7858f550ade133422e4a02505ed6378968) by [maryjenel](https://github.com/maryjenel)

## 2.7.1
### Fixed
#### Android
* Fix broken static options provided as objects [#4d82292](https://github.com/wix/react-native-navigation/commit/4d82292950471979cfb6c4016e82665fa29fe9da) by [guyca](https://github.com/guyca)

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
