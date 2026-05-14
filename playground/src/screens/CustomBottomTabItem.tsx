import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
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

export default function CustomBottomTabItem(props: Props) {
  const tabIndex = props.tabIndex ?? 0;
  const label = props.label ?? TAB_LABELS[tabIndex] ?? `Tab ${tabIndex}`;
  const glyph = TAB_GLYPHS[tabIndex] ?? '●';
  const testID = TAB_TEST_IDS[tabIndex];
  const selected = !!props.selected;

  return (
    <View testID={testID} style={[styles.container, selected && styles.selectedContainer]}>
      <Text style={[styles.glyph, selected && styles.selectedText]}>{glyph}</Text>
      <Text style={[styles.label, selected && styles.selectedText]}>{label}</Text>
      {props.badge ? <Text style={styles.badge}>{props.badge}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  selectedContainer: {
    backgroundColor: 'rgba(0, 122, 255, 0.08)',
  },
  glyph: {
    fontSize: 22,
    color: '#9aa0a6',
  },
  label: {
    marginTop: 2,
    fontSize: 11,
    color: '#9aa0a6',
  },
  selectedText: {
    color: '#007aff',
    fontWeight: '600',
  },
  badge: {
    position: 'absolute',
    top: 4,
    right: 16,
    minWidth: 16,
    paddingHorizontal: 4,
    borderRadius: 8,
    fontSize: 10,
    color: 'white',
    backgroundColor: '#ff3b30',
    overflow: 'hidden',
    textAlign: 'center',
  },
});
