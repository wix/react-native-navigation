// @ts-check
var fs = require('fs');
var path = require('./path');
var { warnn, logn, infon, debugn, errorn } = require('./log');

class AppDelegateLinker {
  constructor() {
    this.appDelegatePath = path.appDelegate;
    this.sucess = true;
  }

  link() {
    if (!this.appDelegatePath) {
      errorn(
        '   AppDelegate not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
      return;
    }

    logn('Linking AppDelegate...');

    var appDelegateContents = fs.readFileSync(this.appDelegatePath, 'utf8');

    appDelegateContents = this._importNavigation(appDelegateContents);

    appDelegateContents = this._bootstrapNavigation(appDelegateContents);

    appDelegateContents = this._extraModulesForBridge(appDelegateContents);

    fs.writeFileSync(this.appDelegatePath, appDelegateContents);

    if (this.sucess) {
      infon('AppDelegate linked successfully!\n');
    } else {
      warnn(
        'AppDelegate was partially linked, please check the details above and proceed with the manual installation documentation to complete the linking process.!\n'
      );
    }
  }

  _importNavigation(content) {
    if (!this._doesImportNavigation(content)) {
      debugn('   Importing ReactNativeNavigation.h');
      return content.replace(
        /#import\s+"AppDelegate.h"/,
        '#import "AppDelegate.h"\n#import <ReactNativeNavigation/ReactNativeNavigation.h>\n' +
          '#import <React/RCTBridge.h>'
      );
    }
    this.sucess = false;
    warnn('   AppDelegate already imports ReactNativeNavigation.h');
    return content;
  }

  _bootstrapNavigation(content) {
    if (this._doesBootstrapNavigation(content)) {
      warnn('   Navigation Bootstrap already present.');
      this.sucess = false;
      return content;
    }

    debugn('   Bootstrapping Navigation');
    return content.replace(
      /self.moduleName =[\s\S]*?\[super application:application didFinishLaunchingWithOptions:launchOptions\];/,
      'RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];\n' +
        '  [ReactNativeNavigation bootstrapWithBridge:bridge];\n' +
        '  return YES;'
    );
  }

  _doesBootstrapNavigation(content) {
    return /ReactNativeNavigation\s+bootstrap/.test(content);
  }

  _extraModulesForBridge(content) {
    if (this._doesImplementsRNNExtraModulesForBridge(content)) {
      warnn('   extraModulesForBridge already present.');
      this.sucess = false;
      return content;
    } else if (this._doesImplementsExtraModulesForBridge(content)) {
      throw new Error(
        'extraModulesForBridge implemented for a different module and needs manual linking. Check the manual installation docs to verify that everything is properly setup:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
    }

    debugn('   Implementing extraModulesForBridge');
    return content.replace(
      /-.*\(NSURL.*\*\)sourceURLForBridge:\(RCTBridge.*\*\)bridge/,
      '- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {\n\
  return [ReactNativeNavigation extraModulesForBridge:bridge];\n\
}\n\
\n\
- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge'
    );
  }

  _doesImplementsExtraModulesForBridge(content) {
    return /-.*\(NSArray.*\*\)extraModulesForBridge:\(RCTBridge.*\*\)bridge/.test(content);
  }

  _doesImplementsRNNExtraModulesForBridge(content) {
    return /ReactNativeNavigation\s+extraModulesForBridge/.test(content);
  }

  _doesImportNavigation(content) {
    return /#import\s+\<ReactNativeNavigation\/ReactNativeNavigation.h>/.test(content);
  }
}

module.exports = AppDelegateLinker;
