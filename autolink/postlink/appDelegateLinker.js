// @ts-check
var path = require('./path');
var fs = require("fs");
var {warnn, logn, infon, debugn} = require("./log");

class ApplicationLinker {
  constructor() {
    this.appDelegatePath = path.appDelegate;
  }

  link() {
    if (this.appDelegatePath) {
      logn("Linking AppDelegate...");
      var appDelegateContents = fs.readFileSync(this.appDelegatePath, "utf8");
      if (this._doesBootstrapNavigation(appDelegateContents)) {
        infon("AppDelegate already linked\n");
        return;
      }
      appDelegateContents = this._removeUnneededImports(appDelegateContents);
      appDelegateContents = this._importNavigation(appDelegateContents);
      appDelegateContents = this._bootstrapNavigation(appDelegateContents);
      appDelegateContents = this._removeApplicationLaunchContent(appDelegateContents);
      fs.writeFileSync(this.appDelegatePath, appDelegateContents);
      infon("AppDelegate linked successfully!\n");
    }
  }

  _doesBootstrapNavigation(applicationContent) {
    return /ReactNativeNavigation\s+bootstrap/.test(applicationContent);
  }

  _removeUnneededImports(applicationContent) {
    const unneededImports = [/\#import\s+\<React\/RCTBridge.h>\s/, /#import\s+\<React\/RCTRootView.h>\s/];
    debugn("   Removing Unneeded imports");
    unneededImports.forEach(unneededImport => {
      if (unneededImport.test(applicationContent)) {
        applicationContent = applicationContent.replace(unneededImport, "");
      }
    });

    return applicationContent;
  }

  _importNavigation(applicationContent) {
    if (!this._doesImportNavigation(applicationContent)) {
      debugn("   ");
      return applicationContent
        .replace(/#import\s+"AppDelegate.h"/, "#import \"AppDelegate.h\"\n#import <ReactNativeNavigation/ReactNativeNavigation.h>")
    }
    warnn("   AppDelegate already imports Navigation");
    return applicationContent;
  }

  _removeApplicationLaunchContent(applicationContent) {
    const toRemove = [/RCTRootView\s+\*rootView((.|\r|\s)*?)];\s+/, /rootView.backgroundColor((.|\r)*)];\s+/,
      /self.window((.|\r)*)];\s+/, /UIViewController\s\*rootViewController((.|\r)*)];\s+/, /rootViewController\.view\s+=\s+rootView;\s+/,
      /self.window.rootViewController\s+=\s+rootViewController;\s+/, /\[self.window\s+makeKeyAndVisible];\s+/
    ]

    toRemove.forEach(element => {
      if (element.test(applicationContent)) {
        applicationContent = applicationContent.replace(element, "");
      }
    });

    return applicationContent;
  }

  _bootstrapNavigation(applicationContent) {
    return applicationContent.replace(/RCTBridge.*];/, "[ReactNativeNavigation bootstrapWithDelegate:self launchOptions:launchOptions];");
  }

  _doesImportNavigation(applicationContent) {
    return /#import\s+\<ReactNativeNavigation\/ReactNativeNavigation.h>/.test(applicationContent);
  }
}

module.exports = ApplicationLinker;