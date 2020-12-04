// @ts-check
var { infon } = require('./log');
var ApplicationLinker = require('./applicationLinker');
var ActivityLinker = require('./activityLinker');
var GradleLinker = require('./gradleLinker');

module.exports = () => {
  infon('\nRunning Android postlink script.\n');
  new ApplicationLinker().link();
  new ActivityLinker().link();
  new GradleLinker().link();
};
