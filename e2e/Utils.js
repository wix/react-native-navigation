const exec = require('shell-utils').exec;

module.exports = {
  elementByLabel: (label) => {
    return element(by.text(label));
  },
  elementById: (id) => {
    return element(by.id(id));
  },
  tapBackIos: () => {
    try {
      return element(by.traits(['button']).and(by.label('Back'))).atIndex(0).tap();
    } catch (err) {
      return element(by.type('_UIModernBarButton').and(by.label('Back'))).tap();
    }
  },
  sleep: ms => new Promise(res => setTimeout(res, ms))
};
