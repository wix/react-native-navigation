const React = require('react');
const { Component } = require('react');

const { StyleSheet, ScrollView, View, Button } = require('react-native');

const Navigation = require('react-native-navigation');

class ScrollViewScreen extends Component {
  static get navigationOptions() {
    return {
      topBar: {
        translucent: false
      }
    };
  }

  constructor(props) {
    super(props);
    this.state = {
      topBarHideOnScroll: false
    };
    this.onClickToggleTopBarHideOnScroll = this.onClickToggleTopBarHideOnScroll.bind(this);
  }

  render() {
    return (
      <ScrollView testID="scrollView" contentContainerStyle={styles.contentContainer}>
        <View>
          <Button title="Toggle Top Bar Hide On Scroll" onPress={this.onClickToggleTopBarHideOnScroll} />
        </View>
      </ScrollView>
    );
  }

  onClickToggleTopBarHideOnScroll() {
    Navigation.setOptions(this.props.containerId, {
      topBar: {
        hideOnScroll: !this.state.topBarHideOnScroll
      }
    });
  }
}

module.exports = ScrollViewScreen;

const styles = StyleSheet.create({
  contentContainer: {
    paddingVertical: 20,
    alignItems: 'center',
    height: 1000
  }
});

