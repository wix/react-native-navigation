const React = require('react');
let ctx = {
  title: 'Title from global context',
  count: 0
};

const stateAwareContext = (component) =>
  new Proxy(ctx, {
    set: function (obj, prop, value) {
      obj[prop] = value;
      component.setState({ context: stateAwareContext(component) });
      return true;
    }
  });

const GlobalContext = React.createContext({});
class ContextProvider extends React.Component {
  state = {context :stateAwareContext(this)};
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
