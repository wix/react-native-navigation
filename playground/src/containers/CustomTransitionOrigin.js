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
        <Navigation.SharedElement elementId={"title1"}>
        <Text style={styles.h1}>{`Custom Transition Screen`}</Text>
        </Navigation.SharedElement>
          <View style={{flex: 1, justifyContent: 'flex-start'}}>
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image1')}> 
              <Navigation.SharedElement type={"image"} resizeMode={"cover"}  elementId={"image1"}>
                <Image resizeMode={"cover"} style={styles.gyroImage} source={require('../../img/400.jpeg')} />
              </Navigation.SharedElement>
            </TouchableOpacity> 
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image2')}> 
              <Navigation.SharedElement   elementId={"image2"}>
                <Image style={styles.gyroImage} source={require('../../img/2048.jpeg')} />
              </Navigation.SharedElement>
            </TouchableOpacity>
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image3')}> 
              <Navigation.SharedElement  elementId={"image3"}>
                <Image style={styles.gyroImage} source={require('../../img/Icon-87.png')} />
              </Navigation.SharedElement>
            </TouchableOpacity> 
            <TouchableOpacity activeOpacity={0.5} onPress={() => this.onClickNavigationIcon('image4')}> 
              <Navigation.SharedElement  elementId={"image4"}>
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
          customTransition: {
            animations: [
              { type:"sharedElement", fromId: "title1", toId: "title2", startDelay: 0, springVelocity: 0.2, duration:0.5},            
              {type:"sharedElement", fromId: "image1", toId: "customDestinationImage", startDelay: 0, springVelocity: 0.9, springDamping:0.9, duration:0.8 ,interactiveImagePop: true},
              {type:"sharedElement", fromId: "image2", toId: "customDestinationImage2", startDelay: 0, duration:0.8 },
              { fromId:'image4', endY:50, endX:50, endAlpha: 0, startDelay: 0, duration:0.8, springVelocity: 0.5 },
              { fromId:'customDestinationParagraph', startY:50, startX:50, startAlpha: 0, endAlpha: 1,  startDelay: 0, duration:0.8 }
                          
            ],
            duration: 0.8
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

