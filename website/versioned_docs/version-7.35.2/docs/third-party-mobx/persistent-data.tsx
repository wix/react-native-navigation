//counter.store.js
import React from 'react';
import { makeObservable, observable, action } from 'mobx';
import { persist } from 'mobx-persist'; // add this.

class CounterStore {
  constructor() {
    makeObservable(this)
  }
  
  @persist @observable count = 0; // count is now persistent.


  @action.bound
  increment() {
    this.count += 1;
  }

  @action.bound
  decrement() {
    this.count -= 1;
  }
}

export const counterStore = new CounterStore(); // You need to export the counterStore instance.
export const CounterStoreContext = React.createContext(counterStore);
export const useCounterStore = () => React.useContext(CounterStoreContext)

// index.js
import { Navigation } from 'react-native-navigation';
import AsyncStorage from '@react-native-community/async-storage';
import { create } from 'mobx-persist';
import { counterStore } from './counter.store'; // import the counter store instance.

// Create a store hydration function.
async function hydrateStores() {
  const hydrate = create({ storage: AsyncStorage });
  await hydrate('CounterStore', counterStore);
}

Navigation.events().registerAppLaunchedListener(() => {
  hydrateStores().then(() => {
    // ...register screens and start the app.
  });
});
