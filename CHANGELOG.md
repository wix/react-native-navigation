# Changelog
# 6.8.0
## Added
* Proper error handling in link script [#daa48ca](https://github.com/wix/react-native-navigation/commit/daa48caa44831efdb60a6b394eb56cf6a94b4f9f) by [eduardopelitti](https://github.com/eduardopelitti)
* Replace deprecated link process [#4c3ed45](https://github.com/wix/react-native-navigation/commit/4c3ed4511a3a7f81586705b24b36e84e21436980) by [eduardopelitti](https://github.com/eduardopelitti)
* Add NavigationFunctionComponent interface [#c56630e](https://github.com/wix/react-native-navigation/commit/c56630e1ca7ed6575ac49c0c0c53a32b7ac3ff05) by [elizabeth-dev](https://github.com/elizabeth-dev)
* Export EventsRegistry [#057f11b](https://github.com/wix/react-native-navigation/commit/057f11b8eb6180066d719e83b9a8f39fe567d1b0) by [yedidyak](https://github.com/yedidyak) 
### Android
* Implement bottomTabs.hideOnScroll [#9544f41](https://github.com/wix/react-native-navigation/commit/9544f412e88505dec5aee4472b736689a0fe249a) by [guyca](https://github.com/guyca)

## Fixed
* Fix updateProps creating new props object [#b1a1e0d](https://github.com/wix/react-native-navigation/commit/b1a1e0d9ee5e00cd6c71ec3df2c8cc0c2c18fb7f) by [jinshin1013](https://github.com/jinshin1013)
### iOS
* Fixes badgeColor not being applied on ios 13+ [#28928e5](https://github.com/wix/react-native-navigation/commit/28928e567edac38a99c9aa71e3749e2e32364ec5) by [DaveAdams88](https://github.com/DaveAdams88)
* Fix black/white flickering when dismissing modals [#fd38fff](https://github.com/wix/react-native-navigation/commit/fd38fffeebbfca8edd8ddebccb4002c3349e5e9f) by [simonmitchell](https://github.com/simonmitchell)
* Fix registerBottomTabSelectedListener unselected value [#fbb72f2](https://github.com/wix/react-native-navigation/commit/fbb72f25df7097f5345d4e769b8cb736e2145785) by [yogevbd](https://github.com/yogevbd)
* Fix bottomTabs long press event [#814de45](https://github.com/wix/react-native-navigation/commit/814de45558a2bc250acc9c496e3974b54888592a) by [yogevbd](https://github.com/yogevbd)
* Fix hiding the SearchBar on iOS 11+ [#58674e8](https://github.com/wix/react-native-navigation/commit/58674e8baf06b09187039897a6ba27aff99ec8cb) by [jinshin1013](https://github.com/jinshin1013)
### Android
* Don't create Navigator if the Activity is finishing [#5c6ccd1](https://github.com/wix/react-native-navigation/commit/5c6ccd12c62190edbef4a614b24374b2402ac90c) by [JK0N](https://github.com/JK0N)
* Fix crash when disabledButtonColor is undefined and attempting to show a disabled button [#3fae8ed](https://github.com/wix/react-native-navigation/commit/3fae8ededc31e4d10e9e4153a614e9f3d159d650) by [guyca](https://github.com/guyca)
* TopBar title and subtitle font size is measured in dp instead of sp [#88e65de](https://github.com/wix/react-native-navigation/commit/88e65defb91e30292e73d228f602b126e1064b59) by [guyca](https://github.com/guyca)
* Fix crash when currentTabIndex is defined [#fbe81d0](https://github.com/wix/react-native-navigation/commit/fbe81d05b5074e82e32c08456b9688cf8c9f4a25) by [guyca](https://github.com/guyca)

# 6.7.5
## Fixed
* Fix TypeScript compilation errors [#2225e97](https://github.com/wix/react-native-navigation/commit/2225e971265a58fec7715630d782d8a8845f48fa) by [ItsNoHax](https://github.com/ItsNoHax)

# 6.7.4
## Fixed
### iOS
* Fix wrong options being applied when selected tab changes [#bd710bc](https://github.com/wix/react-native-navigation/commit/bd710bc0bd91ebdc41c3ebca6cf79ad84b91738e) by [yogevbd](https://github.com/yogevbd)

# 6.7.3
## Fixed
### iOS
* Restore bottomTabs visibility when needed [#80eb489](https://github.com/wix/react-native-navigation/commit/80eb4893ef2c86c2e20480c61c03fc2dc99979f1) by [yogevbd](https://github.com/yogevbd)

# 6.7.2
## Fixed
### iOS
* Fix BottomTabs visibility not working for pushed screens and mergeOptions [#ab63850](https://github.com/wix/react-native-navigation/commit/ab63850a405d01d40689478b3f408fc30ab1d382) by [yogevbd](https://github.com/yogevbd)

# 6.7.0 - 6.7.1
## Added
* Export TypeScript interfaces to streamline work with NavigationComponent + introduce `Navigation.events().registerComponentListener()` api [#ec7f324](https://github.com/wix/react-native-navigation/commit/ec7f32404d1a8cba79517f12c36eccaa4b13d3e2) by [yogevbd](https://github.com/yogevbd)

## Fixed
* Unmount previous root before resolving setRoot promise [#86b344c](https://github.com/wix/react-native-navigation/commit/86b344c7a287815a79891d7a9491c893c8081339) by [guyca](https://github.com/guyca) and [yogevbd](https://github.com/yogevbd)
* Ensure component generator passed to Navigation.registerComponent is invoked only once [#8ec7bcd](https://github.com/wix/react-native-navigation/commit/8ec7bcd83ae7fc721ce026cd11fb62df136edeac) by [guyca](https://github.com/guyca)

### Android
* Fix updating button options with mergeOptions [#b2df65a](https://github.com/wix/react-native-navigation/commit/b2df65a5fb245f123ea96cd2785ebc065cb065ea) by [guyca](https://github.com/guyca)
* Fix react-native-youtube support [#4d8d2ae](https://github.com/wix/react-native-navigation/commit/4d8d2ae40a0455fb5187cb965213fe350052bc50) by [guyca](https://github.com/guyca)
* Fix sideMenu.enabled option getting cleared when set on one drawer and the other drawer is opened [#67191e9](https://github.com/wix/react-native-navigation/commit/67191e9e7c915d612f83136e47000ed6311591e1) by [guyca](https://github.com/guyca)
* Mount all stack children after initial child is mounted [#a1beebe](https://github.com/wix/react-native-navigation/commit/a1beebe74beb265ed196e9e322a85aa40b84aa98) by [guyca](https://github.com/guyca)
* Fix flickering FAB when changing bottom tabs [#9a8bc54](https://github.com/wix/react-native-navigation/commit/9a8bc54dd41f91fbc90c3d8e44f02884dcdaa02c) by [guyca](https://github.com/guyca)
* Fix touch handling in nested Touchables in an Overlay [#851703c](https://github.com/wix/react-native-navigation/commit/851703c0caa75e4b6d8ad66f672ad0dc855842c4) by [guyca](https://github.com/guyca)

### iOS
* Send screen popped event only for rnn components [#0b7507d](https://github.com/wix/react-native-navigation/commit/0b7507d75f05590af7c933d5949b01aa7db04993) by [yogevbd](https://github.com/yogevbd)
* Fix bottomTabs visibility issues [#4e1ac71](https://github.com/wix/react-native-navigation/commit/4e1ac713944373d8d2c5b251912e1a872b2d00a7) by [yogevbd](https://github.com/yogevbd)

# 6.6.0
## Fixed
### Android
* Fix showing Modal from TopBar components in RN 62 [94862ed](https://github.com/wix/react-native-navigation/commit/94862ed66883646d636be95aeaaccd40394b8082) by [guyca](https://github.com/guyca)

# 6.5.0
## Added
### Android
* Added `width` and `height` options to button component which can be used to set exact measurements to button components [#42a6917](https://github.com/wix/react-native-navigation/commit/42a6917eeee149f7348a4eaf524ba76bac1240cf) by [guyca](https://github.com/guyca)
* Reuse button component if a component with the same id already exists  [#42a6917](https://github.com/wix/react-native-navigation/commit/42a6917eeee149f7348a4eaf524ba76bac1240cf) by [guyca](https://github.com/guyca)
* Allow hiding the NavigationBar [#7f6353b](https://github.com/wix/react-native-navigation/commit/7f6353bcead9c6d6e87d72574e0fec29ad9f2d19) by [M-i-k-e-l](https://github.com/M-i-k-e-l)
* Support `rotation` animation in shared element transition [#03dd211](https://github.com/wix/react-native-navigation/commit/03dd211a5425cf14586ef49814c1d3716aeb8441) by [guyca](https://github.com/guyca)
* Implement shared element transition `interpolation` option [#e80eb92](https://github.com/wix/react-native-navigation/commit/e80eb9275a04921976127f8c2775f37088f133c1) by [guyca](https://github.com/guyca)
* Implement shared element transition `startDelay` option [#334ab71](https://github.com/wix/react-native-navigation/commit/334ab7174a599f18af66eca6cee7409bee7537e7) by [guyca](https://github.com/guyca)

### iOS
* Implement rotate animation for shared element transition [#5d9e910](https://github.com/wix/react-native-navigation/commit/5d9e9100b771ca76ac20b916f945406460084b9b) by [yogevbd](https://github.com/yogevbd)

## Fixed
### iOS
* Fixed invalid modalPresentationStyle.popover enum value [#951a07b](https://github.com/wix/react-native-navigation/commit/951a07bb5571dbda2a0c9b665969bc25fc5ae784) by [rfnd](https://github.com/rfnd)
* Fix incorrect layout after changing BottomTabs visibility [#21cafcd](https://github.com/wix/react-native-navigation/commit/21cafcdecca8264dd2157d172dab24d8d4b5b4e6) by [yogevbd](https://github.com/yogevbd)
* Fix SafeAreaView measurement in SideMenu [#0da097e](https://github.com/wix/react-native-navigation/commit/0da097ef8471670e6550152fa5ebbdf4a02b3478) by [rfnd](https://github.com/rfnd)
* Fix backButton.color change on mergeOptions [#da0fd19](https://github.com/wix/react-native-navigation/commit/da0fd194f88b8f1042a0fe74bbf91e75ffac95b3) by [yogevbd](https://github.com/yogevbd)
* Fix bottomTab colors in landscape orientation  [#89402dc](https://github.com/wix/react-native-navigation/commit/89402dc31a3769b4fb1326b95170961497011caf) by [yogevbd](https://github.com/yogevbd)
* FIx screenPopped event not emitted if screen is popped with pop command [#2f31a2f](https://github.com/wix/react-native-navigation/commit/2f31a2fa703659f153162274c846a8f137ee94d1) by [yogevbd](https://github.com/yogevbd)

### Android
* Set textual TopBar button style options by spans instead of applying them on the view [#42a6917](https://github.com/wix/react-native-navigation/commit/42a6917eeee149f7348a4eaf524ba76bac1240cf) by [guyca](https://github.com/guyca)
* Ensure Component layout is not created prematurely by mergeOptions [#111df5a](https://github.com/wix/react-native-navigation/commit/111df5a3ba51ba6762cffd7119071bb4f71d18f7) by [guyca](https://github.com/guyca)
* Resolve tabsAttachMode from default options [#a4b2c76](https://github.com/wix/react-native-navigation/commit/a4b2c76a9d9b934192a4deee496c3ecef4c184ff) by [guyca](https://github.com/guyca)
* Support declaring currentTabIndex and currentTabId in default options [#3e5be29](https://github.com/wix/react-native-navigation/commit/3e5be29af8b1b78be1eec9ebf970b9204354a052) by [guyca](https://github.com/guyca)
* Fix BottomTabs size not adjusted after orientation change [#aa7908c](https://github.com/wix/react-native-navigation/commit/aa7908c57d141c7bb49de64a8e071330a8f7af31) by [guyca](https://github.com/guyca)


# 6.4.0
## Fixed
### iOS
* Fix styling options on iOS 13.4 [#950ac64](https://github.com/wix/react-native-navigation/commit/950ac6404fe1a43021426803d4fdad4ed4711476) by [yogevbd](https://github.com/yogevbd)
* Fix white flicker when pushing a screen [#a2bdfac](https://github.com/wix/react-native-navigation/commit/a2bdfacb27065f2101c3228df98484ba4ec68e03) by [RobertPaul01](https://github.com/RobertPaul01)
* Fix white topBar on pop with swipe gesture [#6227321](https://github.com/wix/react-native-navigation/commit/62273214f0590007ce81be2aef6da1f05e035c4a) by [yogevbd](https://github.com/yogevbd)
### Android
* Fix title component not being replaced via mergeOptions [#b0e8a82](https://github.com/wix/react-native-navigation/commit/b0e8a824f5e1ec141c9d3030dc21f242902ec29f) by [guyca](https://github.com/guyca)

# 6.3.3
## Fixed
### iOS
* Fix status bar visibility on iOS 13 [#f487134](https://github.com/wix/react-native-navigation/commit/f487134d8e4c302f69453c093c49fde17dba46fe) by [yogevbd](https://github.com/yogevbd)

# 6.3.1 - 6.3.2
## Added
* Custom component reference id OptionsTopBarButton typing [#6046372](https://github.com/wix/react-native-navigation/commit/60463729e5e4ace5c4c81ddc854ee2421b431c86) by [jarnove](https://github.com/jarnove)

## Fixed
### Android
* Always resolve dismissAllModals promise [#ec03383](https://github.com/wix/react-native-navigation/commit/ec03383b0de4fe092ddbef807850d131a42a1e7f) [#52bcd5b](https://github.com/wix/react-native-navigation/commit/52bcd5ba5090622db37055d0a18cb673673affa0) by [guyca](https://github.com/guyca)

# 6.3.0
## Fixed
### iOS
* Fix symbol collision with react-native-keyboard-input [#8ad40e1](https://github.com/wix/react-native-navigation/commit/8ad40e1ab23116d432e888801c07b57c6c09ad37) by [yogevbd](https://github.com/yogevbd)
* Fix overlays touch interception on new iPads [#2ed434c](https://github.com/wix/react-native-navigation/commit/2ed434c952b7c9326d9547005caa8c0601e58cb4)
* Removes unable to find UIManager module warning [#ba12604](https://github.com/wix/react-native-navigation/commit/ba1260402cc15409ddfef46fd5cad180d5e1a60f)
* Reject pop command when viewController not found in the hierarchy [#4413aa4](https://github.com/wix/react-native-navigation/commit/4413aa4a76628449116cf9bc7294696a490d6a65)
* Fix mergeOptions merging options with wrong child [#3c38c50](https://github.com/wix/react-native-navigation/commit/3c38c50a8b53e958a21e6fb7453622463e9870ff)
* Fix build warnings and possible retain cycles issues [#3f8577d](https://github.com/wix/react-native-navigation/commit/3f8577da7d23a2e4698d27d12bf9de55be39e7ef)
* Fix bottomTab icon hidden after setting badge [#124f975](https://github.com/wix/react-native-navigation/commit/124f975f42ebaf124d9e7c58296eaafd0f617ad9) by [yogevbd](https://github.com/yogevbd)


# 6.2.0
## Added
* Add windows support to build scripts vai `npm run start-windows` command [#afb5bff](https://github.com/wix/react-native-navigation/commit/afb5bffb49b9e8c670419aaacedf10f65cf82fd2) by [mayconmesquita](https://github.com/mayconmesquita)

## Fixed
### iOS
* Fix largeTitle background color on iOS 13 no being applied [#979cb6e](https://github.com/wix/react-native-navigation/commit/979cb6e08f80bd0b6b8e9286eb21d3c255c88312) by [yogevbd](https://github.com/yogevbd)
* Fix bottomTabs attach mode not working when BottomTabs are inside SideMenu [#7d6029f](https://github.com/wix/react-native-navigation/commit/7d6029f06bd3b4f4336d0d50a1621a5291e43fa7) by [yogevbd](https://github.com/yogevbd)
* Fix crash on iOS 10 when displaying stack layouts [#e923b8c](https://github.com/wix/react-native-navigation/commit/e923b8c02204e31d1ce6781dab11ebeabc2af218) [RomualdPercereau](https://github.com/RomualdPercereau)

# 6.1.2
## Fixed
### iOS
* Fix modal presentation style not being applied on some layouts [#931167e](https://github.com/wix/react-native-navigation/commit/931167e039000502d4198244c450dacce3c39809) by [yogevbd](https://github.com/yogevbd)
* Fix truncated bottomTab.text with semibold fontWeight [#b01629c](https://github.com/wix/react-native-navigation/commit/b01629c41da9197ee0737c937c02684c73dd9042) by [yogevbd](https://github.com/yogevbd)
* Always drawBehind bottomTabs and topBar when translucent: true [#6edbbf5](https://github.com/wix/react-native-navigation/commit/6edbbf512f2230ee0bceaf73c7895bca90475700) by [yogevbd](https://github.com/yogevbd)
* drawBehind when largeTitle is visible - fixes black large title [#6edbbf5](https://github.com/wix/react-native-navigation/commit/6edbbf512f2230ee0bceaf73c7895bca90475700) by [yogevbd](https://github.com/yogevbd)

# 6.1.1
## Fixed
### iOS
* Create new UITabBarItem instance on each bottomTab update [#3757ff7](https://github.com/wix/react-native-navigation/commit/3757ff7aa64cc9b6b8054af3e27b3865e27b2f9f) by [yogevbd](https://github.com/yogevbd)
* Delete duplicate misplaced files in root directory [#6d61ec0](https://github.com/wix/react-native-navigation/commit/6d61ec0e6cabeddc41b5860b4cb5b24f3de92dc2) by [ItsNoHax](https://github.com/ItsNoHax)

# 6.1.0
## Added
* Add componentName to modalDismiss event [#1c2558d](https://github.com/wix/react-native-navigation/commit/1c2558d77e489e2a35adc3a60eebed97ebf52add) by [jinshin1013](https://github.com/jinshin1013)

## Fixed
### iOS
* Support changing backButton fontFamily and fontSize [#b438588](https://github.com/wix/react-native-navigation/commit/b4385883de9ff07ed8915cdcd6f78ddc26bb6691) by [yogevbd](https://github.com/yogevbd)
* Fixed bottomTab text color not working correctly on iOS13 [#211a46e](https://github.com/wix/react-native-navigation/commit/211a46e087213bc72c166a4332cd1d3d0fa01be2) by [yogevbd](https://github.com/yogevbd)
* Support backButton.testID [#e1b76c1](https://github.com/wix/react-native-navigation/commit/e1b76c1fe222a4153eddedf43caba5dd457aadb9) by [yogevbd](https://github.com/yogevbd)
* Handle statusBar.visible in all layout types and not only in components [#a2f5dbd](https://github.com/wix/react-native-navigation/commit/a2f5dbd3131f2cc158a650a01a1b9e271c2952f2) by [yogevbd](https://github.com/yogevbd)
* Fix a lot of large title issues [#54b2855](https://github.com/wix/react-native-navigation/commit/54b285531ea43e0dae76ae08af7de923ccf5917c)
* Fix title and subtitle color animations when popping screens [#5210848](https://github.com/wix/react-native-navigation/commit/52108484cc59ad8aaec9ef51b3c370c7ac80128f) by [yogevbd](https://github.com/yogevbd)

### Android
* Stop rejecting dismissAllModals promise if no modals are displayed [#30b0b47](https://github.com/wix/react-native-navigation/commit/30b0b47b712cd1882b9c944a125c9d06ca5e0dd8) by [guyca](https://github.com/guyca)
* Support tabs without icons on Android [#ef58a6c](https://github.com/wix/react-native-navigation/commit/ef58a6cdeb1c4ea90ff528af50d6d2dc572f9f28) by [guyca](https://github.com/guyca)
* Fix autolink script - set minSdk to 19 [#4ce0e89](https://github.com/wix/react-native-navigation/commit/4ce0e89b06b9ab29d4be5d2eb0d11419deaade7a) by [jinshin1013](https://github.com/jinshin1013)
* ExternalComponentController extends ViewController [#c33ff12](https://github.com/wix/react-native-navigation/commit/c33ff1291ded4b171ef7b3f0736c5bc5b169d850) by [guyca](https://github.com/guyca)
* Support [react-native-youtube](https://github.com/davidohayon669/react-native-youtube) [#2793a02](https://github.com/wix/react-native-navigation/commit/2793a022729043d271fa6ffd80df62297c5f76fa)

# 6.0.1
## Fixed
### iOS
* Fix applying drawBehind through mergeOptions [#e002a68](https://github.com/wix/react-native-navigation/commit/e002a68110cb75877982aed9c693ece8382c7942) by [yogevbd](https://github.com/yogevbd)

# 6.0.0
This release changes how layout.backgroundColor work on iOS to add parity with Android.
* layout.backgroundColor - applies background color to parent layouts (Stack, BottomTabs, SideMenu etc)
* layout.componentBackgroundColor - applies background color only to components

## Fixed
### Android
* Fix custom push animations not working [#c9232cb](https://github.com/wix/react-native-navigation/commit/c9232cb9e49e02cd1975d16de01fa2a6186032b0) by [guyca](https://github.com/guyca)
### iOS
* Remove draw behind deprecation [#950642d](https://github.com/wix/react-native-navigation/commit/950642d48b2ec67f510ae6b3eefaaeb1ebfcf43d) by [yogevbd](https://github.com/yogevbd)
* Fix layout.backgroundColor being applied to components, it's now applied to parent layouts  [#950642d](https://github.com/wix/react-native-navigation/commit/950642d48b2ec67f510ae6b3eefaaeb1ebfcf43d) by [yogevbd](https://github.com/yogevbd)
* Implement layout.componentBackgroundColor which is applied only to component ViewControllers [#950642d](https://github.com/wix/react-native-navigation/commit/950642d48b2ec67f510ae6b3eefaaeb1ebfcf43d) by [yogevbd](https://github.com/yogevbd)

# 5.1.1
## Fixed
### iOS
* Apply extendedLayoutIncludesOpaqueBars true on all viewControllers (this commit was originally added to v4 and was left out of v5 my mistake) [#9fefeca](https://github.com/wix/react-native-navigation/commit/9fefeca9be9844fc80f839a532c97f5c4fa1d299)
### Android
* Fix crash when mergeOptions were called before stack view was created [#defc2aa](https://github.com/wix/react-native-navigation/commit/defc2aaa6e7c845c05cabe8c5753cc6a68ab5830) by [guyca](https://github.com/guyca)

# 5.1.0
## Added
### iOS
* Add window.backgroundColor option [#c99ecf9](https://github.com/wix/react-native-navigation/commit/c99ecf9b145f453a9674c965d1634bcdece973b6) by [yogevbd](https://github.com/yogevbd)

# 5.0.0
This release is focuses on shared element transition and on improving the installation process of the library.

## Upgrading from V4
### Remove missingDimensionStrategy from app/build.gradle
Since RNN supports multiple react-native versions, the library has multiple flavors, each targeting a different RN version. We now chose the appropriate flavor based on the react-native version installed in node_modules.
```diff
-missingDimensionStrategy "RNN.reactNativeVersion", "reactNativeXX" // Where XX is the minor number of the react-native version you're using
```

### Declare Kotlin version in build.gradle
We're starting to migrate RNN to Kotlin. All new code is written in Kotlin and existing code will be gradually converted to Kotlin. This requires you to declare the Kotlin version you're using in your project.
```diff
buildscript {
    ext {
+        kotlinVersion = "1.3.61" // Or any other kotlin version following 1.3.x
+        RNNKotlinVersion = kotlinVersion
+        RNNKotlinStdlib = "kotlin-stdlib-jdk8"
    }
    dependencies {
+        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"    
    }
}
```

### Update MainApplication.java
In an effort to simplify RNN's integrations process as much as possible, we're minimizing the changes required to both MainApplication and MainActivity.
```diff
+import com.facebook.react.PackageList;

public class MainApplication extends NavigationApplication {
-    @Override
-    protected ReactNativeHost createReactNativeHost() {
-    return new NavigationReactNativeHost(this) {  
+    private final ReactNativeHost mReactNativeHost =
            new NavigationReactNativeHost(this) {
                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }

+                @Override
+                public boolean getUseDeveloperSupport() {
+                    return BuildConfig.DEBUG;
+                }

+                @Override
+                public List<ReactPackage> getPackages() {
+                    ArrayList<ReactPackage> packages = new PackageList(this).getPackages();
+                    return packages;
+                }
+            }
-    }

+    @Override
+    public ReactNativeHost getReactNativeHost() {
+        return mReactNativeHost;
+    }

-    @Override
-    public boolean isDebug() {
-        return BuildConfig.DEBUG;
-    }

-    @Nullable
-    @Override
-    public List<ReactPackage> createAdditionalReactPackages() {
-        List<ReactPackage> packages = new ArrayList<>();
-        return packages;
-    }
}
```

### Update settings.gradle
Since RNN now supports auto linking, declaring the dependency manually is no longer needed.
```diff
-include ':react-native-navigation'
-project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../../lib/android/app/')
```

### Make sure your app supports auto linking
#### Update `app/build.gradle`
Add these lines to the bottom of your `app/build.gradle` file.
```diff
+apply from: file("../../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle")
+applyNativeModulesAppBuildGradle(project)
```
#### Update `settings.gradle`
```diff
+apply from: file("../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle")
+applyNativeModulesSettingsGradle(settings)
include ':app'
```

### Remove RNN pod from podspec
As RNN is now autolinked, remove its pod from your podspec file. This will ensure the correct version is linked when running `pod install`
```diff
- pod 'ReactNativeNavigation', :podspec => '../node_modules/react-native-navigation/ReactNativeNavigation.podspec'
```

## Breaking Changes
### Modal animation parity
show and dismiss animation api have been fixed and are now in parity with Android api.
If you've defined a custom modal animation, you can now consolidate the animation declarations.

<table>
<tr>
  <td>New Android + iOS API</td>
  <td>Unsupported iOS API</td>
</tr>
<tr>
<td>

  ```js
  options: {
    animations: {
      showModal: {
        alpha: {
          from: 0,
          to: 1,
          duration: 250,
        }
      }
    }
  }
  ```
</td>
<td>

  ```js
  options: {
    animations: {
      showModal: {
        content: {
          alpha: {
            from: 0,
            to: 1,
            duration: 250
          }
        }
      }
    }
  }
  ```
</td>
</tr>
</table>

### drawBehind is deprecated on iOS
> ❗️topBar and bottomTabs drawBehind option will be removed in the next major version.

The drawBehind option has been an anti pattern on iOS from the start and was introduced only for parity with Android api.
On iOS, when a ScrollView or a SafeAreaView are used as screen root; the system handles insets automatically.
As adoption of notch devices increases, developers use these views regularly, rendering the drawBehind option useless.

>**During the migration phase, leave Android options unchanged and set `drawBehind: true` to both TopBar and BottomTabs in default options.**

### Android: Animation values are now declared in dp
If you're animating `translationY` or `translationX` pass these values in dp instead of pixels.
This is especially relevant to values returned by `await Navigation.constants()` api as these values are returned in dp. Now, if you'd like to use them in animations, you can do so without converting to pixels.

# 4.8.1
## Fixed
### Android
* Fix NPE when showing Overlay [#bfde34a](https://github.com/wix/react-native-navigation/commit/bfde34a5862c971587583aa52cb450cf526d5c66) by [guyca](https://github.com/guyca)
### iOS
* Fix overlays touch interception on new iPads [#433f48b](https://github.com/wix/react-native-navigation/commit/433f48b59d9be5aa328361654341afa8414c2e21) by [yogevbd](https://github.com/yogevbd)

# 4.8.0
## Fixed
### Android
* Support react-native-youtube [#ffbd288](https://github.com/wix/react-native-navigation/commit/ffbd2882b24109ff8f2b51ca3c8c88822cc9afb7) by [guyca](https://github.com/guyca)
### iOS
* Fix wrong SafeAreaView margins when using bottomTabs.drawBehind: true [#527fd49](https://github.com/wix/react-native-navigation/commit/527fd49f2f1517143032a8b14f6ab17d2f74c032) by [yogevbd](https://github.com/yogevbd)

# 4.7.1
## Fixed
* Move selectTabOnPress prop to correct interface [#d6ead65](https://github.com/wix/react-native-navigation/commit/d6ead65c9331f12d3f79fc3b5bb7e0a0de80816e) by [phanghos](https://github.com/phanghos)
### iOS
* Fix external components layout measurement [#1961181](https://github.com/wix/react-native-navigation/commit/196118186fb788200dafcc1e11cd9f7d6e3f6dda) by [yogevbd](https://github.com/yogevbd)

# 4.7.0
## Added
* On tab press event - handle tab selection in Js [#b153142](https://github.com/wix/react-native-navigation/commit/b1531428a0a9608b5d1c84547f228d5de0c1aca2) by [pontusab](https://github.com/pontusab)
* RN 0.62 support on Android [#4bfa7c5](https://github.com/wix/react-native-navigation/commit/4bfa7c5092ac0ca6708b4bd61bd63e59601e8f3e) by [safaiyeh](https://github.com/safaiyeh)
## Fixed
### Android
* Fix dotIndicator not respecting initial visibility option [#d9bd03f](https://github.com/wix/react-native-navigation/commit/d9bd03fea7465acadb6ef17613f8fe98e8be4eb1) by [itsam](https://github.com/itsam)
### iOS
* Set default fontsize value for title and subtitle [#0741799](https://github.com/wix/react-native-navigation/commit/0741799281a43380bc419886a19a8e72fc32d042) by [maryjenel](https://github.com/maryjenel)
* Respect default options when updating bottomTab options [#513138e](https://github.com/wix/react-native-navigation/commit/513138ebd9c620ba9e8d2b8a4a154ced790de1b2) by [yogevbd](https://github.com/yogevbd)

# 4.6.0
## Added
### Android
* Adapt NavigationBar buttons color according to NavigationBar background color [#6521177](https://github.com/wix/react-native-navigation/commit/65211775f6b354945a9dc5a3f7791ddfdd20ebab) by [rverbytskyi](https://github.com/rverbytskyi)

## Fixed
### Android
* Disable TopBar scroll when nestedScrollEnabled is enabled [#9a361a4](https://github.com/wix/react-native-navigation/commit/9a361a4fe340f5af18e1fa87b02f5e7e1f41646d) by [guyca](https://github.com/guyca)
* Reject promise when trying to push two children with same id [#27ceea8](https://github.com/wix/react-native-navigation/commit/27ceea8fb92506fdd756e45768e7235e3e7babc6) by [guyca](https://github.com/guyca)
* Fix drawBehind in default options not working [#0e93366](https://github.com/wix/react-native-navigation/commit/0e933661aed9cb4a6cad6e1e1fa5a352f371f754) by [guyca](https://github.com/guyca)

### iOS
* Fix merge options leaks to next screen in stack [#386bf65](https://github.com/wix/react-native-navigation/commit/386bf65d251f0c6582de2cf529487dfafc69bc9d) by [yogevbd](https://github.com/yogevbd)
* Invoke all commands on the main thread [#8843224](https://github.com/wix/react-native-navigation/commit/8843224a9aed33a9fd92323db48ab80f50cae72f) by [yogevbd](https://github.com/yogevbd)
* Resolve navigationItem from external component [#d81b0bf](https://github.com/wix/react-native-navigation/commit/d81b0bf3cf929136218a71dbc74dcbf0cc668886) by [yogevbd](https://github.com/yogevbd)
* Fix popGesture freezes the app [#37473f8](https://github.com/wix/react-native-navigation/commit/37473f88e9bfdc847f25cd21e5ca521e31157268) by [yogevbd](https://github.com/yogevbd)

# 4.5.4
* Fix title.component fill parent [#7e6b2be](https://github.com/wix/react-native-navigation/commit/7e6b2be676382db74bd393940ddfdea3a3847e2c) by [yogevbd](https://github.com/yogevbd)

# 4.5.2 - 4.5.3
## Fixed
### Android
* Fix NPE when updating tabs before tab views are created [#fccfb4d](https://github.com/wix/react-native-navigation/commit/fccfb4d8c289c10b6424e38690fa4469fa65b7ea) by [guyca](https://github.com/guyca)

# 4.5.1
## Fixed
### iOS
* Fix crash when check UIBarButtonItem are not added by RNN [#233820e](https://github.com/wix/react-native-navigation/commit/233820ef372042d4fae463b0a63f75bfeab160da) by [wixiosalex](https://github.com/wixiosalex)

# 4.5.0
## Added
* Improve accessibility support [#07c558c](https://github.com/wix/react-native-navigation/commit/07c558c76f7bf3acd56a2af4e0e901c81ae0e49d), [#f635b5e](https://github.com/wix/react-native-navigation/commit/f635b5e8be81ee99aaf2726c989624b1dafcbf41) by [yogevbd](https://github.com/yogevbd) and [guyca](https://github.com/guyca)
### iOS
* Add modalAttemptedToDismiss event [#87af42a](https://github.com/wix/react-native-navigation/commit/87af42a56be7deaa32678afb72846c92e293f524) by [manicantic](https://github.com/manicantic)
* Add bottomTabLongPressed event on iOS [#c425f83](https://github.com/wix/react-native-navigation/commit/c425f837b1d99a8b27525d52b6eba37fb77cbded) by [N3TC4T](https://github.com/N3TC4T)
## Fixed
### Android
* Emit modalDismissed event before ViewController is destroyed [#cf591d9](https://github.com/wix/react-native-navigation/commit/cf591d9a9c48ed89c7e7fed4594b8dbcb9732bc9) by [guyca](https://github.com/guyca)

# 4.4.0
## Added
### Android
* Added TitleState showWhenActiveForce option for bottomTabs [#cf18e2d](https://github.com/wix/react-native-navigation/commit/cf18e2d3c98785c28d42b486b70e05b75404ca54) by [BenJeau](https://github.com/BenJeau)

## Fixed
### Android
* Apply BottomTabs visibility only if child is visible [#6ffb301](https://github.com/wix/react-native-navigation/commit/6ffb3011f7e3db11c9dac9d4c9d01b8345079c2e) by [guyca](https://github.com/guyca)

## Fixed
# 4.3.0
## Fixed
### iOS
* Fixed pushing external ViewControllers to stack inside a modal [#4b14c87](https://github.com/wix/react-native-navigation/commit/4b14c8798b4f9bf99ce36dd29b2df8e8ff5bf109) by [yogevbd](https://github.com/yogevbd)

# 4.2.0
## Fixed
### Android
* Support hiding back button with mergeOptions [#3f17dc4](https://github.com/wix/react-native-navigation/commit/3f17dc4a82657c6cfdbdd82c95cbba6f2bf63f55) by [guyca](https://github.com/guyca)
# 4.1.0
## Added
* Send componentType field in componentDidAppear and componentDidDisappear events [#3878b68](https://github.com/wix/react-native-navigation/commit/3878b683ccc045f6c732850833be0633a8ac1b0e) by [guyca](https://github.com/guyca) and [yogevbd](https://github.com/yogevbd)
## Fixed
* Add typing for children on TopTabs [#1f611c6](https://github.com/wix/react-native-navigation/commit/1f611c657ff946493aced56758399f7c240bf002) by [aalises](https://github.com/aalises)
### Android
* Apply translucent StatusBAr flag only if needed [#6782362](https://github.com/wix/react-native-navigation/commit/6782362035228463701003cc7fbbbc0af27d88d0) by [guyca](https://github.com/guyca)
### iOS
* Fix topBar.title.component measurement on iOS 10 [#82e4807](https://github.com/wix/react-native-navigation/commit/82e48079dc4ef24639b215af83d1344aa021c281) by [yogevbd](https://github.com/yogevbd)
* Remove yellow boxes from title and button components [#b82d87f](https://github.com/wix/react-native-navigation/commit/b82d87f0c88d1dcd803af0f8507935a1d818bae3) by [yogevbd](https://github.com/yogevbd)

# 4.0.9
## Fixed
### iOS
* Force translucent on iOS 12 when background is transparent [#2ad41f3](https://github.com/wix/react-native-navigation/commit/2ad41f3adb9883fa20f1d52fcbbc6fd0750976f9) by [yogevbd](https://github.com/yogevbd)
# 4.0.8
## Fixed
### iOS
* Fix TopBar background color on iOS12 [#f202c7e](https://github.com/wix/react-native-navigation/commit/f202c7ec03bcc6cf9d2bc1c587064b78e111432a) by [yogevbd](https://github.com/yogevbd)
# 4.0.7
## Added
* Add screenPopped event [#71af559](https://github.com/wix/react-native-navigation/commit/71af55968db11315cd10aac2e64cb1e24f37c0e0) by [yogevbd](https://github.com/yogevbd) and [guyca](https://github.com/guyca)

## Fixed
### Android
* Fix incorrect bottom inset when hiding BottomTabs in default options [#d0c21e4](https://github.com/wix/react-native-navigation/commit/d0c21e4f6573189e634838a53c518f6bb8587080) by [guyca](https://github.com/guyca)
### iOS
* Fix topBar transparent background on iOS 12 [#cd3d347](https://github.com/wix/react-native-navigation/commit/cd3d3472fcb8e5588507bc16c438d53c581d7d2b) by [yogevbd](https://github.com/yogevbd)

# 4.0.6
## Fixed
* Fix native bottomTab.icon resource not working [#aa1870a](https://github.com/wix/react-native-navigation/commit/aa1870a743ff9e1611c643a5462ca84f81710fcd) by [guyca](https://github.com/guyca)

### iOS
* Fix default font size regression [#8f9e719](https://github.com/wix/react-native-navigation/commit/8f9e719747ef9c0861122f5c5a75b0ec852574fc) by [yogevbd](https://github.com/yogevbd)
* Fix crash when reloading while an overlay is displayed [#2fa17aa](https://github.com/wix/react-native-navigation/commit/2fa17aaaca56a1faef751e1d946eb3cc16ee7284) by [yogevbd](https://github.com/yogevbd)

# 4.0.5
## Fixed
* Fix conflict with React Native's getConstants [#5e6d6fc](https://github.com/wix/react-native-navigation/commit/5e6d6fce132fc37722867c3e43a0036f4fe085b8) by [guyca](https://github.com/guyca)
* Use lodash submodules instead of lodash to reduce bundle size [#e53a9fe](https://github.com/wix/react-native-navigation/commit/e53a9feb20e5851d29f0913fc6c2704dbe00af2e) by (pontusab)[https://github.com/pontusab]
* Replace lodash chain with flow to reduce bundle size [#bf354d7](https://github.com/wix/react-native-navigation/commit/bf354d7c77c889c7e2dc0be0fc29b6a54011ce9b) by [jinshin1013](https://github.com/jinshin1013)
### iOS
* Send ModalDismissed event [#4cb0e98](https://github.com/wix/react-native-navigation/commit/4cb0e98a6756317f8f61f5ee2b3d7530cd180f61) by [yogevbd](https://github.com/yogevbd)
* Fix incorrect `constants.topBarHeight` value when pageSheet modal is displayed [#9ef61a9](https://github.com/wix/react-native-navigation/commit/9ef61a9a1357312208463ef149a7951931043d1e) by [yogevbd](https://github.com/yogevbd)
* Add BottomTabs.attachMode support [#60c4dfc](https://github.com/wix/react-native-navigation/commit/60c4dfcd72245de02836bd4858f311822cdf866d) by [yogevbd](https://github.com/yogevbd)

### Android
* Merge child options with SideMenu parent [#afbaa9a](https://github.com/wix/react-native-navigation/commit/afbaa9a4f53e05e6b00e1b8044a4a8a484ac3ea0) by [guyca](https://github.com/guyca)
* Temporary fix to FAB position on screen [#4714983](https://github.com/wix/react-native-navigation/commit/47149835dcf707806bb211230e63a61d8cf9a1d5) by [guyca](https://github.com/guyca)

# 4.0.4
## Fixed
### iOS
* Fix large title issues [#65118b1](https://github.com/wix/react-native-navigation/commit/65118b1ed28dd5a4636dabd5952c515d18a0b802) by [yogevbd](https://github.com/yogevbd)

# 4.0.3
## Added
### iOS
* Introduce `modal.swipeToDismiss` option [#659a42c](https://github.com/wix/react-native-navigation/commit/

## Fixed
### iOS
* Fix setting TopBar component in default options [#4a1b8b4](https://github.com/wix/react-native-navigation/commit/4a1b8b4a64546050f515f8dd00539f14e18e5f70) by [yogevbd](https://github.com/yogevbd)
* Fix blinking react view button [#d502c69](https://github.com/wix/react-native-navigation/commit/d502c694438903ffb612fec16a0b72444b3bc3ed) by [yogevbd](https://github.com/yogevbd)
* Let the system control modal presentation style [#659a42c](https://github.com/wix/react-native-navigation/commit/659a42cde14a3fb7653ebc40c906ec042761cd7c) by [yogevbd](https://github.com/yogevbd)
* Fix black TopBar on iOS < 13 [#7cf2083](https://github.com/wix/react-native-navigation/commit/7cf208353f22a4f07760f166ba0afd5af24f2e91) by [yogevbd](https://github.com/yogevbd)

# 4.0.2
## Fixed
### iOS
* Fix glitchy pop animation [#0c6e2f0](https://github.com/wix/react-native-navigation/commit/0c6e2f00b1ee8122499df08f060d5f016868355e) by [yogevbd](https://github.com/yogevbd)

# 4.0.1
## Fixed
### iOS
* Fix double back button [#ed7c579](https://github.com/wix/react-native-navigation/commit/ed7c5795e1e7770c1eeb692b1fa300f3163d7503) by [yogevbd](https://github.com/yogevbd)
* Fix transparent TopBar on iOS 13 [#b560265](https://github.com/wix/react-native-navigation/commit/b56026553dfcc5bf9ffe91e848fd3b7723561656) by [yogevbd](https://github.com/yogevbd)
* Fix TopBar.noBorder on iOS 13 [#919fa12](https://github.com/wix/react-native-navigation/commit/919fa12780e1ace13d2b8ce7680795fd26b68f76), [#5b7ddec](https://github.com/wix/react-native-navigation/commit/5b7ddec1dad927e71dc31115111dec5d7e165449) by [yogevbd](https://github.com/yogevbd)
* Fix crash when calling Navigation.constants() when BottomTabs don't exist [#2cd4752](https://github.com/wix/react-native-navigation/commit/2cd4752fa0a43a7c704846b3e3c8833a4c9eff36) by [yogevbd](https://github.com/yogevbd)

### Android
* Create react host after SoLoader.init [#ab2fa63](https://github.com/wix/react-native-navigation/commit/ab2fa632c23fcfbfbb1fbb04e91f7394289e95cb) by [guyca](https://github.com/guyca)

# 4.0.0
## Added
### iOS
* **Support Xcode 11 - Xcode 10 is no longer supported**
* Support centering bottomTab icons using bottomTabs.titleDisplayMode [#26d3d82](https://github.com/wix/react-native-navigation/commit/26d3d8219097fe1b22a9585526ab8a712af6a508) by [yogevbd](https://github.com/yogevbd)
* Prevent creation of button react view with the same componentId [#1807c5b](https://github.com/wix/react-native-navigation/commit/1807c5b48d7b72e4f171dc105db6eff6291614e5) by [yogevbd](https://github.com/yogevbd)

## Fixed
* Change BottomTab.icon back to optional [#d8c34c3](https://github.com/wix/react-native-navigation/commit/d8c34c391097189819972ffd7f5454231ec9b2f3) by [guyca](https://github.com/guyca)

### iOS
* Remeasure title component after orientation change [#619af3e](https://github.com/wix/react-native-navigation/commit/619af3efe081f7069acfbe1149c7b0775f40faeb) by [yogevbd](https://github.com/yogevbd)
* Fix memory leak when reloading the app [#9970853](https://github.com/wix/react-native-navigation/commit/9970853820bf303cb75fb4238050e46c9c65c49d) by [yogevbd](https://github.com/yogevbd)
* Support UINavigationBarBehavior [#6f13d69](https://github.com/wix/react-native-navigation/commit/6f13d6963c6af655506a820b8bbddea4849152d4), [#9f43bca](https://github.com/wix/react-native-navigation/commit/9f43bca3573e8833eb8219c6e7a829d8d92faac0) by [yogevbd](https://github.com/yogevbd)

### Android
* Automatically apply DrawBehind when tabs are hidden [#002b7d8](https://github.com/wix/react-native-navigation/commit/002b7d8f331019f79421e7cd28bab9bf411d73e4) by [guyca](https://github.com/guyca)
* Fix button disabled color has no effect [#b66ff1d](https://github.com/wix/react-native-navigation/commit/b66ff1d4b63196113ee0ba0129d33da39c2e134a) by [guyca](https://github.com/guyca)
* Fix BottomTabs background color changing to white background sometimes [#57eb0db](https://github.com/wix/react-native-navigation/commit/57eb0db7da6d69c784a9b0cd5672a563901738a9) by [guyca](https://github.com/guyca)

# 3.7.0
## Added
### Android
* Add bottomTabs.preferLargeIcons option [#fd93167](https://github.com/wix/react-native-navigation/commit/fd93167dbe70975161c20832f43a499453f76804) by [guyca](https://github.com/guyca)

# 3.6.0
### Added
* Start script for Windows [#764863f](https://github.com/wix/react-native-navigation/commit/764863f46be72f7b29499994e5349ea90084d450) by [Damar95](https://github.com/Damar95)

## Fixed
### Android
* Include commandName in commandCompleted event [#b904608](https://github.com/wix/react-native-navigation/commit/b9046081ace89a2d3d0795c15920b6b2692c7702) by [jpgarcia](https://github.com/jpgarcia)
* Fix crash when title component is destroyed right after being attached [#39ee170](https://github.com/wix/react-native-navigation/commit/39ee1701fd0e626961158d9c342b4c6b0bd3c05b) by [guyca](https://github.com/guyca)
* Fix NPE when component appears under certain conditions [#35851fc](https://github.com/wix/react-native-navigation/commit/35851fc9893917338022f5eb06c25bfb21625d81) by [heroic](https://github.com/heroic)
* Apply layout direction directly on TopBar buttons container [#14b5221](https://github.com/wix/react-native-navigation/commit/14b5221caa2529c0396134c5f7981f093a14b58b) by [guyca](https://github.com/guyca)

# 3.5.1
## Fixed
### iOS
* Fix title component disappearing after mergeOptions [#6d446a8](https://github.com/wix/react-native-navigation/commit/6d446a8882f92b82cd01b78992331d66c31b5abf) by [guyca](https://github.com/guyca)

# 3.5.0
## Fixed
### Android
* Allow navigationBarColor change within mergeOptions [#8720628](https://github.com/wix/react-native-navigation/commit/87206286bfb8fd235e5356da9542c62dfc44f356) by [danielang](https://github.com/danielang)
* Fix crash when null was used as bottomTab.color [#c48ed74](https://github.com/wix/react-native-navigation/commit/c48ed747a163a2917820e62ab40d5047389f5fcb) by [guyca](https://github.com/guyca)

# 3.4.0
## Added
* [stable] Introduce Navigation.updateProps command [#0eb0570](https://github.com/wix/react-native-navigation/commit/0eb0570840a26d4b848d7c763060a2b8faf1dc80) by [guyca](https://github.com/guyca)

### Android
* add support for navigationBarColor [#8af95da](https://github.com/wix/react-native-navigation/commit/8af95da24a0622829d514c8ed61507438491bc27) by [mcuelenaere](https://github.com/mcuelenaere)

## Fixed
* Added setStackRoot animation property to interface [#fcdbe79](https://github.com/wix/react-native-navigation/commit/fcdbe79dc3305cbf12396aa6283a1bf4a5f02889) by [nielsdB97](https://github.com/nielsdB97)

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
