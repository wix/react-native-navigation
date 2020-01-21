const React = require('react');
const { Image, Platform, SafeAreaView, StyleSheet, Text, View } = require('react-native');

class CocktailDetailsScreen extends React.Component {
  static options() {
    return {
      ...Platform.select({
        android: {
          statusBar: {
            style: 'dark',
            backgroundColor: 'white'
          }
        }
      }),
      topBar: {
        title: {
          text: 'Cocktails'
        }
      }
    }
  }

  render() {
    return (
      <SafeAreaView>
        <View style={styles.root}>
          <View nativeID={'backdrop'} style={[styles.header, { backgroundColor: this.props.color }]}>
            <Text style={styles.title} nativeID={`title${this.props.id}Dest`}>{this.props.name}</Text>
          </View>
        </View>
        <Image
          source={this.props.image}
          nativeID={`image${this.props.id}Dest`}
          style={styles.image}
        />
        <Text
          nativeID='description'
          style={styles.description}>
          {this.props.description}
        </Text>
      </SafeAreaView >
    );
  }
}

module.exports = CocktailDetailsScreen;
const SIZE = 120;
const HEADER = 150;
const IMAGE_OFFSET = 52
const styles = StyleSheet.create({
  root: {

  },
  header: {
    height: HEADER,
    width: '100%',
    flexDirection: 'column-reverse'
  },
  title: {
    fontSize: 32,
    color: 'whitesmoke',
    marginLeft: 24,
    marginBottom: 16,
    zIndex: 2
  },
  description: {
    fontSize: 15,
    letterSpacing: 0.2,
    lineHeight: 25,
    marginTop: 32,
    marginHorizontal: 24
  },
  image: {
    height: SIZE,
    width: SIZE,
    position: 'absolute',
    right: 24,
    top: IMAGE_OFFSET
  }
});