#  React Native Navigation v2 (WIP)
We are rebuilding react-native-navigation

- [Why?](#why-rebuild-react-native-navigation)
- [Where is it standing now?](#where-is-it-standing-now)
- [Getting Started](#getting-started-with-v2)
- [Usage](#usage)

## Why Rebuild react-native-navigation?

### A New Improved Core Architecture
react-native-navigation has a few issues which are unsolvable in it’s current architecture. <br>
These issues originate from the same problem: you cannot specify on which screen you wish to make an action. Whenever you want to push a screen, show a modal or any other action, the action defaults to originate from your current screen. This covers most use cases but there are some edge cases: <br>
* What if you want to update your navbar icons and the user pops the screen? Your icons might update on the wrong screen.
* What if you want to push a screen as a result of a redux action?

There are ways to solve some of these problems in v1 but they are not straightforward. We want to change that.

#### New API
To solve this problem in v2, every screen receives as a prop it’s containerId. Whenever you want to perform an action from that screen you need to pass the containerId to the function:
```js
Navigator.pop(this.props.containerId)
```
### Built for Contributors
Currently, it requires a lot of work to accept pull requests. We need to manually make sure that everything works before we approve them because v1 is not thoroughly tested. <br>
v2 is written with contributors in mind from day one.

#### Written In TDD
v2 is written in Test Driven Development. We have a test for every feature including features that are not implemented yet. This makes accepting pull requests extremely easy: If our tests pass, your pull request is accepted.


## Where is it standing now?
v2 currently supports most of react-native-navigation’s basic functions but it is still behind v1.
Here is the full comparison of features between v1 and v2 (will be updated regulary):
### Top Level API

|    API              | v1  | v2 |
|--------------------|-----|----|
| startTabBasedApp   |   ✅    |   ✅  |
| startSinglePageApp |   ✅   |  ✅   |
| registerScreen     |   ✅   |  ✅   |
| drawer             |    ✅  |    ✅ |
### Screen API

|  API              | v1     | v2         |
|---------------------|--------|------------|
| push                |  ✅     |   ✅       |
| pop                 |  ✅     |  ✅        |
| showModal           |  ✅     |  ✅        |
| popToRoot           |   ✅     |   ✅         |
| resetTo             |   ✅     |    ✅        |
| dismissModal        |   ✅     |     ✅       |
| dismissAllModals    |   ✅     |      ✅      |
| showLightBox        |   ✅     |      [Contribute](CONTRIBUTING.md)      |
| dismissLightBox     |   ✅     |       [Contribute](CONTRIBUTING.md)       |
| handleDeepLink      |   ✅     |       [Contribute](CONTRIBUTING.md)       |
| setOnNavigatorEvent |   ✅     |       [Contribute](CONTRIBUTING.md)       |
| setButtons          |   ✅     |         [Contribute](CONTRIBUTING.md)     |
| setTitle            |   ✅     |         [Contribute](CONTRIBUTING.md)     |
| toggleDrawer        |   ✅     |        [Contribute](CONTRIBUTING.md)   |
| toggleTabs          |   ✅     |        [Contribute](CONTRIBUTING.md)     |
| setTabBadge         |    ✅    |       [Contribute](CONTRIBUTING.md)     |
| switchToTab         |    ✅    |        [Contribute](CONTRIBUTING.md)    |
| toggleNavBar        |   ✅     |      [Contribute](CONTRIBUTING.md)      |
| Screen Visibility   |   ✅     |       ✅     |

### Styles

Note:  properties who begin with 'navBar' in v1 are now named 'topBar' to avoid confusion with the android native bottom nav bar.

|                       | v1  | v2 iOS | v2 Android |
|-----------------------|-----|--------|------------|
| topBarBackgroundColor |   ✅    |   in development     |     [Contribute](CONTRIBUTING.md)        |
| topBarTextFontSize    |   ✅    |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarTextFontFamily  |  ✅     |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarBackgroundColor |  ✅     |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarButtonColor     |  ✅     |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarHidden          |   ✅    |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarHideOnScroll    |  ✅     |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarTranslucent     |  ✅     |   in development      |     [Contribute](CONTRIBUTING.md)        |
| topBarTransparent     | ✅      |   in development     |     [Contribute](CONTRIBUTING.md)        |
| topBarNoBorder        |  ✅     |    [Contribute](CONTRIBUTING.md)     |     [Contribute](CONTRIBUTING.md)        |
| drawUnderTabBar       |  ✅     |    [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)       |
| drawUnderTopBar       |  ✅     |    [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)       |
| statusBarBlur         |  ✅     |    [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)       |
| topBarBlur            | ✅      |    [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)       |
| tabBarHidden  |   ✅  |   [Contribute](CONTRIBUTING.md)     |    [Contribute](CONTRIBUTING.md)        |
| statusBarTextColorScheme |  ✅   |   in development      |      / iOS specific    |
| statusBarTextColorSchemeSingleScreen|  ✅   |     in development    |     / iOS specific      |
| navBarSubtitleColor          |  ✅   |   [Contribute](CONTRIBUTING.md)     |      [Contribute](CONTRIBUTING.md)      |
| navBarSubtitleFontFamily    |   ✅  |    [Contribute](CONTRIBUTING.md)    |     [Contribute](CONTRIBUTING.md)       |
| screenBackgroundColor     | ✅    |   in development     |     [Contribute](CONTRIBUTING.md)       |
| orientation     |  ✅   |  [Contribute](CONTRIBUTING.md)      |   [Contribute](CONTRIBUTING.md)          |
| statusBarHideWithTopBar        |  ✅   |    [Contribute](CONTRIBUTING.md)     |     [Contribute](CONTRIBUTING.md)       |
| statusBarHidden       |  ✅   |   [Contribute](CONTRIBUTING.md)       |     [Contribute](CONTRIBUTING.md)      |
| disabledBackGesture       |   ✅  |   [Contribute](CONTRIBUTING.md)       |    / iOS specific     |
| screenBackgroundImageName         |   ✅  |   [Contribute](CONTRIBUTING.md)      |    [Contribute](CONTRIBUTING.md)        |
| rootBackgroundImageName            |  ✅   |    [Contribute](CONTRIBUTING.md)     |    [Contribute](CONTRIBUTING.md)        |

Element tranisitions, adding buttons and styles are not yet implemented. [Contribute](CONTRIBUTING.md)

## Getting started with v2
If v2 supports everything you need for your app we encourage you to use it.

### Installation
1. Download react-native-navigation v2
```bash
yarn add react-native-navigation@alpha
```
##### iOS
2. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `./node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-1))

3. In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2))

4. In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Settings` tab (right pane). In the `Header Search Paths` section add `$(SRCROOT)/../node_modules/react-native-navigation/lib/ios`. Make sure on the right to mark this new path `recursive` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3))

5. In Xcode, under your project files, modify `AppDelegate.m`. add:

`#import <ReactNativeNavigation/ReactNativeNavigation.h>`

remove everything in the method didFinishLaunchingWithOptions and add:

```
NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];
[ReactNativeNavigation bootstrap:jsCodeLocation launchOptions:launchOptions];
```

##### Android
2. Add the following in `android/settings.gradle`.

	```groovy
	include ':react-native-navigation'
	project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-	navigation/lib/android/app/')
	```

3. Update project dependencies in `android/app/build.gradle`.
	```groovy
	android {
		compileSdkVersion 25
		buildToolsVersion "25.0.1"
		...
	}

	dependencies {
		compile fileTree(dir: "libs", include: ["*.jar"])
		compile "com.android.support:appcompat-v7:23.0.1"
		compile "com.facebook.react:react-native:+"
		compile project(':react-native-navigation')
	}
	```

4. In `MainActivity.java` it should extend `com.reactnativenavigation.controllers.SplashActivity` instead of `ReactActivity`.

	This file can be located in `android/app/src/main/java/com/yourproject/`.

	```java
	import com.reactnativenavigation.controllers.SplashActivity;

	public class MainActivity extends SplashActivity {

	}
	```

	If you have any **react-native** related methods, you can safely delete them.

5. In `MainApplication.java`, add the following
	```java
	import com.reactnativenavigation.NavigationApplication;

	public class MainApplication extends NavigationApplication {

		@Override
		public boolean isDebug() {
			// Make sure you are using BuildConfig from your own application
			return BuildConfig.DEBUG;
		}

		protected List<ReactPackage> getPackages() {
			// Add additional packages you require here
			// No need to add RnnPackage and MainReactPackage
			return Arrays.<ReactPackage>asList(
				// eg. new VectorIconsPackage()
			);
		}

		@Override
		public List<ReactPackage> createAdditionalReactPackages() {
			return getPackages();
		}
	}
	```

	Make sure that `isDebug` and `createAdditionalReactPackages` methods are implemented.

6. Update `AndroidManifest.xml` and set **android:name** value to `.MainApplication`
	```xml
	<application
		android:name=".MainApplication"
		...
	/>
## Usage
### Top Screen API

#### Navigation
```js
import Navigation from 'react-native-navigation';
```
#### Events - On App Launched
How to initiate your app.

```js
Navigation.events().onAppLaunched(() => {
    Navigation.setRoot({
      container: {
        name: 'navigation.playground.WelcomeScreen'
      }
    });
  });
```

#### registerContainer(screenID, generator)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.
```js
Navigation.registerContainer(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
```

#### setRoot({params})
Start a Single page app with two side menus:
```js
Navigation.setRoot({
      container: {
        name: 'navigation.playground.WelcomeScreen'
      },
      sideMenu: {
        left: {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a left side menu screen'
            }
          }
        },
        right: {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a right side menu screen'
            }
          }
        }
      }
    });
