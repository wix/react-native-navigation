const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('./../services/Navigation');
const { Platform } = require('react-native');
const {
  NAVIGATION_TAB,
  MODAL_BTN,
  OVERLAY_BTN,
  EXTERNAL_COMP_BTN,
  SHOW_STATIC_EVENTS_SCREEN,
  SHOW_ORIENTATION_SCREEN,
  SET_ROOT_BTN,
  PAGE_SHEET_MODAL_BTN,
  NAVIGATION_SCREEN
} = require('../testIDs');
const Screens = require('./Screens');

class NavigationScreen extends React.Component {
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
      <Root componentId={this.props.componentId} testID={NAVIGATION_SCREEN}>
        <Button label='Set Root' testID={SET_ROOT_BTN} onPress={this.setRoot} />
        <Button label='Modal' testID={MODAL_BTN} onPress={this.showModal} />
        {Platform.OS === 'ios' && <Button label='PageSheet modal' testID={PAGE_SHEET_MODAL_BTN} onPress={this.showPageSheetModal} />}
        <Button label='Overlay' testID={OVERLAY_BTN} onPress={this.showOverlay} />
        <Button label='External Component' testID={EXTERNAL_COMP_BTN} onPress={this.externalComponent} />
        <Button label='Static Events' testID={SHOW_STATIC_EVENTS_SCREEN} onPress={this.pushStaticEventsScreen} />
        <Button label='Orientation' testID={SHOW_ORIENTATION_SCREEN} onPress={this.orientation} />
        <Button label='React Context API' onPress={this.pushContextScreen} />
        {false && <Button label='Shared Element' onPress={this.sharedElement} />}
        <Navigation.TouchablePreview
          touchableComponent={Button}
          onPressIn={this.preview}
          label='Preview' />
      </Root>
    );
  }

  setRoot = () => Navigation.showModal(Screens.SetRoot);
  showModal = () => Navigation.showModal(Screens.Modal);

  showPageSheetModal = () => Navigation.showModal(Screens.Modal, {
    modalPresentationStyle: 'pageSheet',
    modal: {
      swipeToDismiss: false,
    }
  });
  showOverlay = () => Navigation.showModal(Screens.Overlay);
  externalComponent = () => Navigation.showModal(Screens.ExternalComponent);
  pushStaticEventsScreen = () => Navigation.showModal(Screens.EventsScreen)
  orientation = () => Navigation.showModal(Screens.Orientation);
  pushContextScreen = () => Navigation.push(this, Screens.ContextScreen);
  sharedElement = () => Navigation.showModal(Screens.CocktailsListScreen)
  preview = ({ reactTag }) => {
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
