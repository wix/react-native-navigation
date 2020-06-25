import React from 'react';
import { Text } from 'react-native';
import { connect } from 'react-redux';
import { selectors } from './MyStore';

interface Props {
  renderCountIncrement?: () => void;
  printAge?: boolean;
  age?: number;
  name?: string;
}

class MyComponent extends React.Component<Props> {
  render() {
    this.props.renderCountIncrement?.();
    return this.renderText(this.props.printAge ? this.props.age : this.props.name);
  }

  renderText(txt?: string | number) {
    return <Text>{txt}</Text>;
  }
}

function mapStateToProps(state) {
  return {
    name: selectors.getName(state),
    age: selectors.getAge(state),
  };
}

export default connect(mapStateToProps)(MyComponent);
