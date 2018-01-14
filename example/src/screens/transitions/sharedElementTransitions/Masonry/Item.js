import React from 'react';
import {StyleSheet, View, Text, Image} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';

const SHOW_DURATION = 240;
const HIDE_DURATION = 200;

class Item extends React.Component {

  static navigatorStyle = {
    navBarHidden: true,
    drawUnderNavBar: true
  };

  render() {
    return (
      <View style={styles.container}>
        <SharedElementTransition
          sharedElementId={this.props.sharedImageId}
          showDuration={SHOW_DURATION}
          hideDuration={HIDE_DURATION}
          animateClipBounds
          showInterpolation={{
            type: 'linear',
            easing: 'FastInSlowOut',
          }}
          hideInterpolation={{
            type: 'linear',
            easing: 'FastOutSlowIn',
          }}
        >
          <Image
            style={styles.image}
            source={this.props.image}
          />
        </SharedElementTransition>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
    justifyContent: 'center',
  },
  image: {
    width: 400,
    height: 400,
  }
});

export default Item;
