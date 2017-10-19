import React, {Component} from 'react';
import {
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Image,
  Text,
  View,
  ScrolView
} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';
import * as Animatable from 'react-native-animatable';

const FADE_DURATION = 500;
const SHOW_DURATION = 500;
const HIDE_DURATION = 500;

const ICON_MARGIN = 24;

class Profiles extends Component {

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.state = {
      animationType: 'fadeIn'
    }
  }

  componentDidMount() {
    if (__STRESS_TEST__) {
      setTimeout(() => {
        this.setState({
          animationType: 'fadeOut'
        });
        this.props.navigator.pop();
      }, 650);
    }
  }

  onNavigatorEvent(event) {
    if (event.id === 'backPress') {
      this.setState({
        animationType: 'fadeOut'
      });
      this.props.navigator.pop();
    }
  }

  _renderHeader() {
    return (
      <Animatable.View
        animation={this.state.animationType}
        duration={FADE_DURATION}
        style={[styles.header, {backgroundColor: this.props.primaryColor}]}
        useNativeDriver={true}
      >
        <View style={{height: ICON_MARGIN, flexDirection: 'row', backgroundColor: 'white'}}/>
        <View style={{flex: 1, flexDirection: 'row'}}>
          {this._renderIcon()}
          {this._renderTitle()}
        </View>
      </Animatable.View>
    );
  }

  _renderTitle() {
    return (
      <View style={{alignSelf: 'flex-end'}}>
        <SharedElementTransition
          style={styles.titleContainer}
          sharedElementId={this.props.sharedTitleId}
          showDuration={SHOW_DURATION}
          hideDuration={HIDE_DURATION}
          showInterpolation={
            {
              type: 'path',
              controlX1: '0.5',
              controlY1: '1',
              controlX2: '0',
              controlY2: '0.5',
              easing: 'FastOutSlowIn'
            }
          }
          hideInterpolation={
            {
              type: 'path',
              controlX1: '0.5',
              controlY1: '0',
              controlX2: '1',
              controlY2: '0.5',
              easing: 'FastOutSlowIn'
            }
          }
        >
          <Text style={[styles.title, {color: this.props.titleColor}]}>{this.props.title}</Text>
        </SharedElementTransition>
      </View>
    );
  }

  _renderContent() {
    return (
      <Animatable.View
        animation={this.state.animationType}
        duration={FADE_DURATION}
        style={styles.body}
        useNativeDriver={true}>
        <View
          style={styles.list}
          initialListSize={20}>
          {this._genRows()}
        </View>
      </Animatable.View>
    );
  }

  _genRows() {
    const children = [];
    for (let ii = 0; ii < 30; ii++) {
      children.push(
        <Text>{'row ' + ii}</Text>
      );
    }
    return children;
  }

  _renderIcon() {
    return (
      <SharedElementTransition
        sharedElementId={this.props.sharedIconId}
        style={styles.iconContainer}
        showDuration={SHOW_DURATION}
        hideDuration={HIDE_DURATION}
        showInterpolation={
          {
            type: 'path',
            controlX1: '0.5',
            controlY1: '1',
            controlX2: '0',
            controlY2: '0.5',
            easing: 'FastOutSlowIn'
          }
        }
        hideInterpolation={
          {
            type: 'path',
            controlX1: '0.5',
            controlY1: '0',
            controlX2: '1',
            controlY2: '0.5',
            easing: 'FastOutSlowIn'
          }
        }
      >
        <Image
          source={this.props.icon}
          style={styles.heroIcon}
          fadeDuration={0}
        />
      </SharedElementTransition>
    );
  }

  render() {
    return (
      <ScrollView style={[styles.container]}>
        {this._renderHeader()}
        {this._renderContent()}
      </ScrollView>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: 'transparent'
  },
  header: {
    height: 110,
    flexDirection: 'column-reverse'
  },
  titleContainer: {
    marginLeft: ICON_MARGIN + 90 + +16,
    marginBottom: 8
  },
  title: {
    fontSize: 23,
  },
  iconContainer: {
    position: 'absolute',
    bottom: -ICON_MARGIN,
    left: ICON_MARGIN
  },
  heroIcon: {
    width: 90,
    height: 90,
    resizeMode: 'contain'
  },
  body: {
    flex: 4,
    backgroundColor: 'white',
  },
  list: {
    marginTop: 16,
    marginHorizontal: 8
  }
});

export default Profiles;
