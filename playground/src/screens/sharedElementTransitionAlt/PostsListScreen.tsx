import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, StyleSheet, TouchableOpacity } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import posts, { PostItem } from '../../assets/posts';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import PostCard from './PostCard';

const SET_DURATION = 500;

// SET = Shared Element Transition
// TODO: 1. Animate borderRadius in SET
// TODO: 2. Image in PostDetailsScreen is wider than in PostCard. Smoothly animate that resize, so it doesn't jump from small width to big width and then slowly transition.
// TODO: 3. Spring interpolation. Mass = 3. See https://github.com/mrousavy/react-native-navigation/blob/126b11a3ba0c23686ebf7f223ead996a58dbb02a/lib/ios/RNNInterpolator.m#L62-L83
// TODO: 4. Fix weird ScrollView insets on iOS, see: https://github.com/wix/react-native-navigation/issues/6510
// TODO: 5. Fix image transitions on iOS, see: https://github.com/wix/react-native-navigation/issues/6505
// TODO: 6. Make SETs for Overlays possible OR allow parent screen to be visible beneath PostDetailsScreen to allow animation similar to Apple's App of the Day (AppStore) animation. See: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 7. Don't just hard-crash if SET nativeIDs (Views) can't be found
// TODO: 8. Fix ScaleTypeCenterCrop hard-crash on Android. See: https://github.com/wix/react-native-navigation/issues/6517

const PostsListScreen: NavigationFunctionComponent = (props) => {
  const onPostPressed = useCallback(
    (post: PostItem) => {
      Navigation.push(props.componentId, {
        component: {
          name: Screens.PostDetailsScreen,
          passProps: { post: post },
          options: {
            animations: {
              push: {
                content: {
                  alpha: {
                    from: 0,
                    to: 1,
                    duration: SET_DURATION,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}`,
                    toId: `image${post.id}Dest`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}`,
                    toId: `title${post.id}Dest`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                ],
              },
              pop: {
                content: {
                  alpha: {
                    from: 1,
                    to: 0,
                    duration: SET_DURATION,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}Dest`,
                    toId: `image${post.id}`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}Dest`,
                    toId: `title${post.id}`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                ],
              },
            },
          },
        },
      });
    },
    [props.componentId]
  );

  return (
    <SafeAreaView>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {posts.map((p) => (
          <TouchableOpacity key={p.id} onPress={() => onPostPressed(p)}>
            <PostCard post={p} />
          </TouchableOpacity>
        ))}
      </ScrollView>
    </SafeAreaView>
  );
};

PostsListScreen.options = {
  ...Platform.select({
    android: {
      statusBar: {
        style: 'dark' as const,
        backgroundColor: 'white',
      },
    },
  }),
  topBar: {
    title: {
      text: 'Posts',
    },
  },
};

export default PostsListScreen;

const styles = StyleSheet.create({
  scrollContent: {
    paddingVertical: 25,
  },
});
