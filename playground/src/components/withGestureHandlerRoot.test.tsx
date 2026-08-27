import React from 'react';
import { Text } from 'react-native';
import { act, create, ReactTestRenderer } from 'react-test-renderer';
import withGestureHandlerRoot from './withGestureHandlerRoot';

jest.mock('react-native-gesture-handler', () => ({
  GestureHandlerRootView: require('react-native').View,
}));

it('forwards screen props and preserves static navigation options', () => {
  function Screen({ label }: { label: string }) {
    return <Text>{label}</Text>;
  }
  Screen.options = { topBar: { visible: false } };
  const Wrapped = withGestureHandlerRoot(Screen);

  expect(Wrapped).toHaveProperty('options', Screen.options);
  let renderer!: ReactTestRenderer;
  act(() => {
    renderer = create(<Wrapped label="Gesture screen" />);
  });
  expect(renderer.root.findByType(Text).props.children).toBe('Gesture screen');
  act(() => renderer.unmount());
});
