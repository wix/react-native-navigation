import React from 'react';
import {ScrollView, TouchableHighlight, StyleSheet, Image, Text, View, ScrolView} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';

const IMAGE_HEIGHT = 190;

class CardScreen extends React.Component {

  goToCard = (index) => {
    this.props.navigator.push({
      screen: 'example.Transitions.SharedElementTransitions.Cards.Info',
      sharedElements: [`image${index}`],
      animated: false,
      overrideBackPress: true,
      passProps: {
        sharedImageId: `image${index}`
      }
    })
  };

  _renderCard(index) {
    return (
      <View style={styles.cardContainer}>
        <TouchableHighlight
          underlayColor={'rgba(0, 0, 0, 0.054)'}
          onPress={() => this.goToCard(index)}
        >
          <View>
            {this._renderImage(index)}
            {this._renderCardContent()}
          </View>
        </TouchableHighlight>
      </View>
    );
  }

  _renderImage(index) {
    return (
      <SharedElementTransition
        style={styles.imageContainer}
        sharedElementId={`image${index}`}
      >
        <Image
          style={styles.image}
          source={require('../../../../../img/beach.jpg')}
        />
      </SharedElementTransition>
    );
  }

  _renderCardContent() {
    return (
      <View style={styles.cardContentContainer}>
        <Text style={styles.title}>This is a title</Text>
        <Text>This is a very long long long long long long long long long long content</Text>
      </View>
    );
  }

  render() {
    return (
      <ScrollView
        style={styles.container}
        contentContainerStyle={styles.content}
      >
        {this._renderCard(0)}
        {this._renderCard(1)}
        {this._renderCard(2)}
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  content: {
    marginHorizontal: 8,
  },
  cardContainer: {
    marginVertical: 8,
    elevation: 2,
    borderRadius: 2,
    backgroundColor: '#F5F5F5'
  },
  imageContainer: {
    justifyContent: 'flex-start'
  },
  image: {
    height: IMAGE_HEIGHT,
    borderTopLeftRadius: 2,
    borderTopRightRadius: 2
  },
  cardContentContainer: {
    padding: 8
  },
  title: {
    fontWeight: '500',
    paddingBottom: 8,
    fontSize: 17
  },
});

export default CardScreen;
