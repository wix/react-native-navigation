import React from 'react';
import { Platform } from 'react-native';
import {
  NavigationComponent,
  NavigationComponentProps,
  OptionsModalPresentationStyle,
} from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from './../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  NAVIGATION_TAB,
  MODAL_BTN,
  OVERLAY_BTN,
  EXTERNAL_COMP_BTN,
  SHOW_STATIC_EVENTS_SCREEN,
  SHOW_ORIENTATION_SCREEN,
  SET_ROOT_BTN,
  PAGE_SHEET_MODAL_BTN,
  NAVIGATION_SCREEN,
  BACK_BUTTON_SCREEN_BTN,
} = testIDs;

interface Props extends NavigationComponentProps {}

export default class NavigationScreen extends NavigationComponent<Props> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Navigation',
        },
      },
      bottomTab: {
        text: 'Navigation',
        icon: require('../../img/navigation.png'),
        testID: NAVIGATION_TAB,
      },
    };
  }
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }
  componentWillAppear() {
    console.log('componentWillAppear:', this.props.componentId);
  }
  componentDidDisappear() {
    console.log('componentDidDisappear:', this.props.componentId);
  }

  componentDidAppear() {
    console.log('componentDidAppear:', this.props.componentId);
  }
  render() {
    return (
      <Root componentId={this.props.componentId} testID={NAVIGATION_SCREEN}>
        <Button label="Set Root" testID={SET_ROOT_BTN} onPress={this.setRoot} />
        <Button label="Modal" testID={MODAL_BTN} onPress={this.showModal} />
        <Button
          label="PageSheet modal"
          testID={PAGE_SHEET_MODAL_BTN}
          onPress={this.showPageSheetModal}
          platform="ios"
        />
        <Button label="Overlay" testID={OVERLAY_BTN} onPress={this.showOverlay} />
        <Button
          label="External Component"
          testID={EXTERNAL_COMP_BTN}
          onPress={this.externalComponent}
        />
        <Button
          label="Static Events"
          testID={SHOW_STATIC_EVENTS_SCREEN}
          onPress={this.pushStaticEventsScreen}
        />
        <Button label="Orientation" testID={SHOW_ORIENTATION_SCREEN} onPress={this.orientation} />
        <Button
          label="Back Button"
          testID={BACK_BUTTON_SCREEN_BTN}
          onPress={this.pushBackButtonScreen}
        />
        <Button label="React Context API" onPress={this.pushContextScreen} />
        <Button label="Shared Element (Cocktails)" onPress={this.sharedElement} />
        <Button label="Shared Element (Car Dealer)" onPress={this.sharedElementAlt} />
        <Button label="Shared Element (ImageGallery)" onPress={this.sharedElementImageGallery} />
        {Platform.OS === 'ios' && (
          <Navigation.TouchablePreview
            touchableComponent={Button}
            onPressIn={this.preview}
            label="Preview"
          />
        )}
      </Root>
    );
  }

  setRoot = () => Navigation.showModal(Screens.SetRoot);
  showModal = () => Navigation.showModal(Screens.Modal);

  showPageSheetModal = () =>
    Navigation.showModal(Screens.Modal, {
      modalPresentationStyle: OptionsModalPresentationStyle.pageSheet,
      modal: {
        swipeToDismiss: false,
      },
    });
  pushBackButtonScreen = () => Navigation.push(this, Screens.BackButton);
  showOverlay = () => Navigation.showModal(Screens.Overlay);
  externalComponent = () => Navigation.showModal(Screens.ExternalComponent);
  pushStaticEventsScreen = () => Navigation.showModal(Screens.EventsScreen);
  orientation = () => Navigation.showModal(Screens.Orientation);
  pushContextScreen = () => Navigation.push(this, Screens.ContextScreen);
  sharedElement = () => Navigation.showModal(Screens.CocktailsListScreen);
  sharedElementAlt = () => Navigation.push(this, Screens.CarsListScreen);
  sharedElementImageGallery = () => Navigation.push(this, Screens.ImageGalleryListScreen);
  preview = ({ reactTag }: { reactTag: number | null }) => {
    if (reactTag === null) {
      return;
    }
    Navigation.push(this.props.componentId, {
      component: {
        name: Screens.Pushed,
        options: {
          animations: {
            push: {
              enabled: false,
            },
          },
          preview: {
            reactTag,
            height: 300,
            actions: [
              {
                id: 'action-cancel',
                title: 'Cancel',
              },
              {
                id: 'action-delete',
                title: 'Delete',
                actions: [
                  {
                    id: 'action-delete-sure',
                    title: 'Are you sure?',
                    style: 'destructive',
                  },
                ],
              },
            ],
          },
        },
      },
    });
  };
}
