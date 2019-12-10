const React = require('react');
const { StyleSheet, Image, View } = require('react-native');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');

class StatusBarOptions extends React.Component {
  static options() {
    return ({
      statusBar: {
        translucent: true,
        drawBehind: true
      },
      topBar: {
        elevation: 0,
        drawBehind: true,
        background: {
          color: 'transparent'
        },
        title: {
          text: 'StatusBar Options',
          color: 'white'
        },
        backButton: {
          color: 'white'
        }
      }
    });
  }

  render() {
    return (
      <View style={style.container}>
        <Image
            style={style.image}
            source={require('../../img/city.png')}
            fadeDuration={0}
          />
        <Root componentId={this.props.componentId} style={style.root}>
          <Button label='Full Screen Modal' onPress={this.fullScreenModal} />
          <Button label='Push' onPress={this.push} />
          <Button label='BottomTabs' onPress={this.bottomTabs} />
          <Button label='Open Left' onPress={() => this.open('left')} />
          <Button label='Open Right' onPress={() => this.open('right')} />
        </Root>
      </View>
    );
  }

  fullScreenModal = () => Navigation.showModal(Screens.FullScreenModal);
  push = () => Navigation.push(this, Screens.Pushed);
  bottomTabs = () => Navigation.showModal(Screens.StatusBarBottomTabs);
  open = (side) => Navigation.mergeOptions(this, {
    sideMenu: {
      [side]: { visible: true }
    }
  });
  
}

const style = StyleSheet.create({
  root: {
    paddingTop: 0,
    paddingHorizontal: 0
  },
  container: {
    flex: 1,
    flexDirection: 'column'
  },
  image: {
    height: 250,
    width: '100%',
    resizeMode: 'cover',
    marginBottom: 16
  }
});

module.exports = StatusBarOptions;