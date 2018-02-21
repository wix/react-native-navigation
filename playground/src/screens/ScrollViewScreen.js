const React = require('react');
const { Component } = require('react');

const { StyleSheet, ScrollView, View, Button, Platform } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

const FAB = 'fab';

class ScrollViewScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: 'Collapse',
        drawBehind: true,
        textColor: 'black',
        textFontSize: 16,
        visible: true,
        testID: testIDs.TOP_BAR_ELEMENT
      },
      fab: {
        id: FAB,
        backgroundColor: 'blue',
        clickColor: 'blue',
        rippleColor: 'aquamarine',
        hideOnScroll: true
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
      <View>
        <ScrollView testID={testIDs.SCROLLVIEW_ELEMENT} contentContainerStyle={styles.contentContainer}>
          <View>
            <Button title='Toggle Top Bar Hide On Scroll' testID={testIDs.TOGGLE_TOP_BAR_HIDE_ON_SCROLL} onPress={this.onClickToggleTopBarHideOnScroll} />
          </View>
        </ScrollView>
      </View>
    );
  }

  onClickToggleTopBarHideOnScroll() {
    this.setState({
      topBarHideOnScroll: !this.state.topBarHideOnScroll
    });
  }

  componentDidUpdate() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        hideOnScroll: this.state.topBarHideOnScroll
      },
      fab: {
        hideOnScroll: !this.state.topBarHideOnScroll
      }
    });
  }
}

module.exports = ScrollViewScreen;

const styles = StyleSheet.create({
  contentContainer: {
    alignItems: 'center',
    backgroundColor: 'green',
    paddingTop: 200,
    height: 1200
  }
});
