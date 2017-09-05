const _ = require('lodash');
const remx = require('remx');

const state = remx.state({
  person: {
    name: 'no name'
  }
});

const setters = remx.setters({
  setName(newName) {
    state.person.name = newName;
  },

  setAge(age) {
    state.person.age = age;
  }
});

const getters = remx.getters({
  getName() {
    return _.get(state, ['person', 'name']);
  },

  getAge() {
    return _(state).get('person.age');
  }
});

module.exports = {
  setters,
  getters
};
