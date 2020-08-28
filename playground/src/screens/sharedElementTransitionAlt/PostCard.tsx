import React from 'react';
import { Image, View, StyleSheet, Text, Dimensions, ViewProps } from 'react-native';
import { PostItem } from '../../assets/posts';

type PostCardProps = {
  post: PostItem
} & ViewProps

export default function PostCard({
  post,
  style,
  ...passThroughProps
}: PostCardProps) {
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
      <View style={[styles.textContainer, { backgroundColor: post.color }]}>
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
