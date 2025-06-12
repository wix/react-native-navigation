var glob = require('glob');
var ignoreFolders = {
  ignore: ['node_modules/**', '**/build/**', '**/Build/**', '**/DerivedData/**', '**/*-tvOS*/**'],
};
var { warnn, infon, debugn } = require('./log');

exports.mainActivityKotlin = glob.sync('**/MainActivity.kt', ignoreFolders)[0];
var mainApplicationKotlin = glob.sync('**/MainApplication.kt', ignoreFolders)[0];
exports.mainApplicationKotlin = mainApplicationKotlin;
exports.rootGradle = mainApplicationKotlin.replace(/android\/app\/.*\.kt/, 'android/build.gradle');

var reactNativeVersion = require('../../../react-native/package.json').version;
infon('Found React Native version: ' + reactNativeVersion);
infon('Locating the AppDelegate.mm file ...');
exports.appDelegate = glob.sync(
  '**/AppDelegate.mm',
  ignoreFolders
)[0];

if (exports.appDelegate === undefined) {
  warnn('AppDelegate.mm file not found, looking for AppDelegate.swift ...');
  exports.appDelegate = glob.sync(
    '**/AppDelegate.swift',
    ignoreFolders
  )[0];

  if (exports.appDelegate !== undefined) {
    debugn('Found AppDelegate.swift');
  }

} else {
  debugn('Found AppDelegate.mm');
  exports.appDelegateHeader = glob.sync('**/AppDelegate.h', ignoreFolders)[0];
}

exports.podFile = glob.sync('**/Podfile', ignoreFolders)[0];
exports.plist = glob.sync('**/info.plist', ignoreFolders)[0];
