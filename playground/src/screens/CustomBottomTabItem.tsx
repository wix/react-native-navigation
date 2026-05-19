import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, Text, View } from 'react-native';
import Svg, { Path, Circle } from 'react-native-svg';
import testIDs from '../testIDs';

interface Props {
  componentId: string;
  tabIndex: number;
  selected?: boolean;
  badge?: string | null;
  label?: string;
}

const TAB_TEST_IDS = [
  testIDs.CUSTOM_BOTTOM_TAB_ITEM_0,
  testIDs.CUSTOM_BOTTOM_TAB_ITEM_1,
  testIDs.CUSTOM_BOTTOM_TAB_ITEM_2,
];

const TAB_LABELS = ['Home', 'Search', 'Profile'];

const UNSELECTED_COLOR = '#9aa0a6';
const SELECTED_COLOR = '#007aff';
const SELECTED_PILL_COLOR = 'rgba(0, 122, 255, 0.14)';

const AnimatedPath = Animated.createAnimatedComponent(Path);
const AnimatedCircle = Animated.createAnimatedComponent(Circle);

function renderIcon(tabIndex: number, color: Animated.AnimatedInterpolation<string>) {
  switch (tabIndex) {
    case 0:
      return (
        <Svg width={22} height={22} viewBox="0 0 24 24" fill="none">
          <AnimatedPath
            d="M3 11.5L12 4l9 7.5V20a1 1 0 0 1-1 1h-5v-6h-6v6H4a1 1 0 0 1-1-1v-8.5z"
            stroke={color as unknown as string}
            strokeWidth={1.8}
            strokeLinejoin="round"
            strokeLinecap="round"
          />
        </Svg>
      );
    case 1:
      return (
        <Svg width={22} height={22} viewBox="0 0 24 24" fill="none">
          <AnimatedCircle
            cx={10.5}
            cy={10.5}
            r={6.5}
            stroke={color as unknown as string}
            strokeWidth={1.8}
          />
          <AnimatedPath
            d="M20 20l-4.5-4.5"
            stroke={color as unknown as string}
            strokeWidth={1.8}
            strokeLinecap="round"
          />
        </Svg>
      );
    case 2:
    default:
      return (
        <Svg width={22} height={22} viewBox="0 0 24 24" fill="none">
          <AnimatedCircle
            cx={12}
            cy={8}
            r={4}
            stroke={color as unknown as string}
            strokeWidth={1.8}
          />
          <AnimatedPath
            d="M4 20c0-4 4-6 8-6s8 2 8 6"
            stroke={color as unknown as string}
            strokeWidth={1.8}
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </Svg>
      );
  }
}

export default function CustomBottomTabItem(props: Props) {
  const tabIndex = props.tabIndex ?? 0;
  const label = props.label ?? TAB_LABELS[tabIndex] ?? `Tab ${tabIndex}`;
  const testID = TAB_TEST_IDS[tabIndex];
  const selected = !!props.selected;

  const anim = useRef(new Animated.Value(selected ? 1 : 0)).current;

  useEffect(() => {
    Animated.spring(anim, {
      toValue: selected ? 1 : 0,
      friction: 7,
      tension: 90,
      useNativeDriver: false,
    }).start();
  }, [selected, anim]);

  const pillBackground = anim.interpolate({
    inputRange: [0, 1],
    outputRange: ['rgba(0, 122, 255, 0)', SELECTED_PILL_COLOR],
  });
  const tintColor = anim.interpolate({
    inputRange: [0, 1],
    outputRange: [UNSELECTED_COLOR, SELECTED_COLOR],
  });
  const scale = anim.interpolate({
    inputRange: [0, 1],
    outputRange: [1, 1.04],
  });

  return (
    <View testID={testID} style={styles.container}>
      <Animated.View
        style={[styles.pill, { backgroundColor: pillBackground, transform: [{ scale }] }]}
      >
        <View style={styles.iconWrapper}>
          {renderIcon(tabIndex, tintColor)}
          {props.badge ? (
            <View style={styles.badge}>
              <Text style={styles.badgeText}>{props.badge}</Text>
            </View>
          ) : null}
        </View>
        <Animated.Text style={[styles.label, { color: tintColor }]}>{label}</Animated.Text>
      </Animated.View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignSelf: 'stretch',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 4,
  },
  pill: {
    alignSelf: 'stretch',
    height: 52,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 26,
  },
  iconWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  label: {
    marginTop: 2,
    fontSize: 11,
    color: UNSELECTED_COLOR,
    fontWeight: '600',
  },
  badge: {
    position: 'absolute',
    top: -6,
    left: 14,
    minWidth: 20,
    height: 20,
    paddingHorizontal: 5,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#ff3b30',
  },
  badgeText: {
    color: 'white',
    fontSize: 12,
    fontWeight: '700',
    lineHeight: 14,
  },
});
