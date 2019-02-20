const { isString, isArray } = require('lodash');

const stack = (rawChildren, id) => {
  const childrenArray = isArray(rawChildren) ? rawChildren : [rawChildren];
  const children = childrenArray.map(child => component(child));
  return { stack: { children, id } };
}

const component = (component, options) => {
  return isString(component) ? { component: { name: component, options } } : component;
}

module.exports = {
  stack,
  component
}