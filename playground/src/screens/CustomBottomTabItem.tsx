import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, Text, View } from 'react-native';
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
const ICON_BOX = 22;
const STROKE = 1.8;

/**
 * Pure-RN-primitive icons composed from absolutely-positioned Views with
 * borderRadius / rotation. They render byte-identically on both platforms
 * (no system font, no native dependency).
 */
function HomeIcon({
  color,
  size = ICON_BOX,
}: {
  color: Animated.AnimatedInterpolation<string>;
  size?: number;
}) {
  const halfRoof = size / 2;
  return (
    <View style={{ width: size, height: size, alignItems: 'center' }}>
      <Animated.View
        style={{
          position: 'absolute',
          top: 1,
          width: halfRoof * 1.3,
          height: halfRoof * 1.3,
          borderTopWidth: STROKE,
          borderLeftWidth: STROKE,
          borderColor: color as unknown as string,
          transform: [{ rotate: '45deg' }],
        }}
      />
      <Animated.View
        style={{
          position: 'absolute',
          bottom: 0,
          width: size - 4,
          height: size / 2 - 1,
          borderWidth: STROKE,
          borderTopWidth: 0,
          borderColor: color as unknown as string,
        }}
      />
    </View>
  );
}

function SearchIcon({
  color,
  size = ICON_BOX,
}: {
  color: Animated.AnimatedInterpolation<string>;
  size?: number;
}) {
  const lens = size * 0.65;
  return (
    <View style={{ width: size, height: size }}>
      <Animated.View
        style={{
          position: 'absolute',
          top: 1,
          left: 1,
          width: lens,
          height: lens,
          borderWidth: STROKE,
          borderColor: color as unknown as string,
          borderRadius: lens / 2,
        }}
      />
      <Animated.View
        style={{
          position: 'absolute',
          bottom: 1,
          right: 1,
          width: size * 0.34,
          height: STROKE,
          borderRadius: STROKE,
          backgroundColor: color as unknown as string,
          transform: [{ rotate: '45deg' }],
        }}
      />
    </View>
  );
}

function ProfileIcon({
  color,
  size = ICON_BOX,
}: {
  color: Animated.AnimatedInterpolation<string>;
  size?: number;
}) {
  const headSize = size * 0.42;
  const bodyHeight = size * 0.5;
  return (
    <View style={{ width: size, height: size, alignItems: 'center' }}>
      <Animated.View
        style={{
          marginTop: 1,
          width: headSize,
          height: headSize,
          borderWidth: STROKE,
          borderColor: color as unknown as string,
          borderRadius: headSize / 2,
        }}
      />
      <Animated.View
        style={{
          position: 'absolute',
          bottom: 0,
          width: size - 1,
          height: bodyHeight,
          borderTopLeftRadius: size,
          borderTopRightRadius: size,
          borderWidth: STROKE,
          borderBottomWidth: 0,
          borderColor: color as unknown as string,
        }}
      />
    </View>
  );
}

function renderIcon(tabIndex: number, color: Animated.AnimatedInterpolation<string>) {
  switch (tabIndex) {
    case 0:
      return <HomeIcon color={color} />;
    case 1:
      return <SearchIcon color={color} />;
    case 2:
    default:
      return <ProfileIcon color={color} />;
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
