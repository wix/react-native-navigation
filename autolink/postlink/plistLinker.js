// @ts-check
var fs = require('fs');
var path = require('./path');
var {logn, errorn} = require('./log');

const viewControllerBasedStatusBar = /<key>UIViewControllerBasedStatusBarAppearance<\/key>(\s+|\n+)<(.*)\/>/;

class plistLinker {
  constructor() {
    this.plistPath = path.plist;
  }

  link() {
    if (!this.plistPath) {
      errorn(
        '   info.plist not found! Does the file exist in the correct folder?\n   Please check the manual installation docs:\n   https://wix.github.io/react-native-navigation/docs/installing#native-installation'
      );
      return;
    }

    logn('Linking info.plist...');

    var plistContent = fs.readFileSync(this.plistPath, 'utf8');

    if (this._viewControllerBasedStatusBarDefined(plistContent)) {
      plistContent = this._updateViewControllerBasedStatusBar(plistContent);
    } else {
      plistContent = this._applyViewControllerBasedStatusBar(plistContent);
    }

    fs.writeFileSync(this.plistPath, plistContent);
  }

  _viewControllerBasedStatusBarDefined(content) {
    return viewControllerBasedStatusBar.test(content);
  }

  _applyViewControllerBasedStatusBar(content) {
    return content.replace(/<\/dict>\s<\/plist>/, '<key>UIViewControllerBasedStatusBarAppearance</key>\n<true/>\n<\/dict>\n<\/plist>')
  }

  _updateViewControllerBasedStatusBar(content) {
    return content.replace(viewControllerBasedStatusBar, '<key>UIViewControllerBasedStatusBarAppearance</key>\n<true/>')
  }
}

module.exports = plistLinker;
