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
  ROUND_BUTTON_2,
  BUTTON_ONE,
  LEFT_BUTTON,
  SHOW_LIFECYCLE_BTN,
  RESET_BUTTONS,
  CHANGE_BUTTON_PROPS,
  ADD_BUTTON
} = require('../testIDs');

const rightButtons = [
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
      waitForRender: true,
      passProps: {
        title: '2'
      }
    }
  }
];

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
        rightButtons,
        leftButtons: [
          {
            id: 'LEFT',
            testID: LEFT_BUTTON,
            icon: require('../../img/clear.png'),
            color: Colors.primary,
            accessibilityLabel: 'Close button'
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
        <Button label='Add Button @ idx 0' testID={ADD_BUTTON} onPress={() => this.addButtonAtIndex(0)} />
        <Button label='Add Button @ idx 1' testID={ADD_BUTTON} onPress={() => this.addButtonAtIndex(1)} />
        <Button label='Add Button @ idx 2' testID={ADD_BUTTON} onPress={() => this.addButtonAtIndex(2)} />
      </Root>
    );
  }

  addButtonAtIndex = (index) => {
    const rightButtons = [
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
            title: '2'
          }
        }
      }
    ];
    rightButtons.splice(index, 0, {
      testID: ROUND_BUTTON_2,
      id: 'NEW_BTN',
      component: {
        id: 'NEW_BTN',
        name: Screens.RoundButton,
        passProps: {
          title: '3'
        }
      }
    });
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons
      }
    })
  };

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
