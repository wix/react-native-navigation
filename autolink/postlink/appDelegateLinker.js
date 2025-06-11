// @ts-check
var fs = require('fs');
var path = require('./path');
var nodePath = require('path');
var { warnn, logn, infon, debugn, errorn } = require('./log');

class AppDelegateLinker {
  constructor() {
    this.appDelegatePath = path.appDelegate;
    this.appDelegateHeaderPath = path.appDelegateHeader;
    this.removeUnneededImportsSuccess = false;
    this.removeApplicationLaunchContentSuccess = false;
  }

  link() {
    if (!this.appDelegatePath) {
      errorn(
        '   AppDelegate not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
      return;
    }

    logn('Linking AppDelegate...');

    // New flow for Swift
    if (nodePath.extname(this.appDelegatePath) === '.swift') {
      debugn('Entering Swift flow ...');
      var appDelegateContents = fs.readFileSync(this.appDelegatePath, 'utf8');
      appDelegateContents = this._extendRNNAppDelegateSwift(appDelegateContents);
      fs.writeFileSync(this.appDelegatePath, appDelegateContents);
      this.removeUnneededImportsSuccess = true
      this.removeApplicationLaunchContentSuccess = true

    } else { // Old flow for Objective-C
      debugn('Entering Objective-C flow ...');
      var appDelegateContents = fs.readFileSync(this.appDelegatePath, 'utf8');

      if (this.appDelegateHeaderPath) {
        var appDelegateHeaderContents = fs.readFileSync(this.appDelegateHeaderPath, 'utf8');
        appDelegateHeaderContents = this._extendRNNAppDelegate(appDelegateHeaderContents);
        fs.writeFileSync(this.appDelegateHeaderPath, appDelegateHeaderContents);
      }

      try {
        appDelegateContents = this._removeUnneededImports(appDelegateContents);
        this.removeUnneededImportsSuccess = true;
      } catch (e) {
        errorn('   ' + e.message);
      }

      appDelegateContents = this._importNavigation(appDelegateContents);

      appDelegateContents = this._bootstrapNavigation(appDelegateContents);

      try {
        appDelegateContents = this._removeApplicationLaunchContent(appDelegateContents);
        this.removeApplicationLaunchContentSuccess = true;
      } catch (e) {
        errorn('   ' + e.message);
      }

      fs.writeFileSync(this.appDelegatePath, appDelegateContents);
    }

    if (this.removeUnneededImportsSuccess && this.removeApplicationLaunchContentSuccess) {
      infon('AppDelegate linked successfully!\n');
    } else {
      warnn(
        'AppDelegate was partially linked, please check the details above and proceed with the manual installation documentation to complete the linking process.!\n'
      );
    }
  }

  _removeUnneededImports(content) {
    debugn('   Removing Unneeded imports');

    const unneededImports = [/#import\s+\<React\/RCTRootView.h>\s/];
    let elementsRemovedCount = 0;

    unneededImports.forEach((unneededImport) => {
      if (unneededImport.test(content)) {
        content = content.replace(unneededImport, '');
        elementsRemovedCount++;
      }
    });

    if (unneededImports.length === elementsRemovedCount) {
      debugn('   All imports have been removed');
    } else if (elementsRemovedCount === 0) {
      warnn(
        '   No imports could be removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
    } else {
      throw new Error(
        'Some imports were removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
    }

    return content;
  }

  _extendRNNAppDelegate(content) {
    return content
      .replace(
        /#import*.<RCTAppDelegate.h>/,
        '#import "RNNAppDelegate.h"'
      )
      .replace(
        /:*.RCTAppDelegate/,
        ': RNNAppDelegate'
      )
  }

  _importNavigation(content) {
    if (!this._doesImportNavigation(content)) {
      debugn('   Importing ReactNativeNavigation.h');
      return content.replace(
        /#import\s+"AppDelegate.h"/,
        '#import "AppDelegate.h"\n#import <ReactNativeNavigation/ReactNativeNavigation.h>'
      );
    }

    warnn('   AppDelegate already imports ReactNativeNavigation.h');
    return content;
  }

  _bootstrapNavigation(content) {
    if (this._doesBootstrapNavigation(content)) {
      warnn('   Navigation Bootstrap already present.');
      return content;
    }

    debugn('   Bootstrapping Navigation !!!!');
    return content
      .replace(
        /RCTBridge.*];/,
        'RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];\n' +
        '[ReactNativeNavigation bootstrapWithBridge:bridge];'
      )
      .replace(
        /self.moduleName.*;(.|\n)*@{};\n?/,
        ''
      );
  }

  _doesBootstrapNavigation(content) {
    return /ReactNativeNavigation\s+bootstrap/.test(content);
  }

  _removeApplicationLaunchContent(content) {
    debugn('   Removing Application launch content');

    const toRemove = [
      /RCTRootView\s+\*rootView((.|\r|\s)*?)];\s+/,
      /UIView \*rootView = RCTAppSetupDefaultRootView\(bridge, @".*", nil\);/,
      /if \(@available\(iOS 13\.0, \*\)\)\s{\s+ rootView.backgroundColor((.|\r)*)];\s+}\s+else {[^}]*}/,
      /self.window((.|\r)*)];\s+/,
      /UIViewController\s\*rootViewController((.|\r)*)];\s+/,
      /rootViewController\.view\s+=\s+rootView;\s+/,
      /self.window.rootViewController\s+=\s+rootViewController;\s+/,
      /\[self.window\s+makeKeyAndVisible];\s+/,
      // Added from RN 0.69
      /NSDictionary\s+\*initProps\s+=\s+\[self prepareInitialProps];\s+/,
      /UIView \*rootView = RCTAppSetupDefaultRootView\(bridge, @".*", initProps\);/,
    ];

    let elementsRemovedCount = 0;

    toRemove.forEach((element) => {
      if (element.test(content)) {
        content = content.replace(element, '');
        elementsRemovedCount++;
      }
    });

    if (toRemove.length === elementsRemovedCount) {
      debugn('   Application Launch content has been removed');
    } else if (elementsRemovedCount === 0) {
      warnn(
        '   No elements could be removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
    } else {
      warnn(
        'Some elements were removed. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
    }

    return content;
  }

  _doesImportNavigation(content) {
    return /#import\s+\<ReactNativeNavigation\/ReactNativeNavigation.h>/.test(content);
  }

  // SWIFT implementation
  _extendRNNAppDelegateSwift(content) {
    return content
      .replace(
        /import React_RCTAppDelegate/,
        'import ReactNativeNavigation'
      )
      .replace(
        /class AppDelegate: RCTAppDelegate/,
        'class AppDelegate: RNNAppDelegate'
      )
  }
}

module.exports = AppDelegateLinker;
