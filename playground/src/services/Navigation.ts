import { Navigation, Layout, Options, LayoutRoot } from 'react-native-navigation';
import get from 'lodash/get';
import isString from 'lodash/isString';
import { stack, component } from '../commons/Layouts';

type ComponentIdProp = { props: { componentId: string } };
type SelfOrCompId = string | ComponentIdProp;
type CompIdOrLayout<P = {}> = string | Layout<P>;

const push = <P>(selfOrCompId: SelfOrCompId, screen: CompIdOrLayout<P>, options?: Options) =>
  Navigation.push<P>(
    compId(selfOrCompId),
    isString(screen) ? component<P>(screen, options) : (screen as Layout<P>)
  );

const pushExternalComponent = (
  self: { props: { componentId: string } },
  name: string | number,
  passProps?: object
) =>
  Navigation.push(self.props.componentId, {
    externalComponent: {
      name,
      passProps,
    },
  });

const pop = (selfOrCompId: SelfOrCompId, mergeOptions?: Options) =>
  Navigation.pop(compId(selfOrCompId), mergeOptions);

const showModal = (screen: string | Layout, options?: Options) =>
  Navigation.showModal(isString(screen) ? stack(component(screen, options)) : screen);

const dismissModal = (selfOrCompId: SelfOrCompId, mergeOptions?: Options) =>
  Navigation.dismissModal(compId(selfOrCompId), mergeOptions);

const dismissAllModals = () => Navigation.dismissAllModals();

const showOverlay = (name: CompIdOrLayout, options?: Options, passProps?: any) =>
  Navigation.showOverlay(component(name, options, passProps));

const dismissOverlay = (compId: string) => Navigation.dismissOverlay(compId);

const dismissAllOverlays = () => Navigation.dismissAllOverlays();

const popToRoot = (self: ComponentIdProp) => Navigation.popToRoot(self.props.componentId);

const mergeOptions = (selfOrCompId: SelfOrCompId, options: Options) =>
  Navigation.mergeOptions(compId(selfOrCompId), options);

const setStackRoot = (selfOrCompId: SelfOrCompId, root: Layout | Layout[]) =>
  Navigation.setStackRoot(compId(selfOrCompId), root);

const setRoot = (root: LayoutRoot | CompIdOrLayout) => {
  // If provided root is not a string and contain `root` property, it's a LayoutRoot.
  if (!isString(root) && !!get(root, 'root')) {
    return Navigation.setRoot(root as LayoutRoot);
  }

  return Navigation.setRoot({ root: component(root as CompIdOrLayout) });
};

const compId = (selfOrCompId: SelfOrCompId): string => {
  return get(selfOrCompId, 'props.componentId', selfOrCompId);
};

const constants = Navigation.constants;

const CustomNavigation = {
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
  dismissAllOverlays,
  events: Navigation.events.bind(Navigation),
  popTo: Navigation.popTo.bind(Navigation),
  setDefaultOptions: Navigation.setDefaultOptions.bind(Navigation),
  setRoot,
  TouchablePreview: Navigation.TouchablePreview,
  setStackRoot,
  constants,
};

export default CustomNavigation;
