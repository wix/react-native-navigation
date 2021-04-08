const redux = require('redux');
const merge = require('lodash/merge');
const get = require('lodash/get');

const initialState = {
  person: {
    name: 'no name',
  },
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case 'redux.MyStore.setName': {
      return merge({}, state, { person: { name: action.name } });
    }
    case 'redux.MyStore.setAge': {
      return merge({}, state, { person: { age: action.age } });
    }
    default: {
      return state;
    }
  }
};

const selectors = {
  getName(state) {
    return get(state, 'person.name');
  },

  getAge(state) {
    return state.person.age;
  },
};

const reduxStore = redux.createStore(reducer);

module.exports = {
  reduxStore,
  selectors,
};
