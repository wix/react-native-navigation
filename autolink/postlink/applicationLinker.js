// @ts-check
var path = require('./path');
var fs = require("fs");
var { warnn, logn, infon, debugn } = require("./log");

class ApplicationLinker {
  constructor() {
    this.applicationPath = path.mainApplicationJava;
  }

  link() {
    if (this.applicationPath) {
      logn("Linking MainApplication...");
      var applicationContents = fs.readFileSync(this.applicationPath, "utf8");
      var linkers = [this._extendNavigationApplication, this._extendNavigationHost, this._removeSOLoaderInit];
      applicationContents = this._extendNavigationApplication(applicationContents);
      applicationContents = this._extendNavigationHost(applicationContents);
      applicationContents = this._removeSOLoaderInit(applicationContents);
      fs.writeFileSync(this.applicationPath, applicationContents);
      infon("MainApplication linked successfully!\n");
    }
  }

  _extendNavigationApplication(applicationContent) {
    if (this._doesExtendApplication(applicationContent)) {
      debugn("   Extending NavigationApplication");
      return applicationContent
        .replace(/extends\s+Application\s+implements\s+ReactApplication/gi, "extends NavigationApplication")
        .replace("import com.facebook.react.ReactApplication;", "import com.reactnativenavigation.NavigationApplication;");
    }
    warnn("   MainApplication already extends NavigationApplication");
    return applicationContent;
  }

  _doesExtendApplication(applicationContent) {
    return /\s+MainApplication\s+extends\s+Application\s+implements\s+ReactApplication\s+/.test(applicationContent);
  }

  _extendNavigationHost(applicationContent) {
    if (this._doesExtendReactNativeHost(applicationContent)) {
      debugn("   Changing host implementation to NavigationReactNativeHost");
      return applicationContent
        .replace("new ReactNativeHost(this)", "new NavigationReactNativeHost(this)")
        .replace("import com.facebook.react.ReactNativeHost;", "import com.facebook.react.ReactNativeHost;\nimport com.reactnativenavigation.react.NavigationReactNativeHost;")
    }
    warnn("   NavigationReactNativeHost is already used");
    return applicationContent;
  }

  _removeSOLoaderInit(applicationContent) {
    if (this._isSOLoaderInitCalled(applicationContent)) {
      debugn("   Removing call to SOLoader.init()");
      return applicationContent.replace(/SoLoader.init\(\s*this\s*,\s*[/* native exopackage */]*\s*false\s*\);/, "")
    }
    warnn("   SOLoader.init() is not called");
    return applicationContent;
  }

  _isSOLoaderInitCalled(applicationContent) {
    return /SoLoader.init\(this,\s*[/* native exopackage */]*\s*false\);/.test(applicationContent);
  }

  _doesExtendReactNativeHost(applicationContent) {
    return /\s*new ReactNativeHost\(this\)\s*/.test(applicationContent);
  }
}

module.exports = ApplicationLinker;