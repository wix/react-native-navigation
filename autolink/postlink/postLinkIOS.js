// @ts-check
var AppDelegateLinker = require("./appDelegateLinker");

module.exports = () => {
  console.log("Running iOS postlink script\n");
  new AppDelegateLinker().link();
}
