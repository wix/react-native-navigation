
<h1 align="center">
  <img src="./logo.png"/><br>
  React Native Navigation
</h1>

[![NPM Version](https://img.shields.io/npm/v/react-native-navigation.svg?style=flat)](https://www.npmjs.com/package/react-native-navigation)
[![NPM Downloads](https://img.shields.io/npm/dm/react-native-navigation.svg?style=flat)](https://www.npmjs.com/package/react-native-navigation)
[![Build Status](https://travis-ci.org/wix/react-native-navigation.svg?branch=master)](https://travis-ci.org/wix/react-native-navigation)
[![Join us on Discord](https://img.shields.io/badge/discord-react--native--navigation-738bd7.svg?style=flat)](https://discord.gg/DhkZjq2)

App-wide support for 100% native navigation with an easy cross-platform interface. For iOS, this package is a wrapper around [react-native-controllers](https://github.com/wix/react-native-controllers), but provides a simplified more abstract API over it. It also fully supports redux if you use it.

<img src="https://github.com/wix/react-native/blob/master/src/videos/demo.gif?raw=true" width="240">

----

One of the major things missing from React Native core is fully featured native navigation. Navigation includes the entire skeleton of your app with critical components like nav bars, tab bars and side menu drawers.

If you're trying to deliver a user experience that's on par with the best native apps out there, you simply can't compromise on JS-based components trying to fake the real thing.

For example, this package replaces the native [NavigatorIOS](https://facebook.github.io/react-native/docs/navigatorios.html) that has been [abandoned](https://facebook.github.io/react-native/docs/navigator-comparison.html) in favor of JS-based solutions that are easier to maintain. For more details see in-depth discussion [here](https://github.com/wix/react-native-controllers#why-do-we-need-this-package).

> ### Important
> We are currently working hard on redesigning and refactoring this project with high quality and robustness in mind. As a result, issues and pull requests will take more time to process.
To avoid any confusion and breaking existing projects, all continuous development is published under the npm tag `next`, with version `2.0.0-experimental.x`. **This version supports react-native `0.40.0 and above`**. Once stable, we will publish it as `2.0.0`.
If you don't want your code to break on a daily basis and don't need the new features ASAP please use the `latest` version or just specify a specific version number.

## Documentation

The documentation website can be found [here](http://wix.github.io/react-native-navigation).

If you prefer to learn more about the library and the APIs through code, head over to [the bootstrap example app](https://github.com/wix/react-native-navigation-bootstrap) or the more feature rich [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp)


## License

The MIT License.

See [LICENSE](LICENSE)
