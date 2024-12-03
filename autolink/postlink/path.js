var glob = require('glob');
var ignoreFolders = {
  ignore: ['node_modules/**', '**/build/**', '**/Build/**', '**/DerivedData/**', '**/*-tvOS*/**'],
};

exports.mainActivityJava = glob.sync('**/MainActivity.{java,kt}', ignoreFolders)[0];
var mainApplicationJava = glob.sync('**/MainApplication.{java,kt}', ignoreFolders)[0];
exports.mainApplicationJava = mainApplicationJava;
exports.rootGradle = mainApplicationJava.replace(
  /android\/app\/.*\.(java|kt)/,
  'android/build.gradle'
);

var reactNativeVersion = require('../../../react-native/package.json').version;
exports.appDelegate = glob.sync(
  reactNativeVersion < '0.68.0' ? '**/AppDelegate.m' : '**/AppDelegate.mm',
  ignoreFolders
)[0];
exports.appDelegateHeader = glob.sync('**/AppDelegate.h', ignoreFolders)[0];
exports.podFile = glob.sync('**/Podfile', ignoreFolders)[0];
exports.plist = glob.sync('**/info.plist', ignoreFolders)[0];
