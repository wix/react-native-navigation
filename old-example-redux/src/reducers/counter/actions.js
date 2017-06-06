import * as types from './actionTypes';

export function increment() {
  return {type: types.INCREMENT};
}

export function decrement() {
  return {type: types.DECREMENT};
}
