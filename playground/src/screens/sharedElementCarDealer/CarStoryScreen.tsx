import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import {
  Navigation,
  NavigationFunctionComponent,
  OptionsModalPresentationStyle,
  OptionsModalTransitionStyle,
} from 'react-native-navigation';
import { CarItem } from '../../assets/cars';
import Reanimated, { EasingNode } from 'react-native-reanimated';
import DismissableView from './DismissableView';
import useDismissGesture from './useDismissGesture';
import { SET_DURATION } from './Constants';
import colors from '../../commons/Colors';
const ReanimatedTouchableOpacity = Reanimated.createAnimatedComponent(TouchableOpacity);

interface Props {
  car: CarItem;
}

const CarStoryScreen: NavigationFunctionComponent<Props> = ({ car, componentId }) => {
  const isClosing = useRef(false);

  const onClosePressed = useCallback(() => {
    if (isClosing.current === true) return;
    isClosing.current = true;
    Navigation.dismissModal(componentId);
  }, [componentId]);
  const dismissGesture = useDismissGesture(onClosePressed);

  const closeButtonStyle = useMemo(
    () => [styles.closeButton, { opacity: dismissGesture.controlsOpacity }],
    [dismissGesture.controlsOpacity]
  );

  useEffect(() => {
    setTimeout(() => {
      Reanimated.timing(dismissGesture.controlsOpacity, {
        toValue: 1,
        duration: 300,
        easing: EasingNode.linear,
      }).start();
    }, SET_DURATION);
  }, [dismissGesture.controlsOpacity]);

  return (
    <DismissableView dismissGestureState={dismissGesture} style={styles.container}>
      <View style={styles.background} nativeID={`story.${car.id}.background.to`} />
      <Text style={styles.carIcon} nativeID={`story.${car.id}.icon.to`}>
        {car.name.charAt(0)}
      </Text>
      <Text
        style={styles.carName}
        nativeID={`story.${car.id}.title.to`}
        numberOfLines={3}
        lineBreakMode="tail"
        ellipsizeMode="tail"
      >
        {car.name}
      </Text>
      <Text
        style={styles.carDescription}
        numberOfLines={3}
        lineBreakMode="tail"
        ellipsizeMode="tail"
      >
        {car.description}
      </Text>
      <ReanimatedTouchableOpacity style={closeButtonStyle} onPress={onClosePressed}>
        <Text style={styles.closeButtonText}>x</Text>
      </ReanimatedTouchableOpacity>
    </DismissableView>
  );
};
CarStoryScreen.options = {
  statusBar: {
    visible: false,
  },
  topBar: {
    visible: false,
  },
  bottomTabs: {
    visible: false,
  },
  layout: {
    componentBackgroundColor: 'transparent',
    backgroundColor: 'transparent',
  },
  modalTransitionStyle: OptionsModalTransitionStyle.coverVertical,
  modalPresentationStyle: OptionsModalPresentationStyle.overCurrentContext,
};
export default CarStoryScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: '20%',
  },
  closeButton: {
    position: 'absolute',
    top: 50,
    right: 15,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderRadius: 15,
    width: 30,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeButtonText: {
    fontWeight: 'bold',
    color: 'white',
    fontSize: 16,
  },
  background: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: colors.primary.light,
  },
  carIcon: {
    fontSize: 40,
    fontWeight: 'bold',
    color: 'white',
    marginVertical: 10,
    textAlign: 'center',
  },
  carName: {
    fontSize: 22,
    fontWeight: 'bold',
    color: 'white',
    marginVertical: 10,
    textAlign: 'center',
  },
  carDescription: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'white',
  },
});
