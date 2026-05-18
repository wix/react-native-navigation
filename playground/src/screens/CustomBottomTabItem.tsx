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

const TAB_GLYPHS = ['◉', '◎', '◍'];

const UNSELECTED_COLOR = '#9aa0a6';
const SELECTED_COLOR = '#007aff';
const SELECTED_PILL_COLOR = 'rgba(0, 122, 255, 0.14)';

export default function CustomBottomTabItem(props: Props) {
  const tabIndex = props.tabIndex ?? 0;
  const label = props.label ?? TAB_LABELS[tabIndex] ?? `Tab ${tabIndex}`;
  const glyph = TAB_GLYPHS[tabIndex] ?? '●';
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
          <Animated.Text style={[styles.glyph, { color: tintColor }]}>{glyph}</Animated.Text>
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
    justifyContent: 'center',
    paddingHorizontal: 4,
  },
  pill: {
    alignSelf: 'stretch',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 6,
    borderRadius: 999,
  },
  iconWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  glyph: {
    fontSize: 22,
    color: UNSELECTED_COLOR,
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
