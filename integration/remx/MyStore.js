const remx = require('remx');

const state = remx.state({
  person: {
    name: 'no name',
  },
});

const setters = remx.setters({
  setName(newName) {
    state.person.name = newName;
  },

  setAge(age) {
    state.person.age = age;
  },
});

const getters = remx.getters({
  getName() {
    return state.person.name;
  },

  getAge() {
    return state.person.age;
  },
});

module.exports = {
  setters,
  getters,
};
