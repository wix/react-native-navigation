const React = require('react');
const { Component } = require('react');
const { TouchableOpacity, FlatList, View, Image, Text, StyleSheet } = require('react-native');
const { slice } = require('lodash');
const data = require('../../assets/cocktails').default;

class CocktailsView extends Component {
  render() {
    return (
      <FlatList
        style={styles.root}
        data={data}
        keyExtractor={this.keyExtractor}
        ItemSeparatorComponent={this.separatorComponent}
        renderItem={this.renderItem}
      />
    );
  }

  separatorComponent = () => <View style={styles.separator} />;

  renderItem = ({ item }) => (
    <TouchableOpacity
      activeOpacity={0.75}
      style={styles.itemContainer}
      testID={item.id}
      onPress={() => this.props.onItemPress(item)}
      onLongPress={() => this.props.onItemLongPress(item)}>
      <View style={styles.overlayContainer}>
        <Image
          source={item.image}
          nativeID={`image${item.id}`}
          style={styles.image}
          resizeMode={'contain'}
        />
        <View nativeID={`backdrop${item.id}`} style={[styles.backdrop, { backgroundColor: '#aaaaaa' }]} />
      </View>
      <View style={styles.textContainer}>
        <Text style={styles.title} nativeID={`title${item.id}`}>{item.name}</Text>
        <View style={{ flexDirection: 'row' }}>
          <Text style={styles.ingredients}>{slice(item.ingredients, 0, 3).map(i => i.name).join(' • ')}</Text>
        </View>
      </View>
    </TouchableOpacity>
  )

  keyExtractor = item => item.id;
}

module.exports = CocktailsView;
const SIZE = 150;
const styles = StyleSheet.create({
  root: {
    paddingTop: 16
  },
  itemContainer: {
    backgroundColor: 'white',
    marginLeft: 16,
    marginRight: 16,
    height: SIZE,
    flexDirection: 'row',
    padding: 16,
    elevation: 4
  },
  image: {
    backgroundColor: 'white',
    height: '100%',
    width: 118,
    zIndex: 1,
  },
  backdrop: {
    width: 118,
    height: 118,
    backgroundColor: 'green',
    marginTop: -112,
    marginLeft: 6
  },
  overlayContainer: {
  },
  textContainer: {
    flex: 1,
    marginLeft: 16,
  },
  title: {
    fontSize: 22
  },
  ingredients: {
    fontSize: 12,
    marginTop: 8
  },
  separator: {
    height: 16
  }
});
