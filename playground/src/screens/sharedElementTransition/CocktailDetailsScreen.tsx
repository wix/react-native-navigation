import React from 'react';
import { Image, Platform, ScrollView, StyleSheet, Text, View } from 'react-native';
import {
  NavigationComponentProps,
  NavigationButtonPressedEvent,
  Options,
} from 'react-native-navigation';
import Screens from '../Screens';
import Navigation from '../../services/Navigation';
import { CocktailItem } from '../../assets/cocktails';
import testIDs from '../../testIDs';

const { COCKTAILS_DETAILS_HEADER, PUSH_DETAILS_BTN } = testIDs;

interface Props extends NavigationComponentProps, CocktailItem {}

export default class CocktailDetailsScreen extends React.Component<Props> {
  static options(): Options {
    return {
      ...Platform.select({
        android: {
          statusBar: {
            style: 'dark',
            backgroundColor: 'white',
          },
        },
      }),
      topBar: {
        title: {
          text: 'Cocktail Details',
        },
        rightButtons: [
          {
            id: 'pushDetails',
            testID: PUSH_DETAILS_BTN,
            text: 'push',
          },
        ],
      },
    };
  }

  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    if (buttonId === 'pushDetails') {
      Navigation.push(this, Screens.Pushed);
    }
  }

  render() {
    return (
      <ScrollView style={styles.root}>
        <View
          nativeID={'backdrop'}
          style={[styles.backdrop, { backgroundColor: this.props.color }]}
        />
        <View style={styles.header} testID={COCKTAILS_DETAILS_HEADER}>
          <Image
            source={this.props.image}
            // @ts-ignore nativeID isn't included in react-native Image props.
            nativeID={`image${this.props.id}Dest`}
            style={styles.image}
          />
          <Text style={styles.title} nativeID={`title${this.props.id}Dest`}>
            {this.props.name}
          </Text>
        </View>
        <Text nativeID="description" style={styles.description}>
          {this.props.description}
        </Text>
      </ScrollView>
    );
  }
}

const SIZE = 100;
const HEADER = 150;
const styles = StyleSheet.create({
  root: {
    marginTop: 0,
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
    zIndex: 0,
  },
  title: {
    fontSize: 32,
    color: 'whitesmoke',
    marginLeft: 16,
    marginBottom: 16,
    zIndex: 2,
  },
  description: {
    fontSize: 15,
    letterSpacing: 0.2,
    lineHeight: 25,
    marginTop: 32,
    marginHorizontal: 24,
  },
  image: {
    height: SIZE,
    width: SIZE,
    zIndex: 1,
    // transform: [{ rotate: '45deg' }],
    marginLeft: 24,
    marginBottom: -24,
    // borderRadius: 20,
  },
});
