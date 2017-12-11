const React = require('react');
const { Component } = require('react');
const { Text, FlatList } = require('react-native');
const Navigation = require('react-native-navigation');

const FloatingActionButton = Navigation.FloatingActionButton;
const CoordinatorLayout = Navigation.CoordinatorLayout;

class FabScreen extends Component {

  getData() {
    const result = [];
    for (let index = 0; index < 140; index++) {
      result.push({ key: 'Item # ' + index });
    }
    return result;
  }

  render() {
    return (
      <CoordinatorLayout>
        <FlatList
          data={this.getData()}
          keyExtractor={
            (item, index) => {
              return 'index ' + index;
            }
          }
          renderItem={
            ({ item }) => <Text>{item.key}</Text>
          }
        />
        <FloatingActionButton
          elevation={20}
          gravityBottom
          gravityRight
          backgroundColor={'red'}
        />
      </CoordinatorLayout>
    );
  }

  static get navigationOptions() {
    return {
      title: 'Fab test',
      topBarTextColor: 'black',
      topBarTextFontSize: 16,
      topBarTextFontFamily: 'HelveticaNeue-Italic'
    };
  }
}

module.exports = FabScreen;
