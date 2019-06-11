const React = require('react');
const { Component } = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const Colors = require('../commons/Colors');
const {
  CHANGE_TITLE_BTN,
  HIDE_TOP_BAR_BTN,
  SHOW_TOP_BAR_BTN,
  TOP_BAR,
  ROUND_BUTTON,
  BUTTON_ONE,
  LEFT_BUTTON,
  PUSH_BTN,
  HIDE_TOPBAR_DEFAULT_OPTIONS,
  SHOW_YELLOW_BOX_BTN,
  SET_REACT_TITLE_VIEW
} = require('../testIDs');

class Options extends Component {
  static options() {
    return {
      topBar: {
        visible: true,
        testID: TOP_BAR,
        title: {
          text: 'Styling Options'
        },
        rightButtons: [
          {
            id: 'ONE',
            testID: BUTTON_ONE,
            text: 'One',
            color: Colors.primary
          },
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              name: Screens.RoundButton,
              passProps: {
                title: 'Two'
              }
            }
          }
        ],
        leftButtons: [
          {
            id: 'LEFT',
            testID: LEFT_BUTTON,
            icon: require('../../img/clear.png'),
            color: Colors.primary
          }
        ]
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Change title' testID={CHANGE_TITLE_BTN} onPress={this.changeTitle} />
        <Button label='Hide TopBar' testID={HIDE_TOP_BAR_BTN} onPress={this.hideTopBar} />
        <Button label='Show TopBar' testID={SHOW_TOP_BAR_BTN} onPress={this.showTopBar} />
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Hide TopBar in DefaultOptions' testID={HIDE_TOPBAR_DEFAULT_OPTIONS} onPress={this.hideTopBarInDefaultOptions} />
        <Button label='Set React Title View' testID={SET_REACT_TITLE_VIEW} onPress={this.setReactTitleView} />
        <Button label='Show Yellow Box' testID={SHOW_YELLOW_BOX_BTN} onPress={() => console.warn('Yellow Box')} />
        <Button label='StatusBar' onPress={this.statusBarScreen} />
      </Root>
    );
  }

  changeTitle = () => Navigation.mergeOptions(this, {
    topBar: {
      title: {
        text: 'Title Changed'
      }
    }
  });

  hideTopBar = () => Navigation.mergeOptions(this, {
    topBar: {
      visible: false
    }
  });

  showTopBar = () => Navigation.mergeOptions(this, {
    topBar: {
      visible: true
    }
  });

  push = () => Navigation.push(this, Screens.Pushed);

  hideTopBarInDefaultOptions = () => {
    Navigation.setDefaultOptions({
      topBar: {
        visible: false,
        title: {
          text: 'Default Title'
        }
      }
    });
  }

  setReactTitleView = () => Navigation.mergeOptions(this, {
    topBar: {
      title: {
        component: {
          name: Screens.ReactTitleView,
          alignment: 'center',
          passProps: {
            text: 'Press Me'
          }
        }
      }
    }
  });

  statusBarScreen = () => Navigation.showModal(Screens.StatusBar);
}

module.exports = Options;
