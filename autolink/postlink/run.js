// @ts-check
var { infon } = require("./log");
var postLinkAndroid = require('./postLinkAndroid');
var postLinkIOS = require('./postLinkIOS');

postLinkAndroid();
postLinkIOS();
infon("react-native-navigation linked successfully");