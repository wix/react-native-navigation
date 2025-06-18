// @ts-check
var path = require('./path');
var fs = require('fs');
var { errorn, warnn, logn, infon, debugn } = require('./log');

class ActivityLinker {
  constructor() {
    this.activityPath = path.mainActivityKotlin
    this.extendNavigationActivitySuccess = false;
    this.removeGetMainComponentNameSuccess = false;
    this.removeCreateReactActivityDelegate = false;
  }

  link() {
    if (!this.activityPath) {
      errorn(
        '   MainActivity.kt not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#2-update-mainactivityjava'
      );
      return;
    }

    logn('Linking MainActivity...');

    var activityContent = fs.readFileSync(this.activityPath, 'utf8');

    try {
      activityContent = this._extendNavigationActivity(activityContent);
      this.extendNavigationActivitySuccess = true;
    } catch (e) {
      errorn('   ' + e.message);
    }

    try {
      activityContent = this._removeGetMainComponentName(activityContent);
      this.removeGetMainComponentNameSuccess = true;
    } catch (e) {
      errorn('   ' + e.message);
    }

    activityContent = this._removeCreateReactActivityDelegate(activityContent);

    fs.writeFileSync(this.activityPath, activityContent);
    if (this.extendNavigationActivitySuccess && this.removeGetMainComponentNameSuccess) {
      infon('MainActivity.kt linked successfully!\n');
    } else if (!this.extendNavigationActivitySuccess && !this.removeGetMainComponentNameSuccess) {
      errorn(
        'MainActivity.kt was not linked. Please check the logs above for more information and proceed with manual linking of the MainActivity file in Android:\nhttps://wix.github.io/react-native-navigation/docs/installing#2-update-mainactivityjava'
      );
    } else {
      warnn(
        'MainActivity.kt was only partially linked. Please check the logs above for more information and proceed with manual linking for the failed steps:\nhttps://wix.github.io/react-native-navigation/docs/installing#2-update-mainactivityjava'
      );
    }
  }

  _removeGetMainComponentName(contents) {
    var match = /(\/\*\*[\s\S]*?\*\/\s*)?override\s+fun\s+getMainComponentName\s*\(\s*\)\s*:\s*String\s*(\{\s*return[\s\S]*?\}|=[\s\S]*?)/.exec(
      contents
    );
    if (match) {
      debugn('   Removing getMainComponentName function');
      return contents.replace(
        /(\/\*\*[\s\S]*?\*\/\s*)?override\s+fun\s+getMainComponentName\s*\(\s*\)\s*:\s*String\s*(\{\s*return[\s\S]*?\}|=[\s\S]*?(?=\n\s*(?:\/\*\*|override|fun|\}|$)))/,
        ''
      );
    }
    warnn('   getMainComponentName function was not found.');
    return contents;
  }

  _extendNavigationActivity(activityContent) {
    if (this._hasAlreadyExtendNavigationActivity(activityContent)) {
      warnn('   MainActivity already extends NavigationActivity');
      return activityContent;
    }

    if (this._doesActivityExtendReactActivity(activityContent)) {
      debugn('   Extending NavigationActivity');
      return activityContent
        .replace(/:\s*ReactActivity\(\)\s*/, ': NavigationActivity() ')
        .replace(
          'import com.facebook.react.ReactActivity',
          'import com.reactnativenavigation.NavigationActivity'
        );
    }

    throw new Error(
      'MainActivity was not successfully replaced. Please check the documentation and proceed manually.'
    );
  }

  _doesActivityExtendReactActivity(activityContent) {
    return /class\s+MainActivity\s*:\s*ReactActivity\(\)\s*/.test(activityContent);
  }

  _hasAlreadyExtendNavigationActivity(activityContent) {
    return /class\s+MainActivity\s*:\s*NavigationActivity\(\)\s*/.test(activityContent);
  }

  _removeCreateReactActivityDelegate(activityContent) {
    if (this._hasCreateReactActivityDelegate(activityContent)) {
      debugn('   Removing createReactActivityDelegate function');
      return activityContent.replace(
        /(\/\*\*[\s\S]*?\*\/\s*)?override\s+fun\s+createReactActivityDelegate\s*\(\s*\)\s*:\s*ReactActivityDelegate\s*(\{\s*return[\s\S]*?\}|=[\s\S]*?(?=\n\s*(?:\/\*\*|override|fun|\}|$)))/,
        ''
      );
    } else {
      warnn('   createReactActivityDelegate is already not defined in MainActivity');
      return activityContent;
    }
  }

  _hasCreateReactActivityDelegate(activityContent) {
    return /(\/\*\*[\s\S]*?\*\/\s*)?override\s+fun\s+createReactActivityDelegate\s*\(\s*\)\s*:\s*ReactActivityDelegate\s*(\{\s*return[\s\S]*?\}|=[\s\S]*?)/.test(
      activityContent
    );
  }
}

module.exports = ActivityLinker;
