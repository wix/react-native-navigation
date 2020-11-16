import React from 'react';
import { StyleSheet, FlatList, Text, TouchableOpacity } from 'react-native';
import {
  Navigation,
  NavigationComponentProps,
  SearchBarUpdatedEvent,
} from 'react-native-navigation';
import testIDs from '../testIDs';

const ITEMS: { key: string }[] = [];
for (let i = 0; i < 200; i++) {
  ITEMS.push({ key: `Item ${i}` });
}

interface State {
  query: string;
  isFocused: boolean;
}

export default class SearchScreen extends React.Component<NavigationComponentProps, State> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Search',
        },
        largeTitle: {
          visible: true,
        },
        searchBar: {
          visible: true,
          placeholder: 'Start Typing',
          hideTopBarOnFocus: false,
        },
        background: {
          translucent: true,
        },
      },
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    this.state = {
      query: '',
      isFocused: false,
    };
    Navigation.events().bindComponent(this);
  }

  filteredData() {
    return ITEMS.filter(
      (item) => this.state.query.length === 0 || item.key.indexOf(this.state.query) > -1
    );
  }

  highlight(text: string, query: string) {
    if (query.length > 0 && text.indexOf(query) > -1) {
      const before = text.split(query)[0];
      const after = text.split(query)[1];
      return (
        <Text>
          <Text>{before}</Text>
          <Text style={{ backgroundColor: 'yellow' }}>{query}</Text>
          <Text>{after}</Text>
        </Text>
      );
    }
    return text;
  }

  onItemPressed = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        options: {
          topBar: {
            title: {
              text: 'PushedScreen',
            },
            largeTitle: {
              visible: true,
            },
            background: {
              translucent: true,
            },
          },
        },
      },
    });
  };

  render() {
    return (
      <FlatList
        testID={testIDs.SCROLLVIEW_ELEMENT}
        data={this.filteredData()}
        contentContainerStyle={styles.contentContainer}
        renderItem={({ item }) => (
          <TouchableOpacity style={styles.row} onPress={this.onItemPressed}>
            <Text style={styles.rowText} testID={testIDs.SEARCH_RESULT_ITEM}>
              {this.highlight(item.key, this.state.query)}
            </Text>
          </TouchableOpacity>
        )}
      />
    );
  }

  searchBarUpdated({ text, isFocused }: SearchBarUpdatedEvent) {
    this.setState({ query: text, isFocused });
  }
}

const styles = StyleSheet.create({
  contentContainer: {},
  row: {
    height: 60,
    padding: 15,
    justifyContent: 'center',
  },
  rowText: {
    fontSize: 18,
  },
});
