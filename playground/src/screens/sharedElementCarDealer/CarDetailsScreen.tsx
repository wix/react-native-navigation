import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import { Dimensions, StyleSheet, Text, TouchableOpacity, Insets, Image, View } from 'react-native';
import {
  Navigation,
  NavigationFunctionComponent,
  OptionsModalPresentationStyle,
  OptionsModalTransitionStyle,
} from 'react-native-navigation';
import { CarItem } from '../../assets/cars';
import Reanimated, { Easing, Extrapolation, interpolate, useAnimatedScrollHandler, useAnimatedStyle, useDerivedValue, useSharedValue, withTiming } from 'react-native-reanimated';
import DismissableView from './DismissableView';
import useDismissGesture from './useDismissGesture';
import { buildFullScreenSharedElementAnimations, SET_DURATION } from './Constants';
import PressableScale from '../../components/PressableScale';
import colors from '../../commons/Colors';

const ReanimatedTouchableOpacity = Reanimated.createAnimatedComponent(TouchableOpacity);

const HEADER_HEIGHT = 300;
const INDICATOR_INSETS: Insets = { top: HEADER_HEIGHT };

interface Props {
  car: CarItem;
}

const CarDetailsScreen: NavigationFunctionComponent<Props> = ({ car, componentId }) => {
  const isClosing = useRef(false);

  const onClosePressed = useCallback(() => {
    if (isClosing.current === true) return;
    isClosing.current = true;
    Navigation.dismissModal(componentId);
  }, [componentId]);

  const dismissGesture = useDismissGesture(onClosePressed);

  const scrollY = useSharedValue(0);

  const onScroll = useAnimatedScrollHandler((e) => {
    scrollY.value = e.contentOffset.y;
  });

  const closeButtonStyle = useAnimatedStyle(
    () => ({ opacity: dismissGesture.controlsOpacity.value }),
    [dismissGesture.controlsOpacity]
  );

  const headerY = useDerivedValue(
    () => interpolate(scrollY.value, [0, HEADER_HEIGHT], [0, -HEADER_HEIGHT], Extrapolation.CLAMP),
    [scrollY]
  );

  const imageStyle = useAnimatedStyle(
    () => ({ borderRadius: dismissGesture.cardBorderRadius.value, transform: [{ translateY: headerY.value }] }),
    [dismissGesture.cardBorderRadius, headerY]
  );

  const openImage = useCallback(() => {
    Navigation.showModal({
      component: {
        name: 'ImageFullScreenViewer',
        passProps: {
          source: car.image,
          sharedElementId: `image${car.id}Full`,
        },
        options: {
          animations: buildFullScreenSharedElementAnimations(car),
          layout: {
            componentBackgroundColor: 'transparent'
          },
          modalPresentationStyle: OptionsModalPresentationStyle.overCurrentContext
        },
      },
    });
  }, [car]);

  useEffect(() => {
    setTimeout(() => {
      dismissGesture.controlsOpacity.value = withTiming(1, {
        duration: 300,
        easing: Easing.linear
      });
    }, SET_DURATION);
  }, [dismissGesture.controlsOpacity]);

  return (
    <DismissableView dismissGestureState={dismissGesture} style={styles.container}>
      <Reanimated.ScrollView
        contentInsetAdjustmentBehavior="never"
        contentContainerStyle={styles.content}
        onScroll={onScroll}
        scrollEventThrottle={1}
        scrollIndicatorInsets={INDICATOR_INSETS}
        indicatorStyle="black"
        nativeID='description'
      >
        <Text style={styles.title} nativeID={`title${car.id}Dest`}>
          {car.name}
        </Text>
        <Text style={styles.description}>{car.description}</Text>
        <PressableScale weight="medium" activeScale={0.95} style={styles.buyButton}>
          <Text style={styles.buyText}>Buy</Text>
        </PressableScale>
      </Reanimated.ScrollView>
      <ReanimatedTouchableOpacity style={[styles.headerImage, imageStyle]} onPress={openImage}>
        <Image
          source={car.image}
          nativeID={`image${car.id}Dest`}
          style={{ width: '100%', height: '100%' }}
        />
      </ReanimatedTouchableOpacity>
      <ReanimatedTouchableOpacity style={[styles.closeButton, closeButtonStyle]} onPress={onClosePressed}>
        <Text style={styles.closeButtonText}>x</Text>
      </ReanimatedTouchableOpacity>
    </DismissableView>
  );
};

export default CarDetailsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
    overflow: 'hidden'
  },
  headerImage: {
    position: 'absolute',
    height: HEADER_HEIGHT,
    width: Dimensions.get('window').width,
    overflow: 'hidden',
  },
  content: {
    paddingTop: HEADER_HEIGHT,
    paddingHorizontal: 25,
    backgroundColor: colors.background.light,
    height: '100%'
  },
  title: {
    fontSize: 32,
    marginTop: 30,
    fontWeight: '500',
    zIndex: 2,
  },
  description: {
    fontSize: 15,
    letterSpacing: 0.2,
    lineHeight: 25,
    marginTop: 32,
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
  buyButton: {
    alignSelf: 'center',
    marginVertical: 25,
    width: '100%',
    height: 45,
    backgroundColor: 'dodgerblue',
    borderRadius: 15,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buyText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
  },
});
