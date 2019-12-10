const React = require('react');
const _context = {
  title: 'Title from global context',
  count: 0
};
const contextWrapper = (component) => ({
  ..._context,
  incrementCount: () => {
    _context.count++;
    component.setState({ context: contextWrapper(component) })
  }
});

const GlobalContext = React.createContext({});
class ContextProvider extends React.Component {
  state = { context: contextWrapper(this) }

  render() {
    return (
      <GlobalContext.Provider value={this.state.context}>
        {this.props.children}
      </GlobalContext.Provider>
    );
  }
}

module.exports = {
  ContextProvider,
  GlobalContext,
  Context: React.createContext('Default value from Context')
}
