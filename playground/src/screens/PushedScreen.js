const _ = require('lodash');

const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const Navigation = require('react-native-navigation');
const testIDs = require('../testIDs');

class PushedScreen extends Component {
  static get options() {
    return {
      topBar: {
        testID: testIDs.TOP_BAR_ELEMENT
      }
    };
  }

  constructor(props) {
    super(props);
    this.onClickPush = this.onClickPush.bind(this);
    this.onClickPop = this.onClickPop.bind(this);
    this.onClickPopPrevious = this.onClickPopPrevious.bind(this);
    this.onClickPopToFirstPosition = this.onClickPopToFirstPosition.bind(this);
    this.onClickPopToRoot = this.onClickPopToRoot.bind(this);
  }

  render() {
    const stackPosition = this.getStackPosition();
    return (
      <View style={styles.root}>
        <Text testID={testIDs.PUSHED_SCREEN_HEADER} style={styles.h1}>{`Pushed Screen`}</Text>
        <Text style={styles.h2}>{`Stack Position: ${stackPosition}`}</Text>
        <Button title="Push" testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
        <Button title="Pop" testID={testIDs.POP_BUTTON} onPress={this.onClickPop} />
        <Button title="Pop Previous" testID={testIDs.POP_PREVIOUS_BUTTON} onPress={this.onClickPopPrevious} />
        <Button title="Pop To Root" testID={testIDs.POP_TO_ROOT} onPress={this.onClickPopToRoot} />
        {stackPosition > 2 && <Button title="Pop To Stack Position 1" testID={testIDs.POP_STACK_POSITION_ONE_BUTTON} onPress={this.onClickPopToFirstPosition} />}
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }

  async onClickPush() {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          topBar: {
            title: `Pushed ${this.getStackPosition() + 1}`
          }
        }
      }
    });
  }

  async onClickPop() {
    await Navigation.pop(this.props.componentId);
  }

  async onClickPopPrevious() {
    await Navigation.pop(_.last(this.props.previousScreenIds));
  }

  async onClickPopToFirstPosition() {
    await Navigation.popTo(this.props.previousScreenIds[0]);
  }

  async onClickPopToRoot() {
    await Navigation.popToRoot(this.props.componentId);
  }

  getStackPosition() {
    return this.props.stackPosition || 1;
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};

module.exports = PushedScreen;
