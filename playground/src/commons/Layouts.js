const isString = require('lodash/isString');
const isArray = require('lodash/isArray');

const stack = (rawChildren, id) => {
  const childrenArray = isArray(rawChildren) ? rawChildren : [rawChildren];
  const children = childrenArray.map(child => component(child));
  return { stack: { children, id } };
}

const component = (component, options, passProps) => {
  return isString(component) ? { component: { name: component, options, passProps } } : component;
}

module.exports = {
  stack,
  component
}