import React from 'react';
import { Text } from 'react-native';
import { connect } from 'remx';
import store from './MyStore';

interface Props {
  renderCountIncrement?: () => void;
  printAge?: boolean;
  age?: number;
  name?: string;
}

class MyComponent extends React.Component<Props> {
  static options = {
    title: 'MyComponent',
  };

  render() {
    this.props.renderCountIncrement?.();

    return this.renderText(this.props.printAge ? this.props.age : this.props.name);
  }

  renderText(txt?: string | number) {
    return <Text>{txt}</Text>;
  }
}

function mapStateToProps() {
  return {
    name: store.getters.getName(),
    age: store.getters.getAge(),
  };
}

export default connect(mapStateToProps)(MyComponent);
