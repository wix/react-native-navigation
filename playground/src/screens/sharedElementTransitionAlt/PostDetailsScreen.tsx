import React, { useCallback, useMemo, useRef } from 'react';
import { Dimensions, StyleSheet, Text, TouchableOpacity, Insets } from 'react-native';
import { Navigation, NavigationFunctionComponent } from 'react-native-navigation';
import { PostItem } from '../../assets/posts';
import Reanimated, { useValue } from 'react-native-reanimated';
import DismissableView from './DismissableView';
import useDismissGesture from './useDismissGesture';

const HEADER_HEIGHT = 300;
const INDICATOR_INSETS: Insets = { top: HEADER_HEIGHT };

interface Props {
  post: PostItem;
}

const PostDetailsScreen: NavigationFunctionComponent<Props> = ({ post, componentId }) => {
  const isClosing = useRef(false);

  const onClosePressed = useCallback(() => {
    if (isClosing.current === true) return;
    isClosing.current = true;
    Navigation.pop(componentId);
  }, [componentId]);
  const dismissGesture = useDismissGesture(onClosePressed);

  const scrollY = useValue(0);
  const onScroll = useMemo(
    () => Reanimated.event([{ nativeEvent: { contentOffset: { y: scrollY } } }]),
    [scrollY]
  );

  const headerY = useMemo(
    () =>
      Reanimated.interpolate(scrollY, {
        inputRange: [0, HEADER_HEIGHT],
        outputRange: [0, -HEADER_HEIGHT],
        extrapolateLeft: Reanimated.Extrapolate.CLAMP,
        extrapolateRight: Reanimated.Extrapolate.EXTEND,
      }),
    [scrollY]
  );
  const imageStyle = useMemo(
    () => [
      styles.headerImage,
      { borderRadius: dismissGesture.cardBorderRadius, transform: [{ translateY: headerY }] },
    ],
    [dismissGesture.cardBorderRadius, headerY]
  );

  return (
    <DismissableView dismissGestureState={dismissGesture} style={styles.container}>
      <Reanimated.ScrollView
        contentContainerStyle={styles.content}
        onScroll={onScroll}
        scrollEventThrottle={1}
        scrollIndicatorInsets={INDICATOR_INSETS}
      >
        <Text style={styles.title} nativeID={`title${post.id}Dest`}>
          {post.name}
        </Text>
        <Text style={styles.description}>{post.description}</Text>
        <TouchableOpacity style={styles.buyButton}>
          <Text style={styles.buyText}>Buy</Text>
        </TouchableOpacity>
      </Reanimated.ScrollView>
      <Reanimated.Image
        source={post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${post.id}Dest`}
        style={imageStyle}
        resizeMode="cover"
        fadeDuration={0}
      />
      <TouchableOpacity style={styles.closeButton} onPress={onClosePressed}>
        <Text style={styles.closeButtonText}>x</Text>
      </TouchableOpacity>
    </DismissableView>
  );
};
PostDetailsScreen.options = {
  statusBar: {
    visible: false,
  },
  topBar: {
    visible: false,
  },
  bottomTabs: {
    visible: false,
  },
};
export default PostDetailsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  headerImage: {
    position: 'absolute',
    height: HEADER_HEIGHT,
    width: Dimensions.get('window').width,
  },
  content: {
    paddingTop: HEADER_HEIGHT,
    paddingHorizontal: 25,
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
