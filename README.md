
<h1 align="center">
  <img src="./logo.png"/><br>
  React Native Navigation
</h1>

[![NPM Version](https://img.shields.io/npm/v/react-native-navigation.svg?style=flat)](https://www.npmjs.com/package/react-native-navigation)
[![NPM Downloads](https://img.shields.io/npm/dm/react-native-navigation.svg?style=flat)](https://www.npmjs.com/package/react-native-navigation)
[![Build Status](https://jenkins-oss.wixpress.com/buildStatus/icon?job=react-native-navigation-master)](https://jenkins-oss.wixpress.com/job/react-native-navigation-master/)
[![Join us on Discord](https://img.shields.io/badge/discord-react--native--navigation-738bd7.svg?style=flat)](https://discord.gg/DhkZjq2)

## Important
We are currently in late stages of development of [v2](https://github.com/wix/react-native-navigation/tree/v2), which is published to npm under `alpha` tag.
* New to RNN? We recommend you start with [v2](https://github.com/wix/react-native-navigation/tree/v2).
* Already using v1? now is the time to migrate your code base to [v2](https://github.com/wix/react-native-navigation/tree/v2), and you can easily do do with a single line of code using the [v1-v2 adapter](https://github.com/wix-playground/react-native-navigation-v1-v2-adapter).
<br><br>Have any questions regarding v2? Join us in [Discord](https://discord.gg/DhkZjq2) #v2 channel.

<br><br>Latest stable version is `1.1.x` and is published to npm under tag `latest`. It supports react-native >= 0.48.
>⚠️Since we're focusing our efforts on v2, we are not accepting pull requests for v1 and are not addresing issues for v1.

### tl;dr

React Native Navigation provides 100% native platform navigation on both iOS and Android for React Native apps. The JavaScript API is simple and cross-platform - just install it in your app and give your users the native feel they deserve. Using redux? No problem: React Native Navigation comes with optional redux support out of the box. Ready to get started? Check out the [docs](https://wix.github.io/react-native-navigation/).

### Real world examples

<img src="https://github.com/wix/react-native/blob/master/src/videos/demo.gif?raw=true" width="240">&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/wix/react-native/blob/master/src/videos/rnn-example-demo.gif?raw=true" width="240">

On the left - The Wix app.

On the right - The example app.


## Quick Links
* [Documentation](https://wix.github.io/react-native-navigation/#/)
* [Stack Overflow](http://stackoverflow.com/questions/tagged/react-native-navigation)
* [Chat with us](https://discord.gg/DhkZjq2)
* Bootstrap - If you prefer to learn more about the library and the APIs through code, head over to [the bootstrap example app](https://github.com/wix/react-native-navigation-bootstrap) or the more feature rich [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp)
* [v2 - Under Development](https://github.com/wix/react-native-navigation/tree/v2#react-native-navigation-v2-wip)

----

One of the major things missing from React Native core is fully featured native navigation. Navigation includes the entire skeleton of your app with critical components like nav bars, tab bars and side menu drawers.

If you're trying to deliver a user experience that's on par with the best native apps out there, you simply can't compromise on JS-based components trying to fake the real thing.

For example, this package replaces the native [NavigatorIOS](https://facebook.github.io/react-native/docs/navigatorios.html) that has been [abandoned](https://facebook.github.io/react-native/docs/navigator-comparison.html) in favor of JS-based solutions that are easier to maintain. For more details see in-depth discussion [here](https://github.com/wix/react-native-controllers#why-do-we-need-this-package).


## License

The MIT License.

See [LICENSE](LICENSE)

