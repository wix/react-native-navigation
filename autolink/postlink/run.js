// @ts-check
var { infon, warnn } = require("./log");
var postLinkAndroid = require("./postLinkAndroid");
var postLinkIOS = require("./postLinkIOS");

postLinkAndroid();
postLinkIOS();

infon("\nReact Native Navigation link is completed. Check the logs above for more information.\n");
warnn("   If any of the steps failed, check the installation docs and go through the necessary steps manually:");
warnn("   https://wix.github.io/react-native-navigation/docs/installing#manual-installation\n");
infon("When you're done, don't forget to update the index.js file as mentioned in docs!\n");
infon("Thank you for using React Native Navigation!\n\n");
