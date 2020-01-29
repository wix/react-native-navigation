const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('./../services/Navigation');
const {
  NAVIGATION_TAB,
  SET_MULTIPLE_ROOTS_BTN
} = require('../testIDs');
const Screens = require('./Screens');

class SetRootScreen  extends React.Component {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Navigation'
        }
      },
      bottomTab: {
        text: 'Navigation',
        icon: require('../../img/navigation.png'),
        testID: NAVIGATION_TAB
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Set Multiple Roots' testID={SET_MULTIPLE_ROOTS_BTN} onPress={this.setMultipleRoot} />
      </Root>
    );
  }

  setMultipleRoot = async () => {
    await this.setRoot();
    await this.setRoot();
  };

  setRoot = async () => await Navigation.setRoot({
    root: {
      stack: {
        id: 'stack',
        children: [{
          component: {
            id: 'component',
            name: Screens.Pushed
          }
        }]
      }
    }
  });
}

module.exports = SetRootScreen;
