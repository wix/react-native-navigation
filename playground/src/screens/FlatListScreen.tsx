import React from 'react';
import { FlatList, View, Text } from 'react-native';
import {
  Navigation,
  NavigationComponentProps,
  NavigationButtonPressedEvent,
  NavigationComponent,
  Options,
} from 'react-native-navigation';

const FakeListData: { data: FakeDataItem[] } = require('../assets/FakeListData');

interface State {
  isFetching: boolean;
  shouldHideOnScroll: boolean;
}

type FakeDataItem = {
  id: number;
  first_name: string;
  last_name: string;
  email: string;
  gender: string;
};

export default class FlatListScreen extends NavigationComponent<NavigationComponentProps, State> {
  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'FlatList with fake data',
        },
        // iOS 11+ native UISearchBar inside topBar
        searchBar: {
          visible: true,
          hideOnScroll: true,
          placeholder: 'Search',
        },
        largeTitle: {
          visible: true,
          fontSize: 30,
          color: 'white',
          fontFamily: 'Helvetica',
        },
        leftButtons: [
          {
            id: 'sideMenu',
            color: 'red',
            icon: require('../../img/two.png'),
          },
        ],
        rightButtons: [
          {
            id: 'toggle',
            color: 'red',
            icon: require('../../img/one.png'),
          },
        ],
      },
      bottomTabs: {
        translucent: true,
      },
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
    this.state = { isFetching: false, shouldHideOnScroll: false };
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    switch (buttonId) {
      case 'sideMenu':
        Navigation.mergeOptions(this.props.componentId, {
          sideMenu: {
            left: {
              visible: true,
            },
          },
        });
        break;

      case 'toggle':
        const { shouldHideOnScroll } = this.state;
        Navigation.mergeOptions(this.props.componentId, {
          topBar: {
            hideOnScroll: !shouldHideOnScroll,
            drawBehind: !shouldHideOnScroll,
          },
        });
        this.setState({
          shouldHideOnScroll: !shouldHideOnScroll,
        });
        alert(`hideOnScroll/drawBehind is now ${!shouldHideOnScroll}`);
        break;

      default:
        break;
    }
  }

  onRefresh = () => {
    this.setState({ isFetching: true }, () => {
      setTimeout(() => {
        this.setState({ isFetching: false });
      }, 2000);
    });
  };

  seperatorComponent = () => <View style={styles.seperatorComponent} />;

  keyExtractor = (item: FakeDataItem) => `${item.id}`;

  renderItem = ({ item }: { item: FakeDataItem }) => (
    <View style={styles.listItem}>
      <Text>
        {item.first_name} {item.last_name}
      </Text>
      <Text>{item.email}</Text>
    </View>
  );

  render() {
    return (
      <FlatList
        data={FakeListData.data}
        keyExtractor={this.keyExtractor}
        onRefresh={this.onRefresh}
        ItemSeparatorComponent={this.seperatorComponent}
        refreshing={this.state.isFetching}
        renderItem={this.renderItem}
      />
    );
  }
}

const styles = {
  root: {
    flex: 1,
    backgroundColor: 'whitesmoke',
  },
  listItem: {
    height: 50,
  },
  seperatorComponent: {
    height: 5,
    backgroundColor: 'black',
  },
};
