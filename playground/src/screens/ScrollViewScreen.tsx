import React from 'react';
import { StyleSheet, ScrollView, View } from 'react-native';
import { NavigationComponentProps, Options } from 'react-native-navigation';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Colors from '../commons/Colors';

interface State {
  topBarHideOnScroll: boolean;
  bottomTabsHideOnScroll: boolean;
  fabHideOnScroll: boolean;
}

export default class ScrollViewScreen extends React.Component<NavigationComponentProps, State> {
  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'Hide on scroll',
        },
        drawBehind: true,
        hideOnScroll: true,
      },
      bottomTabs: {
        hideOnScroll: true,
      },
      fab: {
        id: 'FAB',
        icon: require('../../img/whatshot.png'),
        iconColor: 'white',
        backgroundColor: Colors.primary,
        clickColor: Colors.primary,
        rippleColor: Colors.accent,
        hideOnScroll: true,
      },
    };
  }

  state = {
    topBarHideOnScroll: true,
    bottomTabsHideOnScroll: true,
    fabHideOnScroll: true,
  };

  render() {
    return (
      <ScrollView
        contentContainerStyle={styles.contentContainer}
        nestedScrollEnabled={this.state.bottomTabsHideOnScroll}
      >
        <View>
          <Button
            label="Toggle Top Bar Hide On Scroll"
            onPress={this.onClickToggleTopBarHideOnScroll}
          />
          <Button label="Toggle hide BottomTabs on scroll" onPress={this.hideBottomTabsOnScroll} />
          <Button label="Toggle hide FAB on scroll" onPress={this.hideFabOnScroll} />
        </View>
      </ScrollView>
    );
  }

  onClickToggleTopBarHideOnScroll = () => {
    const hideOnScroll = !this.state.topBarHideOnScroll;
    this.setState({ topBarHideOnScroll: hideOnScroll });
    Navigation.mergeOptions(this, {
      topBar: {
        hideOnScroll,
      },
    });
  };

  hideBottomTabsOnScroll = () => {
    const hideOnScroll = !this.state.bottomTabsHideOnScroll;
    this.setState({ bottomTabsHideOnScroll: hideOnScroll });
    Navigation.mergeOptions(this, {
      bottomTabs: {
        hideOnScroll,
      },
    });
  };

  hideFabOnScroll = () => {
    const hideOnScroll = !this.state.fabHideOnScroll;
    this.setState({ fabHideOnScroll: hideOnScroll });
    Navigation.mergeOptions(this, {
      fab: {
        id: 'FAB',
        hideOnScroll,
      },
    });
  };
}

const styles = StyleSheet.create({
  contentContainer: {
    alignItems: 'center',
    paddingTop: 250,
    height: 1200,
  },
});
