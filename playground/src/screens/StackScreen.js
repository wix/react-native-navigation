const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button');
const Screens = require('./Screens');
const Navigation = require('../services/Navigation');
const {stack, component} = require('../commons/Layouts');
const {
  PUSH_BTN,
  STACK_SCREEN_HEADER,
  PUSH_LIFECYCLE_BTN,
  POP_NONE_EXISTENT_SCREEN_BTN,
  PUSH_CUSTOM_BACK_BTN,
  CUSTOM_BACK_BTN,
  SEARCH_BTN,
  SET_STACK_ROOT_BTN
} = require('../testIDs');

class StackScreen extends React.Component {
  static options() {
    return {
      topBar: {
        testID: STACK_SCREEN_HEADER,
        title: {
          text: 'Stack'
        }
      }
    }
  }

  state = {
    backPress: ''
  };

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Push Lifecycle Screen' testID={PUSH_LIFECYCLE_BTN} onPress={this.pushLifecycleScreen} />
        <Button label='Pop None Existent Screen' testID={POP_NONE_EXISTENT_SCREEN_BTN} onPress={this.popNoneExistent} />
        <Button label='Push Custom Back Button' testID={PUSH_CUSTOM_BACK_BTN} onPress={this.pushCustomBackButton} />
        <Button label='Set Stack Root' testID={SET_STACK_ROOT_BTN} onPress={this.setStackRoot} />
        <Button label='Search' testID={SEARCH_BTN} onPress={this.search} />
      </Root>
    );
  }

  push = () => Navigation.push(this, Screens.Pushed);

  pushLifecycleScreen = () => Navigation.push(this, Screens.Lifecycle);

  popNoneExistent = () => Navigation.pop('noneExistentComponentId');

  pushCustomBackButton = () => Navigation.push(this, {
    component: {
      name: Screens.Pushed,
      options: {
        topBar: {
          backButton: {
            id: 'backPress',
            icon: require('../../img/navicon_add.png'),
            visible: true,
            color: 'black',
            testID: CUSTOM_BACK_BTN
          }
        }
      }
    }
  });

  search = () => Navigation.push(this, Screens.Search);

  setStackRoot = () => Navigation.setStackRoot(this, stack([
    component(Screens.Pushed, { topBar: { title: { text: 'Screen A' } } }),
    component(Screens.Pushed, { topBar: { title: { text: 'Screen B' } } }),
  ]));
}

module.exports = StackScreen;
