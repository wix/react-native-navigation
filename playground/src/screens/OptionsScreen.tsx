import React from 'react';
import { NavigationComponent, NavigationComponentProps } from 'react-native-navigation';
import Button from '../components/Button';
import Root from '../components/Root';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';
import Screens from './Screens';

const {
  CHANGE_TITLE_BTN,
  HIDE_TOP_BAR_BTN,
  SHOW_TOP_BAR_BTN,
  TOP_BAR,
  PUSH_BTN,
  HIDE_TOPBAR_DEFAULT_OPTIONS,
  SHOW_YELLOW_BOX_BTN,
  SET_REACT_TITLE_VIEW,
  GOTO_BUTTONS_SCREEN,
  GOTO_SEARCHBAR_SCREEN,
  GOTO_SEARCHBAR_MODAL,
} = testIDs;

interface Props extends NavigationComponentProps {}

export default class Options extends NavigationComponent<Props> {
  static options() {
    return {
      topBar: {
        visible: true,
        testID: TOP_BAR,
        title: {
          text: 'Styling Options',
        },
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

  state = {
    isAndroidNavigationBarVisible: true,
  };

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Change title" testID={CHANGE_TITLE_BTN} onPress={this.changeTitle} />
        <Button label="Hide TopBar" testID={HIDE_TOP_BAR_BTN} onPress={this.hideTopBar} />
        <Button label="Show TopBar" testID={SHOW_TOP_BAR_BTN} onPress={this.showTopBar} />
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button
          label="Hide TopBar in DefaultOptions"
          testID={HIDE_TOPBAR_DEFAULT_OPTIONS}
          onPress={this.hideTopBarInDefaultOptions}
        />
        <Button
          label="Set React Title View"
          testID={SET_REACT_TITLE_VIEW}
          onPress={this.setReactTitleView}
        />
        <Button
          label="Show Yellow Box"
          testID={SHOW_YELLOW_BOX_BTN}
          onPress={() => console.warn('Yellow Box')}
        />
        <Button
          label="Buttons Screen"
          testID={GOTO_BUTTONS_SCREEN}
          onPress={this.pushButtonsScreen}
        />
        <Button label="SystemUi" onPress={this.systemUi} />
        <Button
          platform={'ios'}
          testID={GOTO_SEARCHBAR_SCREEN}
          label="Search Bar"
          onPress={this.searchBarScreen}
        />
        <Button
          platform={'ios'}
          testID={GOTO_SEARCHBAR_MODAL}
          label="Search Bar Modal"
          onPress={this.searchBarModal}
        />
        <Button
          label="Toggle Navigation bar visibility"
          platform="android"
          onPress={this.toggleAndroidNavigationBar}
        />
      </Root>
    );
  }

  changeTitle = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        title: {
          alignment: 'center',
          text: 'Title Changed',
        },
      },
    });

  hideTopBar = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        visible: false,
      },
    });

  showTopBar = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        visible: true,
      },
    });

  hideSearchBar = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        searchBar: {
          visible: false,
        },
      },
    });

  showSearchBar = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        searchBar: {
          visible: true,
        },
      },
    });

  toggleAndroidNavigationBar = () => {
    this.setState({ isAndroidNavigationBarVisible: !this.state.isAndroidNavigationBarVisible });
    Navigation.mergeOptions(this, {
      navigationBar: {
        visible: !this.state.isAndroidNavigationBarVisible,
      },
    });
  };

  push = () =>
    Navigation.push(this, {
      component: {
        name: Screens.Pushed,
        passProps: {
          previousScreenIds: [this.props.componentId],
        },
      },
    });

  hideTopBarInDefaultOptions = () => {
    Navigation.setDefaultOptions({
      topBar: {
        visible: false,
        title: {
          text: 'Default Title',
        },
      },
    });
  };

  setReactTitleView = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: [
          {
            id: 'ONE',
            text: 'One',
          },
          {
            id: 'ROUND',
            component: {
              id: 'ROUND_COMPONENT',
              name: Screens.RoundButton,
              passProps: {
                title: 'Two',
                timesCreated: 1,
              },
            },
          },
        ],
        leftButtons: [],
        title: {
          component: {
            name: Screens.ReactTitleView,
            alignment: 'fill',
            passProps: {
              clickable: true,
              text: 'Press Me',
            },
          },
        },
      },
    });

  systemUi = () => Navigation.showModal(Screens.SystemUi);

  searchBarScreen = () => Navigation.push(this, Screens.SearchBar, {});

  searchBarModal = () => Navigation.showModal(Screens.SearchBarModal);

  pushButtonsScreen = () =>
    Navigation.push(this, Screens.Buttons, {
      animations: {
        push: {
          waitForRender: true,
        },
      },
    });
}
