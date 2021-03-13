// @ts-check
var {infon} = require('./log');
var AppDelegateLinker = require('./appDelegateLinker');
var PodfileLinker = require('./podfileLinker');
var PlistLinker = require('./plistLinker');

module.exports = () => {
  infon('Running iOS postlink script.\n');
  new AppDelegateLinker().link();
  new PodfileLinker().link();
  new PlistLinker().link();
};
