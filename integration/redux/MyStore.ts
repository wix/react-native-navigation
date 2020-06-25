import redux from 'redux';
import merge from 'lodash/merge';
import get from 'lodash/get';

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

export { reduxStore, selectors };
