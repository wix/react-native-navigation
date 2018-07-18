const React = require('react');
const { Component } = require('react');
const { View, Text, Image, TouchableOpacity } = require('react-native');
const { Navigation } = require('react-native-navigation');

class CustomTransitionOrigin extends Component {
  constructor(props) {
    super(props);
    this.onClickNavigationIcon = this.onClickNavigationIcon.bind(this);
  }
  static get options() {
    return {
      topBar: {
        title: {
          fontFamily: 'HelveticaNeue-Italic',
          fontSize: 16
        },
        largeTitle: {
          visible: false
        },
        translucent: true
      }
    };
  }
  render() {
    return (
      <View style={styles.root}>
        <Navigation.Element elementId='title1'>
          <Text style={styles.h1}>Custom Transition Screen</Text>
        </Navigation.Element>
        <View style={{ flex: 1, justifyContent: 'flex-start' }}>
          <TouchableOpacity testID='shared_image1' activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
            <Navigation.Element resizeMode='cover' elementId='image1'>
              <Image resizeMode='cover' style={styles.gyroImage} source={require('../../img/400.jpeg')} />
            </Navigation.Element>
          </TouchableOpacity>
          <TouchableOpacity activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
            <Navigation.Element elementId='image2'>
              <Image style={styles.gyroImage} source={require('../../img/2048.jpeg')} />
            </Navigation.Element>
          </TouchableOpacity>
          <TouchableOpacity activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
            <Navigation.Element elementId='image3'>
              <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
            </Navigation.Element>
          </TouchableOpacity>
          <TouchableOpacity activeOpacity={0.5} onPress={this.onClickNavigationIcon}>
            <Navigation.Element elementId='image4'>
              <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
            </Navigation.Element>
          </TouchableOpacity>
        </View>

      </View>
    );
  }
  onClickNavigationIcon() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.CustomTransitionDestination',
        options: {
          customTransition: {
            animations: [
              { type: 'sharedElement', fromId: 'title1', toId: 'title2', startDelay: 0, springVelocity: 0.2, duration: 0.5 },
              { type: 'sharedElement', fromId: 'image1', toId: 'customDestinationImage', startDelay: 0, springVelocity: 0.9,
              springDamping: 0.9, duration: 0.8, interactivePop: true },
              { type: 'sharedElement', fromId: 'image2', toId: 'customDestinationImage2', startDelay: 0, duration: 0.8 },
              { fromId: 'image4', x: { to: 50 }, y: { to: 50 }, endAlpha: 0, startDelay: 0, duration: 0.8, springVelocity: 0.5 },
              { fromId: 'customDestinationParagraph', startY: 50, startAlpha: 0, endAlpha: 1, startDelay: 0, duration: 0.8 }
            ],
            duration: 0.8
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
    flexGrow: 1,
    backgroundColor: '#f5fcff'
  },
  h1: {

    fontSize: 24,
    textAlign: 'center',
    marginTop: 100
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  },
  gyroImage: {
    marginTop: 10,
    width: 100,
    height: 100
  }
};
