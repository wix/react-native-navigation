import { Options, Layout } from 'react-native-navigation';
import isString from 'lodash/isString';
import isArray from 'lodash/isArray';

type CompIdOrLayout = string | Layout;

const stack = (rawChildren: CompIdOrLayout | CompIdOrLayout[], id?: string): Layout => {
  const childrenArray = isArray(rawChildren) ? rawChildren : [rawChildren];
  const children = childrenArray.map((child) => component(child));
  return { stack: { children, id } };
};

const component = (
  compIdOrLayout: CompIdOrLayout,
  options?: Options,
  passProps?: object
): Layout => {
  return isString(compIdOrLayout)
    ? { component: { name: compIdOrLayout, options, passProps } }
    : compIdOrLayout;
};

export { stack, component };
