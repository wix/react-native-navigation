import remx from 'remx';

const state = remx.state({
  person: {
    name: 'no name',
  },
});

const setters = remx.setters({
  setName(newName: string) {
    state.person.name = newName;
  },

  setAge(age: number) {
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

export { setters, getters };
