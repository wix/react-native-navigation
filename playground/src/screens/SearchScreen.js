const React = require('react');
const { Component } = require('react');

const {
  StyleSheet,
  FlatList,
  View,
  Button,
  Text,
  Platform
} = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

const ITEMS = [...Array(200).keys()].map(key => ({ key: `Item ${key}` }));

class SearchControllerScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: {
          text: 'Search'
        },
        largeTitle: {
          visible: true
        },
        searchBar: true,
        searchBarHiddenWhenScrolling: true,
        translucent: true,
        searchBarPlaceholder: 'Start Typing'
      }
    };
  }

  constructor(props) {
    super(props);
    this.state = {
      query: ''
    };
  }

  filteredData() {
    return ITEMS.filter(
      item =>
        this.state.query.length === 0 || item.key.indexOf(this.state.query) > -1
    );
  }

  highlight(text, query) {
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

  render() {
    return (
      <FlatList
        testID={testIDs.SCROLLVIEW_ELEMENT}
        data={this.filteredData()}
        contentContainerStyle={styles.contentContainer}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.rowText} testID={testIDs.SEARCH_RESULT_ITEM}>
              {this.highlight(item.key, this.state.query)}
            </Text>
          </View>
        )}
      />
    );
  }

  onSearchBarUpdated(query, isFocused) {
    this.setState({ query, isFocused });
  }
}

module.exports = SearchControllerScreen;

const styles = StyleSheet.create({
  contentContainer: {},
  row: {
    height: 50,
    padding: 20,
    justifyContent: 'center'
  },
  rowText: {
    fontSize: 18
  }
});
