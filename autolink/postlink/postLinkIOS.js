// @ts-check
var AppDelegateLinker = require("./appDelegateLinker");
var PodfileLinker = require('./podfileLinker');

module.exports = () => {
  console.log("Running iOS postlink script\n");
  new AppDelegateLinker().link();
  new PodfileLinker().link();
}
