import { Options, Layout, OptionsModalPresentationStyle } from 'react-native-navigation';
import isString from 'lodash/isString';
import isArray from 'lodash/isArray';
import { Platform } from 'react-native';

type CompIdOrLayout = string | Layout;

const stack = (rawChildren: CompIdOrLayout | CompIdOrLayout[], id?: string): Layout => {
  const childrenArray = isArray(rawChildren) ? rawChildren : [rawChildren];
  const children = childrenArray.map((child) => component(child));
  return { stack: { children, id } };
};

const component = <P = {}>(
  compIdOrLayout: CompIdOrLayout,
  options?: Options,
  passProps?: P
): Layout<P> => {
  return isString(compIdOrLayout)
    ? { component: { name: compIdOrLayout, options, passProps } }
    : (compIdOrLayout as Layout<P>);
};

const sheet = <P = {}>(
  compIdOrLayout: CompIdOrLayout,
  options?: Options,
  passProps?: P
): Layout<P> => {
  return isString(compIdOrLayout)
    ? {
        sheet: {
          name: compIdOrLayout,
          passProps,
          options: {
            ...options,
            layout: {
              componentBackgroundColor: '#FFF',
              ...options?.layout,
            },
            modalPresentationStyle:
              Platform.OS === 'android'
                ? OptionsModalPresentationStyle.overCurrentContext
                : OptionsModalPresentationStyle.overFullScreen,
            animations: {
              showModal: {
                enter: { enabled: false },
                exit: { enabled: false },
              },
              dismissModal: {
                enter: { enabled: false },
                exit: { enabled: false },
              },
            },
          },
        },
      }
    : (compIdOrLayout as Layout<P>);
};

export { stack, component, sheet };
