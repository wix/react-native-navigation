const React = require('react');
const { Image, Platform, ScrollView, StyleSheet, Text, View } = require('react-native');
const {
  COCKTAILS_DETAILS_HEADER,
  PUSH_DETAILS_BTN
} = require('../../testIDs');
const Screens = require('../Screens');
const Navigation = require('../../services/Navigation');

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
          text: 'Cocktail Details'
        },
        rightButtons: [{
          id: 'pushDetails',
          testID: PUSH_DETAILS_BTN,
          text: 'push'
        }]
      }
    }
  }

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({buttonId}) {
    if (buttonId === 'pushDetails') {
      Navigation.push(this, Screens.Pushed)
    }
  }

  render() {
    return (
      <ScrollView style={styles.root}>
        <View nativeID={'backdrop'} style={[styles.backdrop, { backgroundColor: this.props.color }]}/> 
        <View style={styles.header} testID={COCKTAILS_DETAILS_HEADER}>
          <Image
            source={this.props.image}
            nativeID={`image${this.props.id}Dest`}
            style={styles.image}
          />
          <Text style={styles.title} nativeID={`title${this.props.id}Dest`}>{this.props.name}</Text>
        </View>
        <Text
          nativeID='description'
          style={styles.description}>
          {this.props.description}
        </Text>
      </ScrollView >
    );
  }
}

module.exports = CocktailDetailsScreen;
const SIZE = 120;
const HEADER = 150;
const styles = StyleSheet.create({
  root: {
    marginTop: 0
  },
  header: {
    marginTop: -HEADER,
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: HEADER,
  },
  backdrop: {
    height: HEADER,
    width: '100%',
    zIndex: 0
  },
  title: {
    fontSize: 32,
    color: 'whitesmoke',
    marginLeft: 16,
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
    zIndex: 1,
    marginLeft: 24,
    marginBottom: -24
  }
});