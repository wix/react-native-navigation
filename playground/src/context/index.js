const React = require('react');
const titleContext = React.createContext('Default title from Context');

module.exports = {
  TitleContext: titleContext,
  Context: React.createContext('Default value from Context')
}
