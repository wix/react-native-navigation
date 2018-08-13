const React = require('react');
const { Component } = require('react');
const { View, TouchableOpacity, Image, Text } = require('react-native');
const { Navigation } = require('react-native-navigation');
import ViewOverflow from 'react-native-view-overflow';

class CustomTransitionDestination extends Component {
  constructor(props) {
    super(props);
    this.pop = this.pop.bind(this);
    this.push = this.push.bind(this);
  }

  static get options() {
    return {
      topBar: {
        title: {
          text: 'Shared Element Transition',
          fontFamily: 'HelveticaNeue-Italic'
        },
        backButton: {
          transition: 'custom'
        },
        largeTitle: {
          visible: false
        }
      },
      animations: {
        pop: {
          content: {
            alpha: {
              from: 1,
              to: 0,
              duration: 250
            }
          }
        }
      }
    };
  }
  push() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.OptionsScreen'
      }
    });
  }
  pop() {
    Navigation.pop(this.props.componentId, {
      customTransition: {
        animations: [
          { type: 'sharedElement', fromId: 'title2', toId: 'title1', startDelay: 0, springVelocity: 0.2, duration: 0.5 },
          { type: 'sharedElement', toId: 'image1', fromId: 'customDestinationImage', startDelay: 0, springVelocity: 0.2, duration: 0.5 },
          { type: 'sharedElement', toId: 'image2', fromId: 'customDestinationImage2', startDelay: 0, duration: 0.8 },
          { fromId: 'image4', x: { from: 50 }, y: { from: 50 }, startAlpha: 0, startDelay: 0, duration: 0.8, springVelocity: 0.5 },
          { fromId: 'customDestinationParagraph', y: { to: 50 }, x: { to: 50 }, endAlpha: 0, startAlpha: 1, startDelay: 0, duration: 0.8 }
        ],
        duration: 0.8
      }
    });
  }
  render() {
    return (
      <View style={styles.root}>
        <View>
          <Navigation.Element resizeMode={'contain'} elementId={'customDestinationImage'}>
            <Image resizeMode={'contain'} style={{ width: 300, height: 300 }} source={require('../../img/400.jpeg')} />
          </Navigation.Element>

          <View style={{ width: 120, height: 120, margin: 15, alignItems: 'center', justifyContent: 'center' }}>
            <Navigation.Element elementId={'customDestinationImage2'} style={{ width: 100, height: 100, zIndex: 1 }}>
              <Image resizeMode={'contain'} style={{ width: 100, height: 100 }} source={require('../../img/2048.jpeg')} />
            </Navigation.Element>
            <ViewOverflow>
              <Navigation.Element elementId='image2bgDestination' style={{ width: 120, height: 120, marginTop: -110 }}>
                <View style={{ width: 120, height: 120, backgroundColor: 'yellow' }} />
              </Navigation.Element>
            </ViewOverflow>
          </View>
        </View>

        <TouchableOpacity testID={'shared_image2'} onPress={this.pop}>
          <Navigation.Element elementId={'title2'}>
            <Text style={[{color: 'red', textAlign: 'center'}, styles.h1]}>{`Custom Transition Screen`}</Text>
          </Navigation.Element>
        </TouchableOpacity>
        <Navigation.Element elementId={'customDestinationParagraph'}>
          <Text style={styles.p}>{`Lorem ipsum dolor sit amet, consectetur adipiscing elit,
           sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
           Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
           nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit
           in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat
          cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum`}
          </Text>

        </Navigation.Element>
      </View>
    );
  }
}
module.exports = CustomTransitionDestination;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'left',
    margin: 10
  },
  p: {
    fontSize: 14,
    margin: 10,
    textAlign: 'left'
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};
