// CounterContext.tsx
import React from 'react';

// Declaring the state object globally.
const initialCounterState = {
  count: 0,
};

const counterContextWrapper = (component?: React.Component) => ({
  ...initialCounterState,
  increment: () => {
    initialCounterState.count += 1;
    component?.setState({ context: counterContextWrapper(component) });
  },
  decrement: () => {
    initialCounterState.count -= 1;
    component?.setState({ context: counterContextWrapper(component) });
  },
});

type Context = ReturnType<typeof counterContextWrapper>;

export const CounterContext = React.createContext<Context>(counterContextWrapper());

interface State {
  context: Context;
}

export class CounterContextProvider extends React.Component {
  state: State = {
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
