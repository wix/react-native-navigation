import React from 'react';
import { StyleSheet, Image, View, Text, TextInput } from 'react-native';
import { ColorPalette, Switch } from 'react-native-ui-lib';
import { NavigationComponentProps, Options } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

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
  '#00AAAF00',
  '#20303C',
  '#3182C8',
  '#00AAAF',
  '#00A65F',
  '#E2902B',
  '#D9644A',
  '#CF262F',
  '#8B1079',
];

export default class SystemUiOptions extends React.Component<NavigationComponentProps, State> {
  static options(): Options {
    return {
      statusBar: {
        translucent: true,
        style: 'dark',
        drawBehind: false,
        backgroundColor: '#000000',
      },
      navigationBar: {
        backgroundColor: '#000000',
      },
      topBar: {
        drawBehind: true,
        elevation: 0,
        background: {
          color: 'transparent',
        },
        title: {
          text: 'System UI Options',
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
        <Root componentId={this.props.componentId} style={style.root}>
          <Image style={style.image} source={require('../../img/city.png')} fadeDuration={0} />

          <Text>Navigation & Status Bar Color</Text>
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
          <TextInput
            style={style.input}
            testID={testIDs.TEXT_INPUT1}
            placeholderTextColor="rgba(255, 0, 0, 0.5)"
            placeholder="Submit opens modal"
            onSubmitEditing={async (event) => {
              if (event.nativeEvent.text || event.nativeEvent.text.trim().length > 0)
                console.warn('Submitted!!');
            }}
          />
          <Button label="Full Screen Modal" onPress={this.fullScreenModal} />
          <Button label="Push" onPress={this.push} />
          <Button label="Show Overlay" onPress={this.showOverlay} />
          <Button label="BottomTabs" onPress={this.bottomTabs} />
          <Button label="Set As Root" onPress={this.setAsRoot} />
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
      navigationBar: {
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

  setAsRoot = async () => {
    await Navigation.setRoot({
      stack: {
        options: {
          statusBar: {
            translucent: this.state.translucent,
            style: this.state.darkStatusBarScheme ? 'dark' : 'light',
            drawBehind: this.state.drawBehind,
            backgroundColor: this.state.selectedColor,
          },
        },
        children: [
          {
            component: {
              name: Screens.SystemUiOptions,
            },
          },
        ],
      },
    });
  };
  fullScreenModal = () => Navigation.showModal(Screens.FullScreenModal);
  push = () => Navigation.push(this, Screens.Pushed);
  showOverlay = () =>
    Navigation.showOverlay({
      component: {
        name: Screens.Alert,
        options: {
          statusBar: {
            drawBehind: true,
            backgroundColor: '#3e434aa1',
            style: 'light',
            translucent: true,
          },
        },
        passProps: {
          title: 'Title',
          message: 'Message',
        },
      },
    });
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
  input: {
    color: 'red',
  },
  translucentSwitch: { flexDirection: 'row' },
  image: {
    height: 250,
    width: '100%',
    resizeMode: 'cover',
    marginBottom: 16,
  },
});
