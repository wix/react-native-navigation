// counter.store.js
import React from 'react';
import { observable, action } from 'mobx';

class CounterStore {
  @observable count = 0;

  @action.bound
  increment() {
    this.count += 1;
  }

  @action.bound
  decrement() {
    this.count -= 1;
  }
}

// Instantiate the counter store.
const counterStore = new CounterStore();
// Create a React Context with the counter store instance.
export const CounterStoreContext = React.createContext(counterStore);
