// @ts-check
var path = require('./path');
var fs = require('fs');
var { logn, debugn, infon, errorn, warnn } = require('./log');

class PodfileLinker {
  constructor() {
    this.podfilePath = path.podFile;
  }

  link() {
    if (!this.podfilePath) {
      errorn(
        'Podfile not found! Does the file exist in the correct folder?\n   Please check the manual installation docs.'
      );
      return;
    }

    logn('Updating Podfile...');
    var podfileContent = fs.readFileSync(this.podfilePath, 'utf8');

    podfileContent = this._removeRNNPodLink(podfileContent);
    podfileContent = this._setMinimumIOSVersion(podfileContent);

    fs.writeFileSync(this.podfilePath, podfileContent);
  }

  /**
   * Sets the minimum iOS version to iOS 11.0 which is the minimum version required by the library.
   */
  _setMinimumIOSVersion(contents) {
    const platformDefinition = contents.match(/platform :ios, '.*'/);
    const minimumIOSVersion = contents.match(/(?<=platform\s:ios,\s(?:"|'))(.*)(?=(?:"|'))/);

    if (parseFloat(minimumIOSVersion) < 11) {
      debugn('   Bump minumum iOS version to iOS 11.0');
      return contents.replace(platformDefinition, "platform :ios, '11.0'");
    }

    return contents;
  }

  /**
   * Removes the RNN pod added by react-native link script.
   */
  _removeRNNPodLink(contents) {
    const rnnPodLink = contents.match(/\s+.*pod 'ReactNativeNavigation'.+react-native-navigation'/);

    if (!rnnPodLink) {
      warnn('   RNN Pod has not been added to Podfile');
      return contents;
    }

    debugn('   Removing RNN Pod from Podfile');
    return contents.replace(rnnPodLink, '');
  }
}

module.exports = PodfileLinker;
