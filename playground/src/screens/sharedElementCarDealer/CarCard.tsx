import { BlurView } from '@react-native-community/blur';
import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import { StyleSheet, Text, Dimensions, ViewProps, Platform } from 'react-native';
import Reanimated, { EasingNode, useValue } from 'react-native-reanimated';
import FastImage from 'react-native-fast-image';
import { CarItem } from '../../assets/cars';
import { hexToRgba } from '../../commons/Colors';
import PressableScale from '../../components/PressableScale';
import { Navigation } from 'react-native-navigation';

type CarCardProps = {
  car: CarItem;
  parentComponentId: string;
  onCarPressed: () => unknown;
} & ViewProps;

const TEXT_BANNER_OPACITY = Platform.select({
  android: 1,
  ios: 0.4,
});

export default function CarCard({
  car,
  parentComponentId,
  style,
  onCarPressed,
  ...passThroughProps
}: CarCardProps) {
  const isTextHidden = useRef(false);

  const color = useMemo(() => hexToRgba(car.color, TEXT_BANNER_OPACITY), [car.color]);

  const textContainerOpacity = useValue(1);

  const containerStyle = useMemo(() => [styles.container, style], [style]);
  const textContainerStyle = useMemo(
    () => [styles.textContainer, { opacity: textContainerOpacity, backgroundColor: color }],
    [color, textContainerOpacity]
  );

  const onPress = useCallback(() => {
    onCarPressed();
    isTextHidden.current = true;
    Reanimated.timing(textContainerOpacity, {
      toValue: 0,
      duration: 300,
      easing: EasingNode.linear,
    }).start();
  }, [onCarPressed, textContainerOpacity]);
  const onFocus = useCallback(() => {
    if (isTextHidden.current === true) {
      isTextHidden.current = false;
      Reanimated.timing(textContainerOpacity, {
        toValue: 1,
        duration: 300,
        easing: EasingNode.linear,
      }).start();
    }
  }, [textContainerOpacity]);

  useEffect(() => {
    const subscription = Navigation.events().registerComponentDidAppearListener(
      ({ componentId }) => {
        if (componentId === parentComponentId) onFocus();
      }
    );
    return () => subscription.remove();
  }, [onFocus, parentComponentId]);

  return (
    <PressableScale weight="medium" onPress={onPress} style={containerStyle} {...passThroughProps}>
      <FastImage
        source={car.image}
        // @ts-ignore nativeID isn't included in react-native-fast-image props.
        nativeID={`image${car.id}`}
        style={styles.image}
        resizeMode="cover"
      />
      <Reanimated.View style={textContainerStyle}>
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
    marginTop: 35,
    alignSelf: 'center',
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
