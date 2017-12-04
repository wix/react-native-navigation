module.exports = {
  elementByLabel: (label) => {
    return element(by.label(label));
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
  }
};
