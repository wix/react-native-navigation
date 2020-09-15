import { BlurView } from '@react-native-community/blur';
import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import { Image, StyleSheet, Text, Dimensions, ViewProps, Platform } from 'react-native';
import Reanimated, { Easing, useValue } from 'react-native-reanimated';
import { PostItem } from '../../assets/posts';
import { hexToRgba } from '../../commons/Colors';
import PressableScale from '../../components/PressableScale';
import { Navigation } from 'react-native-navigation';

type PostCardProps = {
  post: PostItem;
  parentComponentId: string;
  onPostPressed: () => unknown;
} & ViewProps;

const TEXT_BANNER_OPACITY = Platform.select({
  android: 1,
  ios: 0.4,
});

export default function PostCard({
  post,
  parentComponentId,
  style,
  onPostPressed,
  ...passThroughProps
}: PostCardProps) {
  const isTextHidden = useRef(false);

  const color = useMemo(() => hexToRgba(post.color, TEXT_BANNER_OPACITY), [post.color]);

  const textContainerOpacity = useValue(1);

  const containerStyle = useMemo(() => [styles.container, style], [style]);
  const textContainerStyle = useMemo(
    () => [styles.textContainer, { opacity: textContainerOpacity, backgroundColor: color }],
    [color, textContainerOpacity]
  );

  const onPress = useCallback(() => {
    onPostPressed();
    isTextHidden.current = true;
    Reanimated.timing(textContainerOpacity, {
      toValue: 0,
      duration: 300,
      easing: Easing.linear,
    }).start();
  }, [onPostPressed, textContainerOpacity]);
  const onFocus = useCallback(() => {
    if (isTextHidden.current === true) {
      isTextHidden.current = false;
      Reanimated.timing(textContainerOpacity, {
        toValue: 1,
        duration: 300,
        easing: Easing.linear,
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
      <Image
        source={post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${post.id}`}
        style={styles.image}
        resizeMode="cover"
        fadeDuration={0}
      />
      <Reanimated.View style={textContainerStyle}>
        {Platform.OS === 'ios' && <BlurView blurType="light" style={StyleSheet.absoluteFill} />}
        <Text
          nativeID={`title${post.id}`}
          style={styles.title}
          numberOfLines={2}
          ellipsizeMode="tail"
        >
          {post.name}
        </Text>
        <Text style={styles.description} numberOfLines={3} ellipsizeMode="tail">
          {post.description}
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
