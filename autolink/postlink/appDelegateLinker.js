// @ts-check
var fs = require("fs");
var path = require("./path");
var { warnn, logn, infon, debugn, errorn } = require("./log");

class AppDelegateLinker {
  constructor() {
    this.appDelegatePath = path.appDelegate;
    this.removeUnneededImportsSuccess = false;
    this.removeApplicationLaunchContentSuccess = false;
  }

  link() {
    if (!this.appDelegatePath) {
      errorn(
        "   AppDelegate not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation"
      );
      return;
    }

    logn("Linking AppDelegate...");

    var appDelegateContents = fs.readFileSync(this.appDelegatePath, "utf8");

    try {
      appDelegateContents = this._removeUnneededImports(appDelegateContents);
      this.removeUnneededImportsSuccess = true;
    } catch (e) {
      errorn("   " + e.message);
    }

    appDelegateContents = this._importNavigation(appDelegateContents);

    appDelegateContents = this._bootstrapNavigation(appDelegateContents);

    try {
      appDelegateContents = this._removeApplicationLaunchContent(appDelegateContents);
      this.removeApplicationLaunchContentSuccess = true;
    } catch (e) {
      errorn("   " + e.message);
    }

    fs.writeFileSync(this.appDelegatePath, appDelegateContents);

    if (this.removeUnneededImportsSuccess && this.removeApplicationLaunchContentSuccess) {
      infon("AppDelegate linked successfully!\n");
    } else {
      warnn("AppDelegate was partially linked, please check the details above and proceed with the manual installation documentation to complete the linking process.!\n");
    }
  }

  _removeUnneededImports(content) {
    debugn("   Removing Unneeded imports");

    const unneededImports = [/#import\s+\<React\/RCTRootView.h>\s/];
    let elementsRemovedCount = 0;

    unneededImports.forEach((unneededImport) => {
      if (unneededImport.test(content)) {
        content = content.replace(unneededImport, "");
        elementsRemovedCount++;
      }
    });

    if (unneededImports.length === elementsRemovedCount) {
      debugn("   All imports have been removed");
    } else if (elementsRemovedCount === 0) {
      warnn(
        "   No imports could be removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation"
      );
    } else {
      throw new Error(
        "Some imports were removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation"
      );
    }

    return content;
  }

  _importNavigation(content) {
    if (!this._doesImportNavigation(content)) {
      debugn("   Importing ReactNativeNavigation.h");
      return content.replace(/#import\s+"AppDelegate.h"/, '#import "AppDelegate.h"\n#import <ReactNativeNavigation/ReactNativeNavigation.h>');
    }

    warnn("   AppDelegate already imports ReactNativeNavigation.h");
    return content;
  }

  _bootstrapNavigation(content) {
    if (this._doesBootstrapNavigation(content)) {
      warnn("   Navigation Bootstrap already present.");
      return content;
    }

    debugn("   Bootstrapping Navigation");
    return content.replace(/RCTBridge.*];/, "[ReactNativeNavigation bootstrapWithDelegate:self launchOptions:launchOptions];");
  }

  _doesBootstrapNavigation(content) {
    return /ReactNativeNavigation\s+bootstrap/.test(content);
  }

  _removeApplicationLaunchContent(content) {
    debugn("   Removing Application launch content");

    const toRemove = [
      /RCTRootView\s+\*rootView((.|\r|\s)*?)];\s+/,
      /rootView.backgroundColor((.|\r)*)];\s+/,
      /self.window((.|\r)*)];\s+/,
      /UIViewController\s\*rootViewController((.|\r)*)];\s+/,
      /rootViewController\.view\s+=\s+rootView;\s+/,
      /self.window.rootViewController\s+=\s+rootViewController;\s+/,
      /\[self.window\s+makeKeyAndVisible];\s+/,
    ];
    let elementsRemovedCount = 0;

    toRemove.forEach((element) => {
      if (element.test(content)) {
        content = content.replace(element, "");
        elementsRemovedCount++;
      }
    });

    if (toRemove.length === elementsRemovedCount) {
      debugn("   Application Launch content has been removed");
    } else if (elementsRemovedCount === 0) {
      warnn(
        "   No elements could be removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation"
      );
    } else {
      throw new Error(
        "Some elements were removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation"
      );
    }

    return content;
  }

  _doesImportNavigation(content) {
    return /#import\s+\<ReactNativeNavigation\/ReactNativeNavigation.h>/.test(content);
  }
}

module.exports = AppDelegateLinker;
