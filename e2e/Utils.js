module.exports = {
  elementByLabel: (label) => {
    return element(by.label(label));
  },
  elementById: (id) => {
    return element(by.id(id));
  }
};
