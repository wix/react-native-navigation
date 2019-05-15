const { isString, get } = require('lodash');
const { stack, component } = require('../commons/Layouts');
const { Navigation } = require('react-native-navigation');

const push = (selfOrCompId, screen) => Navigation.push(compId(selfOrCompId), isString(screen) ? component(screen) : screen);

const pushExternalComponent = (self, name, passProps) => Navigation.push(self.props.componentId, {
  externalComponent: {
    name,
    passProps
  }
});

const pop = (selfOrCompId) => Navigation.pop(compId(selfOrCompId));

const showModal = (screen) => Navigation.showModal(isString(screen) ? stack(screen) : screen);

const dismissModal = (selfOrCompId) => Navigation.dismissModal(compId(selfOrCompId));

const dismissAllModals = () => Navigation.dismissAllModals();

const showOverlay = (name, options) => Navigation.showOverlay(component(name, options));

const dismissOverlay = (name) => Navigation.dismissOverlay(name);

const popToRoot = (self) => Navigation.popToRoot(self.props.componentId);

const mergeOptions = (selfOrCompId, options) => Navigation.mergeOptions(compId(selfOrCompId), options);

const setStackRoot = (self, root) => Navigation.setStackRoot(self.props.componentId, root)

const compId = (selfOrCompId) => {
  return get(selfOrCompId, 'props.componentId', selfOrCompId);
}

module.exports = {
  mergeOptions,
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
  setRoot: Navigation.setRoot.bind(Navigation),
  TouchablePreview: Navigation.TouchablePreview,
  setStackRoot
}
