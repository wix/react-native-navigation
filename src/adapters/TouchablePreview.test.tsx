import * as React from 'react';
import * as PropTypes from 'prop-types';
import { Platform, TouchableNativeFeedback, TouchableWithoutFeedback } from 'react-native';
import { TouchablePreview } from './TouchablePreview';

describe('TouchablePreview', () => {
  const originalPlatform = Platform.OS;

  afterEach(() => {
    Platform.OS = originalPlatform;
    TouchablePreview.peeking = false;
    jest.restoreAllMocks();
    jest.useRealTimers();
  });

  it('accepts any React element type as the touchable component', () => {
    const MemoizedTouchable = React.memo(() => null);
    const consoleError = jest.spyOn(console, 'error').mockImplementation();

    PropTypes.checkPropTypes(
      TouchablePreview.propTypes,
      { touchableComponent: MemoizedTouchable },
      'prop',
      'TouchablePreview'
    );

    expect(consoleError).not.toHaveBeenCalled();
  });

  it('falls back from TouchableNativeFeedback on iOS', () => {
    Platform.OS = 'ios';
    const uut = new TouchablePreview({ touchableComponent: TouchableNativeFeedback });

    expect(uut.render().type).toBe(TouchableWithoutFeedback);
  });

  it('clears pending preview work when unmounted', () => {
    jest.useFakeTimers();
    const onPeekOut = jest.fn();
    const uut = new TouchablePreview({ onPeekOut });
    uut.onTouchStart({ nativeEvent: { timestamp: 0 } } as any);
    uut.onTouchMove({ nativeEvent: { force: 0.5, timestamp: 400 } } as any);

    (uut as any).componentWillUnmount();
    jest.runOnlyPendingTimers();

    expect(onPeekOut).not.toHaveBeenCalled();
    expect(TouchablePreview.peeking).toBe(false);
  });

  it('does not clear another instance\'s active peek when unmounted', () => {
    jest.useFakeTimers();
    const owner = new TouchablePreview({});
    const nonOwner = new TouchablePreview({});
    owner.onTouchStart({ nativeEvent: { timestamp: 0 } } as any);
    owner.onTouchMove({ nativeEvent: { force: 0.5, timestamp: 400 } } as any);

    nonOwner.componentWillUnmount();

    expect(TouchablePreview.peeking).toBe(true);

    owner.componentWillUnmount();
    expect(TouchablePreview.peeking).toBe(false);
  });
});
