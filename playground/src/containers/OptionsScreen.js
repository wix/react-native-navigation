const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const Navigation = require('react-native-navigation');

class OptionsScreen extends Component {
  static get navigationOptions() {
    return {
      title: 'Static Title',
      topBarBackgroundColor: 'red',
      topBarTextFontFamily: 'HelveticaNeue-Italic'
    };
  }

  constructor(props) {
    super(props);
    this.onClickDynamicOptions = this.onClickDynamicOptions.bind(this);
    this.onClickShowTopBar = this.onClickShowTopBar.bind(this);
    this.onClickHideTopBar = this.onClickHideTopBar.bind(this);
    this.onClickScrollViewScreen = this.onClickScrollViewScreen.bind(this);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Options Screen`}</Text>
        <Button title="Dynamic Options" onPress={this.onClickDynamicOptions} />
        <Button title="Show Top Bar" onPress={this.onClickShowTopBar} />
        <Button title="Hide Top Bar" onPress={this.onClickHideTopBar} />
        <Button title="scrollView Screen" onPress={this.onClickScrollViewScreen} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
      </View>
    );
  }

  onClickDynamicOptions() {
    Navigation.setOptions(this.props.containerId, {
      title: 'Dynamic Title',
      topBarTextColor: '#00FFFF',
      topBarBackgroundColor: 'green',
      topBarButtonColor: 'red',
      topBarTextFontFamily: 'HelveticaNeue-CondensedBold'
    });
  }

  onClickScrollViewScreen() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.ScrollViewScreen'
    });
  }

  onClickShowTopBar() {
    Navigation.setOptions(this.props.containerId, {
      topBarHidden: false
    });
  }

  onClickHideTopBar() {
    Navigation.setOptions(this.props.containerId, {
      topBarHidden: true
    });
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center'
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

module.exports = OptionsScreen;
