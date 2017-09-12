const React = require('react');
const { Component } = require('react');
const { View, Text, Button, Image, TouchableOpacity } = require('react-native');
const Navigation = require('react-native-navigation');

class CustomTransitionOrigin extends Component {
  constructor(props) {
    super(props);
    this.onClickNavigationIcon = this.onClickNavigationIcon.bind(this);
  }
  static get navigationOptions() {
    return {
      topBarTextFontFamily: 'HelveticaNeue-Italic',
    };
  }
  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Custom Transition Screen`}</Text>

          <View style={{flex: 1, justifyContent: 'flex-start'}}>
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image1')}> 
              <Navigation.SharedElement type={"image"} elementId={"image1"}>
                <Image resizeMode={"contain"} style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
              </Navigation.SharedElement>
            </TouchableOpacity> 
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image2')}> 
              <Navigation.SharedElement type={"image"} elementId={"image2"}>
                <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
              </Navigation.SharedElement>
            </TouchableOpacity>
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image3')}> 
              <Navigation.SharedElement type={"image"} elementId={"image3"}>
                <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
              </Navigation.SharedElement>
            </TouchableOpacity> 
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image4')}> 
              <Navigation.SharedElement type={"image"} elementId={"image4"}>
                <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />  
              </Navigation.SharedElement>
            </TouchableOpacity>  
          </View>
        
       
      </View>
    );
  }
  onClickNavigationIcon(elementId) {
      Navigation.push( this.props.containerId, {
          name: 'navigation.playground.CustomTransitionDestination',
          transition: {
            transitions: [{type:"sharedElement", fromId: elementId, toId: "customDestinationImage", interactiveImagePop: true}],
            duration: 0.4
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

