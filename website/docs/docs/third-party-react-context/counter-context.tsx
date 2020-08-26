// CounterContext.js
import React from 'react';

// Declaring the state object globally.
const initialCounterState = {
  count: 0,
};

const counterContextWrapper = (component) => ({
  ...initialCounterState,
  increment: () => {
    initialCounterState.count += 1;
    component.setState({ context: counterContextWrapper(component) });
  },
  decrement: () => {
    initialCounterState.count -= 1;
    component.setState({ context: counterContextWrapper(component) });
  },
});

export const CounterContext = React.createContext({});

export class CounterContextProvider extends React.Component {
  state = {
    context: counterContextWrapper(this),
  };

  render() {
    return (
      <CounterContext.Provider value={this.state.context}>
        {this.props.children}
      </CounterContext.Provider>
    );
  }
}
