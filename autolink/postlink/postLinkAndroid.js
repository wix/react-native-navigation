// @ts-check
var { infon } = require("./log");
var AppLinker = require("./applicationLinker");
var ActivityLinker = require("./activityLinker");
var GradleLinker = require("./gradleLinker");

module.exports = () => {
  console.log("Running android postlink script\n");
  new AppLinker().link();
  new ActivityLinker().link();
  new GradleLinker().link();
}
