import React from 'react';
import { StyleSheet, ScrollView, View } from 'react-native';
import { NavigationComponentProps, Options } from 'react-native-navigation';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Colors from '../commons/Colors';

interface State {
  topBarHideOnScroll: boolean;
  bottomTabsHideOnScroll: boolean;
}

export default class ScrollViewScreen extends React.Component<NavigationComponentProps, State> {
  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'Hide on scroll',
        },
        drawBehind: true,
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
    topBarHideOnScroll: false,
    bottomTabsHideOnScroll: false,
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
          <Button label="Pop screen" onPress={this.onClickPop} />
          <Button label="Toggle hide BottomTabs on scroll" onPress={this.hideBottomTabsOnScroll} />
        </View>
      </ScrollView>
    );
  }

  onClickToggleTopBarHideOnScroll = () => {
    this.setState({
      topBarHideOnScroll: !this.state.topBarHideOnScroll,
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

  onClickPop() {
    Navigation.pop(this.props.componentId);
  }

  componentDidUpdate() {
    Navigation.mergeOptions(this.props.componentId, {
      topBar: {
        hideOnScroll: this.state.topBarHideOnScroll,
      },
      fab: {
        hideOnScroll: !this.state.topBarHideOnScroll,
      },
    });
  }
}

const styles = StyleSheet.create({
  contentContainer: {
    alignItems: 'center',
    paddingTop: 200,
    height: 1200,
  },
});
