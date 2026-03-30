// @ts-check
var path = require('./path');
var fs = require('fs');
var { warnn, logn, infon, debugn, errorn } = require('./log');

class ApplicationLinker {
  constructor() {
    this.applicationPath = path.mainApplicationKotlin;
    this.navigationApplicationSuccess = false;
    this.navigationHostSuccess = false;
    this.soLoaderInitSuccess = false;
    this.newArchEntryPointLoadSuccess = false;
  }

  link() {
    if (!this.applicationPath) {
      errorn(
        'MainApplication.kt not found! Does the file exist in the correct folder?\n' +
          '   Please check the manual installation docs:\n' +
          '   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava'
      );
    }

    logn('Linking MainApplication...');
    var applicationContents = fs.readFileSync(this.applicationPath, 'utf8');

    try {
      applicationContents = this._extendNavigationApplication(applicationContents);
      this.navigationApplicationSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }
    try {
      applicationContents = this._extendNavigationHost(applicationContents);
      this.navigationHostSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }
    try {
      applicationContents = this._removeSOLoaderInit(applicationContents);
      this.soLoaderInitSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }
    try {
      applicationContents = this._removeNewArchEntryPointLoad(applicationContents);
      this.newArchEntryPointLoadSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }

    fs.writeFileSync(this.applicationPath, applicationContents);

    if (
      this.navigationApplicationSuccess &&
      this.navigationHostSuccess &&
      this.soLoaderInitSuccess &&
      this.newArchEntryPointLoadSuccess
    ) {
      infon('MainApplication.kt linked successfully!\n');
    } else if (
      !this.navigationApplicationSuccess &&
      !this.navigationHostSuccess &&
      !this.soLoaderInitSuccess &&
      !this.newArchEntryPointLoadSuccess
    ) {
      errorn(
        'MainApplication.kt was not successfully linked! Please check the information above:\n   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava'
      );
    } else {
      warnn(
        'MainApplication.kt was partially linked. Please check the information above and complete the missing steps manually:\n   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava'
      );
    }
  }

  _extendNavigationApplication(applicationContent) {
    if (this._doesExtendApplication(applicationContent)) {
      debugn('   Extending NavigationApplication');
      return applicationContent
        .replace(/:\s*Application\(\)\s*,\s*ReactApplication/gi, ': NavigationApplication()')
        .replace(
          'import com.facebook.react.ReactApplication',
          'import com.reactnativenavigation.NavigationApplication'
        );
    }

    if (this._hasAlreadyLinkedApplication(applicationContent)) {
      warnn('   MainApplication already extends NavigationApplication, skipping.');
      return applicationContent;
    }

    throw new Error(
      'There was a problem extending NavigationApplication from your MainApplication file.'
    );
  }

  _doesExtendApplication(applicationContent) {
    return /\s+MainApplication\s*:\s*Application\(\)\s*,\s*ReactApplication\s+/.test(
      applicationContent
    );
  }

  _hasAlreadyLinkedApplication(applicationContent) {
    return /\s*:\s*NavigationApplication\(\)\s*/.test(applicationContent);
  }

  _extendNavigationHost(applicationContent) {
    if (this._hasAlreadyLinkedNavigationHost(applicationContent)) {
      warnn('   NavigationReactNativeHost is already used, skipping.');
      return applicationContent;
    }

    if (this._doesExtendDefaultReactNativeHost(applicationContent)) {
      debugn('   Changing host implementation to NavigationReactNativeHost');
      return applicationContent
        .replace('DefaultReactNativeHost(this)', 'NavigationReactNativeHost(this)')
        .replace(
          'import com.facebook.react.defaults.DefaultReactNativeHost',
          'import com.facebook.react.defaults.DefaultReactNativeHost\nimport com.reactnativenavigation.react.NavigationReactNativeHost'
        );
    } else if (this._doesExtendReactNativeHost(applicationContent)) {
      debugn('   Changing host implementation to NavigationReactNativeHost');
      return applicationContent
        .replace('ReactNativeHost(this)', 'NavigationReactNativeHost(this)')
        .replace(
          'import com.facebook.react.ReactNativeHost',
          'import com.facebook.react.ReactNativeHost\nimport com.reactnativenavigation.react.NavigationReactNativeHost'
        );
    }

    throw new Error('There was a problem extending NavigationReactNativeHost().');
  }

  _doesExtendReactNativeHost(applicationContent) {
    return /\s*ReactNativeHost\(this\)\s*/.test(applicationContent);
  }

  _doesExtendDefaultReactNativeHost(applicationContent) {
    return /\s*DefaultReactNativeHost\(this\)\s*/.test(applicationContent);
  }

  _hasAlreadyLinkedNavigationHost(applicationContent) {
    return /\s*NavigationReactNativeHost\(this\)\s*/.test(applicationContent);
  }

  _removeSOLoaderInit(applicationContent) {
    if (this._isSOLoaderInitCalled(applicationContent)) {
      debugn('   Removing call to SOLoader.init()');
      return applicationContent.replace(
        /SoLoader\.init\(\s*this\s*,\s*OpenSourceMergedSoMapping\s*\);?/,
        ''
      );
    }
    warnn('   SOLoader.init() is not called, skipping.');
    return applicationContent;
  }

  _isSOLoaderInitCalled(applicationContent) {
    return /SoLoader\.init\(\s*this\s*,\s*OpenSourceMergedSoMapping\s*\);?/.test(
      applicationContent
    );
  }

  _removeNewArchEntryPointLoad(applicationContent) {
    if (this._isNewArchEntryPointLoadCalled(applicationContent)) {
      debugn('   Removing New Architecture entry point load block');
      return applicationContent.replace(
        /if\s*\(\s*BuildConfig\.IS_NEW_ARCHITECTURE_ENABLED\s*\)\s*\{[\s\S]*?load\(\)\s*[\s}]*?\}/,
        ''
      );
    }
    warnn('   New Architecture entry point load block is not called, skipping.');
    return applicationContent;
  }

  _isNewArchEntryPointLoadCalled(applicationContent) {
    return /if\s*\(\s*BuildConfig\.IS_NEW_ARCHITECTURE_ENABLED\s*\)\s*\{[\s\S]*?load\(\)\s*[\s}]*?\}/.test(
      applicationContent
    );
  }
}

module.exports = ApplicationLinker;
