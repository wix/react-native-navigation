const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('./../services/Navigation');
const {
  NAVIGATION_TAB,
  MODAL_BTN,
  OVERLAY_BTN,
  EXTERNAL_COMP_BTN,
  SHOW_STATIC_EVENTS_SCREEN,
  SHOW_ORIENTATION_SCREEN,
  SET_ROOT_BTN
} = require('../testIDs');
const Screens = require('./Screens');

class NavigationScreen  extends React.Component {
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
        <Button label='Set Root' testID={SET_ROOT_BTN} onPress={this.setRoot} />
        <Button label='Modal' testID={MODAL_BTN} onPress={this.showModal} />
        <Button label='Overlay' testID={OVERLAY_BTN} onPress={this.showOverlay} />
        <Button label='External Component' testID={EXTERNAL_COMP_BTN} onPress={this.externalComponent} />
        <Button label='Static Events' testID={SHOW_STATIC_EVENTS_SCREEN} onPress={this.pushStaticEventsScreen} />
        <Button label='Orientation' testID={SHOW_ORIENTATION_SCREEN} onPress={this.orientation} />
        <Navigation.TouchablePreview
          touchableComponent={Button}
          onPressIn={this.preview}
          label='Preview' />
      </Root>
    );
  }

  setRoot = () => Navigation.showModal(Screens.SetRoot);
  showModal = () => Navigation.showModal(Screens.Modal);
  showOverlay = () => Navigation.showModal(Screens.Overlay);
  externalComponent = () => Navigation.showModal(Screens.ExternalComponent);
  pushStaticEventsScreen = () => Navigation.showModal(Screens.EventsScreen)
  orientation = () => Navigation.showModal(Screens.Orientation);
  preview = ({reactTag}) => {
    Navigation.push(this.props.componentId, {
      component: {
        name: Screens.Pushed,
        options: {
          animations: {
            push: {
              enabled: false
            }
          },
          preview: {
            reactTag: reactTag,
            height: 300,
            actions: [{
              id: 'action-cancel',
              title: 'Cancel'
            }, {
              id: 'action-delete',
              title: 'Delete',
              actions: [{
                id: 'action-delete-sure',
                title: 'Are you sure?',
                style: 'destructive'
              }]
            }]
          }
        }
      }
    });
  }
}

module.exports = NavigationScreen;
