# React Native Navigation

App-wide support for 100% native navigation with an easy cross-platform interface. For iOS, this package is a wrapper around [react-native-controllers](https://github.com/wix/react-native-controllers), but provides a simplified more abstract API over it. This abstract API will be unified with the Android solution which is currently work in progress. It also fully supports redux if you use it.

<img src="https://github.com/wix/react-native/blob/master/assets/themes/bootstrap-3/images/demo.gif?raw=true" width="240">

> ### Notice
> We are currently working hard on redesigning and refactoring this project with high quality and robustness in mind. As a result, issues and pull requests will take more time to process. 

> To avoid any confusion and breaking existing projects, all continuous development is published under the npm tag `next`, with version `2.0.0-experimental.x`. Once stable, we will publish it as `2.0.0`. **This version supports react-native `0.31.0`**.

> The last stable version is `1.30.x` with npm tag `latest`. **This version supports react-native `0.25.1`**.

## Wiki

* [Overview](https://github.com/wix/react-native-navigation/wiki)
* [Why use this package](https://github.com/wix/react-native-navigation/wiki#why-use-this-package)
* [Installation - iOS](https://github.com/wix/react-native-navigation/wiki/Installation---iOS#installation---ios)
* [Installation - Android](https://github.com/wix/react-native-navigation/wiki/Installation---Android)
* [Usage](https://github.com/wix/react-native-navigation/wiki/Usage)
* [Migrating to version 2.0](https://github.com/wix/react-native-navigation/wiki/Migrating-to-version-2.0)
* [Top Level API](https://github.com/wix/react-native-navigation/wiki/Top-Level-API)
* [Screen API](https://github.com/wix/react-native-navigation/wiki/Screen-API)
* [Styling the navigator](https://github.com/wix/react-native-navigation/wiki/Styling-the-navigator)
* [Adding buttons to the navigator](https://github.com/wix/react-native-navigation/wiki/Adding-buttons-to-the-navigator)
* [Styling the tab bar](https://github.com/wix/react-native-navigation/wiki/Styling-the-tab-bar)
* [Deep links](https://github.com/wix/react-native-navigation/wiki/Deep-links)
* [Third party libraries support](https://github.com/wix/react-native-navigation/wiki/Third-party-libraries-support)
* [Release Notes](https://github.com/wix/react-native-navigation/blob/master/CHANGELOG.md)
* [Milestones](https://github.com/wix/react-native-navigation/wiki/Milestones)
* [License](https://github.com/wix/react-native-navigation/wiki#license)

## Why use this package

One of the major things missing from React Native core is fully featured native navigation. Navigation includes the entire skeleton of your app with critical components like nav bars, tab bars and side menu drawers.

If you're trying to deliver a user experience that's on par with the best native apps out there, you simply can't compromise on JS-based components trying to fake the real thing.

For example, this package replaces the native [NavigatorIOS](https://facebook.github.io/react-native/docs/navigatorios.html) that has been [abandoned](https://facebook.github.io/react-native/docs/navigator-comparison.html) in favor of JS-based solutions that are easier to maintain. For more details see in-depth discussion [here](https://github.com/wix/react-native-controllers#why-do-we-need-this-package).

## License

The MIT License.

See [LICENSE](LICENSE)
