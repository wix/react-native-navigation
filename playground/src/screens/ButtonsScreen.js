const React = require('react');
const {Component} = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const Colors = require('../commons/Colors');
const {
  PUSH_BTN,
  TOP_BAR,
  ROUND_BUTTON,
  BUTTON_ONE,
  LEFT_BUTTON,
  SHOW_LIFECYCLE_BTN,
  RESET_BUTTONS,
  CHANGE_BUTTON_PROPS
} = require('../testIDs');

class Options extends Component {
  static options() {
    return {
      fab: {
        id: 'fab',
        icon: require('../../img/navicon_add.png'),
        backgroundColor: Colors.secondary
      },
      topBar: {
        testID: TOP_BAR,
        title: {
          text: 'Buttons'
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
              id: 'ROUND_COMPONENT',
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
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Show Lifecycle button' testID={SHOW_LIFECYCLE_BTN} onPress={this.showLifecycleButton} />
        <Button label='Remove all buttons' testID={RESET_BUTTONS} onPress={this.resetButtons} />
        <Button label='Change Button Props'  testID={CHANGE_BUTTON_PROPS} onPress={this.changeButtonProps} />
      </Root>
    );
  }

  push = () => Navigation.push(this, Screens.Pushed);

  showLifecycleButton = () => Navigation.mergeOptions(this, {
    topBar: {
      rightButtons: [
        {
          id: 'ROUND',
          testID: ROUND_BUTTON,
          component: {
            name: Screens.LifecycleButton,
            passProps: {
              title: 'Two'
            }
          }
        }
      ]
    }
  });

  resetButtons = () => Navigation.mergeOptions(this, {
    topBar: {
      rightButtons: [],
      leftButtons: []
    }
  });

  changeButtonProps = () => {
    Navigation.updateProps('ROUND_COMPONENT', {
      title: 'Three'
    });
  }
}

module.exports = Options;
