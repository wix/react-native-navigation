const React = require('react');
const {Component} = require('react');
const {View, Text, Image, TouchableOpacity} = require('react-native');
const {Navigation} = require('react-native-navigation');
const Root = require('../components/Root');

import ViewOverflow from 'react-native-view-overflow';
import {SafeAreaView} from 'react-native';
class CustomTransitionOrigin extends Component {
  constructor(props) {
    super(props);
    this.onClickNavigationIcon = this.onClickNavigationIcon.bind(this);
  }
  static options() {
    return {
      topBar: {
        title: {
          fontFamily: 'HelveticaNeue-Italic',
          fontSize: 16
        },
        largeTitle: {
          visible: false
        },
        background: {
          translucent: true
        }
      }
    };
  }
  render() {
    return (
      <SafeAreaView style={styles.root}>
        <TouchableOpacity testID='shared_image1' activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
            <Text testID={`title1`} nativeID={`title1`} style={[styles.h1]}>Custom Transition Screen</Text>
        </TouchableOpacity>
        <View style={{flex: 1, justifyContent: 'flex-start'}}>
          <TouchableOpacity testID='shared_image1' activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
              <Image testID={`image1`} nativeID={`image1`} resizeMode='cover' style={styles.gyroImage} source={require('../../img/400.jpeg')} />
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    );
  }
  onClickNavigationIcon() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.CustomTransitionDestination',
        options: {
          animations: {
            push: {
              screen: {
                alpha: {
                  from: 0,
                  to: 1,
                  duration: 250
                }
              },
              elements: {
                shared: [
                  {fromId: 'title1', toId: 'title2'},
                  {fromId: 'image1', toId: 'customDestinationImage'}
                ],
                transition: [
                  {id: 'kjdf'}
                ]
              }
            }
          }
        }
      }
    });
  }
}
module.exports = CustomTransitionOrigin;

const styles = {
  root: {
    alignItems: 'center',
    flex: 1,
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center'
  },
  footer: {
    fontSize: 10,
    color: '#888'
  },
  gyroImage: {
    width: 100,
    height: 100
  }
};
