// @ts-check
var path = require('./path');
var fs = require('fs');
var { warnn, errorn, logn, infon, debugn } = require('./log');
var { insertString } = require('./stringUtils');
var DEFAULT_KOTLIN_VERSION = '1.4.31';
// This should be the minSdkVersion required for RNN.
var DEFAULT_MIN_SDK_VERSION = 21;

class GradleLinker {
  constructor() {
    this.gradlePath = path.rootGradle;
    this.setKlotinVersionSuccess = false;
    this.setKotlinPluginDependencySuccess = false;
    this.setMinSdkVersionSuccess = false;
  }

  link() {
    if (!this.gradlePath) {
      errorn(
        'Root build.gradle not found! Does the file exist in the correct folder?\n   Please check the manual installation docs.'
      );
      return;
    }

    logn('Linking root build.gradle...');
    var contents = fs.readFileSync(this.gradlePath, 'utf8');

    try {
      contents = this._setKotlinVersion(contents);
      this.setKlotinVersionSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }
    try {
      contents = this._setKotlinPluginDependency(contents);
      this.setKotlinPluginDependencySuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }
    try {
      contents = this._setMinSdkVersion(contents);
      this.setMinSdkVersionSuccess = true;
    } catch (e) {
      errorn('   ' + e);
    }

    fs.writeFileSync(this.gradlePath, contents);

    if (
      this.setKlotinVersionSuccess &&
      this.setKotlinPluginDependencySuccess &&
      this.setMinSdkVersionSuccess
    ) {
      infon('Root build.gradle linked successfully!\n');
    } else if (
      !this.setKlotinVersionSuccess &&
      !this.setKotlinPluginDependencySuccess &&
      !this.setMinSdkVersionSuccess
    ) {
      errorn(
        'Root build.gradle link failed. Please review the information above and complete the necessary steps manually by following the instructions on https://wix.github.io/react-native-navigation/docs/installing#1-update-androidbuildgradle\n'
      );
    } else {
      warnn(
        'Root build.gradle link partially succeeded. Please review the information above and complete the necessary steps manually by following the instructions on https://wix.github.io/react-native-navigation/docs/installing#1-update-androidbuildgradle\n'
      );
    }
  }

  _setKotlinPluginDependency(contents) {
    if (this._isKotlinPluginDeclared(contents)) {
      warnn('   Kotlin plugin already declared');
      return contents;
    }

    var match = /classpath\s*\(*["']com\.android\.tools\.build:gradle:/.exec(contents);
    if (match) {
      debugn('   Adding Kotlin plugin');
      return insertString(
        contents,
        match.index,
        `classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${DEFAULT_KOTLIN_VERSION}"\n        `
      );
    } else {
      throw new Error('   Could not add kotlin plugin dependency');
    }
  }

  _setKotlinVersion(contents) {
    if (this._isKotlinVersionSpecified(contents)) {
      warnn('   Kotlin version already specified');
    } else {
      var kotlinVersion = this._getKotlinVersion(contents);
      if (this._hasExtensionVariablesBlock(contents)) {
        debugn('   Adding RNNKotlinVersion to extension block');
        return contents.replace(/ext\s*{/, `ext {\n        RNNKotlinVersion = ${kotlinVersion}`);
      } else {
        debugn('   Adding RNNKotlinVersion extension variable');
        return contents.replace(
          /buildscript\s*{/,
          `buildscript {\n    ext.RNNKotlinVersion = ${kotlinVersion}`
        );
      }
    }
    return contents;
  }

  /**
   * Check the current minSdkVersion specified and if it's lower than
   * the required version, set it to the required version otherwise leave as it is.
   */
  _setMinSdkVersion(contents) {
    var minSdkVersion = this._getMinSdkVersion(contents);
    // If user entered minSdkVersion is lower than the default, set it to default.
    if (minSdkVersion < DEFAULT_MIN_SDK_VERSION) {
      debugn(`   Updating minSdkVersion to ${DEFAULT_MIN_SDK_VERSION}`);
      return contents.replace(
        /minSdkVersion\s{0,}=\s{0,}\d*/,
        `minSdkVersion = ${DEFAULT_MIN_SDK_VERSION}`
      );
    }

    warnn(`   Already specified minSdkVersion ${minSdkVersion}`);
    return contents.replace(/minSdkVersion\s{0,}=\s{0,}\d*/, `minSdkVersion = ${minSdkVersion}`);
  }

  /**
   * @param { string } contents
   */
  _getKotlinVersion(contents) {
    var hardCodedVersion = contents.match(/(?<=kotlin-gradle-plugin:)\$*[\d\.]{3,}/);
    if (hardCodedVersion && hardCodedVersion.length > 0) {
      return `"${hardCodedVersion[0]}"`;
    }
    var extensionVariableVersion = contents.match(/(?<=kotlin-gradle-plugin:)\$*[a-zA-Z\d\.]*/);
    if (extensionVariableVersion && extensionVariableVersion.length > 0) {
      return extensionVariableVersion[0].replace('$', '');
    }
    return `"${DEFAULT_KOTLIN_VERSION}"`;
  }

  /**
   * Get the minSdkVersion value.
   * @param { string } contents
   */
  _getMinSdkVersion(contents) {
    var minSdkVersion = contents.match(/minSdkVersion\s{0,}=\s{0,}(\d*)/);

    if (minSdkVersion && minSdkVersion[1]) {
      // It'd be something like 16 for a fresh React Native project.
      return +minSdkVersion[1];
    }

    return DEFAULT_MIN_SDK_VERSION;
  }

  /**
   * @param {string} contents
   */
  _hasExtensionVariablesBlock(contents) {
    return /ext\s*{/.test(contents);
  }

  /**
   * @param {string} contents
   */
  _isKotlinVersionSpecified(contents) {
    return /RNNKotlinVersion/.test(contents);
  }

  /**
   * @param {string} contents
   */
  _isKotlinPluginDeclared(contents) {
    return /org.jetbrains.kotlin:kotlin-gradle-plugin:/.test(contents);
  }
}

module.exports = GradleLinker;
