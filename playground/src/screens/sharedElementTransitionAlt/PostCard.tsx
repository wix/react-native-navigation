import { BlurView } from '@react-native-community/blur';
import React, { useMemo } from 'react';
import { Image, View, StyleSheet, Text, Dimensions, ViewProps, Platform } from 'react-native';
import { PostItem } from '../../assets/posts';

type PostCardProps = {
  post: PostItem;
} & ViewProps;

export function hexToRgba(hex: string, a = 1): string {
  // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
  const shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
  hex = hex.replace(shorthandRegex, function (m, r, g, b) {
    return r + r + g + g + b + b;
  });

  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? `rgba(${parseInt(result[1], 16)}, ${parseInt(result[2], 16)}, ${parseInt(
        result[3],
        16
      )}, ${a})`
    : hex;
}

export default function PostCard({ post, style, ...passThroughProps }: PostCardProps) {
  const color = useMemo(() => hexToRgba(post.color, 0.4), [post.color]);

  return (
    <View style={[styles.container, style]} {...passThroughProps}>
      <Image
        source={post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${post.id}`}
        style={styles.image}
        resizeMode="cover"
        fadeDuration={0}
      />
      <View style={[styles.textContainer, { backgroundColor: color }]}>
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
      </View>
    </View>
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
  },
  textContainer: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    padding: 15,
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
