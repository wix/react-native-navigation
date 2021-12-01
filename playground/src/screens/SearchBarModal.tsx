import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Button from '../components/Button';
import Root from '../components/Root';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const { SHOW_SEARCH_BAR_BTN, HIDE_SEARCH_BAR_BTN, TOP_BAR } = testIDs;

interface Props extends NavigationComponentProps {}

export default class SearchBarModal extends React.Component<Props> {
  static options() {
    return {
      topBar: {
        visible: true,
        testID: TOP_BAR,
        title: {
          text: 'SearchBar Modal Options',
        },
      },
    };
  }

  state = {
    isAndroidNavigationBarVisible: true,
  };

  render() {
    return (
      <Root componentId={this.props.componentId}>
        {/* <Button label="Hide TopBar" testID={HIDE_TOP_BAR_BTN} onPress={this.hideTopBar} /> */}
        {/* <Button label="Show TopBar" testID={SHOW_TOP_BAR_BTN} onPress={this.showTopBar} /> */}
        <Button label="Hide SearchBar" testID={HIDE_SEARCH_BAR_BTN} onPress={this.hideSearchBar} />
        <Button label="Show SearchBar" testID={SHOW_SEARCH_BAR_BTN} onPress={this.showSearchBar} />
      </Root>
    );
  }

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
}
