var glob = require("glob");
var ignoreFolders = { ignore: ["node_modules/**", "**/build/**"] };

var manifestPath = glob.sync("**/AndroidManifest.xml", ignoreFolders)[0];

exports.mainActivityJava = glob.sync("**/MainActivity.java", ignoreFolders)[0];
exports.mainActivityKotlin = glob.sync("**/MainActivity.kt", ignoreFolders)[0];
var mainApplicationJava = glob.sync("**/MainApplication.java", ignoreFolders)[0];
exports.mainApplicationJava = mainApplicationJava;
exports.rootGradle = mainApplicationJava.replace(/android\/app\/.*\.java/, "android/build.gradle");;

exports.appDelegate = glob.sync("**/AppDelegate.m", ignoreFolders)[0];
exports.podFile = glob.sync("**/Podfile", ignoreFolders)[0]