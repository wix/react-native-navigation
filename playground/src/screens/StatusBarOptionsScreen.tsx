import React from 'react';
import { StyleSheet, Image, View, Text } from 'react-native';
import { ColorPalette, Switch } from 'react-native-ui-lib';
import { NavigationComponentProps, Options } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';

interface State {
  statusBarVisible: boolean;
  navigationBarVisible: boolean;
  translucent: boolean;
  darkStatusBarScheme: boolean;
  drawBehind: boolean;
  selectedColor: string;
}
const colors = [
  '#000000',
  '#20303C',
  '#3182C8',
  '#00AAAF',
  '#00A65F',
  '#E2902B',
  '#D9644A',
  '#CF262F',
  '#8B1079',
];

export default class StatusBarOptions extends React.Component<NavigationComponentProps, State> {
  static options(): Options {
    return {
      statusBar: {
        translucent: true,
        style: 'dark',
        drawBehind: false,
        backgroundColor: '#000000',
      },
      topBar: {
        elevation: 3,
        drawBehind: true,
        background: {
          color: 'argb(44,255,0,0)',
        },
        title: {
          text: 'StatusBar Options',
          color: 'white',
        },
        backButton: {
          color: 'white',
        },
      },
    };
  }

  constructor(props: Readonly<NavigationComponentProps>) {
    super(props);
    this.state = {
      navigationBarVisible: true,
      statusBarVisible: true,
      translucent: true,
      darkStatusBarScheme: true,
      drawBehind: false,
      selectedColor: '#000000',
    };
  }

  render() {
    return (
      <View style={style.container}>
        <Image style={style.image} source={require('../../img/city.png')} fadeDuration={0} />
        <Root componentId={this.props.componentId} style={style.root}>
          {/*<Button label="Full Screen Modal" onPress={this.fullScreenModal} />*/}
          {/*<Button label="Push" onPress={this.push} />*/}
          <Text>Status Bar Color</Text>
          <ColorPalette
            value={this.state.selectedColor}
            onValueChange={this.onPaletteValueChange}
            colors={colors}
          />
          <View style={style.translucentSwitch}>
            <Text>Translucent: </Text>
            <Switch value={this.state.translucent} onValueChange={this.onTranslucentChanged} />
          </View>

          <View style={style.translucentSwitch}>
            <Text>Light Status Bar Icons: </Text>
            <Switch
              value={this.state.darkStatusBarScheme}
              onValueChange={this.toggleStatusBarColorScheme}
            />
          </View>
          <View style={style.translucentSwitch}>
            <Text>Draw Behind: </Text>
            <Switch value={this.state.drawBehind} onValueChange={this.onDrawBehindValueChanged} />
          </View>
          <View style={style.translucentSwitch}>
            <Text>StatusBar Visible: </Text>
            <Switch
              value={this.state.statusBarVisible}
              onValueChange={this.onStatusBarVisibilityValueChanged}
            />
          </View>
          <View style={style.translucentSwitch}>
            <Text>NavigationBar Visible: </Text>
            <Switch
              value={this.state.navigationBarVisible}
              onValueChange={this.onNavBarVisibilityValueChanged}
            />
          </View>
          <Button label="BottomTabs" onPress={this.bottomTabs} />
          <Button label="Open Left" onPress={() => this.open('left')} />
          <Button label="Open Right" onPress={() => this.open('right')} />
        </Root>
      </View>
    );
  }
  onPaletteValueChange = (value: string, _: object) => {
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        backgroundColor: value,
      },
    });
    this.setState({ selectedColor: value });
  };

  onTranslucentChanged = (value: boolean) => {
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        translucent: value,
      },
    });
    this.setState({ translucent: value });
  };
  toggleStatusBarColorScheme = (value: boolean) => {
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        style: value ? 'dark' : 'light',
      },
    });
    this.setState({ darkStatusBarScheme: value });
  };

  onDrawBehindValueChanged = (value: boolean) => {
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        drawBehind: value,
      },
    });
    this.setState({ drawBehind: value });
  };
  onStatusBarVisibilityValueChanged = (value: boolean) => {
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        visible: value,
      },
    });
    this.setState({ statusBarVisible: value });
  };
  onNavBarVisibilityValueChanged = (value: boolean) => {
    Navigation.mergeOptions(this.props.componentId, {
      navigationBar: {
        visible: value,
      },
    });
    this.setState({ navigationBarVisible: value });
  };

  fullScreenModal = () => Navigation.showModal(Screens.FullScreenModal);
  push = () => Navigation.push(this, Screens.Pushed);
  bottomTabs = () => Navigation.showModal(Screens.StatusBarBottomTabs);
  open = (side: 'left' | 'right') =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: { visible: true },
      },
    });
}

const style = StyleSheet.create({
  root: {
    paddingTop: 0,
    paddingHorizontal: 0,
  },
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  translucentSwitch: { flexDirection: 'row' },
  image: {
    height: 250,
    width: '100%',
    resizeMode: 'cover',
    marginBottom: 16,
  },
});