```
Start a tab based app:
```js
Navigation.setRoot({
      tabs: [
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 1',
              myFunction: () => 'Hello from a function!'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 2'
            }
          }
        }
      ]
    });
```
### Screen API

#### push(params)
Push a new screen into this screen's navigation stack.
```js
Navigation.push(this.props.containerId, {
      name: 'navigation.playground.PushedScreen',
      passProps: {}
    });
```
#### pop(containerId)
Pop the top screen from this screen's navigation stack.
```js
Navigation.pop(this.props.containerId);
```
#### popTo(params)
```js
Navigation.popTo(this.props.containerId, this.props.previousScreenIds[0]);
```
#### popToRoot()
Pop all the screens until the root from this screen's navigation stack
```js
Navigation.popToRoot(this.props.containerId);
```
#### showModal(params = {})
Show a screen as a modal.
```js
Navigation.showModal({
      container: {
        name: 'navigation.playground.ModalScreen',
        passProps: {
            key: 'value'
        }
      }
    });
```
#### dismissModal(containerId)
Dismiss modal.
```js
Navigation.dismissModal(this.props.containerId);
```
#### dismissAllModals()
Dismiss all the current modals at the same time.
```js
Navigation.dismissAllModals();
```
#### Screen Lifecycle - onStop() and onStart()

The onStop() and onStart() functions are lifecycle functions that are added to the screen and run when a screen apears and disappears from the screen. To use them simply add them to your component like any other react lifecycle function:

```js
class LifecycleScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: 'nothing yet'
    };
  }

  onStart() {
    this.setState({ text: 'onStart' });
  }

  onStop() {
    alert('onStop'); //eslint-disable-line
  }

  componentWillUnmount() {
    alert('componentWillUnmount'); //eslint-disable-line
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Lifecycle Screen`}</Text>
	<Text style={styles.h1}>{this.state.text}</Text>
      </View>
    );
  }
}
```


