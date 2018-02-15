const React = require('react');
const { Component } = require('react');

const { View, Text, Button, Platform } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

const BUTTON_ONE = 'buttonOne';
const BUTTON_TWO = 'buttonTwo';
const BUTTON_LEFT = 'buttonLeft';

class OptionsScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: 'Static Title',
        textColor: 'black',
        ...Platform.select({
          android: { drawBehind: true },
          ios: { drawBehind: false, }
        }),
        largeTitle: false,
        visible: true,
        textFontSize: 16,
        textFontFamily: 'HelveticaNeue-Italic',
        testID: testIDs.TOP_BAR_ELEMENT,
        rightButtons: [{
          id: BUTTON_ONE,
          testID: BUTTON_ONE,
          title: 'One',
          buttonFontSize: 28,
          buttonColor: 'red'
        }],
        leftButtons: [{
          id: BUTTON_LEFT,
          testID: BUTTON_LEFT,
          icon: require('../../img/navicon_add.png'),
          title: 'Left',
          buttonColor: 'purple'
        }]
      }
    };
  }

  constructor(props) {
    super(props);
    this.onClickDynamicOptions = this.onClickDynamicOptions.bind(this);
    this.onClickShowTopBar = this.onClickShowTopBar.bind(this);
    this.onClickHideTopBar = this.onClickHideTopBar.bind(this);
    this.onClickScrollViewScreen = this.onClickScrollViewScreen.bind(this);
    this.onClickTopBarTransparent = this.onClickTopBarTransparent.bind(this);
    this.onClickTopBarOpaque = this.onClickTopBarOpaque.bind(this);
    this.onClickCustomTranstition = this.onClickCustomTranstition.bind(this);
    this.onClickShowOverlay = this.onClickShowOverlay.bind(this);
    this.onClickPushDefaultOptionsScreen = this.onClickPushDefaultOptionsScreen.bind(this);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.OPTIONS_SCREEN_HEADER}>{`Options Screen`}</Text>
        <Button title='Dynamic Options' testID={testIDs.DYNAMIC_OPTIONS_BUTTON} onPress={this.onClickDynamicOptions} />
        <Button title='Show Top Bar' testID={testIDs.SHOW_TOP_BAR_BUTTON} onPress={this.onClickShowTopBar} />
        <Button title='Hide Top Bar' testID={testIDs.HIDE_TOP_BAR_BUTTON} onPress={this.onClickHideTopBar} />
        <Button title='Top Bar Transparent' onPress={this.onClickTopBarTransparent} />
        <Button title='Top Bar Opaque' onPress={this.onClickTopBarOpaque} />
        <Button title='scrollView Screen' testID={testIDs.SCROLLVIEW_SCREEN_BUTTON} onPress={this.onClickScrollViewScreen} />
        <Button title='Custom Transition' testID={testIDs.CUSTOM_TRANSITION_BUTTON} onPress={this.onClickCustomTranstition} />
        <Button title='Show custom alert' testID={testIDs.SHOW_CUSTOM_ALERT_BUTTON} onPress={this.onClickAlert} />
        <Button title='Show snackbar' testID={testIDs.SHOW_SNACKBAR_BUTTON} onPress={this.onClickSnackbar} />
        <Button title='Show overlay' testID={testIDs.SHOW_OVERLAY_BUTTON} onPress={() => this.onClickShowOverlay(true)} />
        <Button title='Show touch through overlay' testID={testIDs.SHOW_TOUCH_THROUGH_OVERLAY_BUTTON} onPress={() => this.onClickShowOverlay(false)} />
        <Button title='Push Default Options Screen' testID={testIDs.PUSH_DEFAULT_OPTIONS_BUTTON} onPress={this.onClickPushDefaultOptionsScreen} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
      </View>
    );
  }

  onNavigationButtonPressed(id) {
    if (id === BUTTON_ONE) {
      Navigation.setOptions(this.props.componentId, {
        topBar: {
          rightButtons: [{
            id: BUTTON_TWO,
            testID: BUTTON_TWO,
            title: 'Two',
            icon: require('../../img/navicon_add.png'),
            disableIconTint: true,
            // disabled: true,
            showAsAction: 'ifRoom',
            buttonColor: 'green',
            buttonFontSize: 28,
            buttonFontWeight: '800'
          }],
          leftButtons: []
        }
      });
    } else if (id === BUTTON_TWO) {
      Navigation.setOptions(this.props.componentId, {
        topBar: {
          rightButtons: [{
            id: BUTTON_ONE,
            testID: BUTTON_ONE,
            title: 'One',
            buttonColor: 'red'
          }],
          leftButtons: [{
            id: BUTTON_LEFT,
            testID: BUTTON_LEFT,
            icon: require('../../img/navicon_add.png'),
            title: 'Left',
            buttonColor: 'purple'
          }]
        }
      });
    } else if (id === BUTTON_LEFT) {
      Navigation.pop(this.props.componentId);
    }
  }

  onClickDynamicOptions() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        title: 'Dynamic Title',
        textColor: '#00FFFF',
        largeTitle: false,
        buttonColor: 'red',
        textFontSize: 20,
        textFontFamily: 'HelveticaNeue-CondensedBold'
      }
    });
  }

  onClickScrollViewScreen() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.ScrollViewScreen'
      }
    });
  }

  onClickCustomTranstition() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.CustomTransitionOrigin'
      }
    });
  }

  onClickTopBarTransparent() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        transparent: true
      }
    });
  }
  onClickTopBarOpaque() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        transparent: false
      }
    });
  }
  onClickShowTopBar() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        visible: true,
        animateHide: true
      }
    });
  }

  onClickHideTopBar() {
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        visible: false,
        animateHide: true
      }
    });
  }

  onClickAlert() {
    Navigation.showOverlay('custom', 'navigation.playground.CustomDialog');
  }

  onClickSnackbar() {
    Navigation.showOverlay('snackbar', {
      text: 'Test!',
      // textColor: 'red',
      // backgroundColor: 'green',
      duration: 'long',
      button: {
        text: 'Action',
        textColor: 'blue'
      }
    });
  }

  onClickShowOverlay(interceptTouchOutside) {
    Navigation.showOverlay({
      component: {
        name: 'navigation.playground.CustomDialog',
        options: {
          overlay: {
            interceptTouchOutside
          }
        }
      }
    });
  }

  onClickPushDefaultOptionsScreen() {
    Navigation.setDefaultOptions({
      topBar: {
        visible: false
      }
    });

    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen'
      }
    });
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white'
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
