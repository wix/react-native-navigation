import { BlurView } from '@react-native-community/blur';
import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import { StyleSheet, Text, Dimensions, ViewProps, Platform, Image } from 'react-native';
import Reanimated, { Easing, useAnimatedStyle, useSharedValue, withTiming } from 'react-native-reanimated';
import { CarItem } from '../../assets/cars';
import { hexToRgba } from '../../commons/Colors';
import PressableScale from '../../components/PressableScale';
import { Navigation } from 'react-native-navigation';

type CarCardProps = {
  car: CarItem;
  modalComponentId: string;
  onCarPressed: () => unknown;
} & ViewProps;

const TEXT_BANNER_OPACITY = Platform.select({
  android: 1,
  ios: 0.4,
});

export default function CarCard({
  car,
  modalComponentId,
  style,
  onCarPressed,
  ...passThroughProps
}: CarCardProps) {
  const isTextHidden = useRef(false);

  const color = useMemo(() => hexToRgba(car.color, TEXT_BANNER_OPACITY), [car.color]);

  const textContainerOpacity = useSharedValue(1);

  const containerStyle = useMemo(() => [styles.container, style], [style]);

  const textContainerStyle = useAnimatedStyle(
    () => ({ opacity: textContainerOpacity.value, backgroundColor: color }),
    [color, textContainerOpacity]
  );

  const onPress = useCallback(() => {
    onCarPressed();
    isTextHidden.current = true;
    textContainerOpacity.value = withTiming(0, {
      duration: 300,
      easing: Easing.linear
    });
  }, [onCarPressed, textContainerOpacity]);
  const onFocus = useCallback(() => {
    if (isTextHidden.current === true) {
      isTextHidden.current = false;
      textContainerOpacity.value = withTiming(1, {
        duration: 300,
        easing: Easing.linear
      });
    }
  }, [textContainerOpacity]);

  useEffect(() => {
    const subscription = Navigation.events().registerModalDismissedListener(
      ({ componentId: modalId }) => {
        console.log(modalComponentId, modalId)
        if (modalId === modalComponentId) {
          console.log('here')
          onFocus();
        }
      }
    );
    return () => subscription.remove();
  }, [onFocus, modalComponentId]);

  return (
    <PressableScale weight="medium" onPress={onPress} style={containerStyle} {...passThroughProps}>
      <Image
        source={car.image}
        nativeID={`image${car.id}`}
        style={styles.image}
        resizeMode="cover"
      />
      <Reanimated.View style={[styles.textContainer, textContainerStyle]}>
        {Platform.OS === 'ios' && <BlurView blurType="light" style={StyleSheet.absoluteFill} />}
        <Text
          nativeID={`title${car.id}`}
          style={styles.title}
          numberOfLines={2}
          ellipsizeMode="tail"
        >
          {car.name}
        </Text>
        <Text style={styles.description} numberOfLines={3} ellipsizeMode="tail">
          {car.description}
        </Text>
      </Reanimated.View>
    </PressableScale>
  );
}

const styles = StyleSheet.create({
  container: {
    //alignSelf: 'center',
    width: Dimensions.get('window').width * 0.9,
    height: 350,
    borderRadius: 20,
    overflow: 'hidden',
  },
  image: {
    width: '100%',
    height: '100%',
    borderRadius: 20,
    zIndex: 0,
  },
  textContainer: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    padding: 15,
    zIndex: 1,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  description: {
    fontSize: 13,
    marginTop: 5,
    fontWeight: '500',
    color: '#333333',
  },
});
