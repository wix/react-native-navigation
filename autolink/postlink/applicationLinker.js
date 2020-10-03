// @ts-check
var path = require("./path");
var fs = require("fs");
var { warnn, logn, infon, debugn, errorn } = require("./log");

class ApplicationLinker {
  constructor() {
    this.applicationPath = path.mainApplicationJava;
    this.navigationApplicationSuccess = false;
    this.navigationHostSuccess = false;
    this.soLoaderInitSuccess = false;
  }

  link() {
    if (!this.applicationPath) {
      errorn(
        "MainApplication.java not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava"
      );
    }

    logn("Linking MainApplication...");
    var applicationContents = fs.readFileSync(this.applicationPath, "utf8");

    try {
      applicationContents = this._extendNavigationApplication(applicationContents);
      this.navigationApplicationSuccess = true;
    } catch (e) {
      errorn("   " + e);
    }
    try {
      applicationContents = this._extendNavigationHost(applicationContents);
      this.navigationHostSuccess = true;
    } catch (e) {
      errorn("   " + e);
    }
    try {
      applicationContents = this._removeSOLoaderInit(applicationContents);
      this.soLoaderInitSuccess = true;
    } catch (e) {
      errorn("   " + e);
    }

    fs.writeFileSync(this.applicationPath, applicationContents);

    if (this.navigationApplicationSuccess && this.navigationHostSuccess && this.soLoaderInitSuccess) {
      infon("MainApplication.java linked successfully!\n");
    } else if (!this.navigationApplicationSuccess && !this.navigationHostSuccess && !this.soLoaderInitSuccess) {
      errorn("MainApplication.java was not successfully linked! Please check the information above:\n   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava");
    } else {
      warnn(
        "MainApplication.java was partially linked. Please check the information above and complete the missing steps manually:\n   https://wix.github.io/react-native-navigation/docs/installing#3-update-mainapplicationjava"
      );
    }
  }

  _extendNavigationApplication(applicationContent) {
    if (this._doesExtendApplication(applicationContent)) {
      debugn("   Extending NavigationApplication");
      return applicationContent
        .replace(/extends\s+Application\s+implements\s+ReactApplication/gi, "extends NavigationApplication")
        .replace("import com.facebook.react.ReactApplication;", "import com.reactnativenavigation.NavigationApplication;");
    }

    if (this._hasAlreadyLinkedApplication(applicationContent)) {
      warnn("   MainApplication already extends NavigationApplication, skipping.");
      return applicationContent;
    }

    throw new Error("There was a problem extending NavigationApplication from your MainApplication file.");
  }

  _doesExtendApplication(applicationContent) {
    return /\s+MainApplication\s+extends\s+Application\s+implements\s+ReactApplication\s+/.test(applicationContent);
  }

  _hasAlreadyLinkedApplication(applicationContent) {
    return /\s+extends\s+NavigationApplication\s+/.test(applicationContent);
  }

  _extendNavigationHost(applicationContent) {
    if (this._doesExtendReactNativeHost(applicationContent)) {
      debugn("   Changing host implementation to NavigationReactNativeHost");
      return applicationContent
        .replace("new ReactNativeHost(this)", "new NavigationReactNativeHost(this)")
        .replace("import com.facebook.react.ReactNativeHost;", "import com.facebook.react.ReactNativeHost;\nimport com.reactnativenavigation.react.NavigationReactNativeHost;");
    }

    if (this._hasAlreadyLinkedNavigationHost(applicationContent)) {
      warnn("   NavigationReactNativeHost is already used, skipping.");
      return applicationContent;
    }

    throw new Error("There was a problem extending NavigationReactNativeHost().");
  }

  _doesExtendReactNativeHost(applicationContent) {
    return /\s*new ReactNativeHost\(this\)\s*/.test(applicationContent);
  }

  _hasAlreadyLinkedNavigationHost(applicationContent) {
    return /\s*new NavigationReactNativeHost\(this\)\s*/.test(applicationContent);
  }

  _removeSOLoaderInit(applicationContent) {
    if (this._isSOLoaderInitCalled(applicationContent)) {
      debugn("   Removing call to SOLoader.init()");
      return applicationContent.replace(/SoLoader.init\(\s*this\s*,\s*[/* native exopackage */]*\s*false\s*\);/, "");
    }
    warnn("   SOLoader.init() is not called, skipping.");
    return applicationContent;
  }

  _isSOLoaderInitCalled(applicationContent) {
    return /SoLoader.init\(this,\s*[/* native exopackage */]*\s*false\);/.test(applicationContent);
  }
}

module.exports = ApplicationLinker;
