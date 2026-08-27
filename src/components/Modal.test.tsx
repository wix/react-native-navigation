import React from 'react';
import { Dimensions, View } from 'react-native';
import { act, render } from '@testing-library/react-native';
import { Modal } from './Modal';

it('updates content dimensions and slide distance on rotation', () => {
  const original = Dimensions.get('window');
  const { UNSAFE_getAllByType, UNSAFE_root, unmount } = render(
    <Modal visible onRequestClose={() => {}}>
      <View testID="content" />
    </Modal>
  );
  try {
    act(() => {
      Dimensions.set({ window: { ...original, width: 800, height: 400 } });
    });
    const container = UNSAFE_getAllByType(View).find((view) => view.props.collapsable === false)!;
    expect(container.props.style).toEqual({ width: 800, height: 400 });
    const nativeModal = UNSAFE_root.find((node) => node.props.animation !== undefined);
    expect(nativeModal.props.animation.dismissModal.exit.translationY.to).toBe(400);
    expect(nativeModal.props.animation.showModal.enter.translationY.from).toBe(400);
  } finally {
    unmount();
    act(() => {
      Dimensions.set({ window: original });
    });
  }
});

it('does not mount native modal content while hidden', () => {
  const { toJSON } = render(<Modal visible={false} onRequestClose={() => {}} />);
  expect(toJSON()).toBeNull();
});
