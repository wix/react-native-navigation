const React = require('react');
const { Component } = require('react');
const { SafeAreaView, FlatList, View, Text } = require('react-native');
const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

const FakeListData = require('../assets/FakeListData');

class FlatListScreen extends Component {
  static options(passProps) {
    return {
      topBar: {
        title: {
          text: 'FlatList with fake data',
        },
        searchBar: true, // iOS 11+ native UISearchBar inside topBar
        searchBarHiddenWhenScrolling: true,
        searchBarPlaceholder: 'Search', // iOS 11+ SearchBar placeholder
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
            icon: require('../images/two.png'),
          }
        ],
        rightButtons: [
          {
            id: 'toggle',
            color: 'red',
            icon: require('../images/one.png'),
          },
        ],
      },
    };
  }

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
    this.state = { isFetching: false, shouldHideOnScroll: false };
  }

  navigationButtonPressed({ buttonId }) {
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
          }
        });
        this.setState({
          shouldHideOnScroll: !shouldHideOnScroll
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
  }

  seperatorComponent = () => <View style={styles.seperatorComponent} />;

  keyExtractor = item => item.id;

  renderItem = ({ item }) => (
    <View style={styles.listItem}>
      <Text>{item.first_name} {item.last_name}</Text>
      <Text>{item.email}</Text>
    </View>
  )

  render() {
    return (
      <SafeAreaView style={styles.root}>
        <FlatList
          data={FakeListData.data}
          keyExtractor={this.keyExtractor}
          onRefresh={this.onRefresh}
          ItemSeparatorComponent={this.seperatorComponent}
          refreshing={this.state.isFetching}
          renderItem={this.renderItem}
        />
      </SafeAreaView>
    );
  }
}
module.exports = FlatListScreen;
const styles = {
  root: {
    flex: 1,
    backgroundColor: 'whitesmoke'
  },
  listItem: {
    height: 50,
  },
  seperatorComponent: {
    height: 5,
    backgroundColor: 'black',
  }
};
