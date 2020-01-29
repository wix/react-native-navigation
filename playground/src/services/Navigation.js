const get = require('lodash/get');
const isString= require('lodash/isString');

const { stack, component } = require('../commons/Layouts');
const { Navigation } = require('react-native-navigation');

const push = (selfOrCompId, screen, options) => Navigation.push(compId(selfOrCompId), isString(screen) ? component(screen, options) : screen);

const pushExternalComponent = (self, name, passProps) => Navigation.push(self.props.componentId, {
  externalComponent: {
    name,
    passProps
  }
});

const pop = (selfOrCompId) => Navigation.pop(compId(selfOrCompId));

const showModal = (screen, options) => Navigation.showModal(isString(screen) ? stack(component(screen, options)) : screen);

const dismissModal = (selfOrCompId) => Navigation.dismissModal(compId(selfOrCompId));

const dismissAllModals = () => Navigation.dismissAllModals();

const showOverlay = (name, options) => Navigation.showOverlay(component(name, options));

const dismissOverlay = (name) => Navigation.dismissOverlay(name);

const popToRoot = (self) => Navigation.popToRoot(self.props.componentId);

const mergeOptions = (selfOrCompId, options) => Navigation.mergeOptions(compId(selfOrCompId), options);

const setStackRoot = (selfOrCompId, root) => Navigation.setStackRoot(compId(selfOrCompId), root);

const setRoot = (root) => Navigation.setRoot(root.root ? root : { root: component(root, {}) });

const compId = (selfOrCompId) => {
  return get(selfOrCompId, 'props.componentId', selfOrCompId);
}

const constants = Navigation.constants;

module.exports = {
  mergeOptions,
  updateProps: Navigation.updateProps.bind(Navigation),
  push,
  pushExternalComponent,
  pop,
  popToRoot,
  showModal,
  dismissModal,
  dismissAllModals,
  showOverlay,
  dismissOverlay,
  events: Navigation.events.bind(Navigation),
  popTo: Navigation.popTo.bind(Navigation),
  setDefaultOptions: Navigation.setDefaultOptions.bind(Navigation),
  setRoot,
  TouchablePreview: Navigation.TouchablePreview,
  setStackRoot,
  constants
}
