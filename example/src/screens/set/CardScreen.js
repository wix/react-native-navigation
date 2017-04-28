import React, {Component} from 'react';
import {
  ScrollView,
  TouchableWithoutFeedback,
  TouchableNativeFeedback,
  StyleSheet,
  Image,
  Text,
  View,
  Platform,
  ScrolView
} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';
import * as Animatable from 'react-native-animatable';
import * as setStyles from './styles';

const IMAGE_HEIGHT = 190;

export default class CardScreen extends Component {
  static navigatorStyle = {
    ...setStyles.navigatorStyle
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <ScrollView>
        <View style={[styles.container]}>
          {this._renderCard(0)}
          {this._renderCard(1)}
          {this._renderCard(2)}
        </View>
      </ScrollView>
    );
  }

  _renderCard(index) {
    return (
      <View style={styles.cardContainer}>
        <TouchableWithoutFeedback onPress={() => this._onCardPress(index)}>
          {this._renderImage(index)}
        </TouchableWithoutFeedback>
        <TouchableNativeFeedback onPress={() => this._onCardPress(index)}>
          {this._renderCardContent()}
        </TouchableNativeFeedback>
      </View>
    );
  }

  _onCardPress(index) {
    this.props.navigator.push({
      screen: 'example.infoScreen',
      _title: 'Shared Element Transition',
      sharedElements: [`image${index}`],
      animated: false,
      overrideBackPress: true,
      passProps: {
        sharedImageId: `image${index}`
      }
    })
  }

  _renderImage(index) {
    return (
      <SharedElementTransition
        style={styles.imageContainer}
        sharedElementId={`image${index}`}
      >
        <Image
          style={styles.image}
          source={require('../../../img/beach.jpg')}
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
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 16,
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
  }
});