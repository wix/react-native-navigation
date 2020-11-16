import React from 'react';

const _context = {
  title: 'Title from global context',
  count: 0,
  incrementCount: () => {},
};
const contextWrapper = (component: any) => ({
  ..._context,
  incrementCount: () => {
    _context.count++;
    component.setState({ context: contextWrapper(component) });
  },
});

const GlobalContext = React.createContext<{
  title: string;
  count: number;
  incrementCount: () => void;
}>(_context);
class ContextProvider extends React.Component {
  state = { context: contextWrapper(this) };

  render() {
    return (
      <GlobalContext.Provider value={this.state.context}>
        {this.props.children}
      </GlobalContext.Provider>
    );
  }
}

const Context = React.createContext('Default value from Context');
export { ContextProvider, GlobalContext, Context };
